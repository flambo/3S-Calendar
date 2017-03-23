package kr.ac.hansung.a3scalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Owner on 2017-01-11.
 */

public class DateCalendarFragment extends Fragment {
    private ScheduleManager SM;

    public DateCalendarFragment newInstance(){
        DateCalendarFragment fragment = new DateCalendarFragment();
        //SM을 부모로부터 받아올것
        return fragment;
    }

    public DateCalendarFragment(){

    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        SM = new ScheduleManager();//메인에서 받아오는 것으로 변경
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar_date,container,false);
        return v;
    }
}
