package org.spacevseti.merger;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.spacevseti.cssmerger.AnalyzeResult;
import org.spacevseti.cssmerger.Analyzer;
import org.spacevseti.cssmerger.CssMerger;
import org.spacevseti.cssmerger.StringConstants;
import org.spacevseti.filemerger.MergingResult;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String mergingFileName = file == null ? StringUtils.EMPTY : FilenameUtils.getName(file.getCanonicalPath());
        if (!StringConstants.CSS_EXTENSION.getValue().equals(FilenameUtils.getExtension(mergingFileName))) {
            Messages.showWarningDialog(project, "File name '" + mergingFileName + "' isn't valid. We can merge only " + StringConstants.CSS_EXTENSION.getValue() + " file.", "Warning");
            return;
        }

        Analyzer analyzer = new Analyzer();
        assert file != null;
        File mergingFile = new File(file.getCanonicalPath());
        try {
            AnalyzeResult analyzeResult = analyzer.preMergeAnalyze(mergingFile);
            String message = "Will merge file '" + mergingFileName + "'?\n\n";
//            String message = "Will merge file '" + mergingFileName + "'?\n" + analyzeResult;
//            int dialogResult = Messages.showOkCancelDialog(project, "Will merge file '" + mergingFileName + "'?\n" + analyzeResult, "Information", Messages.getQuestionIcon());
            int dialogResult = Messages.CANCEL;
//            DialogWrapper.
//            CheckBoxList
//            Messages.showEditableChooseDialog(message, "", null, "1,2,3".split(","), null, null);
            MessagesExt.CheckBoxListDialog dialog = new MessagesExt.CheckBoxListDialog(project, message,
                    "Merge CSS", Messages.getQuestionIcon(), analyzeResult);
            dialog.show();
            List<JCheckBox> result = dialog.getResult();
            dialogResult = result == null ? Messages.CANCEL : Messages.OK;
//            dialogResult = Messages.showYesNoCancelDialog(project, message, "Information", "Yes", "Yes and remove imported files", "Cancel", Messages.getQuestionIcon());
            if (Messages.CANCEL == dialogResult) {
                return;
            }

            MergingResult mergingResult = new CssMerger(mergingFile).
                    setExcludeImportFilePaths(getExcludeImportFilePaths(analyzeResult, result))
//                    .setRemoveImportedFiles(Messages.NO == dialogResult)
                    .merge();
            Messages.showMessageDialog(project, "Merging css file '" + mergingFileName + "' finished!\n" + mergingResult,
                    "Information", Messages.getInformationIcon());
        } catch (IOException e1) {
            Messages.showErrorDialog(project, "Merging css file '" + mergingFileName + "' isn't finished!\n" + e1.getMessage(), "Error");
        }
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
