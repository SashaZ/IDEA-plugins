package org.spacevseti.merger;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.CheckBoxList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spacevseti.cssmerger.AnalyzeResult;
import org.spacevseti.cssmerger.StringConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by space on 02.10.14.
 * Version 1
 */
public class MessagesExt extends Messages {
    public static class CheckBoxListDialog extends InputDialog {
        private final AnalyzeResult analyzeResult;
        private CheckBoxList<String> checkboxes;
        private JCheckBox agreeRemoveImportedFiles;

        public CheckBoxListDialog(@Nullable Project project,
                                  String message,
                                  String title,
                                  @Nullable Icon icon,
                                  @NotNull AnalyzeResult analyzeResult) {
            super(project, message, title, icon, null, null, new String[]{OK_BUTTON, CANCEL_BUTTON}, 0);
            this.analyzeResult = analyzeResult;
            fillCheckBoxes();
            show();
        }

        protected JPanel createMessagePanel() {
            JPanel messagePanel = super.createMessagePanel();
            if (myMessage != null) {
                JComponent textComponent = createTextComponent();
                messagePanel.add(textComponent, BorderLayout.NORTH);
            }

            checkboxes = new CheckBoxList<String>();
            checkboxes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            messagePanel.add(checkboxes, BorderLayout.CENTER);

            this.agreeRemoveImportedFiles = new JCheckBox(StringConstants.AGREE_REMOVE_IMPORTED_FILES.getValue(), false);
            messagePanel.add(agreeRemoveImportedFiles, BorderLayout.SOUTH);

            return messagePanel;
        }

        private void fillCheckBoxes() {
            Set<String> importFileNames = analyzeResult.getImportFileNames();
            Map<String, String> excludeImportFileNamesWithCause = analyzeResult.getExcludeImportFileNamesWithCause();
            for (String importFileName : importFileNames) {
                String checkBoxText = importFileName;
                boolean checkBoxSelected = true;
                if (excludeImportFileNamesWithCause.containsKey(importFileName)) {
//                    checkBoxText += " (" + excludeImportFileNamesWithCause.get(importFileName) + ")";
                    checkBoxSelected = false;
                }
                checkboxes.addItem(importFileName, checkBoxText, checkBoxSelected);
            }
        }

        public Collection<String> getExcludeImportFilePaths() {
            if (getExitCode() != 0) {
                return null;
            }
            checkboxes.setSelectionInterval(0, analyzeResult.getImportFileNames().size() - 1);
            java.util.List<JCheckBox> selectedValues = checkboxes.getSelectedValuesList();

            Collection<String> result = new HashSet<String>();
            for (JCheckBox checkBox : selectedValues) {
                if (!checkBox.isSelected()) {
                    result.add(checkBox.getText());
                }
            }
            return result;
        }

        public boolean isAgreeRemoveImportedFiles() {
            return agreeRemoveImportedFiles != null && agreeRemoveImportedFiles.isSelected();
        }
    }
}
