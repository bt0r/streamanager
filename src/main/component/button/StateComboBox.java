package main.component.button;

import services.LanguageService;

import javax.swing.*;

/**
 * Created by btor on 24/08/2016.
 */
public class StateComboBox extends CommonComboBox {
    private  LanguageService     trans   = LanguageService.getInstance();
    private DefaultComboBoxModel model = new DefaultComboBoxModel();

    public StateComboBox(){
        model.addElement(trans.getProp("common.enable"));
        model.addElement(trans.getProp("common.disable"));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setModel(model);
    }

    public DefaultComboBoxModel getDefaultModel(){
        return this.model;
    }
}
