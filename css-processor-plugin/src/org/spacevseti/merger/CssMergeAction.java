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

import java.io.File;
import java.io.IOException;

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
//            int dialogResult = Messages.showOkCancelDialog(project, "Will merge file '" + mergingFileName + "'?\n" + analyzeResult, "Information", Messages.getQuestionIcon());
            String message = "Will merge file '" + mergingFileName + "'?\n" + analyzeResult;
            int dialogResult = Messages.showYesNoCancelDialog(project, message, "Information",
                    "Yes", "Yes and remove imported files", "Cancel", Messages.getQuestionIcon());
            if (Messages.CANCEL == dialogResult) {
                return;
            }

            MergingResult mergingResult = new CssMerger(mergingFile).
                    setExcludeImportFilePaths(analyzeResult.getExcludeImportFileNamesWithCause().keySet())
                    .setRemoveImportedFiles(Messages.NO == dialogResult)
                    .merge();
            Messages.showMessageDialog(project, "Merging css file '" + mergingFileName + "' finished!" + mergingResult,
                    "Information", Messages.getInformationIcon());
        } catch (IOException e1) {
            Messages.showErrorDialog(project, "Merging css file '" + mergingFileName + "' isn't finished!\n" + e1.getMessage(), "Error");
        }
    }
}
