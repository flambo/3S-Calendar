package kr.ac.hansung.a3scalendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Owner on 2017-02-07.
 */

public class LongClickDialogFragment extends DialogFragment {
    private CalData calData;
    private  String selectedDate; // '2017년 1월 17일 일정'


    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        //2017년 1월 17일 일정'   --> 선택한 날짜 받아와야 함.

        final String []DialogItem={"추가","수정","동결","삭제"};
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(selectedDate)
                .setItems(DialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),DialogItem[which]+"선택!",Toast.LENGTH_SHORT).show();


                          if( DialogItem[which].equals("추가")){
                              //프래그먼트 화면
                             FragmentManager manager = getFragmentManager();
                              AddScheduleDialogFragment addScheduleDialogFragment = new AddScheduleDialogFragment();
                              addScheduleDialogFragment.setSelectedDate(selectedDate);

                              addScheduleDialogFragment.show(manager,"requestAddScheduleFragment ");


                          }else if(DialogItem[which].equals("수정")){


                          }else if(DialogItem[which].equals("동결")){
                              calData.setFreezen(true);//해당 날자 동결시키기

                          }else{//삭제

                          }
                    }
                });

        return builder.create();

    }
}