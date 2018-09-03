package com.roamersoft.helpmeremind;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.roamersoft.helpmeremind.aSyncTasks.SaveReminderParams;
import com.roamersoft.helpmeremind.aSyncTasks.SaveReminderTask;
import com.roamersoft.helpmeremind.fragments.DatePickerFragment;
import com.roamersoft.helpmeremind.fragments.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.OnDateSelectListener,
        TimePickerFragment.OnTimeSelectListener, View.OnClickListener {

    /**
     * Views
     */
    private TextInputLayout mReminderTitle;
    private TextInputLayout mReminderText;
    private TextView mReminderInfoTextView;
    private View mViewToShowFragmentsOn;
    private TextView mReminderExampleTitle;
    private TextView mReminderExampleText;
    private TextView mReminderExampleTime;

    /**
     * Date and time in ints
     */
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHourOfDay;
    private int mMinute;

    /**
     * Date and time in Date object
     */
    private Date mDateAndTime;

    /**
     * Lifecycle callback keys
     */
    private static final String LIFECYCLE_CALLBACKS_REMINDER_TITLE_KEY = "mReminderTitle";
    private static final String LIFECYCLE_CALLBACKS_REMINDER_TEXT_KEY = "mReminderText";
    private static final String LIFECYCLE_CALLBACKS_REMINDER_INFO_KEY = "mReminderInfoTextView";
    private static final String LIFECYCLE_CALLBACKS_DATE_TIME__KEY = "mDateAndTime";
    private static final String LIFECYCLE_CALLBACKS_REMINDER_EXAMPLE_TITLE__KEY = "mReminderExampleTitle";
    private static final String LIFECYCLE_CALLBACKS_REMINDER_EXAMPLE_TEXT_KEY = "mReminderExampleText";
    private static final String LIFECYCLE_CALLBACKS_REMINDER_EXAMPLE_TIME_KEY = "mReminderExampleTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mReminderTitle = findViewById(R.id.reminder_input_title);
        this.mReminderText = findViewById(R.id.reminder_input_text);
        this.mReminderInfoTextView = findViewById(R.id.reminder_info);
        this.mReminderExampleTitle = findViewById(R.id.reminder_example_title);
        this.mReminderExampleText = findViewById(R.id.reminder_example_text);
        this.mReminderExampleTime = findViewById(R.id.notificationTime);

        if (savedInstanceState != null){
            this.mReminderTitle.getEditText().setText(savedInstanceState.getString(LIFECYCLE_CALLBACKS_REMINDER_TITLE_KEY));
            this.mReminderText.getEditText().setText(savedInstanceState.getString(LIFECYCLE_CALLBACKS_REMINDER_TEXT_KEY));
            this.mDateAndTime = new Date(savedInstanceState.getLong(LIFECYCLE_CALLBACKS_DATE_TIME__KEY));
            this.mReminderInfoTextView.setText(savedInstanceState.getString(LIFECYCLE_CALLBACKS_REMINDER_INFO_KEY));
            this.mReminderExampleTitle.setText(savedInstanceState.getString(LIFECYCLE_CALLBACKS_REMINDER_EXAMPLE_TITLE__KEY));
            this.mReminderExampleText.setText(savedInstanceState.getString(LIFECYCLE_CALLBACKS_REMINDER_EXAMPLE_TEXT_KEY));
            this.mReminderExampleTime.setText(savedInstanceState.getString(LIFECYCLE_CALLBACKS_REMINDER_EXAMPLE_TIME_KEY));
        }

        //OnChangeListeners
        FloatingTextButton callButton = (FloatingTextButton) findViewById(R.id.add_reminder_button);
        callButton.setOnClickListener(this);
        this.mReminderTitle.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeReminderExampleTitle(s);
            }
        });

        //mReminderTitleOnFocusChange
        this.mReminderTitle.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mReminderTitle.getEditText().setText(makeFirstLetterOfStringCapital(mReminderTitle.getEditText().getText().toString()));
                }
            }
        });

        this.mReminderText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeReminderExampleText(s);
            }
        });

        //mReminderTitleOnFocusChange
        this.mReminderText.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mReminderText.getEditText().setText(makeFirstLetterOfStringCapital(mReminderText.getEditText().getText().toString()));
                }
            }
        });
    }

    private void changeReminderExampleTitle(Editable s) {
        this.mReminderExampleTitle.setText(s);
    }

    private void changeReminderExampleText(Editable s) {
        this.mReminderExampleText.setText(s);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LIFECYCLE_CALLBACKS_REMINDER_TITLE_KEY, this.mReminderTitle.getEditText().getText().toString());
        outState.putString(LIFECYCLE_CALLBACKS_REMINDER_TEXT_KEY, this.mReminderText.getEditText().getText().toString());
        outState.putString(LIFECYCLE_CALLBACKS_REMINDER_INFO_KEY, this.mReminderInfoTextView.getText().toString());

        if (this.mReminderExampleTitle != null) {
            outState.putString(LIFECYCLE_CALLBACKS_REMINDER_EXAMPLE_TITLE__KEY, this.mReminderExampleTitle.getText().toString());
        }

        if (this.mReminderExampleText != null) {
            outState.putString(LIFECYCLE_CALLBACKS_REMINDER_EXAMPLE_TEXT_KEY, this.mReminderExampleText.getText().toString());
        }

        if (this.mReminderExampleTime != null) {
            outState.putString(LIFECYCLE_CALLBACKS_REMINDER_EXAMPLE_TIME_KEY, this.mReminderExampleTime.getText().toString());
        }

        if (this.mDateAndTime != null){
            outState.putLong(LIFECYCLE_CALLBACKS_DATE_TIME__KEY, this.mDateAndTime.getTime());
        }
    }

    /**
     * onClickListener for the first button in the button bar.
     * @param v The view where the request came from.
     */
    public void onButtonBarButton1Pressed(View v) {
        this.colorPressedButtonTextByView(v);
        this.mDateAndTime = this.addHoursToCurrentTime(0);
        this.mReminderExampleTime.setText(this.getTimeStringFromDate(this.mDateAndTime));
    }

    /**
     * onClickListener for the second button in the button bar.
     * @param v The view where the request came from.
     */
    public void onButtonBarButton2Pressed(View v) {
        this.colorPressedButtonTextByView(v);
        this.mDateAndTime = this.addHoursToCurrentTime(1);
        this.mReminderExampleTime.setText(this.getTimeStringFromDate(this.mDateAndTime));
    }

    /**
     * onClickListener for the third button in the button bar.
     * @param v The view where the request came from.
     */
    public void onButtonBarButton3Pressed(View v) {
        this.colorPressedButtonTextByView(v);
        this.mDateAndTime = this.addHoursToCurrentTime(3);
        this.mReminderExampleTime.setText(this.getTimeStringFromDate(this.mDateAndTime));
    }

    /**
     * onClickListener for the fourth button in the button bar.
     * @param v The view where the request came from.
     */
    public void onButtonBarButton4Pressed(View v) {
        this.colorPressedButtonTextByView(v);
        this.mViewToShowFragmentsOn = v;
        this.showDatePickerDialog(mViewToShowFragmentsOn);
    }

    /**
     * Shows the date picker dialog
     * @param v The view where the request came from.
     */
    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * Sets the text color of all buttons in the button bar to default
     * and sets the button from the given view to colorAccent.
     * @param v The view where the request came from.
     */
    private void colorPressedButtonTextByView(View v) {
        Button otherButton = findViewById(R.id.button_bar_button1);
        otherButton.setTextColor(getResources().getColor(R.color.colorTextAndIcons));

        otherButton = findViewById(R.id.button_bar_button2);
        otherButton.setTextColor(getResources().getColor(R.color.colorTextAndIcons));

        otherButton = findViewById(R.id.button_bar_button3);
        otherButton.setTextColor(getResources().getColor(R.color.colorTextAndIcons));

        otherButton = findViewById(R.id.button_bar_button4);
        otherButton.setTextColor(getResources().getColor(R.color.colorTextAndIcons));

        Button button = (Button) v;
        button.setTextColor(getResources().getColor(R.color.pressedButton));
    }

    /**
     * onCLickListener for the date picker dialog.
     * @param year The year chosen by the user.
     * @param month The month chosen by the user.
     * @param day The day chosen by the user.
     */
    @Override
    public void onDateSelected(int year, int month, int day) {
        this.mYear = year;
        this.mMonth = month+1;
        this.mDay = day;
        this.showTimePickerDialog(this.mViewToShowFragmentsOn);
    }

    /**
     * Shows the time picker dialog
     * @param v The view where the request came from.
     */
    private void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    /**
     * onCLickListener for the date picker dialog.
     * @param hourOfDay The hour chosen by the user.
     * @param minute The minute chosen by the user.
     */
    @Override
    public void onTimeSelected(int hourOfDay, int minute) {
        this.mHourOfDay = hourOfDay;
        this.mMinute = minute;
        this.mDateAndTime = this.convertToDateWithTime(this.mYear, this.mMonth, this.mDay, this.mHourOfDay, this.mMinute);
        this.mReminderExampleTime.setText(this.getTimeStringFromDate(this.mDateAndTime));
    }

    /**
     * onClickListener for the 'add reminder' button.
     * Checks if at least the title and time is set.
     * It sends the data to SaveReminderTask when this is true.
     * @param v The view where the request came from.
     */
    @Override
    public void onClick(View v) {
        if (this.mReminderTitle.getEditText().getText().toString().equals("") || this.mReminderTitle.getEditText().getText().toString().equals(" ")){
            MainActivity.showToastOnTop(this, getString(R.string.please_set_title_message));
        } else if (this.mDateAndTime == null) {
            MainActivity.showToastOnTop(this, getString(R.string.please_set_time_message));
        } else {
            new SaveReminderTask().execute(new SaveReminderParams(this, this.mReminderTitle.getEditText().getText().toString(), this.mReminderText.getEditText().getText().toString(), this.mDateAndTime));
            MainActivity.showToastOnTop(this, getString(R.string.reminder_set_message));
        }
    }

    /**
     * Adds the given hours to the current time.
     * @param hours Hours to add.
     * @return Returns the future date as a Date object.
     */
    private Date addHoursToCurrentTime (int hours) {
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, hours);

        return calendar.getTime();
    }

    /**
     * Returns a time string when the given date is today
     * Returns a date and time string when the given date is not today.
     * @param date Date object to convert.
     * @return Returns a String.
     */
    private String getTimeStringFromDate(Date date) {
        if (checkIfDatesAreIdentical(date, this.addHoursToCurrentTime(0))){
            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
            return localDateFormat.format(date);
        } else {
            SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return localDateFormat.format(date);
        }
    }

    /**
     * Returns a date string.
     * @param date Date object to convert.
     * @return Returns a String.
     */
    private String getDateStringFromDate(Date date) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return localDateFormat.format(date);
    }

    /**
     * Checks if the given dates are identical.
     * @param date1 First date.
     * @param date2 Second date.
     * @return Returns a boolean.
     */
    private Boolean checkIfDatesAreIdentical(Date date1, Date date2){
        if (this.getDateStringFromDate(date1).equals(this.getDateStringFromDate(date2))){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Shows a toast at the top of the screen.
     * @param context The context to use.
     * @param text The text that have to be showed.
     */
    public static void showToastOnTop(Context context, String text){
        Toast toast= Toast.makeText(context,
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
        toast.show();
    }

    /**
     * Converts the given ints to a date object.
     * @param year The given year.
     * @param month The given month.
     * @param day The given day.
     * @param hourOfDay The given hour.
     * @param minute The given minute.
     * @return Returns a Date object.
     */
    private Date convertToDateWithTime(int year, int month, int day, int hourOfDay, int minute){
        String dateInString = day+"/"+month+"/"+year+" "+hourOfDay+":"+minute;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dateToReturn = null;

        try {
            dateToReturn = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToReturn;
    }

    /**
     * Returns the given string with the first letter capitalized.
     * @param str The string to capitalized.
     * @return The capitalized string.
     */
    public String makeFirstLetterOfStringCapital(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


}
