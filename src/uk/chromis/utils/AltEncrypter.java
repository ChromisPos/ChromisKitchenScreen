/*
 Chromis POS  - The New Face of Open Source POS
 Copyright (c) 2015 (John Lewis) Chromis.co.uk

 http://www.chromis.co.uk

 kitchen Screen v1.5

 This file is part of chromis & its associated programs

 chromis is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 chromis is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with chromis.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.chromis.utils;

import java.io.UnsupportedEncodingException;
import java.security.*;
import javax.crypto.*;

public class AltEncrypter {
    
    private Cipher cipherDecrypt;
    private Cipher cipherEncrypt;
    
    /** Creates a new instance of Encrypter
     * @param passPhrase */
    public AltEncrypter(String passPhrase) {
        
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(passPhrase.getBytes("UTF8"));
            KeyGenerator kGen = KeyGenerator.getInstance("DESEDE");
            kGen.init(168, sr);
            Key key = kGen.generateKey();

            cipherEncrypt = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, key);
            
            cipherDecrypt = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, key);

        } catch (UnsupportedEncodingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
        }
    }
    
    /**
     *
     * @param str
     * @return
     */
    public String encrypt(String str) {
        try {
            return StringUtils.byte2hex(cipherEncrypt.doFinal(str.getBytes("UTF8")));
        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException e) {
        }
        return null;
    }
    
    /**
     *
     * @param str
     * @return
     */
    public String decrypt(String str) {
        try {
            return new String(cipherDecrypt.doFinal(StringUtils.hex2byte(str)), "UTF8");
        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException e) {       
        }
        return null;
    }    
}
