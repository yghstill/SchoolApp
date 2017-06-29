package ui.me;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.schoolwq.BaseActivity;
import com.example.schoolwq.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Bean.NewsBean;
import Bean.WeeklinBean;
import Internet.NormalPostRequest2;

import static Internet.NormalPostRequest.mURL;


/**
 * Created by Y-GH on 2017/5/28.
 */

public class WeekLineActivity extends BaseActivity {
    private TextView x1j1,x1j2,x1j3,x1j4,x1j5;
    private TextView x2j1,x2j2,x2j3,x2j4,x2j5;
    private TextView x3j1,x3j2,x3j3,x3j4,x3j5;
    private TextView x4j1,x4j2,x4j3,x4j4,x4j5;
    private TextView x5j1,x5j2,x5j3,x5j4,x5j5;
    private TextView x6j1,x6j2,x6j3,x6j4,x6j5;
    private static ProgressDialog dialog;
    private SharedPreferences pref;
    private static String httpurl1 = mURL+":8080/wanqing/getweeklin";
    private java.util.List<WeeklinBean> List = new ArrayList<WeeklinBean>();
    private TextView[][] T ={
            {x1j1,x1j2,x1j3,x1j4,x1j5},
            {x2j1,x2j2,x2j3,x2j4,x2j5},
            {x3j1,x3j2,x3j3,x3j4,x3j5},
            {x4j1,x4j2,x4j3,x4j4,x4j5},
            {x5j1,x5j2,x5j3,x5j4,x5j5},
            {x6j1,x6j2,x6j3,x6j4,x6j5}
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weeklin);
        initView();
        initData();
    }

    private void initData() {
        getSupportActionBar().setTitle("课程表");
        pref = getSharedPreferences("user", MODE_PRIVATE);
        /**
         * loading...
         */

        if (dialog == null) {
            dialog = new ProgressDialog(WeekLineActivity.this);
        }
        dialog.setMessage("加载中...");
        dialog.setCancelable(false);
        dialog.show();
        onGetWeekData(WeekLineActivity.this,1);



    }


    private Handler mHandler = new Handler() {
        @SuppressLint("LongLogTag")
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
                    if (dialog != null) {
                        dialog.cancel();
                        dialog = null;
                    }
                    Log.e("list长度=====》",List.size()+"");
                    for (int i = 0;i<List.size();i++){
                        TextView Week = T[Integer.parseInt(List.get(i).getTian())-1][Integer.parseInt(List.get(i).getJie())-1];
                        Log.e("WEek---",Integer.parseInt(List.get(i).getTian())-1+"---"+(Integer.parseInt(List.get(i).getJie())-1));
                        switch(i%5){
                            case 0:
                                Week.setBackgroundResource(R.drawable.weekback);
                                break;
                            case 1:
                                Week.setBackgroundResource(R.drawable.weekback1);
                                break;
                            case 2:
                                Week.setBackgroundResource(R.drawable.weekback2);
                                break;
                            case 3:
                                Week.setBackgroundResource(R.drawable.weekback3);
                                break;
                            case 4:
                                Week.setBackgroundResource(R.drawable.weekback4);
                                break;

                        }
                        Week.setText(List.get(i).getWeeklinname());
                        }
                    break;
            }
        };
    };


    /**
     * 获取课程信息
     * @param context
     */
    private void onGetWeekData(final Context context, final int FLAG) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid",pref.getString("userid",null) );

        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        Request<JSONArray> request = new NormalPostRequest2(httpurl1,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("加载成功", response.toString() );
                        if(response!=null){
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject json = response.getJSONObject(i);
                                    WeeklinBean bean = new WeeklinBean(json.getString("userid"),json.getString("weeklinname"),json.getString("tian"),json.getString("jie"));
                                    List.add(bean);
                                }
                                mHandler.sendEmptyMessage(FLAG);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {

                            if (dialog != null) {
                                dialog.cancel();
                                dialog = null;
                            }
                            Toast.makeText(context,"内部错误", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(context, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }


    private void initView() {
        T[0][0] = (TextView) findViewById(R.id.xingqi1_jie1);
        T[0][1] = (TextView) findViewById(R.id.xingqi1_jie2);
        T[0][2] = (TextView) findViewById(R.id.xingqi1_jie3);
        T[0][3]= (TextView) findViewById(R.id.xingqi1_jie4);
        T[0][4] = (TextView) findViewById(R.id.xingqi1_jie5);

        T[1][0] = (TextView) findViewById(R.id.xingqi2_jie1);
        T[1][1]= (TextView) findViewById(R.id.xingqi2_jie2);
        T[1][2] = (TextView) findViewById(R.id.xingqi2_jie3);
        T[1][3] = (TextView) findViewById(R.id.xingqi2_jie4);
        T[1][4] = (TextView) findViewById(R.id.xingqi2_jie5);

        T[2][0] = (TextView) findViewById(R.id.xingqi3_jie1);
        T[2][1] = (TextView) findViewById(R.id.xingqi3_jie2);
        T[2][2] = (TextView) findViewById(R.id.xingqi3_jie3);
        T[2][3] = (TextView) findViewById(R.id.xingqi3_jie4);
        T[2][4] = (TextView) findViewById(R.id.xingqi3_jie5);

        T[3][0] = (TextView) findViewById(R.id.xingqi4_jie1);
        T[3][1] = (TextView) findViewById(R.id.xingqi4_jie2);
        T[3][2] = (TextView) findViewById(R.id.xingqi4_jie3);
        T[3][3] = (TextView) findViewById(R.id.xingqi4_jie4);
        T[3][4] = (TextView) findViewById(R.id.xingqi4_jie5);

        T[4][0] = (TextView) findViewById(R.id.xingqi5_jie1);
        T[4][1] = (TextView) findViewById(R.id.xingqi5_jie2);
        T[4][2] = (TextView) findViewById(R.id.xingqi5_jie3);
        T[4][3] = (TextView) findViewById(R.id.xingqi5_jie4);
        T[4][4] = (TextView) findViewById(R.id.xingqi5_jie5);

        T[5][0] = (TextView) findViewById(R.id.xingqi6_jie1);
        T[5][1] = (TextView) findViewById(R.id.xingqi6_jie2);
        T[5][2] = (TextView) findViewById(R.id.xingqi6_jie3);
        T[5][3] = (TextView) findViewById(R.id.xingqi6_jie4);
        T[5][4] = (TextView) findViewById(R.id.xingqi6_jie5);
    }



}
