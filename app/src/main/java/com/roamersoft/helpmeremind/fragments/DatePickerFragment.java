package com.roamersoft.helpmeremind.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    /**
     * Callback
     */
    OnDateSelectListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * Container Activity must implement this interface
     */
    public interface OnDateSelectListener {
        public void onDateSelected(int year, int month, int day);
    }

    /**
     * This makes sure that the container activity has implemented the callback interface. If not, it throws an exception
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnDateSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDateSelectListener");
        }
    }

    /**
     * Sends the chosen date back to the Container Activity.
     * @param view The view where the request came from.
     * @param year The year chosen by the user.
     * @param month The month chosen by the user.
     * @param day The day chosen by the user.
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        mCallback.onDateSelected(year, month, day);

    }
}  