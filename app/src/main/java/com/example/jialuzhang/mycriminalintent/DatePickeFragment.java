package com.example.jialuzhang.mycriminalintent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jialuzhang on 2017/3/12.
 */

public class DatePickeFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.example.jialuzhang.mycriminalintent.date";
    private Date mDate;
    private  void sendResult(int result_code){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(),result_code,intent);
    }
    //初始化的同时获得从另一个Fragment传递过来的date
    public static DatePickeFragment newInstance(Date date){
        Bundle argc = new Bundle();
        argc.putSerializable(EXTRA_DATE,date);
        DatePickeFragment datePickeFragment = new DatePickeFragment();
        datePickeFragment.setArguments(argc);
        return datePickeFragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        View v = getActivity().getLayoutInflater().inflate(R.layout.datepicke,null);
        DatePicker datePicker = (DatePicker)v.findViewById(R.id.date_Picker);
        datePicker.init(y, m, d, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
                Log.d("RESULT",mDate.toString());
                getArguments().putSerializable(EXTRA_DATE,mDate);
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setTitle("MyDialog")
                .setView(datePicker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }
}
