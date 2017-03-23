package kr.ac.hansung.a3scalendar;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ieem5 on 2017-02-22.
 */

public class AddScheduleDialogFragment extends DialogFragment  {



    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private  static final  int REQUEST_READ_CALENDAR_PERMISSION = 4;
    private  static final  int REQUEST_WRITE_CALENDAR_PERMISSION = 5;

    public static final String DIALOG_START_TIME = "DialogStartTime";
    public static final String DIALOG_FINISH_TIME = "DialogFinishTime";
    public static final int REQUEST_START_TIME = 6;
    public static final int REQUEST_FINISH_TIME = 7;

    AlertDialog.Builder  builder;
    private EditText ScheduleEditText;//스제줄명 입력
    private  EditText SummaryScheduleEditText;//소제목 입력
    private Button startTimeBtn;//시작 시간
    private Button finishTimeBtn;// 종료 시간
    private Button selectLocationBtn;//위치 선택 버튼
    private Button selectSharerBtn;
    private  String selectedDate; // '2017년 1월 17일 일정'
    private TextView startTimeTV;
    private  TextView finishTimeTv;
    private  Button addScheduleBtn;
    //2017년 1월 17일 일정'   --> 선택한 날짜 받아와야 함.

    Date date;

   // long changedStartTime;
    //long changedFinishTime;

    int hour ;
    int minute;
    String AM_PM ;
    String dateClean;
    String year;
    String month ;
    String day ;
    long startMillis = 0;
    long endMillis = 0;
    long calID=0;
    Calendar endTime  = Calendar.getInstance();
    Calendar beginTime = Calendar.getInstance();
    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.fragment_add_schedule,null);
        ScheduleEditText = (EditText)v.findViewById(R.id.scheduleEditText);
        SummaryScheduleEditText = (EditText)v.findViewById(R.id.sumaaryScheduleEditText);
        startTimeBtn= (Button)v.findViewById(R.id.startTimeBtn);
        finishTimeBtn =(Button)v.findViewById(R.id.finishTimeBtn);
        selectLocationBtn =(Button)v.findViewById(R.id.selectLocationBtn);
        selectSharerBtn =(Button)v.findViewById(R.id.selectSharerBtn);
        startTimeTV=(TextView) v.findViewById(R.id.startTimeTv);
        finishTimeTv=(TextView) v.findViewById(R.id.finishTimeTv);
        addScheduleBtn = (Button)v.findViewById(R.id.addBtn);


        //시작 시간 TimePicker에서 고르기
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                FragmentManager manager = getFragmentManager();
                TimePickerFragment startTimeDialog =  new TimePickerFragment();
                startTimeDialog.setTargetFragment(AddScheduleDialogFragment.this,REQUEST_START_TIME);
                startTimeDialog.show(manager,DIALOG_START_TIME);

            }
        });

        // 종료 시간 TimePicker에서 고르기
        finishTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                FragmentManager manager = getFragmentManager();
                TimePickerFragment finishTimeDialog = new TimePickerFragment();
                finishTimeDialog.setTargetFragment(AddScheduleDialogFragment.this,REQUEST_FINISH_TIME);
                finishTimeDialog.show(manager,DIALOG_FINISH_TIME);

            }
        });

        //스케줄 추가 버튼
        //추가 버튼 누르면 입력된 내용 모두가 저장되면서 대화창 종료
        addScheduleBtn.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //스케줄 추가 버튼 클릭
                //캘린더 프로바이더로 이벤트를 캘린더에 저장해야 함.

                // Run query
                Cursor cur = null;
                ContentResolver cr = getActivity().getContentResolver();
                Uri uri = CalendarContract.Calendars.CONTENT_URI;
                String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                        + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                        + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
                //ieem52057@gmail.com
                //jadelee0303@gmail.com
                String[] selectionArgs = new String[] {"ieem52057@gmail.com", "com.google",
                        "ieem52057@gmail.com"};


// Submit the query and get a Cursor object back.

                if(ContextCompat.checkSelfPermission(AddScheduleDialogFragment.this.getActivity(),
                        android.Manifest.permission.READ_CALENDAR)
                        == PackageManager.PERMISSION_GRANTED&&
                        ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.WRITE_CALENDAR)
                                ==PackageManager.PERMISSION_GRANTED){

                    cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
                }

                else{
                    ActivityCompat.requestPermissions
                            (AddScheduleDialogFragment.this.getActivity(),
                                    new String[]{android.Manifest.permission.READ_CALENDAR}
                                    ,REQUEST_READ_CALENDAR_PERMISSION);
                    ActivityCompat.requestPermissions
                            (AddScheduleDialogFragment.this.getActivity(),
                                    new String[]{android.Manifest.permission.WRITE_CALENDAR}
                                    ,REQUEST_WRITE_CALENDAR_PERMISSION);

                    cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
                }
                //시간 11 : 31 AM 이런 식으로 텍스트뷰에 표시되어 있음.

                while(cur.moveToNext()){
                    // Get the field values
                    calID = cur.getLong(PROJECTION_ID_INDEX);


                }


                ContentResolver insertCr= getActivity().getContentResolver();
                ContentValues values = new ContentValues();
                values.put(Events.DTSTART, startMillis);
                values.put(Events.DTEND, endMillis);

                values.put(Events.TITLE,   ScheduleEditText.getText().toString());
                values.put(Events.DESCRIPTION,   SummaryScheduleEditText.getText().toString());
                values.put(Events.CALENDAR_ID, calID);
                values.put(Events.EVENT_TIMEZONE, "Asia/Seoul");
                Uri insertUri = insertCr.insert(Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
                long eventID = Long.parseLong(insertUri.getLastPathSegment());
//
// ... do something with event ID

                Toast.makeText(getActivity(),"Added Successfully!",Toast.LENGTH_SHORT).show();

            }
        });

        builder = new AlertDialog.Builder(getActivity());


        builder.setTitle(selectedDate)
                //.setItems()
                .setView(v);


        return builder.create();
    }

    //TimePicker에서 선택한 시간 받아오는 코드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       /* int hour ;
        int minute;
        String AM_PM ;*/
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_START_TIME) {
           // Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // mCrime.setDate(date);
            // updateDate();
            //TimePicker에서 사용자가 선택한 날짜
            //SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
           // String getDataString = format.format(date);
             hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
             minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);
            AM_PM = data.getStringExtra(TimePickerFragment.EXTRA_TIME_AM_PM);
            //long형인 시간을 Event로 처리할 수 있음
            //yyyyMMddHHmmss
            //'2017년1월17일'에서 년월일 뽑아오기



             if(minute<10)  startTimeTV.setText(hour+" : "+"0"+minute+" "+AM_PM);
            else startTimeTV.setText(hour+" : "+minute+" "+AM_PM);
            String dateClean= selectedDate.replaceAll("[^0-9]", "");//20170227 이런형식
            Log.i("tag"," startTime : dateClean : "+dateClean);
            //long형으로..

             year = dateClean.substring(0,4);
             month = dateClean.substring(4,6);
             day = dateClean.substring(6);



            beginTime.set(Integer.parseInt(year), (Integer.parseInt(month))-1, Integer.parseInt(day), hour, minute);
            startMillis = beginTime.getTimeInMillis();

            //년 월 일 정수로 분리해서 long형?
            //

            //changedStartTime =  changeTime(dateClean);
            //사용자가 선택한 시간을 long형으로 바꾸기
            //Date.getTime()은 long 반환함.


        }
        else if (requestCode == REQUEST_FINISH_TIME) {
            // Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // mCrime.setDate(date);
            // updateDate();
            //TimePicker에서 사용자가 선택한 날짜
            //SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            // String getDataString = format.format(date);
             hour = data.getIntExtra(TimePickerFragment.EXTRA_TIME_HOUR,0);
             minute = data.getIntExtra(TimePickerFragment.EXTRA_TIME_MINUTE,0);
            AM_PM = data.getStringExtra(TimePickerFragment.EXTRA_TIME_AM_PM);



            if(minute<10)  finishTimeTv.setText(hour+" : "+"0"+minute+" "+AM_PM);
            else finishTimeTv.setText(hour+" : "+minute+" "+AM_PM);
             dateClean= selectedDate.replaceAll("[^0-9]", "");//20170227 이런형식
            //changedFinishTime =  changeTime(dateClean);
             year = dateClean.substring(0,4);
             month = dateClean.substring(4,6);
             day = dateClean.substring(6);



            endTime .set(Integer.parseInt(year), (Integer.parseInt(month))-1, Integer.parseInt(day), hour, minute);
            endMillis = endTime .getTimeInMillis();
            Log.i("tag","finishTime : dateClean : "+dateClean);

        }


    }


  /*  @Override
    public void onStart() {

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height =ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onStart();
    }*/

    @Override
    public void onResume() {

        //Add Schedule 창 크기 mach_parent로 조절
       ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height =ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }


    public long    changeTime(String time){

        String []yMd = new String[9];

        //dateClean을 Date로 바꿔야 long으로 바꿀수 있음
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        Date changedDate = null;
        try{

            changedDate = originalFormat.parse(time);
        }catch (Exception e){
            e.printStackTrace();
        }
        //date->long으로
        return changedDate.getTime();


    }

   /*public void addBtnClicked(View view){

       switch (view.getId()){
           case R.id.addBtn:




               break;
       }
   }*/

}
