package us.byeol.uobpyonestwo.atm.api;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Contains a list of components to be shown.
 */
public abstract class Screen extends ArrayList<JComponent> {

    private static final List<Screen> REGISTERED_SCREENS = new ArrayList<>();

    /**
     * Gets a screen that's been created already [would have been initialized at {@link us.byeol.uobpyonestwo.atm.gui.MainFrame#init()}
     * @param clazz
     * @return
     */
    public static Screen getScreen(Class<? extends Screen> clazz) {
        for (Screen screen : Screen.REGISTERED_SCREENS)
            if (screen.getClass() == clazz)
                return screen;
        return null;
    }

    private final JFrame parent;

    /**
     * Creates a new screen with the given parent.
     *
     * @param parent the parent frame.
     */
    public Screen(JFrame parent) {
        this.parent = parent;
        Screen.REGISTERED_SCREENS.add(this);
    }

    /**
     * Gets the parent frame of this screen.
     *
     * @return the frame.
     */
    public JFrame getParent() {
        return parent;
    }

}