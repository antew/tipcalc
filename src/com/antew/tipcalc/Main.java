package com.antew.tipcalc;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.antew.tipcalc.widgets.HorizontalNumberPicker;

public class Main extends SherlockActivity {
    private static final int       ROUND_MODE  = BigDecimal.ROUND_HALF_UP;
    static BigDecimal              mBillTotal;
    static BigDecimal              mPerPerson;
    static BigDecimal              mTipAmount;
    static HorizontalNumberPicker  billTotal;
    static HorizontalNumberPicker  tipPercentage;
    static HorizontalNumberPicker  tipAmount;
    static HorizontalNumberPicker  numOfPeople;
    static TextView                roundedNote1;
    static TextView                roundedNote2;
    static TextView                perPersonTip;
    static TextView                perPersonTotal;
    static TextView                totalTip;
    static TextView                totalWithTip;
    public static final BigDecimal ONE_HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN).setScale(2, ROUND_MODE);

    public enum text_types {
        bill_total, tip_percentage, number_of_people, tip_amount
    };

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("billTotal", billTotal.getText());
        savedInstanceState.putString("tipPercentage", tipPercentage.getText());
        savedInstanceState.putString("tipAmount", tipAmount.getText());
        savedInstanceState.putString("numOfPeople", numOfPeople.getText());
        savedInstanceState.putString("perPersonTip", perPersonTip.getText().toString());
        savedInstanceState.putString("perPersonTotal", perPersonTotal.getText().toString());
        savedInstanceState.putString("totalTip", totalTip.getText().toString());
        savedInstanceState.putString("totalWithTip", totalWithTip.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        removeTextChangedListeners();
        
        billTotal    .setText(savedInstanceState.getString("billTotal"));
        tipPercentage.setText(savedInstanceState.getString("tipPercentage"));
        tipAmount    .setText(savedInstanceState.getString("tipAmount"));
        numOfPeople  .setText(savedInstanceState.getString("numOfPeople"));

        perPersonTip  .setText(savedInstanceState.getString("perPersonTip"));
        perPersonTotal.setText(savedInstanceState.getString("perPersonTotal"));
        totalTip      .setText(savedInstanceState.getString("totalTip"));
        totalWithTip  .setText(savedInstanceState.getString("totalWithTip"));

        addTextChangedListeners();

    }

    public static void toggleRoundedIndicators(boolean showIndicators) {
        roundedNote1.setVisibility(showIndicators ? View.VISIBLE : View.INVISIBLE);
        roundedNote2.setVisibility(showIndicators ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        billTotal      = (HorizontalNumberPicker) findViewById(R.id.billTotal);
        tipPercentage  = (HorizontalNumberPicker) findViewById(R.id.tipPercentage);
        tipAmount      = (HorizontalNumberPicker) findViewById(R.id.tipAmount);
        numOfPeople    = (HorizontalNumberPicker) findViewById(R.id.numOfPeople);
        roundedNote1   = (TextView) findViewById(R.id.rounded_note);
        roundedNote2   = (TextView) findViewById(R.id.rounded_note2);
        perPersonTip   = (TextView) findViewById(R.id.perPersonTip);
        perPersonTotal = (TextView) findViewById(R.id.perPersonTotal);
        totalTip       = (TextView) findViewById(R.id.totalTip);
        totalWithTip   = (TextView) findViewById(R.id.totalWithTip);

        if (savedInstanceState == null) {
            Reset();

        } else {
            removeTextChangedListeners();
            billTotal     .setText(savedInstanceState.getString("billTotal"));
            tipPercentage. setText(savedInstanceState.getString("tipPercentage"));
            tipAmount     .setText(savedInstanceState.getString("tipAmount"));
            numOfPeople   .setText(savedInstanceState.getString("numOfPeople"));
            perPersonTip  .setText(savedInstanceState.getString("perPersonTip"));
            perPersonTotal.setText(savedInstanceState.getString("perPersonTotal"));
            totalTip      .setText(savedInstanceState.getString("totalTip"));
            totalWithTip  .setText(savedInstanceState.getString("totalWithTip"));

            addTextChangedListeners();

        }
    }

    public void removeTextChangedListeners() {
        billTotal    .removeTextChangedListener();
        tipPercentage.removeTextChangedListener();
        tipAmount    .removeTextChangedListener();
        numOfPeople  .removeTextChangedListener();
    }

    public void addTextChangedListeners() {
        billTotal    .addTextChangedListener();
        tipPercentage.addTextChangedListener();
        tipAmount    .addTextChangedListener();
        numOfPeople  .addTextChangedListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            Reset();
        }
        return true;
    }

    public void Reset() {

        removeTextChangedListeners();
        
        billTotal     .setText("25.00");
        tipPercentage .setText("20");
        tipAmount     .setText("5.00");
        numOfPeople   .setText("2");
        perPersonTip  .setText("$2.50");
        perPersonTotal.setText("$15.00");
        totalTip      .setText("$5.00");
        totalWithTip  .setText("$30.00");
        
        billTotal.requestFocus();
        addTextChangedListeners();

    }

    public static void calculateNewValues(String string, String type, int precision) {
        BigDecimal bBillTotal;
        BigDecimal bTipPercentage;
        BigDecimal iNumberOfPeople;
        BigDecimal bTipAmount;

        // Hopefully avoid any NumberFormatExceptions between
        // the combination of this and the InputFilter
        if (billTotal.getText().trim().equals("")   || tipPercentage.getText().trim().equals("") 
         || numOfPeople.getText().trim().equals("") || tipAmount.getText().trim().equals("")) {
            Log.i("calculateNewValues", "One of the values was blank, exiting early");
            return;
        }

        try {
            bBillTotal      = new BigDecimal(billTotal.getText()).setScale(2, ROUND_MODE);
            bTipPercentage  = new BigDecimal(tipPercentage.getText()).setScale(2, ROUND_MODE);
            iNumberOfPeople = new BigDecimal(numOfPeople.getText()).setScale(0, ROUND_MODE);
            bTipAmount      = new BigDecimal(tipAmount.getText()).setScale(2, ROUND_MODE);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage().toString());
        }

        switch (text_types.valueOf(type)) {
        case bill_total:
            calculateBillTotal(string, bTipPercentage, iNumberOfPeople);
            break;

        case number_of_people:
            calculateNumberOfPeople(string, bBillTotal, bTipAmount);
            break;

        case tip_amount:
            calculateTipAmount(bBillTotal, iNumberOfPeople, new BigDecimal(string));
            break;

        case tip_percentage:
            calculateTipPercentage(string, bBillTotal, iNumberOfPeople);
            break;
        }

        updateTotals();

    }

    private static void updateTotals() {
        NumberFormat nf     = NumberFormat.getCurrencyInstance(Locale.getDefault());
        BigDecimal   pp     = new BigDecimal(perPersonTotal.getText().toString());
        BigDecimal   people = new BigDecimal(numOfPeople.getText().toString());
        BigDecimal   result = pp.multiply(people).setScale(2, ROUND_MODE);

        mBillTotal = new BigDecimal(billTotal.getText().toString());
        mTipAmount = new BigDecimal(tipAmount.getText().toString());

        BigDecimal bPerPersonTip = result.subtract(mBillTotal).setScale(2, ROUND_MODE).divide(people, 2, ROUND_MODE);

        perPersonTip  .setText(nf.format(bPerPersonTip.doubleValue()));
        perPersonTotal.setText(nf.format(pp.doubleValue()));
        totalTip      .setText(nf.format(mTipAmount.doubleValue()));
        totalWithTip  .setText(nf.format(result.doubleValue()));

        toggleRoundedIndicators((!result.equals(mBillTotal.add(mTipAmount))));
        
    }

    private static void calculateBillTotal(String string, BigDecimal bTipPercentage, BigDecimal iNumberOfPeople) {
        BigDecimal bBillTotal   = new BigDecimal(string).setScale(2, ROUND_MODE);
        BigDecimal newTipAmount = bBillTotal.multiply(bTipPercentage.divide(ONE_HUNDRED)).setScale(2, ROUND_MODE);
        BigDecimal totalWithTip = bBillTotal.add(newTipAmount).setScale(2, ROUND_MODE);
        BigDecimal newPerPerson = totalWithTip.divide(iNumberOfPeople, 2, ROUND_MODE);

        tipAmount.removeTextChangedListener();

        tipAmount     .setText(newTipAmount.toString());
        perPersonTotal.setText(newPerPerson.toString());

        tipAmount.addTextChangedListener();
    }

    private static void calculateTipPercentage(String string, BigDecimal bBillTotal, BigDecimal iNumberOfPeople) {
        BigDecimal bTipPercentage = new BigDecimal(string).setScale(2, ROUND_MODE);
        BigDecimal newTipAmount   = bBillTotal.multiply(bTipPercentage.divide(ONE_HUNDRED)).setScale(2, ROUND_MODE);
        BigDecimal totalWithTip   = bBillTotal.add(newTipAmount).setScale(2, ROUND_MODE);
        BigDecimal newPerPerson   = totalWithTip.divide(iNumberOfPeople, 2, ROUND_MODE);

        // Stop the recursion
        tipAmount.removeTextChangedListener();

        tipAmount     .setText(newTipAmount.toString());
        perPersonTotal.setText(newPerPerson.toString());

        tipAmount.addTextChangedListener();
    }

    private static void calculateNumberOfPeople(String string, BigDecimal bBillTotal, BigDecimal bTipAmount) {
        BigDecimal bNumberOfPeople = new BigDecimal(string);
        BigDecimal totalWithTip    = bBillTotal.add(bTipAmount).setScale(2, ROUND_MODE);
        BigDecimal newPerPerson    = totalWithTip.divide(bNumberOfPeople, 2, ROUND_MODE);

        perPersonTotal.setText(newPerPerson.toString());
    }

    private static void calculateTipAmount(BigDecimal bBillTotal, BigDecimal iNumberOfPeople, BigDecimal bTipAmount) {
        // Do nothing if we were going to divide by zero
        if (bBillTotal.compareTo(BigDecimal.ZERO) > 0) {

            // Solve for the new tip percentage
            BigDecimal newTipPercentage = bTipAmount.divide(bBillTotal, 2, ROUND_MODE).multiply(ONE_HUNDRED).setScale(2, ROUND_MODE);
            BigDecimal totalWithTip     = bBillTotal.add(bTipAmount).setScale(2, ROUND_MODE);
            BigDecimal newPerPerson     = totalWithTip.divide(iNumberOfPeople, 2, ROUND_MODE);

            tipPercentage.removeTextChangedListener();

            tipPercentage .setText(newTipPercentage.toString());
            perPersonTotal.setText(newPerPerson.toString());

            tipPercentage.addTextChangedListener();
        }
    }

}