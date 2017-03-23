package kr.ac.hansung.a3scalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Owner on 2017-01-11.
 */

public class WeekCalendarFragment extends Fragment {


    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    private  static final String DIALOG_OPTION = "AddModifyFreezeDelete";

    private  static final int REQUEST_DIALOG_OPTION = 1;
    private ScheduleManager SM;

    private GridView mGridView;
    private WeekDateAdapter weekDateAdapter;


   // private ArrayList<CalData> weekArr;


    private ListView listView;
    private ArrayList<CalData> weekSchduleArr;
    //private     Calendar prevCalendar = Calendar.getInstance();
    ArrayAdapter<CharSequence> yearAdapter;
    ArrayAdapter<CharSequence> monthAdapter;

    String yearStr;
    String monthStr;
    ToggleButton MWTBtn;
    Calendar mCal;
    Calendar mCalToday;
    Calendar prevCal;
    Calendar nextCal;
    Calendar todayCal;

    Spinner yearSpinner;
    Spinner monthSpinner;
    TextView yearTv;
    TextView monthTv;
    Button selectDateBtn;
    Date date;
    TextView testTv;
    HashMap dateAndScheduleMap;

    public WeekCalendarFragment newInstance() {
        WeekCalendarFragment fragment = new WeekCalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        SM = new ScheduleManager();//메인에서 받아오는 것으로 변경


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar_week, container, false);
        mCal = Calendar.getInstance();
        mCal.add(Calendar.DATE, 1 - mCal.get(Calendar.DAY_OF_WEEK));//오늘 날짜의 주 첫번째 요일인 일요일 날짜로 mCal 세팅
        mCalToday = Calendar.getInstance();
        //달력 세팅

        yearSpinner = (Spinner) v.findViewById(R.id.spinner_year);
        monthSpinner = (Spinner) v.findViewById(R.id.spinner_month);

        yearTv = (TextView) v.findViewById(R.id.yearTv);
        monthTv = (TextView) v.findViewById(R.id.monthTv);

        testTv = (TextView) v.findViewById(R.id.testTv);

        selectDateBtn = (Button) v.findViewById(R.id.selectDateBtn);
        selectDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                date = new Date();
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(WeekCalendarFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);

            }
        });

        setYearSpinner(v);
        setMonthSpinner(v);
        //yearStr monthStr 이 왜 null이지? 캘린더뷰에서는 값이 잘 들어가 있는데..ㅜ
        yearStr = Integer.toString(mCalToday.get(Calendar.YEAR)) + "년";
        monthStr = Integer.toString(mCalToday.get(Calendar.MONTH) + 1) + "월";
        //yearStr = weekDateAdapter.getCalendar().get(Calendar.YEAR)+ "년";
       // monthStr=weekDateAdapter.getCalendar().get(Calendar.MONTH)+ "월";
        yearTv.setText(yearStr);
        monthTv.setText(monthStr);
        weekDateAdapter = new WeekDateAdapter(getActivity());

        weekDateAdapter.setWeekArrList(mCalToday); //1 주의 날짜 셋팅
        setWeekSchedule();//1주별 스케줄 세팅


        mGridView = (GridView) v.findViewById(R.id.weekGridView);
        mGridView.setAdapter(weekDateAdapter);


        //final WeekScheduleAdapter weekScheduleAdapter = new WeekScheduleAdapter(getActivity(), weekSchduleArr);
        listView = (ListView) v.findViewById(R.id.listView);
        //리스트 동적 생성 , 여러개 추가 가능
        final ArrayList<CalData> scheduleList = new ArrayList<>();

        //날짜를  주간달력에서 읽어오는 것이므로 날짜 형식은 String이면 됨.
        //하나의 날짜에 스케줄 여러 개이다. 이것을 어떻게 보관하는가...
        /*scheduleList.add(new CalData("2017219","10:00","am","월요일 회의 준비"));
        scheduleList.add(new CalData("2017220","11:00","am","화요일 회의 준비"));
        scheduleList.add(new CalData("2017220","18:00","pm","김나나와 식사"));
        scheduleList.add(new CalData("2017221","12:00","am","수요일 회의 준비1"));
        scheduleList.add(new CalData("2017221","13:00","am","수요일 회의 준비2"));
        scheduleList.add(new CalData("2017221","14:00","am","수요일 회의 준비3"));
        scheduleList.add(new CalData("2017222","15:30","am","목요일 회의 준비1"));
        scheduleList.add(new CalData("2017222","16:00","am","목요일 회의 준비2"));
        scheduleList.add(new CalData("2017222","17:00","am","목요일 회의 준비3"));
        scheduleList.add(new CalData("2017223","18:00","am","금요일 회의 준비1"));
        scheduleList.add(new CalData("2017223","18:30","am","금요일 회의 준비2"));
        scheduleList.add(new CalData("2017223","19:00","am","금요일 회의 준비3"));
        scheduleList.add(new CalData("2017224","20:00","am","토요일 회의 준비"));
        scheduleList.add(new CalData("2017225","21:00","am","영화 보기"));*/



        // ArrayAdapter<String>schduleArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,);
        // listView.setAdapter();

        //listView.setAdapter(weekScheduleAdapter);


        //날짜 클릭하면 그 날짜에 해당하는 스케줄 보이게 하기
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            ArrayAdapter<String> scheduleArrayAdapter;

            //날짜별로 일정 리스트 나오게 한다.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //날짜칸 클릭하면 2010년 2월 5일 이런식으로 클릭한 날짜칸의 날짜 Log로 출력!!
                String yearStr = yearTv.getText().toString();
                String monthStr = monthTv.getText().toString();
                Log.i("tag", "yearStr" + yearStr + "monthStr" + monthStr + " | " +
                        weekDateAdapter.weekArr.get(position).getDay() + "일");

                //replaceAll("[^0-9]", "")   :숫자만 남기고 지운다.
                //String clean1 = string1.replaceAll("[^0-9]", "");
                //년월일 합치기    2017220 이런 형식으로~~~
                String SelectedDate = yearStr.replaceAll("[^0-9]", "") + "" + monthStr.replaceAll("[^0-9]", "")
                        + weekDateAdapter.weekArr.get(position).getDay();
                Log.i("tag", "SelectedDate" + SelectedDate);




               /* ArrayList<String> sList = new ArrayList<String>();





                scheduleArrayAdapter =
                        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sList);
                listView.setAdapter(scheduleArrayAdapter);
                scheduleArrayAdapter.notifyDataSetChanged();*/

                //해당 날짜 스케줄 리스트뷰에 업로드...

                // updateListView()
                //어댑터 = new ArrayAdapter
                //리스트뷰.setAdapter(adapter);


                //weekScheduleAdapter.setWeekArr(weekSchduleArr);
                // weekScheduleAdapter.notifyDataSetChanged();
            }


        });

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //롱클릭시 캘린더뷰처럼 추가 수정 동결 삭제 알림창 뜨게 하기

           // 이전 다음 버튼 클릭해서 날짜 이동했을 때 , 날짜 표시가 2017년 13월 01일로 표기됨
            // 2018년 01월 01로 표기되어야 함
            //데이트 피커로 이동했을 때도 2017년 13월 01알로 표시됨
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String yearStr = yearTv.getText().toString();
                String monthStr = monthTv.getText().toString();
                //월,일이 한자리수일 경우 0을 붙여준다
                String monthClean= monthStr.replaceAll("[^0-9]", "");
                Log.i("tag","monthStr:"+monthStr);
                if(Integer.parseInt(monthClean) <10){
                    monthClean = "0"+monthClean;
                }
                Log.i("tag","monthClean:"+monthClean);


                int day = weekDateAdapter.weekArr.get(position).getDay();
                String dayStr=day+"";
                if( day<10) dayStr="0"+day;
               // String date = yearStr+" "+monthClean+"월"+dayStr+"일";
                Log.i("tag","date:"+date);
                //String date=yearStr+monthStr+weekDateAdapter.weekArr.get(position).getDay()+"일";
                //클릭한 날짜 하루 전날이 그 달의 마지막 날짜와 일치하면 그 날짜는 다음 달 날짜이므로
                //텍스트뷰에 출력된 달+1 해준다.
               Calendar monthCheckCal= Calendar.getInstance();
                String yearClean=yearStr.replaceAll("[^0-9]", "");
                monthCheckCal.set(Integer.parseInt(yearClean),Integer.parseInt(monthClean),day);
               // monthCheckCal.getActualMaximum(Calendar.DATE);
                //롱클릭한 날짜의 그리드뷰 인덱스가 0(1일이 일요일)이 아니라면.
                //1일~6일까지의 날짜만 이전 달 날짜와 같이 있으므로 day<7 조건 필요.
                //
                if( day<7 && position!=0){
                    Log.i("tag","not 0 position:"+position);
                    Log.i("tag","monthCheckCal:"+monthCheckCal.get(Calendar.YEAR)+"년"+(monthCheckCal.get(Calendar.MONTH)+1)
                            +"월"+monthCheckCal.get(Calendar.DAY_OF_MONTH)+"일");
                    Log.i("tag","getMaximumDay:"+position);
                    monthCheckCal.add(Calendar.MONTH,-1);
                    //1일은 position-1, 2일은 position-2

                    if(weekDateAdapter.weekArr.get(position-day).getDay()==
                            monthCheckCal.getActualMaximum(Calendar.DATE)){
                        //1일~6일까지 날짜 주는 이전달 날짜와 같이 있다.
                        // 그 중에서 이전달 날짜가 그 전달의 날짜의 마지막일이라면
                        // 1일~6일 눌렀을 때 (이전달+1)월로 해줘야 함. 아래는 그 과정
                        //만약 12월 31일이 있는 주라면, 다음 년도로 바꿔주고 1월로 바꿔줘야 함

                        int m = Integer.parseInt(monthClean);

                        if((monthCheckCal.get(Calendar.MONTH) )== (Calendar.DECEMBER)){
                            //이전달이 12월이라면 다음년도 1월로 바꿔주기
                            int y = Integer.parseInt(yearClean);
                            y+=1;
                            yearClean = Integer.toString(y);
                            yearStr = yearClean+"년";

                            monthClean="1";
                        }
                        else{

                            m+=1;
                            monthClean = Integer.toString(m);

                        }


                        if(Integer.parseInt(monthClean)<10) monthClean="0"+monthClean;
                        Log.i("tag","**monthClean:"+monthClean);
                    }
                }

                String date=yearStr+monthClean+"월"+dayStr+"일";
                FragmentManager manager = getFragmentManager();
                LongClickDialogFragment dialog = new LongClickDialogFragment();
                dialog.setSelectedDate(date);
                dialog.setTargetFragment(WeekCalendarFragment.this,REQUEST_DIALOG_OPTION);
                dialog.show(manager,DIALOG_OPTION);

                return true;
            }
        });
        MWTBtn = (ToggleButton) v.findViewById(R.id.MWToggleBtn);
        //MWTBtn.setVisibility(View.INVISIBLE);
        MWTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //addToBackStack()해놓고 불러오기?
                //이전에 만들어 놓은 MonthCalendarFragment 불러와야함. 새로 만들지 말고
               /* Fragment fragment = new MonthCalendarFragment().newInstance();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();*/
                ((MainActivity) getActivity()).ChangeFragment(R.id.calendars);
            }
        });

        Button prevBtn = (Button) v.findViewById(R.id.prev_week_btn);
        prevBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                prevCal = weekDateAdapter.getCalendar();
                weekDateAdapter.setPrevWeek();

                weekDateAdapter.setWeekArrList(prevCal);
                weekDateAdapter.notifyDataSetChanged();

                Log.i("tag", "PREV CLICK!" + prevCal.get(Calendar.YEAR) +
                        "/" + (prevCal.get(Calendar.MONTH) + 1) + "/" + prevCal.get(Calendar.DATE));

                Toast.makeText(getActivity(), "이전 버튼 클릭! " + prevCal.get(Calendar.YEAR) +
                        "/" + (prevCal.get(Calendar.MONTH) + 1) + "/" + prevCal.get(Calendar.DATE), Toast.LENGTH_SHORT).show();

                yearTv.setText(prevCal.get(Calendar.YEAR) + "년");
                monthTv.setText((prevCal.get(Calendar.MONTH) + 1) + "월");

            }
        });

        final Button nextBtn = (Button) v.findViewById(R.id.next_week_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            // Calendar nextCalendar = Calendar.getInstance();
            @Override
            public void onClick(View v) {


                nextCal=weekDateAdapter.getCalendar();
                weekDateAdapter.setNextWeek();

                weekDateAdapter.setWeekArrList(nextCal);
                weekDateAdapter.notifyDataSetChanged();
                //기준 날짜 변경!!!!! -7 일주일 전으로 !!!! 누를 때마다 이전주로
                Log.i("tag", "NEXT CLICK!" + nextCal.get(Calendar.YEAR) +
                        "/" + (nextCal.get(Calendar.MONTH) + 1) + "/" + nextCal.get(Calendar.DATE));

                Log.i("tag", "NEXT CLICK!*********" + nextCal.get(Calendar.YEAR) +
                        "/" + (nextCal.get(Calendar.MONTH) + 1) + "/" + nextCal.get(Calendar.DATE));

                weekDateAdapter.notifyDataSetChanged();
                // mGridView.setAdapter(weekDateAdapter);
                //오늘이 아닌 날짜 텍스트뷰DateView를 원래 색깔로 돌려놓는 코드가 notifyDataSetChanged를 쓰면
                //필요하고 mGridView.setAdapter(weekDateAdapter); 와 같이 어댑터를 다시 SET하면 필요 없다.
                //MonthCalendarFragement와 다른 점!!

                Toast.makeText(getActivity(), "다음 버튼 클릭! " + nextCal.get(Calendar.YEAR) +
                        "/" + (nextCal.get(Calendar.MONTH) + 1) + "/" + nextCal.get(Calendar.DATE), Toast.LENGTH_SHORT).show();

                yearTv.setText(nextCal.get(Calendar.YEAR) + "년");
                monthTv.setText((nextCal.get(Calendar.MONTH) + 1) + "월");


            }
        });

        Button todayBtn = (Button) v.findViewById(R.id.todayBtn);
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setCalendarDate(mCal.get(Calendar.YEAR),mCal.get(Calendar.MONTH)+1,v);
                Calendar todayCal = Calendar.getInstance();
                weekDateAdapter.setCalendar(todayCal);
                weekDateAdapter.setWeekArrList(todayCal);
               // weekDateAdapter.setWeekArr( weekDateAdapter.getWeekArr().weekArr);
                weekDateAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "오늘: " + todayCal.get(Calendar.YEAR) + "년" +
                        (todayCal.get(Calendar.MONTH) + 1) + "월" + todayCal.get(Calendar.DATE) + "일", Toast.LENGTH_SHORT).show();

                Log.i("#####date", todayCal.get(Calendar.DATE) + "일");
                yearTv.setText(todayCal.get(Calendar.YEAR) + "년");
                monthTv.setText((todayCal.get(Calendar.MONTH) + 1) + "월");


            }
        });


        return v;
    }

    //DatePicker에서 선택한 날짜 받아오는 코드
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            // mCrime.setDate(date);
            // updateDate();
            //DatePicker에서 사용자가 선택한 날짜
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            String getDataString = format.format(date);
            testTv.setText(getDataString);


            updateDate(date);

        }

    }

    public void updateDate(Date seletedDate) {
        //Calendar calendar = Calendar.getInstance();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(seletedDate);//사용자가 선택한 날짜를 얻어온다.

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);

        weekDateAdapter.setCalendar(calendar);
        weekDateAdapter.setWeekArrList(calendar);




        weekDateAdapter.notifyDataSetChanged();


        //사용자가 선택한 날짜가 1일~6일이면 그 전달의 날짜와 같이 표시가 된다.
        //이 경우에는 이전 달을 출력해야 한다.
        if( date<7) {

            //1월달을 선택했다면 0월이 되지 않고
            //작년 12월이 표기되게
            //ex) 2018년 1월 1일 골랐으면 2017년 12월로 표시되어야 함
            //오류:2018년 0월로 표시됨
            if( month== 1){
                //1월 선택했다면
               year = year-1;
                month = 12;
            }
            else{
                //monthTv.setText((month-1) + "월");
                month-=1;
            }


            Log.i("tag","month : "+month+"date : "+date);

        }


            monthTv.setText(month + "월");

             yearTv.setText(year + "년");

    }

    public void setYearSpinner(View v) {

        //yearSpinner
        // Spinner yearSpinner = (Spinner) v.findViewById(R.id.spinner_year);


        yearAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.date_year,
                android.R.layout.simple_spinner_item);

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        //현재 날짜에 맞는 년 스피너 초기값으로 설정
        //2017년부터 시작
        //2017 -> 0   2018 -> 1
        yearSpinner.setSelection((mCalToday.get(Calendar.YEAR) - 2017));


        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                yearStr = yearAdapter.getItem(position).toString();
                Log.d("tag", "weekview 연도 " + yearStr + " 월 :" + monthStr);
               /* yearTv.setText(yearStr);*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setMonthSpinner(View v) {

        //monthSpinner
        // Spinner monthSpinner = (Spinner) v.findViewById(R.id.spinner_month);


        String[] monthArr = getResources().getStringArray(R.array.date_month);


        monthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.date_month,
                android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        //현재 날짜에 맞는 월 스피너 초기값으로 설정
        monthSpinner.setSelection(mCalToday.get(Calendar.MONTH));


        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                monthStr = monthAdapter.getItem(position).toString();
                /*monthTv.setText(monthStr);*/
                Log.d("tag", "weekview 연도 " + yearStr + " 월 :" + monthStr);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    public void setWeekSchedule() {
        //2월 12일~18일 weekly schedule 넣어 보자~~
        //CalData() 생성자 첫 번째 인자는 날짜(date)이다.
       /* weekSchduleArr = new ArrayList<CalData>();
        for (int i = 0; i < 20; i++) {
            String time = "1" + i + ":" + i + "2";
            String AM_PM = null;
            if (i % 2 == 0) AM_PM = "am";
            else AM_PM = "pm";
            weekSchduleArr.add(new CalData(1, "고기 " + i, time, AM_PM));
        }*/
    }

    private class WeekDateAdapter extends BaseAdapter {
        private Context context;

        private ArrayList<CalData> weekArr;
        private LayoutInflater inflater;
        TextView DateText;
        Calendar mCalendar;

       /* int curYear;
        int curMonth;*/

        public WeekDateAdapter(Context context) {
            mCalendar = Calendar.getInstance();
            //this.weekArr = weekArr;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            setWeekArrList(mCalendar);
        }

        public Calendar getCalendar() {
            return mCalendar;
        }

        public void setCalendar(Calendar mCalendar) {
            this.mCalendar = mCalendar;
        }

        public ArrayList<CalData> getWeekArr() {
            return weekArr;
        }

        public void setWeekArr(ArrayList<CalData> weekArr) {
            this.weekArr = weekArr;
        }

        @Override
        public int getCount() {
            return weekArr.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return weekArr.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.week_date, parent, false);
            }

            //날짜 표시 TextView
            DateText = (TextView) convertView.findViewById(R.id.weekDate);
            //앱 실행 기준 오늘 날짜 달력에 표시
            // Calendar mCalendar = Calendar.getInstance();
            Calendar todayCal = Calendar.getInstance();
            String year = todayCal.get(Calendar.YEAR) + "년";
            String month = (todayCal.get(Calendar.MONTH) + 1) + "월";

            int date = weekArr.get(position).getDay();
            if (year.equals(yearTv.getText().toString()) && month.equals(monthTv.getText().toString()) &&
                    date == (todayCal.get(Calendar.DATE))) {

                DateText.setBackgroundColor(Color.GREEN);
                //int d= Integer.parseInt(DateText.getText().toString());
                Log.d("tag", "표시된날짜: " + DateText.getText().toString());
                Log.d("tag", "??weekView: year " + year + "month " + month + "yearStr " + yearStr + "monthStr " + monthStr + "Date" + date + "일");
            } else {
                //오늘날짜가 아닐 때는 텍스트뷰 색깔 원래대로
                DateText.setBackgroundColor(Color.WHITE);
                Log.d("tag", "*****??weekView: year " + year + "month " + month + "yearStr " + yearStr + "monthStr " + monthStr + "Date" + date + "일"
                        + "일치날짜 없음?!!");
            }


            //날씨 표시 ImageView
            //ImageView weatherImage = (ImageView)convertView.findViewById(R.id.weatherImageView);

            if (weekArr.get(position) == null) {
                DateText.setText("");
                //weatherImage.setBackgroundColor(Color.YELLOW);
            } else {
                DateText.setText(weekArr.get(position).getDay() + "");
            }
            return convertView;
        }



        public void setWeekArrList(Calendar calendar) {
            weekArr = new ArrayList<CalData>();

            int firstValue = calendar.get(Calendar.DAY_OF_WEEK);
            //주 캘린더 날짜 기준 시작 일요일 구하기
            calendar.add(Calendar.DATE, (1 - firstValue));
            //***오늘 날짜 기준으로 한 주 달력 생성***
            String tokenDate;
            //시작 일요일
            //calendar.add(Calendar.DATE, 1 - calendar.get(Calendar.DAY_OF_WEEK) );
            //기준일 정하고 기준일로부터 7일씩 표시
            for (int i = 0; i < 7; i++) {//시작 일요일부터 7일 표시

                weekArr.add(
                        new CalData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DATE)));
                calendar.add(Calendar.DATE, 1);
            }
            //+7된 날짜 돌려놓기
            // int backToOriginal = -7+(1-firstValue);
            calendar.add(Calendar.DATE, -7 + (firstValue - 1));


        }//end of public void setWeekArr(int btnselect,int prevBtn,int nextBtn)
        public void setPrevWeek() {

            //오늘 날짜 주의 시작 일요일
            mCalendar.add(Calendar.DATE, 1 - mCalendar.get(Calendar.DAY_OF_WEEK));
            mCalendar.add(Calendar.DATE, -7);

        }


        public void setNextWeek() {

            //오늘 날짜 주의 시작 일요일
            mCalendar.add(Calendar.DATE, 1 - mCalendar.get(Calendar.DAY_OF_WEEK));
            mCalendar.add(Calendar.DATE, 7);

        }

    }

}