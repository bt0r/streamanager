package main.component.frame;

import services.LanguageService;

import javax.swing.*;

/**
 * Created by btor on 31/10/2016.
 */
public class RestartDialog {
    private LanguageService trans = LanguageService.getInstance();

    public RestartDialog() {
        JOptionPane op = new JOptionPane();
        op.showMessageDialog(null, trans.getProp("settings.restart"), trans.getProp("severity.warning"), JOptionPane.WARNING_MESSAGE);
        System.exit(0);
    }
}
