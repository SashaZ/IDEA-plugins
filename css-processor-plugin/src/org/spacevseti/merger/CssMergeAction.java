package org.spacevseti.merger;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NullableComputable;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.spacevseti.Utils;
import org.spacevseti.cssmerger.AnalyzeResult;
import org.spacevseti.cssmerger.Analyzer;
import org.spacevseti.cssmerger.CssMerger;
import org.spacevseti.cssmerger.StringConstants;
import org.spacevseti.filemerger.MergingResult;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class CssMergeAction extends AnAction {
//    private static final String CSS_EXTENSION = "css";

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        Project project = e.getData(PlatformDataKeys.PROJECT);
        VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);

        boolean visible = project != null && files != null && files.length > 0 && file != null;
        if (visible) {
            visible = StringConstants.CSS_EXTENSION.getValue().equals(FilenameUtils.getExtension(file.getCanonicalPath()));
        }
        // Enable or disable
        e.getPresentation().setEnabled(visible);

    }

    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        VirtualFile selectedVirtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String mergingFileName = selectedVirtualFile == null ? StringUtils.EMPTY : FilenameUtils.getName(selectedVirtualFile.getCanonicalPath());
        if (!StringConstants.CSS_EXTENSION.getValue().equals(FilenameUtils.getExtension(mergingFileName))) {
            Messages.showWarningDialog(project, "File name '" + mergingFileName + "' isn't valid. We can merge only " + StringConstants.CSS_EXTENSION.getValue() + " file.", "Warning");
            return;
        }

        Analyzer analyzer = new Analyzer();
        assert selectedVirtualFile != null;
        File mergingFile = new File(selectedVirtualFile.getCanonicalPath());
        try {
            AnalyzeResult analyzeResult = analyzer.preMergeAnalyze(mergingFile);
            String message = "Will merge file '" + mergingFileName + "'?\n\n";
            MessagesExt.CheckBoxListDialog dialog = new MessagesExt.CheckBoxListDialog(project, message,
                    "Merge CSS", Messages.getQuestionIcon(), analyzeResult);
            dialog.show();
            List<JCheckBox> result = dialog.getResult();
            int dialogResult = result == null ? Messages.CANCEL : Messages.OK;
//            dialogResult = Messages.showYesNoCancelDialog(project, message, "Information", "Yes", "Yes and remove imported files", "Cancel", Messages.getQuestionIcon());
            if (Messages.CANCEL == dialogResult) {
                return;
            }

            MergingResult mergingResult = new CssMerger(mergingFile).
                    setExcludeImportFilePaths(getExcludeImportFilePaths(analyzeResult, result))
//                    .setRemoveImportedFiles(dialog.isAgreeRemoveImportedFiles())
                    .merge();

            Collection<String> deletedImportedFiles = Collections.emptyList();
            if (dialog.isAgreeRemoveImportedFiles()) {
                deletedImportedFiles = deleteImportedFiles(selectedVirtualFile, mergingResult);
            }

            Messages.showMessageDialog(project, getResultMessage(mergingFileName, mergingResult, deletedImportedFiles),
                    "Information", Messages.getInformationIcon());
        } catch (IOException e1) {
            Messages.showErrorDialog(project, "Merging css file '" + mergingFileName + "' isn't finished!\n" + e1.getMessage(), "Error");
        }
        selectedVirtualFile.getParent().refresh(false, true);
    }

    private String getResultMessage(@NotNull String mergingFileName, @NotNull MergingResult mergingResult, @NotNull Collection<String> deletedImportedFiles) {
        StringBuilder result = new StringBuilder()
                .append("Merging css file '")
                .append(mergingFileName)
                .append("' finished!\n")
                .append(mergingResult);
        if (!deletedImportedFiles.isEmpty()) {
            result.append(Utils.formatList("Deleted imported files", deletedImportedFiles));
        }
        return result.toString();
    }

    private Collection<String> deleteImportedFiles(@NotNull final VirtualFile selectedVirtualFile, @NotNull final MergingResult mergingResult) {
        Application application = ApplicationManager.getApplication();
        return application.runWriteAction(new NullableComputable<Collection<String>>() {
                                              public Collection<String> compute() {
                                                  Collection<String> deletedFileNames = new ArrayList<String>();

                                                  for (VirtualFile virtualFile : selectedVirtualFile.getParent().getChildren()) {
                                                      String virtualFileName = virtualFile.getName();
                                                      if (!mergingResult.getMergedFiles().contains(virtualFileName)) {
                                                          continue;
                                                      }
                                                      try {
                                                          virtualFile.delete(null);
                                                          deletedFileNames.add(virtualFileName);
                                                      } catch (IOException e) {
                                                          e.printStackTrace();
                                                      }
                                                  }
                                                  return deletedFileNames;
                                              }
                                          }
        );
    }

    private Collection<String> getExcludeImportFilePaths(AnalyzeResult analyzeResult, List<JCheckBox> resultCheckBoxes) {
        Collection<String> result = new HashSet<String>(/*analyzeResult.getExcludeImportFileNamesWithCause().keySet()*/);
        for (JCheckBox checkBox : resultCheckBoxes) {
            if (!checkBox.isSelected()) {
                result.add(checkBox.getText());
            }
        }
        return result;
    }
}
