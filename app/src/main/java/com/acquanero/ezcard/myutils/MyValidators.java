package com.acquanero.ezcard.myutils;

public class MyValidators {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isLongetThan(CharSequence word, int large){

        boolean rta;

        if(word.length() < large){
            rta = false;
        } else {
            rta = true;
        }

        return rta;

    }

}
