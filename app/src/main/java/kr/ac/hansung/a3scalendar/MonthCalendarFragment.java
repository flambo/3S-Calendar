package kr.ac.hansung.a3scalendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by Owner on 2017-01-11.
 */

public class MonthCalendarFragment extends Fragment implements MainActivity.onKeyBackPressedListener{
    private ScheduleManager SM;

    private  static final String DIALOG_DAY_SCHEDULE = "DialogDaySchedule";

    private  static final int REQUEST_DAY_SCHDULE = 0;

    private  static final String DIALOG_OPTION = "AddModifyFreezeDelete";

    private  static final int REQUEST_DIALOG_OPTION = 1;


    ArrayAdapter<CharSequence>yearAdapter;
    ArrayAdapter<CharSequence> monthAdapter;
    TextView yearTv;
    TextView monthTv;

    String yearStr;
    String monthStr;
    Calendar mCalToday;
    Calendar mCal;

    int selectedYear;
    int selectedMonth;

    private ArrayList<CalData> arrData;


    private ArrayList<ScheduleStr>arrSchedule;

    private GridView mGridView;
    private DateAdapter dateAdapter;
    ToggleButton MWTBtn;
    Button todayBtn;
    //private  AlertDialog.Builder alertDialogBuilder_Click;

   Spinner yearSpinner;
   Spinner monthSpinner;
    public MonthCalendarFragment newInstance(){
        MonthCalendarFragment fragment = new MonthCalendarFragment();
        return fragment;
    }


    @Override
    public void onBack() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        SM = new ScheduleManager();//메인에서 받아오는 것으로 변경
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar_month,container,false);

        MWTBtn =(ToggleButton)v.findViewById(R.id.MWToggleBtn);
        //MWTBtn.setVisibility(View.INVISIBLE);

        MWTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new WeekCalendarFragment().newInstance();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.commit();
            }
        });
//오늘 버튼
        todayBtn = (Button)v.findViewById(R.id.todayBtn);



        //Calendar객체 생성
        mCalToday = Calendar.getInstance();
        mCal = Calendar.getInstance();


        //달력 세팅
        setCalendarDate(0,mCal.get(Calendar.MONTH)+1,v);
        setYearSpinner(v);
        setMonthSpinner(v);
      //  mGridView = (GridView)v.findViewById(R.id.calGrid);
       // mGridView.setAdapter(dateAdapter);
        //dateAdapter.notifyDataSetChanged();



        return v;
    }




    public void setYearSpinner(final View v){

        //yearSpinner
         yearSpinner = (Spinner)v.findViewById(R.id.spinner_year);


        yearAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.date_year,
                android.R.layout.simple_spinner_item);

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        //현재 날짜에 맞는 년 스피너 초기값으로 설정
        //2017년부터 시작
        //2017 -> 0   2018 -> 1
        yearSpinner.setSelection( (mCalToday.get(Calendar.YEAR)-2017) );


        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                yearStr = yearAdapter.getItem(position).toString();
                //selectedYear = Integer.parseInt(yearStr); //선택한 년 정수로 저장


                selectedYear = Integer.parseInt(yearStr.replaceAll("[^0-9]", ""));
                //replaceAll("[^0-9]", "")   :숫자만 남기고 지운다.
                //String clean1 = string1.replaceAll("[^0-9]", "");
                Log.d("tag","연도 "+selectedYear+" 월 :"+selectedMonth);
                setCalendarDate(selectedYear,selectedMonth,v);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setMonthSpinner(final View v){

        //monthSpinner
         monthSpinner = (Spinner)v.findViewById(R.id.spinner_month);


        String[]monthArr = getResources().getStringArray(R.array.date_month);


        monthAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.date_month,
                android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        //현재 날짜에 맞는 월 스피너 초기값으로 설정
        monthSpinner.setSelection(mCalToday.get(Calendar.MONTH));


        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthStr = monthAdapter.getItem(position).toString();
                // selectedMonth=Integer.valueOf(monthStr); //선택한 월 정수로 저장

                selectedMonth = Integer.parseInt(monthStr.replaceAll("[^0-9]", ""));
                Log.d("tag","연도 "+selectedYear+" 월 :"+selectedMonth);
                setCalendarDate(selectedYear,selectedMonth,v);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void setCalendarDate(int year,int month,View v){

        arrData = new ArrayList<CalData>();
        arrSchedule = new ArrayList<ScheduleStr>();

        //1일에 맞는 요일을 세팅하기 위한 설정
        if(year ==0 )
            mCalToday.set(mCal.get(Calendar.YEAR),month-1,1);
        else{

            mCal.set(year,month-1,1);//이렇게 해야 윤년 아닌 2월이 29일까지 표시되는 것을 막을 수 있다..
            mCalToday.set(mCal.get(Calendar.YEAR),month-1,1);
        }

        /*
        * 요일 정보
        *
        * 1     ~   7
        * Calendar.DAY_OF_WEEK
        * 일요일~토요일
        * */

        int startday = mCalToday.get(Calendar.DAY_OF_WEEK); //요일을 리턴
        if(startday != 1){
            for(int i=0;i<startday-1;i++){
                arrData.add(null);
                arrSchedule.add(null);
            }

        }



        //요일은 +1 해야 되기 때문에 달력에 요일을 세팅할 때는 -1 해준다.
        mCal.set(Calendar.MONTH,month-1);
        for(int i=0;i<mCal.getActualMaximum(Calendar.DAY_OF_MONTH);i++){
            mCalToday.set(mCal.get(Calendar.YEAR),month-1,(i+1));
            arrData.add(new CalData(mCalToday.get(Calendar.YEAR),
                    mCalToday.get(Calendar.MONTH),(i+1),mCalToday.get(Calendar.DAY_OF_WEEK)));
            arrSchedule.add(new ScheduleStr( "a"+(i+1),"b"+(i+1),"c"+(i+1) ) );

        }
        dateAdapter = new DateAdapter(getActivity(),arrData,arrSchedule);
        mGridView = (GridView)v.findViewById(R.id.calGrid);

        if(dateAdapter == null){
            Log.i("date","DateAdapter is null");

        }
        if( mGridView.getAdapter() == null){
            Log.i("date","getAdapter is null");
        }
        mGridView.setAdapter(dateAdapter);











    }//END OF public void setCalendarDate(int year,int month,View v){
    @Override
    public void onResume() {
        super.onResume();

        todayBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                ArrayList arrData = new ArrayList<>();

                ArrayList arrSchedule = new ArrayList<>();
                    Calendar today = Calendar.getInstance();
                Calendar calToday = Calendar.getInstance();
                int startday = today.get(Calendar.DAY_OF_WEEK); //요일을 리턴


                //오늘인데 오늘버튼 누르면 동작하면 안됨
                String yearOfToday = today.get(Calendar.YEAR)+"년";
                String monthOfToday  = (today.get(Calendar.MONTH)+1)+"월";
                int      dateOfToday  = today.get(Calendar.DATE);
                if(  yearOfToday.equals(yearStr) && monthOfToday.equals(monthStr) &&
                        dateOfToday == (today.get(Calendar.DATE)) ){

                    return;
                }


                if(startday != 1){
                    for(int i=0;i<startday-1;i++){
                        arrData.add(null);
                        arrSchedule.add(null);
                    }

                }


              int month = today.get(Calendar.MONTH)+1;
                //요일은 +1 해야 되기 때문에 달력에 요일을 세팅할 때는 -1 해준다.
                today.set(Calendar.MONTH,month-1);
                for(int i=0;i<today.getActualMaximum(Calendar.DAY_OF_MONTH);i++){
                    calToday.set(today.get(Calendar.YEAR),month-1,(i+1));
                    arrData.add(new CalData(calToday.get(Calendar.YEAR),
                            calToday.get(Calendar.MONTH),(i+1),calToday.get(Calendar.DAY_OF_WEEK)));
                    arrSchedule.add(new ScheduleStr( "a"+(i+1),"b"+(i+1),"c"+(i+1) ) );

                }
                dateAdapter = new DateAdapter(getActivity(),arrData,arrSchedule);
               mGridView.setAdapter(dateAdapter);

                //오늘 버튼 눌렀을 때 년,월 스피너도 오늘 년,도로 설정되어 있어야 함.
                yearSpinner.setSelection( (calToday.get(Calendar.YEAR)-2017) );

                monthSpinner.setSelection(calToday.get(Calendar.MONTH));
                Toast.makeText(getActivity(),today.get(Calendar.YEAR)+"년"+(today.get(Calendar.MONTH)+1)+
                        "월"+today.get(Calendar.DATE)+"일",Toast.LENGTH_SHORT).show();


            }
        });
    }
   private  class DateAdapter extends BaseAdapter

    {

        // private ImageView imageView;
        private  TextView schedule1Tv;
        private TextView schedule2Tv;
        private  TextView schedule3Tv;
        private Context context;
        private ArrayList<CalData> arrData;
        private LayoutInflater inflater;
        private ArrayList<ScheduleStr>arrSchedule;

        public DateAdapter(Context c, ArrayList<CalData>arr, ArrayList<ScheduleStr>arrSchedule){
        this.context = c;
        this.arrData = arr;
        this.arrSchedule = arrSchedule;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

        public ArrayList<ScheduleStr> getArrSchedule() {
            return arrSchedule;
        }

        public void setArrSchedule(ArrayList<ScheduleStr> arrSchedule) {
            this.arrSchedule = arrSchedule;
        }

        public void setArrData(ArrayList<CalData> arrData) {
            this.arrData = arrData;
        }

        @Override
        public int getCount() {
        return arrData.size();
    }

        @Override
        public long getItemId(int position) {
        return position;
    }

        @Override
        public Object getItem(int position) {
        return arrData.get(position);
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_item, parent, false);
            //holder= new ViewHolder();
            //날짜 표시 TextView
            //holder.viewText = (TextView)convertView.findViewById(R.id.ViewText);
            // convertView.setTag(holder);
        }

        //holder = (ViewHolder)convertView.getTag();
         TextView ViewText = (TextView)convertView.findViewById(R.id.ViewText);

        //날씨 표시
        //ImageView imageView = (ImageView)convertView.findViewById(R.id.weatherImageView);
        if( arrSchedule.get(position)== null){
           // imageView.setVisibility(View.INVISIBLE);
        }else{
            //imageView.setImageResource(arrSchedule.get(position).getWeatherImage());
            //imageView.setVisibility(View.VISIBLE);
        }
        //스케줄 표시 TextView

        schedule1Tv = (TextView)convertView.findViewById(R.id.schedule1);
        schedule2Tv =  (TextView)convertView.findViewById(R.id.schedule2);
        schedule3Tv =  (TextView)convertView.findViewById(R.id.schedule3);

        if(arrData.get(position) == null){

            ViewText.setText("");
            //holder.viewText.setText("");
            schedule1Tv.setText("");
            schedule2Tv.setText("");
            schedule3Tv.setText("");

        }


        else
        {
            //앱 실행 기준 오늘 날짜 달력에 표시
            Calendar today = Calendar.getInstance();
            String year = today.get(Calendar.YEAR)+"년";
            String month = (today.get(Calendar.MONTH)+1)+"월";
            int      date = arrData.get(position).getDay();
            if(  year.equals(yearStr) && month.equals(monthStr) &&
                    date == (today.get(Calendar.DATE)) ){

                ViewText.setBackgroundColor(Color.BLUE);
                 Log.d("tag","calendarView:year "+year+"month "+month+"yearStr "+yearStr+"monthStr "+monthStr+"date"+date+"일");
            }
            else
                Log.d("tag","*****??calendarView: year "+year+"month "+month+"yearStr "+yearStr+"monthStr "+monthStr+"Date"+date+"일"
                        +"일치날짜 없음?!!");


            ViewText.setText(arrData.get(position).getDay()+"");
            // holder.viewText.setText(arrData.get(position).getDay()+"");
            schedule1Tv.setText(arrSchedule.get(position).getSchedule1());
            schedule2Tv.setText(arrSchedule.get(position).getSchedule2());
            schedule3Tv.setText(arrSchedule.get(position).getSchedule3());
            if(arrData.get(position).getDayofweek() == 1)
            {
                ViewText.setTextColor(Color.RED);
            }
            else if(arrData.get(position).getDayofweek() == 7)
            {
                ViewText.setTextColor(Color.BLUE);
            }
            else
            {
                ViewText.setTextColor(Color.BLACK);
            }
        }



            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(MainActivity.this,position+"번째",Toast.LENGTH_SHORT).show();
                    int ClickFlag=0;
                    //Log.d("tag",arrData.get(position).getDay()+" 일"+"\n 오늘날짜"+c.get(Calendar.DAY_OF_MONTH));
                    //arrData.get(position).getDay() null 에러???!!!

                    //날짜가 없는 빈 칸 클릭했을 때는 아무 반응 없어야 함.
                    if(  arrData.get(position) == null  ){
                        return;
                    }
                    String monthClean=monthStr.replaceAll("[^0-9]", "");
                    if( Integer.parseInt(monthClean)<10){
                        monthClean="0"+monthClean;
                    }
                    int day = arrData.get(position).getDay();
                    String dayStr=null;
                    if(day < 10 ){
                        dayStr="0"+day;


                    }else{
                        dayStr=day+"";
                    }
                    String date = yearStr+" "+monthClean+"월"+" "+dayStr+"일 일정";


                    FragmentManager manager = getFragmentManager();

                    String s1 = arrSchedule.get(position).getSchedule1();
                    String s2= arrSchedule.get(position).getSchedule2();
                    String s3 = arrSchedule.get(position).getSchedule3();
                    ArrayList<String>scheArr = new ArrayList<>();
                    scheArr.add(s1);
                    scheArr.add(s2);
                    scheArr.add(s3);
                    scheArr.add("뉴코아 쇼핑");
                    scheArr.add("롯대백화점 쇼핑");
                    scheArr.add("그랜드 쇼핑");

                    ClickDialogFragment dialog = new ClickDialogFragment();
                    dialog.setSelectedDate(date);
                    dialog.setDataArr(scheArr);
                    dialog.setTargetFragment(MonthCalendarFragment.this,REQUEST_DAY_SCHDULE);
                    dialog.show(manager,DIALOG_DAY_SCHEDULE);


                    //makeAlertDialog(position,ClickFlag,scheArr);


                    //dialog.dismiss();


                }
            });

            mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    //날짜가 없는 빈 칸 클릭했을 때는 아무 반응 없어야 함.
                    if(  arrData.get(position) == null  ){
                        return true;
                    }
                    //월,일이 한자리수일 경우 0을 붙여준다
                    String monthClean= monthStr.replaceAll("[^0-9]", "");
                    Log.i("tag","monthStr:"+monthStr);
                    if(Integer.parseInt(monthClean) <10){
                        monthClean = "0"+monthClean;
                    }
                    Log.i("tag","monthClean:"+monthClean);
                    //String dayClean= arrData.get(position).getDay().replaceAll("[^0-9]", "");
                   // Log.i("tag","dayClean:"+dayClean);
                  //  if( Integer.parseInt(dayClean) <10){
                        //dayClean = "0"+dayClean;
                   // }
                    //Log.i("tag","dayClean:"+dayClean);
                   //String date = yearStr+" "+monthStr+" "+arrData.get(position).getDay()+"일";

                    int day = arrData.get(position).getDay();
                    String dayStr=day+"";
                    if( day<10) dayStr="0"+day;
                    String date = yearStr+" "+monthClean+"월"+dayStr+"일";
                    Log.i("tag","date:"+date);
                    FragmentManager manager = getFragmentManager();
                    //  ClickDialogFragment dialog = ClickDialogFragment.newInstance(date,scheduleArr);
                    LongClickDialogFragment dialog = new LongClickDialogFragment();
                    dialog.setSelectedDate(date);
                    dialog.setTargetFragment(MonthCalendarFragment.this,REQUEST_DIALOG_OPTION);
                    dialog.show(manager,DIALOG_OPTION);


                    return true;
                }
            });


            return convertView;
    }


    }





}
