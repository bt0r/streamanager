/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package updater;

import main.component.frame.mainFrame;

import javax.swing.*;

/**
 * Created by btor on 13/04/2017.
 */
public class ConfirmUpdate extends mainFrame {

    public ConfirmUpdate() {
        int         dialogButton = JOptionPane.YES_NO_OPTION;
        JOptionPane op           = new JOptionPane();
        int         dialogResult = op.showConfirmDialog(null, trans.getProp("update.new_version"), trans.getProp("severity.info"), dialogButton);

        if (dialogResult == JOptionPane.YES_OPTION) {
            new UpdaterFrame();
        }

    }
}
