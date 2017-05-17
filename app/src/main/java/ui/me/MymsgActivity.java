package ui.me;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.schoolwq.BaseActivity;
import com.example.schoolwq.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Internet.NormalPostRequest;

/**
 * Created by Y-GH on 2017/5/12.
 */

public class MymsgActivity extends BaseActivity {
    private TextView username,sex;
    private TextView xuehao,xueyuan,zhuanye,type;
    private SharedPreferences pref;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymsg);
        initView();
        initData();
    }

    private void initData() {
        pref = getSharedPreferences("user", MODE_PRIVATE);
        username.setText(pref.getString("username",""));
        sex.setText(pref.getString("sex",""));
        xuehao.setText(pref.getString("userid",""));
        xueyuan.setText(pref.getString("xueyuan",""));
        zhuanye.setText(pref.getString("zhuanye",""));
        type.setText(pref.getString("type",""));
    }

    private void initView() {
        getSupportActionBar().setTitle("教务信息");
        username = (TextView) findViewById(R.id.msg_username);
        sex = (TextView) findViewById(R.id.msg_idcard);
        xuehao = (TextView) findViewById(R.id.keshi);
        xueyuan = (TextView) findViewById(R.id.category);
        zhuanye = (TextView) findViewById(R.id.repert_info);
        type = (TextView) findViewById(R.id.time);



    }


}
