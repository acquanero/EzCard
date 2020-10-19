package com.acquanero.ezcard.myutils;

public class MyValidators {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isBetween(CharSequence word, int min, int max){

        boolean rta = false;

        if(word.length() >= min && word.length() <= max){
            rta = true;
        }

        return rta;

    }

    public final static boolean isOnlyChar(CharSequence word){
        boolean rta = false;

        if(word.toString().matches("[a-zA-Z]+")){
            rta = true;
        }

        return rta;
    }

    public final static boolean isOnlyNumber(CharSequence word){
        boolean rta = false;

        if(word.toString().matches("[0-9]+")){
            rta = true;
        }

        return rta;
    }



}
