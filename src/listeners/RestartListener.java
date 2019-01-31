package listeners;

import main.component.frame.RestartDialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by btor on 31/10/2016.
 */
public class RestartListener extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        new RestartDialog();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
