package com.kunzisoft.remembirthday;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by joker on 25/02/17.
 */

public class SelectBirthdayFragment extends DialogFragment {

    private final static String TAG = "SelectBirthdayFragment";

    private final static int YEAR_DELTA = 80;

    private Spinner spinnerMonth;
    private Spinner spinnerDay;
    private Spinner spinnerYear;

    private OnClickBirthdayListener onClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View root = inflater.inflate(R.layout.fragment_birthday_select, null);
        // Get views
        spinnerMonth = (Spinner) root.findViewById(R.id.fragment_birthday_select_month);
        spinnerDay = (Spinner) root.findViewById(R.id.fragment_birthday_select_day);
        spinnerYear = (Spinner) root.findViewById(R.id.fragment_birthday_select_year);

        // Create a calendar object and set year and month
        final Calendar calendar = new GregorianCalendar();

        // MONTHS
        String[] months = new DateFormatSymbols().getMonths();
        List<String> listMonth = new ArrayList<>(Arrays.asList(months));
        // Current position of month in array
        int positionCurrentMonth = calendar.get(Calendar.MONTH);

        // DAYS
        List<Integer> listDays = new ArrayList<>();
        // Get the number of days in that month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
        for(int i = 1; i<=daysInMonth; i++) {
            listDays.add(i);
        }
        // Current day of month
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // YEARS
        // Current year
        int currentYear = calendar.get(Calendar.YEAR);
        List<Integer> listYears = new ArrayList<>();
        for(int i = currentYear-YEAR_DELTA; i<currentYear+YEAR_DELTA; i++) {
            listYears.add(i);
        }

        // Spinners and Adapters
        final ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listMonth);
        spinnerMonth.setAdapter(monthAdapter);
        spinnerMonth.setSelection(positionCurrentMonth);

        ArrayAdapter<Integer> daysAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listDays);
        spinnerDay.setAdapter(daysAdapter);
        spinnerDay.setSelection(currentDay-1);

        ArrayAdapter<Integer> yearsAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listYears);
        spinnerYear.setAdapter(yearsAdapter);
        spinnerYear.setSelection(YEAR_DELTA);

        builder.setView(root)
                //TODO string
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, calcDate().toString());
                        if(onClickListener!=null)
                            onClickListener.onClickPositiveButton(calcDate());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(onClickListener!=null)
                            onClickListener.onClickNegativeButton(calcDate());
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /** TODO JAVADOC
     *
     * @return
     */
    private Date calcDate() {
        DateFormat dateFormat = new SimpleDateFormat("d MMMMM yyyy", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(
                    spinnerDay.getSelectedItem()+ " "+
                            spinnerMonth.getSelectedItem()+ " "+
                            spinnerYear.getSelectedItem());
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return date;
    }

    public OnClickBirthdayListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickBirthdayListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickBirthdayListener {
        void onClickPositiveButton(Date selectedDate);
        void onClickNegativeButton(Date selectedDate);
    }
}
