package kr.ac.hansung.a3scalendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



/**
 * Created by ieem5 on 2017-02-21.
 */

public class TimePickerFragment extends DialogFragment {



    public  static final String EXTRA_TIME_HOUR="kr.ac.hansung.a3scalendar.hour";
    public  static final String EXTRA_TIME_MINUTE="kr.ac.hansung.a3scalendar.minute";
    public  static final String EXTRA_TIME_AM_PM="kr.ac.hansung.a3scalendar.AM_PM";


    private  static final String ARG_TIME = "time";


    int selectedHour ;
    int selectedMinute ;
    String AM_PM;
    private  TimePicker mTimePicker;
    Date date2;

    public static TimePickerFragment newInstance(/*Date date*/){
       // Bundle args = new Bundle();
       // args.putSerializable(ARG_TIME,date);

        TimePickerFragment fragment = new TimePickerFragment();
        //fragment.setArguments(args);
        return  fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);

        mTimePicker = (TimePicker)v.findViewById(R.id.dialog_time_picker);
       // mTimePicker.setHour();//API 23( 마시멜로)이상

      /*  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //API 23 이상이면
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }else{
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }*/

        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {



                 if( hourOfDay<12)
                     AM_PM="AM";
                else
                 AM_PM="PM";
                selectedHour = hourOfDay;
                selectedMinute = minute;


                //getArguments().putSerializable(EXTRA_TIME, date2);
            }
        });


        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        sendResult(Activity.RESULT_OK,selectedHour,selectedMinute,AM_PM);
                    }
                })
                .create();
    }

    private  void sendResult(int resultCode, int hour, int minute,String AM_PM){
        //사용자가 대화상자의 확인 버튼을 누르면
        //TimePicker로부터 날짜르 받아서  CrimeFragment에 결과로 전달해야 한다.
        if( getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME_HOUR,hour);
        intent.putExtra(EXTRA_TIME_MINUTE,minute);
        intent.putExtra(EXTRA_TIME_AM_PM,AM_PM);


        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }


}
