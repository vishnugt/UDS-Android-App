package in.uds.vishnugt.alpha3;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;

/**
 * The Class MonthYearPicker.
 *
 * @author SuRendra Reddy
 */
@SuppressLint("InflateParams")
public class MonthYearPicker {

    public static int MIN_YEAR = 2014;

    public static int MAX_YEAR = 2017;

    private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec" };

    private static final String[] MONTHS = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };

    private View view;
    private Activity activity;
    private AlertDialog.Builder builder;
    private AlertDialog pickerDialog;
    private boolean build = false;
    private NumberPicker monthNumberPicker;
    private NumberPicker yearNumberPicker;


    public MonthYearPicker(Activity activity) {
        this.activity = activity;
        this.view = activity.getLayoutInflater().inflate(R.layout.month_year_picker_view, null);
    }
    public void build(DialogInterface.OnClickListener positiveButtonListener, DialogInterface.OnClickListener negativeButtonListener) {
        this.build(-1, -1, positiveButtonListener, negativeButtonListener);
    }

    private int currentYear;

    private int currentMonth;
    public void build(int selectedMonth, int selectedYear, DialogInterface.OnClickListener positiveButtonListener,
                      DialogInterface.OnClickListener negativeButtonListener) {

        final Calendar instance = Calendar.getInstance();
        currentMonth = instance.get(Calendar.MONTH);
        currentYear = instance.get(Calendar.YEAR);

        if (selectedMonth > 11 || selectedMonth < -1) {
            selectedMonth = currentMonth;
        }

        if (selectedYear < MIN_YEAR || selectedYear > MAX_YEAR) {
            selectedYear = currentYear;
        }

        if (selectedMonth == -1) {
            selectedMonth = currentMonth;
        }

        if (selectedYear == -1) {
            selectedYear = currentYear;
        }

        builder = new AlertDialog.Builder(activity);
        builder.setView(view);

        monthNumberPicker = (NumberPicker) view.findViewById(R.id.monthNumberPicker);
        monthNumberPicker.setDisplayedValues(PICKER_DISPLAY_MONTHS_NAMES);

        monthNumberPicker.setMinValue(0);
        monthNumberPicker.setMaxValue(MONTHS.length - 1);

        yearNumberPicker = (NumberPicker) view.findViewById(R.id.yearNumberPicker);
        yearNumberPicker.setMinValue(MIN_YEAR);
        yearNumberPicker.setMaxValue(MAX_YEAR);

        monthNumberPicker.setValue(selectedMonth);
        yearNumberPicker.setValue(selectedYear);

        monthNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        builder.setTitle(activity.getString(R.string.SELECT_MONTH));
        builder.setPositiveButton(activity.getString(R.string.OKAY), positiveButtonListener);
        builder.setNegativeButton(activity.getString(R.string.NOT_OKAY), negativeButtonListener);
        build = true;
        pickerDialog = builder.create();

    }

    public void show() {
        if (build) {
            pickerDialog.show();
        } else {
            throw new IllegalStateException("Build picker before use");
        }
    }

    public int getSelectedMonth() {
        return monthNumberPicker.getValue();
    }

    public String getSelectedMonthName() {
        return MONTHS[monthNumberPicker.getValue()];
    }

    public String getSelectedMonthShortName() {
        return PICKER_DISPLAY_MONTHS_NAMES[monthNumberPicker.getValue()];
    }

    public int getSelectedYear() {
        return yearNumberPicker.getValue();
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setMonthValueChangedListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        monthNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    public void setYearValueChangedListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        yearNumberPicker.setOnValueChangedListener(valueChangeListener);
    }

    public void setMonthWrapSelectorWheel(boolean wrapSelectorWheel) {
        monthNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }
    public void setYearWrapSelectorWheel(boolean wrapSelectorWheel) {
        yearNumberPicker.setWrapSelectorWheel(wrapSelectorWheel);
    }

}