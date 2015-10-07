/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.chromis.customcontrol;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.ModifierValue;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author cs_nd
 */
public class KeyComboTextField extends TextField {
	
	private KeyCodeCombination keyCodeCombo;
	
	KeyComboTextFieldSkin keyComboTextFieldSkin = new KeyComboTextFieldSkin(this);
	
	
	public KeyComboTextField(){
		super();
		setKeyFunctionality();
	}
	
	
	public KeyComboTextField( KeyCodeCombination setCombo ) {
		this();
		keyCodeCombo = setCombo;
		setText(keyCodeCombo.getName());
	}
	
	
	public void setKeyCodeCombination( KeyCodeCombination setCombo ) {
		keyCodeCombo = setCombo;
		setText(keyCodeCombo.getName());
	}
	
	public KeyCodeCombination getKeyCodeCombination() {
		return keyCodeCombo;
	}

	private void setKeyFunctionality() {
				
		super.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode theCode = event.getCode();
				if (theCode.isModifierKey()) {
					keyCodeCombo = null;
    				event.consume();
                } else if ( theCode == KeyCode.TAB ) {
                    // do nothing, allow the user to tab
                    // this means we won't be able to pick up tab as a key code, but c'est la vie
				} else { 
					ModifierValue shiftValue = (event.isShiftDown()) ? ModifierValue.DOWN : ModifierValue.UP;
					ModifierValue controlValue = (event.isControlDown()) ? ModifierValue.DOWN : ModifierValue.UP;
					ModifierValue altValue = (event.isAltDown()) ? ModifierValue.DOWN : ModifierValue.UP;
					ModifierValue metaValue = (event.isMetaDown()) ? ModifierValue.DOWN : ModifierValue.UP;
					ModifierValue shortcutValue = (event.isShortcutDown()) ? ModifierValue.DOWN : ModifierValue.UP;
					keyCodeCombo = new KeyCodeCombination(theCode, shiftValue, controlValue, altValue, metaValue, shortcutValue);
					setText(keyCodeCombo.getName());
    				event.consume();
				}
			}
		});
		super.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
			}
		});
		super.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				event.consume();
			}
		});
	}
	
	

	public boolean isComplete() {
		return (keyCodeCombo != null);
	}
	
	
	
}
