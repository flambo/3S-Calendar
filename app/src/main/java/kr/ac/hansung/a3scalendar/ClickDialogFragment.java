package kr.ac.hansung.a3scalendar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Owner on 2017-02-07.
 */

public class ClickDialogFragment extends DialogFragment {
    ListView listView;
    ArrayAdapter<String> dayScheduleAdapter;//ListView 세팅 어댑터
    ArrayList<String> dataArr;
    // private ArrayList<ScheduleStr>arrSchedule;

    private  String selectedDate; // '2017년 1월 17일 일정'

    //2017년 1월 17일 일정'   --> 선택한 날짜 받아와야 함.

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public ArrayList<String> getDataArr() {
        return dataArr;
    }

    public void setDataArr(ArrayList<String> dataArr) {
        this.dataArr = dataArr;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.dialog_click,null);
        //dataArr = new ArrayList<String>();
        dayScheduleAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dataArr);


        listView = (ListView)v.findViewById(R.id.DayScheduleList);
        listView.setAdapter(dayScheduleAdapter);

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());


        builder.setTitle(selectedDate)
                //.setItems()
                .setView(v);


        return builder.create();

    }
}
