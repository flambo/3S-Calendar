package kr.ac.hansung.a3scalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Owner on 2017-01-31.
 */

public class WeekScheduleAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<CalData> weekArr;
    private LayoutInflater inflater;

    public WeekScheduleAdapter(Context context,ArrayList<CalData> weekArr) {
        this.context = context;

        this.weekArr = weekArr;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


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
            convertView = inflater.inflate(R.layout.schedule_list_item, parent, false);
        }

        //시간 표시 TextView
        TextView TimeText = (TextView)convertView.findViewById(R.id.timeTextView);
        //스케줄 표시 TextView
        TextView ScheduleText = (TextView)convertView.findViewById(R.id.schduleTextView);

        if(weekArr.get(position) == null){
            TimeText.setText("");
            ScheduleText.setText("");

        }else{

            TimeText.setText(weekArr.get(position).getTime());
            ScheduleText.setText(weekArr.get(position).getSchedule());
        }
        return convertView;
    }

}
