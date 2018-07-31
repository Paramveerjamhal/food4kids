package com.example.paramveerjamhal.food4kids.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.paramveerjamhal.food4kids.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
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

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        EditText date= (EditText) getActivity().findViewById(R.id.input_date);
        date.setText("");
        //tv1.setText("Year: "+view.getYear()+" Month: "+view.getMonth()+" Day: "+view.getDayOfMonth());
        //tv1.setText((view.getMonth()+1)+" "+view.getDayOfMonth()+", "+view.getYear());
        // Create a Date variable/object with user chosen date
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, 0, 0, 0);
        Date chosenDate = cal.getTime();
       // date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        // Format the date using style medium and US locale
      /*  DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        String df_medium_us_str = df_medium_us.format(chosenDate);*/


        /*
         * Use format method of SimpleDateFormat class to format the date.
         */
        String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

        System.out.println("Today is " + sdf.format(chosenDate));
        // Display the formatted date
        date.setText(date.getText() + sdf.format(chosenDate));

    }
}
