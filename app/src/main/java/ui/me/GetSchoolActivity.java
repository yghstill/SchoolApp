package ui.me;

import android.app.ProgressDialog;
import android.content.Context;
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

import static Internet.NormalPostRequest.mURL;


/**
 * Created by Y-GH on 2017/5/12.
 */

public class GetSchoolActivity extends BaseActivity {
    private TextView title,provider,content,time;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schoolmore);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title.setText(bundle.getString("title"));
        content.setText(bundle.getString("content",null));
        provider.setText("教务处");
        time.setText("2017-05-20");
    }

    private void initView() {
        getSupportActionBar().setTitle("详情");
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        provider = (TextView) findViewById(R.id.provider);
        time = (TextView) findViewById(R.id.time);



    }

}
