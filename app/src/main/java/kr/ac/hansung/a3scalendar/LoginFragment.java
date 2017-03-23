package kr.ac.hansung.a3scalendar;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class LoginFragment extends Fragment implements MainActivity.onKeyBackPressedListener {
    private ScheduleManager SM;
    private EditText userId,userPw;
    private Button loginButton;

    public LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        //SM을 부모로부터 받아올것
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        SM = new ScheduleManager();//메인에서 받아오는 것으로 변경
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        userId = (EditText)v.findViewById(R.id.userId);

        userPw = (EditText)v.findViewById(R.id.userPw);

        loginButton = (Button)v.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버에서 구글 아이디와 비밀번호를 확인하기

//                if(false){
//                    root.ChangeFragment(R.id.calendars);//첫 로그인 시에 튜토리얼
//                }
//                else {
//                    root.ChangeFragment(R.id.calendars);//로그인하면 달력 화면으로
//                }

                ((MainActivity)getActivity()).ChangeFragment(R.id.calendars);
            }
        });
        return v;
    }

    @Override
    public void onBack() {
        // if (mWebView.canGoBack()) { //다른 조건이 있을 경우에 사용하기
        //     mWebView.goBack();
        // } else { }
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //((MainActivity) context).setOnKeyBackPressedListener(this);
        Log.i("backKey", "LoginFragment onAttach");
    }
}

