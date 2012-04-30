package com.antew.tipcalc.widgets;

import java.math.BigDecimal;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.antew.tipcalc.DecimalDigitsInputFilter;
import com.antew.tipcalc.Main;
import com.antew.tipcalc.R;

public class HorizontalNumberPicker extends RelativeLayout {
    Button                 minus;
    Button                 plus;
    EditText               value;
    int                    precision;
    BigDecimal             step;
    BigDecimal             minValue;
    String                 type;
    HorizontalNumberPicker billTotal;
    HorizontalNumberPicker tipPercentage;
    HorizontalNumberPicker tipAmount;
    HorizontalNumberPicker numOfPeople;

    TextWatcher            valueTextWatcher;

    public HorizontalNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

        LayoutInflater.from(context).inflate(R.layout.horizontal_number_picker, this, true);

        minus = (Button)   findViewById(R.id.btnMinus);
        value = (EditText) findViewById(R.id.txtValue);
        plus  = (Button)   findViewById(R.id.btnPlus);
        minus.getBackground().setAlpha(85);
        plus.getBackground().setAlpha(85);

        InputFilter[] currency = { new DecimalDigitsInputFilter(5, precision) };
        value.setFilters(currency);

        hookupButton();
        createTextChangedListener();

    }

    @Override
    public String toString() {
        return value.toString();
    }

    public EditText getEditText() {
        return value;
    }

    public void setText(String text) {
        value.setText(text);
    }

    public void hideButtons() {
        plus.setVisibility(INVISIBLE);
        minus.setVisibility(INVISIBLE);
    }

    public void disableTextView() {
        value.setEnabled(false);
    }

    public void enableTextView() {
        value.setEnabled(true);
    }

    private void createTextChangedListener() {
        valueTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    value.removeTextChangedListener(this);
                    String valueToPass = s.toString();
                    BigDecimal val;

                    try {
                        val = new BigDecimal(s.toString()).setScale(precision, BigDecimal.ROUND_HALF_UP);
                    }
                    catch (NumberFormatException e) {
                        // Text field was blank or we couldn't parse it, don't try to calculate new values
                        return;
                    }

                    if (val.compareTo(minValue) < 0)
                    {
                        valueToPass = minValue.setScale(precision, BigDecimal.ROUND_HALF_UP).toString();
                        value.setText(valueToPass);
                    }

                    Main.calculateNewValues(valueToPass, type, precision);
                } finally {
                    value.addTextChangedListener(this);
                }

            }
        };

        value.addTextChangedListener(valueTextWatcher);

    }

    public void removeTextChangedListener() {
        value.removeTextChangedListener(valueTextWatcher);
    }

    public void addTextChangedListener() {
        value.addTextChangedListener(valueTextWatcher);
    }

    public String getText() {
        return value.getText().toString();
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalNumberPicker);

        type      = ta.getString(R.styleable.HorizontalNumberPicker_type);
        precision = Integer.parseInt(ta.getString(R.styleable.HorizontalNumberPicker_precision));
        step      = new BigDecimal(ta.getString(R.styleable.HorizontalNumberPicker_step));
        minValue  = new BigDecimal(ta.getString(R.styleable.HorizontalNumberPicker_minValue));

        ta.recycle();
    }

    private void hookupButton() {
        minus.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    BigDecimal bd = new BigDecimal(value.getText().toString()).setScale(precision, BigDecimal.ROUND_HALF_UP);
                    bd = bd.subtract(step);

                    value.setText(bd.toString());

                } catch (Exception e) {
                    value.setText("0");
                }
            }
        });

        plus.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                try {
                    BigDecimal val = new BigDecimal(value.getText().toString()).setScale(precision, BigDecimal.ROUND_HALF_UP);
                    val = val.add(step);
                    value.setText(val.toString());
                } catch (Exception e) {
                    value.setText("0");
                }
            }
        });

    }

}
