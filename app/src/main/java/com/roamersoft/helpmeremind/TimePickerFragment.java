package com.roamersoft.helpmeremind;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    /**
     * Callback
     */
    OnTimeSelectListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * Container Activity must implement this interface
     */
    public interface OnTimeSelectListener {
        public void onTimeSelected(int hourOfDay, int minute);
    }

    /**
     * This makes sure that the container activity has implemented the callback interface. If not, it throws an exception
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnTimeSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTimeSelectListener");
        }
    }

    /**
     * Sends the chosen date back to the Container Activity.
     * @param view The view where the request came from.
     * @param hourOfDay The hour chosen by the user.
     * @param minute The minute chosen by the user.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCallback.onTimeSelected(hourOfDay, minute);
    }
}  