package com.roamersoft.helpmeremind;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mReminderInfoTextView = findViewById(R.id.reminder_info);
        this.mReminderTitle = findViewById(R.id.reminder_title);
        this.mReminderText = findViewById(R.id.reminder_text);

        FloatingTextButton callButton = (FloatingTextButton) findViewById(R.id.add_reminder_button);
        callButton.setOnClickListener(this);
    }

    /**
     * onClickListener for the first button in the button bar.
     * @param v The view where the request came from.
     */
    public void onButtonBarButton1Pressed(View v) {
        this.mDateAndTime = this.addHoursToCurrentTime(1);
        this.setReminderInfoTextView();
    }

    /**
     * onClickListener for the second button in the button bar.
     * @param v The view where the request came from.
     */
    public void onButtonBarButton2Pressed(View v) {
        this.mDateAndTime = this.addHoursToCurrentTime(6);
        this.setReminderInfoTextView();
    }

    /**
     * onClickListener for the third button in the button bar.
     * @param v The view where the request came from.
     */
    public void onButtonBarButton3Pressed(View v) {
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
        this.setReminderInfoTextView();
    }

    /**
     * Shows the date and time on which the reminder will be showed.
     */
    private void setReminderInfoTextView() {
        Resources res = getResources();
        this.mReminderInfoTextView.setText(String.format(res.getString(R.string.reminder_info), DateFormat.getDateInstance(DateFormat.LONG).format(this.mDateAndTime), DateFormat.getTimeInstance(DateFormat.SHORT).format(this.mDateAndTime)));
    }

    /**
     * onClickListener for the 'add reminder' button.
     * @param v The view where the request came from.
     */
    @Override
    public void onClick(View v) {
        this.scheduleReminder(this.mReminderTitle.getEditText().getText().toString(), this.mReminderText.getEditText().getText().toString());

        this.showToastOnTop(getString(R.string.notification_set_message));
    }

    /**
     * Schedules the notification.
     * @param reminderTitle The reminder title for in the notification.
     * @param reminderText The reminder text for in the notification.
     */
    private void scheduleReminder (String reminderTitle, String reminderText) {
        //alarm service
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Add data
        Intent intent = new Intent(this,  AlarmReceiver.class);
        intent.putExtra("mReminderTitle", reminderTitle);
        intent.putExtra("mReminderText", reminderText);

        //create broadcast
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //set alarm
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, this.mDateAndTime.getTime(), broadcast);
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
     * Shows a toast at the top of the screen
     * @param text The text that have to be showed.
     */
    private void showToastOnTop(String text){
        Toast toast= Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 15);
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
    
}
