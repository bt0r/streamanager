package listeners;

import main.component.button.StateComboBox;
import services.ConfigService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Created by btor on 25/08/16.
 */
public class CommandStateListener implements ActionListener {
    private ConfigService config  = ConfigService.getInstance();
    private StateComboBox comboBox;
    private String commandName;
    private JCheckBox whisperBox;

    public CommandStateListener(StateComboBox comboBox,JCheckBox whisperBox ,String commandName){
        this.comboBox = comboBox;
        this.commandName = commandName;
        this.whisperBox = whisperBox;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultComboBoxModel model = comboBox.getDefaultModel();
        int modelValue = model.getIndexOf(model.getSelectedItem());
        boolean isWhisper = whisperBox.isSelected();
        switch(commandName){
            default:
                // Default value, enable/disable only one command
                if (modelValue== 0) {
                    config.setProp("command."+commandName, "true");
                } else {
                    config.setProp("command."+commandName, "false");
                }
                break;
            case "poke":
                // enable/disable poke/invite commands
                if (modelValue == 0) {
                    config.setProp("command.poke", "true");
                    config.setProp("command.invite", "true");
                } else {
                    config.setProp("command.poke", "false");
                    config.setProp("command.invite", "false");
                }
            break;
            case "points":
                // enable/disable poke/invite commands
                if (modelValue == 0) {
                    config.setProp("command.points", "true");
                    config.setProp("command.rank", "true");
                } else {
                    config.setProp("command.points", "false");
                    config.setProp("command.rank", "false");
                }
                break;
            case "onJoin":
                // enable/disable onJoin/onPart messages
                if (modelValue == 0) {
                    config.setProp("command.onJoin", "true");
                    config.setProp("command.onPart", "true");
                } else {
                    config.setProp("command.onJoin", "false");
                    config.setProp("command.onPart", "false");
                }
            break;
        }
        if(isWhisper){
            config.setProp("command."+commandName+".whisper","true");
        }else{
            config.setProp("command."+commandName+".whisper","false");
        }
    }
}
