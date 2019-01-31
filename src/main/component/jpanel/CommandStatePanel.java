package main.component.jpanel;

import main.component.button.CommonCheckBox;
import main.component.button.StateComboBox;
import main.component.jxlabel.Label;
import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by btor on 24/08/2016.
 */
public class CommandStatePanel extends CommonPanel {
    private ConfigService   config    = ConfigService.getInstance();
    private LanguageService trans     = LanguageService.getInstance();
    private boolean         isWhisper = true;
    private CommonCheckBox whisperBox;
    private StateComboBox stateComboBox = new StateComboBox();

    public CommandStatePanel(String labelName) {


        Label label = new Label(labelName);
        whisperBox = new CommonCheckBox(trans.getProp("common.isWhisper"));
        label.setPreferredSize(new Dimension(150, 40));
        label.setBorder(new EmptyBorder(0, 10, 0, 10));
        setPreferredSize(new Dimension(300, 10));
        add(label);
        add(stateComboBox);
        add(whisperBox);
    }

    public void setWhisper(boolean isWhisper) {
        whisperBox.setSelected(isWhisper);

    }

    public void setWhisper(String isWhisper) {
        if (isWhisper.equals("true")) {
            whisperBox.setSelected(true);
        } else {
            whisperBox.setSelected(false);
        }

    }

    public JCheckBox getWhisperBox() {
        return this.whisperBox;
    }

    public void setForceWhisper(boolean toForce) {
        if (toForce) {
            whisperBox.setEnabled(false);
        } else {
            whisperBox.setEnabled(true);
        }

    }

    public void setState(String state) {
        if (state == null || state.equals("true")) {
            getComboBox().getDefaultModel().setSelectedItem(trans.getProp("common.enable"));
        } else {
            getComboBox().getDefaultModel().setSelectedItem(trans.getProp("common.disable"));

        }
    }

    public StateComboBox getComboBox() {
        return this.stateComboBox;
    }
}

