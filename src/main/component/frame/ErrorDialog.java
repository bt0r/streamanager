/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package main.component.frame;

import services.LanguageService;

import javax.swing.*;

/**
 * Created by btor on 22/03/2017.
 */
public class ErrorDialog {
    private LanguageService trans = LanguageService.getInstance();

    public ErrorDialog(String message) {
        JOptionPane op = new JOptionPane();
        op.showMessageDialog(null, message, trans.getProp("severity.error"), JOptionPane.ERROR_MESSAGE);
    }
}
