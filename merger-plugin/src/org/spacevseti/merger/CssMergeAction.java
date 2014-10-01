package org.spacevseti.merger;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class CssMergeAction extends AnAction {
    private static final String CSS_EXTENSION = "css";

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        Project project = e.getData(PlatformDataKeys.PROJECT);
        VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);

        boolean visible = project != null && files != null && files.length > 0 && file!=null;
        if (visible) {
            visible = CSS_EXTENSION.equals(FilenameUtils.getExtension(StringUtils.defaultString(file.getCanonicalPath())));
        }
        // Visibility
//        e.getPresentation().setVisible(visible);
        // Enable or disable
        e.getPresentation().setEnabled(visible);

    }

    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String mergingFileName = file == null ? StringUtils.EMPTY : FilenameUtils.getName(file.getCanonicalPath());
        if (!CSS_EXTENSION.equals(FilenameUtils.getExtension(mergingFileName))) {
            Messages.showWarningDialog(project, "File name '" + mergingFileName + "' isn't valid. We can merge only " + CSS_EXTENSION + " file.", "Warning");
            return;
        }

        int okCancelDialogResult = Messages.showOkCancelDialog(project, "Will merge file '" + mergingFileName + "'?", "Information", Messages.getQuestionIcon());
        if (Messages.CANCEL == okCancelDialogResult) {
            return;
        }
        try {
            File mergingFile = new File(file.getCanonicalPath());
            ResultMerger resultMerger = new CssMerger(mergingFile).merge();
            Messages.showMessageDialog(project, "Merging css file '" + mergingFileName + "' finished!" + formatResultMerger(resultMerger),
                    "Information", Messages.getInformationIcon());
        } catch (IOException e1) {
            Messages.showErrorDialog(project, "Merging css file '" + mergingFileName + "' isn't finished!\n" + e1.getMessage(), "Error");
        }
    }

    private String formatResultMerger(ResultMerger resultMerger) {
        StringBuilder result = new StringBuilder();
        formatResultMergerList(result, "Merged files: ", resultMerger.getMergedFiles());
        formatResultMergerList(result, "Excluded files: ", resultMerger.getExcludedFiles());
        formatResultMergerList(result, "Not founded files: ", resultMerger.getNotFoundFiles());
        formatResultMergerList(result, "Warnings: ", resultMerger.getWarnings());
        return result.length()==0 ? StringUtils.EMPTY : "\n\n" + result.toString();
    }

    private void formatResultMergerList(StringBuilder result, String ChapterName, List<String> fileNameList) {
        if (!fileNameList.isEmpty()) {
            result.append(ChapterName).append("\n");
            for (String fileName : fileNameList) {
                result.append("\t- ").append(fileName).append("\n");
            }
            result.append("\n");
        }
    }
}
