/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package main.component.jxlabel;

import javax.swing.*;

/**
 * Created by btor on 01/01/2017.
 */
public class UserLabel extends Label {
    private String twitchUsername;

    public UserLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    public UserLabel(String text) {
        super(text);
    }

    public UserLabel(Icon image) {
        super(image);
    }

    public UserLabel() {
    }

    /**
     * Set twitch username to current JLabel
     * @param username
     * @return
     */
    public UserLabel setTwitchUsername(String username) {
        this.twitchUsername = username;
        this.setName(username);

        return this;
    }

    /**
     * Return the twitch username of current JLabel
     * @return
     */
    public String getTwitchUsername() {
        return this.twitchUsername;
    }
}
