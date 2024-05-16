package us.byeol.uobpyonestwo.misc.swing;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JPromptTextField extends JTextField {

    private final String prompt;

    /**
     * Creates a new text field with the given prompt.
     *
     * @param prompt the prompt to show.
     */
    public JPromptTextField(String prompt) {
        super(prompt);
        this.prompt = prompt;
        this.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (isPrompt())
                    setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().trim().isEmpty())
                    setText(prompt);
            }

        });
    }

    @Override
    public String getText() {
        String actual = super.getText();
        return actual.equals(this.prompt) ? "" : super.getText();
    }

    /**
     * Checks if the current password is the prompt.
     *
     * @return the true if it is the prompt.
     */
    private boolean isPrompt() {
        return super.getText().equals(this.prompt);
    }

}
