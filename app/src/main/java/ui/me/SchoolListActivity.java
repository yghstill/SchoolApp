package ui.me;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.schoolwq.BaseActivity;
import com.example.schoolwq.R;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Bean.schoolBean;
import Internet.NormalPostRequest2;
import ui.adapter.SchoolAdapter;

import static Internet.NormalPostRequest.mURL;


/**
 * Created by Y-GH on 2016/6/13.
 */
public class SchoolListActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private List<schoolBean> List = new ArrayList<schoolBean>();
    private ListView listview;
    private SchoolAdapter Adapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private static ProgressDialog dialog;
    private SharedPreferences pref;
    private static String httpurl1 = mURL+":8080/wanqing/schoollist";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_list);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("学校介绍");
        pref = getSharedPreferences("user", MODE_PRIVATE);
        listview = (ListView) findViewById(R.id.order_list_view);
        Adapter = new SchoolAdapter(SchoolListActivity.this,
                R.layout.school_list_item,List);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                List.clear();
                onGetSchoolData(SchoolListActivity.this,2);
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("----------","点击listview");
                schoolBean bean = List.get(position);
                Intent intent = new Intent(SchoolListActivity.this, GetSchoolActivity.class);
                intent.putExtra("title",bean.getXueyuanid());
                intent.putExtra("content","\u3000\u3000"+bean.getXueyuandesc().replace("\n\u3000\u3000", "\\n")  );
                startActivity(intent);
            }
        });
        initData();
    }

    private void initData() {
        /**
         * loading...
         */

        if (dialog == null) {
            dialog = new ProgressDialog(SchoolListActivity.this);
        }
        dialog.setMessage("加载中...");
        dialog.setCancelable(false);
        dialog.show();
        onGetSchoolData(SchoolListActivity.this,1);
    }

    @Override
    public void onClick(View v) {

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
                    listview.setAdapter(Adapter);
                    break;
                case 2:
                    listview.setAdapter(Adapter);
                    swipyRefreshLayout.setRefreshing(false);
                    break;
            }
        };
    };

    /**
     * 获取学校列表
     * @param context
     */
    private void onGetSchoolData(final Context context, final int FLAG) {
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
                                    schoolBean bean = new schoolBean(json.getString("xueyuanid"),json.getString("xueyuandesc"));
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

}
