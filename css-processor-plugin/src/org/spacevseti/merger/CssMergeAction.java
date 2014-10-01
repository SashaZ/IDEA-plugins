package org.spacevseti.merger;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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

        int okCancelDialogResult = Messages.showOkCancelDialog(project, "Will merge file '" + mergingFileName + "'?", "Information", Messages.getQuestionIcon());
        if (Messages.CANCEL == okCancelDialogResult) {
            return;
        }
        Pair<String, Boolean> stringBooleanPair = Messages.showInputDialogWithCheckBox("Message", "Title", "checkbox text", true, true, null, "initial value", null);
        System.out.println("stringBooleanPair = " + stringBooleanPair);

        try {
            File mergingFile = new File(file.getCanonicalPath());
            MergingResult mergingResult = new CssMerger(mergingFile).mergeTemp();
            Messages.showMessageDialog(project, "Merging css file '" + mergingFileName + "' finished!" + mergingResult,
                    "Information", Messages.getInformationIcon());
        } catch (IOException e1) {
            Messages.showErrorDialog(project, "Merging css file '" + mergingFileName + "' isn't finished!\n" + e1.getMessage(), "Error");
        }
    }
}