package com.antew.tipcalc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputFilter;
import android.text.Spanned;

/**
 *
 * @author <a href="http://stackoverflow.com/a/8272212">Pinhassi on Stackoverflow</a>
 *
 */
public class DecimalDigitsInputFilter implements InputFilter {

    Pattern mPattern;

    public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        if (digitsAfterZero == 0) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}");
        }
        else
        {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches())
            return "";

        return null;
    }

}