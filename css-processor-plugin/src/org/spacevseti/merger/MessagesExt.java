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
            super(project, message, title, icon, "", null, new String[]{OK_BUTTON, CANCEL_BUTTON}, 0);
            this.analyzeResult = analyzeResult;
            fillCheckBoxes();
        }

        /*
                @NotNull
                @Override
                protected Action[] createActions() {
                    final Action[] actions = new Action[myOptions.length];
                    for (int i = 0; i < myOptions.length; i++) {
                        String option = myOptions[i];
                        final int exitCode = i;
                        if (i == 0) { // "OK" is default button. It has index 0.
                            actions[i] = getOKAction();
                            actions[i].putValue(DEFAULT_ACTION, Boolean.TRUE);
                        }
                        else {
                            actions[i] = new AbstractAction(option) {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    close(exitCode);
                                }
                            };
                        }
                    }
                    return actions;
                }

                @Override
                protected void doOKAction() {
                    String inputString = myField.getText().trim();
                    close(0);
                }

                @Override
                protected JComponent createCenterPanel() {
                    return null;
                }

                @Override
                protected JComponent createNorthPanel() {
                    JPanel panel = new JPanel(new BorderLayout(15, 0));
                    if (myIcon != null) {
                        JLabel iconLabel = new JLabel(myIcon);
                        Container container = new Container();
                        container.setLayout(new BorderLayout());
                        container.add(iconLabel, BorderLayout.NORTH);
                        panel.add(container, BorderLayout.WEST);
                    }

                    JPanel messagePanel = createMessagePanel();
                    panel.add(messagePanel, BorderLayout.CENTER);

                    return panel;
                }*/
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

        @Nullable
        public java.util.List<JCheckBox> getResult() {
            if (getExitCode() == 0) {
                checkboxes.setSelectionInterval(0, analyzeResult.getImportFileNames().size() - 1);
                return checkboxes.getSelectedValuesList();
            }
            return null;
        }

        public boolean isAgreeRemoveImportedFiles() {
            return agreeRemoveImportedFiles != null && agreeRemoveImportedFiles.isSelected();
        }

        /*protected JComponent createTextComponent() {
            JComponent textComponent;
            if (BasicHTML.isHTMLString(myMessage)) {
                textComponent = createMessageComponent(myMessage);
            } else {
                JLabel textLabel = new JLabel(myMessage);
                textLabel.setUI(new MultiLineLabelUI());
                textComponent = textLabel;
            }
            textComponent.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            return textComponent;
        }*/

        /*

        protected JPanel createMessagePanel() {
            JPanel messagePanel = new JPanel(new BorderLayout());
            if (myMessage != null) {
                JComponent textComponent = createTextComponent();
                messagePanel.add(textComponent, BorderLayout.NORTH);
            }

            myField = createTextFieldComponent();
            messagePanel.add(myField, BorderLayout.SOUTH);

            return messagePanel;
        }

        protected JComponent createTextComponent() {
            JComponent textComponent;
            if (BasicHTML.isHTMLString(myMessage)) {
                textComponent = createMessageComponent(myMessage);
            }
            else {
                JLabel textLabel = new JLabel(myMessage);
                textLabel.setUI(new MultiLineLabelUI());
                textComponent = textLabel;
            }
            textComponent.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            return textComponent;
        }

        public JTextComponent getTextField() {
            return myField;
        }

        protected JTextComponent createTextFieldComponent() {
            return new JTextField(30);
        }

        @Override
        public JComponent getPreferredFocusedComponent() {
            return myField;
        }

        @Nullable
        public String getInputString() {
            if (getExitCode() == 0) {
                return myField.getText().trim();
            }
            return null;
        }*/
    }
}
