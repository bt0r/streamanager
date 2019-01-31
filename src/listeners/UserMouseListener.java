package listeners;


import chat.UserMenu;
import main.component.jxlabel.UserLabel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UserMouseListener implements MouseListener {

    public UserMouseListener() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        showPopup(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        showPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showPopup(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            UserMenu menu;
            if (e.getSource().getClass().getSimpleName().equals("UserLabel")){
                UserLabel viewerLabel = (UserLabel) e.getComponent();
                if(viewerLabel.getTwitchUsername() != null && !viewerLabel.getTwitchUsername().isEmpty()){
                    // Show user menu if label contain an usersame
                    menu = new UserMenu(viewerLabel.getTwitchUsername());
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }


    }


}
