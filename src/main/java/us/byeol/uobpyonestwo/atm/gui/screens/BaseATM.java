package us.byeol.uobpyonestwo.atm.gui.screens;

import us.byeol.uobpyonestwo.atm.api.Screen;
import us.byeol.uobpyonestwo.atm.api.Authorized;

import javax.swing.JFrame;

public class BaseATM extends Screen implements Authorized {

    public BaseATM(JFrame parent) {
        super(parent);
    }

}