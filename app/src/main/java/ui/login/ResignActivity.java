package ui.login;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
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


public class ResignActivity extends BaseActivity implements View.OnClickListener {
    private TextInputEditText userid, password, surepassword,username,xueyuan,zhuanye;
    RadioGroup mode,mode1;
    private String msex=null,mtype = null;
    private Button resign;
    private static ProgressDialog dialog;
    private static final String MSG_LOGIN_SUCCESS = "注册成功。";
    private String httpurl1 = mURL+":8080/wanqing/resign";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resign_all);
        initView();
        setOnListener();


    }

    private void initView() {
        getSupportActionBar().setTitle("注册");

        //控件
        userid = (TextInputEditText) findViewById(R.id.et_number);
        password = (TextInputEditText) findViewById(R.id.et_password);
        surepassword = (TextInputEditText) findViewById(R.id.et_sure_password);
        username = (TextInputEditText) findViewById(R.id.et_name);
        xueyuan = (TextInputEditText) findViewById(R.id.et_xueyuan);
        zhuanye = (TextInputEditText) findViewById(R.id.et_zhuanye);
        mode = (RadioGroup) findViewById(R.id.mode);
        mode1 = (RadioGroup) findViewById(R.id.mode2);
        resign = (Button) findViewById(R.id.btn_resign);
        pref = getSharedPreferences("user", MODE_PRIVATE);
    }

    private void setOnListener() {
        //按钮监听
        resign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String loginName = userid.getText().toString();
                final String loginPassword = password.getText().toString();
                final String Xueyuan = xueyuan.getText().toString();
                final String realName = username.getText().toString();
                final String Zhuanye = zhuanye.getText().toString();
                /**
                 * loading...
                 */

                if (dialog == null) {
                    dialog = new ProgressDialog(ResignActivity.this);
                }
                dialog.setMessage("注册中...");
                dialog.setCancelable(false);
                dialog.show();
                if(!loginPassword.equals(surepassword.getText().toString())){
                    if (dialog != null) {
                        dialog.cancel();
                        dialog = null;
                    }
                    Toast.makeText(ResignActivity.this,"密码前后不一致", Toast.LENGTH_SHORT).show();
                }else{
                    onresignLogin(loginName, loginPassword,msex,realName,Xueyuan,Zhuanye,mtype);
                }


            }
        });

        mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.man_select:
                        msex = "男";
                        break;
                    case R.id.women_select:
                        msex = "女";
                        break;
                }
            }
        });

        mode1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.type1_select:
                        mtype = "学生";
                        break;
                    case R.id.type2_select:
                        mtype = "教师";
                        break;
                }
            }
        });

        // 用户名 输入监听
        userid.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (userid.getText().toString().length() > 0) {
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (password.getText().toString().length() > 0) {
                    findViewById(R.id.qrpsw_layout).setVisibility(View.VISIBLE);
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                    findViewById(R.id.qrpsw_layout).setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });

        surepassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (surepassword.getText().toString().length() > 0) {
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });
        // 真是姓名输入监听
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (username.getText().toString().length() > 0) {
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });

        xueyuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (xueyuan.getText().toString().length() > 0) {
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });

        zhuanye.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (zhuanye.getText().toString().length() > 0) {
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });

    }

    /**
     * 按钮状态监听
     */
    private void initmsucess() {
        if (userid.getText().toString().length() > 0
                & password.getText().toString().length() > 0
                & surepassword.getText().toString().length() > 0
                & username.getText().toString().length() > 0
                & xueyuan.getText().toString().length() > 0
                & zhuanye.getText().toString().length() > 0){
            resign.setEnabled(true);
        } else {
            resign.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        if (str == MSG_LOGIN_SUCCESS) {
            editor = pref.edit();
            editor.putString("userid", userid.getText().toString());
            editor.commit();
            finish();
        }

    }

    private void onresignLogin(final String userid, String password, String sex, String username, String xueyuan,String zhuanye,String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userid);
        params.put("password", password);
        params.put("username", username);
        params.put("sex", sex);
        params.put("xueyuan", xueyuan);
        params.put("zhuanye",zhuanye);
        params.put("type",type);
        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        String json = new Gson().toJson(response);
                        Log.e("注册成功", response.toString() );
                        try {
                            Log.e("====>>>>>",response.getString("status"));
                            if (response.getString("status").equals("success")) {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                showTip(MSG_LOGIN_SUCCESS);
                            } else {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                Toast.makeText(ResignActivity.this,"用户名已存在", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Old_LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(ResignActivity.this, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }
}
