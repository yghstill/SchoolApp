package ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.schoolwq.MainActivity;
import com.example.schoolwq.R;
import com.githang.statusbar.StatusBarCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Bean.Userbean;
import Internet.NormalPostRequest;

import static Internet.NormalPostRequest.mURL;


/**
 * Created by Y-GH on 2017/5/9.
 */
public class LoginActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener {
    private TextInputEditText username, password;
    private Button login;
    private TextView resign;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String httpurl1 = mURL+":8080/wanqing/login";

    private static final String MSG_LOGIN_SUCCESS = "登录成功。";
    private static ProgressDialog dialog;
    private Userbean user = new Userbean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this,R.color.colorPrimaryDark,true);
        setContentView(R.layout.login);
        initView();
        initUID();
        setOnListener();
    }

    private void initView() {
        login = (Button) findViewById(R.id.btn_login);
        username = (TextInputEditText) findViewById(R.id.et_number);
        password = (TextInputEditText) findViewById(R.id.et_password);
        resign = (TextView) findViewById(R.id.tv_quick_sign_up);



    }

    private void setOnListener() {

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (username.getText().toString().length() > 0) {
                    if (password.getText().toString().length() > 0 ) {
                        login.setEnabled(true);
                    } else {
                        login.setEnabled(false);
                    }
                } else {
                    login.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (password.getText().toString().length() > 0) {
                    if (username.getText().toString().length() > 0) {
                        login.setEnabled(true);
                    } else {
                        login.setEnabled(false);
                    }
                } else {
                    login.setEnabled(false);
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


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String loginName = username.getText().toString();
                final String loginPassword = password.getText().toString();
                /**
                 * loading...
                 */

                if (dialog == null) {
                    dialog = new ProgressDialog(LoginActivity.this);
                }
                dialog.setMessage("登录中...");
                dialog.setCancelable(false);
                dialog.show();
                /**
                 * 访问网络
                 */
                onLoginDialogLogin(loginName, loginPassword);
//                showTip(MSG_LOGIN_SUCCESS);//服务器写好再注释掉

            }
        });







        resign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        ResignActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化记住的用户名
     */

    private void initUID() {
        pref = getSharedPreferences("user", MODE_PRIVATE);
        String Username = pref.getString("username", "");
        username.setText(Username);
    }


    /**
     * 清空控件文本
     */
    private void clearText(EditText edit) {
        edit.setText("");
    }

    /**
     * 是否显示密码
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // 显示密码
            password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            // 隐藏密码
            password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        username.setText(pref.getString("userid",null));
    }

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        if (str == MSG_LOGIN_SUCCESS) {
            editor = pref.edit();
            editor.putString("userid", user.getUserid());
            editor.putString("username", user.getUsername());
            editor.putString("sex", user.getSex());
            editor.putString("xueyuan", user.getXueyuan());
            editor.putString("zhuanye", user.getZhuanye());
            editor.putString("type", user.getType());
            editor.putBoolean("islogin", true);
            editor.commit();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }


    private void onLoginDialogLogin(final String username, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", username);
        params.put("password", password);
        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("登录成功", response.toString() );
                        if(response!=null){
                            try {
                                Log.e("登录成功", response.toString() );
                                user.setUserid(response.getString("userid"));
                                user.setSex(response.getString("sex"));
                                user.setUsername(response.getString("username"));
                                user.setXueyuan(response.getString("xueyuan"));
                                user.setZhuanye(response.getString("zhuanye"));
                                user.setType(response.getString("type"));
                                    if (dialog   != null) {
                                        dialog.cancel();
                                        dialog = null;
                                    Log.e("登录成功", "  wwwww " );
                                    showTip(MSG_LOGIN_SUCCESS);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {

                            if (dialog != null) {
                                dialog.cancel();
                                dialog = null;
                            }
                            Toast.makeText(LoginActivity.this,"用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(LoginActivity.this, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }


}
