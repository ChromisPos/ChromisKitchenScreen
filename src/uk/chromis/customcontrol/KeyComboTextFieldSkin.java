/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.chromis.customcontrol;

import com.sun.javafx.scene.control.skin.TextFieldSkin;

/**
 *
 * @author cs_nd
 */
public class KeyComboTextFieldSkin extends TextFieldSkin {

	KeyComboTextField keyComboTextField;
	
	public KeyComboTextFieldSkin(KeyComboTextField keyComboTextField) {
		super(keyComboTextField);
		this.keyComboTextField = keyComboTextField;
	}
	
}
