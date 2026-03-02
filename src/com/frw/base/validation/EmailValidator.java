package com.frw.base.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Maximiliano
 */
public class EmailValidator {
  
    public boolean validaEmail(String email){
        Pattern p = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");  
        Matcher m = p.matcher(email);  
        boolean matchFound = m.matches(); 
        if (matchFound)
            return true;
        else
            return false;
    }
    
}
