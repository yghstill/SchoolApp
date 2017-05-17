package ui.loading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;

import com.example.schoolwq.MainActivity;
import com.example.schoolwq.R;

import ui.login.LoginActivity;


/**
 * Created by Y-GH on 2017/5/15.
 */
public class Loading extends FragmentActivity {
    private static final int GOTO_MAIN_ACTIVITY = 1;
    private static final int GOTO_LOGIN_ACTIVITY = 2;
    private SharedPreferences pref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_launch_page);
        pref = getSharedPreferences("user", MODE_PRIVATE);
        Log.e("登录状态==》",pref.getBoolean("islogin", false)+"");
        if(pref.getBoolean("islogin", false)==true){
            mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 3000);
        }else{
            mHandler.sendEmptyMessageDelayed(GOTO_LOGIN_ACTIVITY, 3000);
        }


    }


    private Handler mHandler = new Handler() {
        @SuppressLint("LongLogTag")
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    Intent intent1 = new Intent(Loading.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                case GOTO_LOGIN_ACTIVITY:
                    Intent intent2 = new Intent(Loading.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
            }
        };
    };


}
