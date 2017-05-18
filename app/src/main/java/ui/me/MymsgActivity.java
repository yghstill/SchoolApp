package ui.me;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.example.schoolwq.MainActivity;
import com.example.schoolwq.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Internet.NormalPostRequest;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import ui.loading.BaseApplication;
import ui.main.AddPictureActivity;

import static Internet.NormalPostRequest.mURL;

/**
 * Created by Y-GH on 2017/5/12.
 */

public class MymsgActivity extends BaseActivity implements Runnable{
    private TextView username,sex;
    private TextView xuehao,xueyuan,zhuanye,type;
    private SharedPreferences pref;
    private CircleImageView headimg;
    private static final int REQUEST_IMAGE_SELECT = 200;
    private Uri fileUri;
    private Uri cropUri;
    private static final int RESULT_REQUEST_CODE = 300;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Bitmap bmp;
    private static final String LOG_TAG = "MymsgActivity";
    private ProgressDialog dialog;
    String imgPath;
    private String httpurl1 = mURL+":8080/wanqing/updateheadimg";
    private SharedPreferences.Editor editor;
    private String imgurl = mURL+":8080/wanqing/gethead/";
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
        if(pref.getBoolean("imghead",false)==true){
            Picasso.with(this).load(imgurl+pref.getString("userid","")).into(headimg);
        }
    }

    private void initView() {
        getSupportActionBar().setTitle("教务信息");
        username = (TextView) findViewById(R.id.msg_username);
        sex = (TextView) findViewById(R.id.msg_idcard);
        xuehao = (TextView) findViewById(R.id.keshi);
        xueyuan = (TextView) findViewById(R.id.category);
        zhuanye = (TextView) findViewById(R.id.repert_info);
        type = (TextView) findViewById(R.id.time);
        headimg = (CircleImageView) findViewById(R.id.image_head);
        headimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MymsgActivity.this);
                builder.setTitle("更换头像");
                builder.setMessage("从相册中截取？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //这里添加点击确定后的逻辑
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, REQUEST_IMAGE_SELECT);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //这里添加点击取消后的逻辑
                    }
                });
                builder.create().show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode){
                case REQUEST_IMAGE_SELECT:
                    Uri selectedImage = data.getData();
                    startPhotoZoom(selectedImage);
                    break;
                case RESULT_REQUEST_CODE:
                    imgPath = cropUri.getPath();

                    bmp = BitmapFactory.decodeFile(imgPath);
                    headimg.setImageBitmap(bmp);
                    Log.e(LOG_TAG, imgPath);
                    Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));
                    Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
                    /**
                     * loading...
                     */

                    if (dialog == null) {
                        dialog = new ProgressDialog(MymsgActivity.this);
                    }
                    dialog.setMessage("上传中...");
                    dialog.setCancelable(false);
                    dialog.show();
                    new Thread(MymsgActivity.this).start();
                    break;
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        try {
            PostFile(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param imgPath
     * @throws IOException
     * @throws JSONException
     */
    private void PostFile(String imgPath) throws IOException, JSONException {
        File file = new File(imgPath);
        if (!file.exists())
        {
            Toast.makeText(MymsgActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        pref = getSharedPreferences("user", MODE_PRIVATE);
        String Username = pref.getString("userid", "");
        RequestBody body = new  MultipartBody.Builder()
                .addFormDataPart("file_img",imgPath , RequestBody.create(MediaType.parse("media/type"), new File(imgPath)))
                .addFormDataPart("userid",Username)
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(httpurl1)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String tempResponse =  response.body().string();
            Log.e("==返回結果==","---"+tempResponse);
            if(Integer.parseInt(tempResponse)>0){
                mHandler.sendEmptyMessage(0);
            }else {
                Log.e("==返回==","---出错---");
                mHandler.sendEmptyMessage(1);
            }
        } else {
            dialog.cancel();
            mHandler.sendEmptyMessage(1);
            throw new IOException("Unexpected code " + response);
        }


    }


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    dialog.cancel();
                    Toast.makeText(MymsgActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    editor = pref.edit();
                    editor.putBoolean("imghead",true);
                    editor.commit();
//                    Intent intent = new Intent(MymsgActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
                    break;
                case 1:
                    dialog.cancel();
                    Toast.makeText(MymsgActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        };
    };



    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "img_wanqing");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String name = "img_test";
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath()  + File.separator + name + ".jpg");
            Log.e("----文件路径----","====="+mediaFile);
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * 裁剪图片
     */

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//发起剪切动作
        intent.setDataAndType(uri, "image/*");//设置剪切图片的uri和类型
        intent.putExtra("crop", "true");//剪切动作的信号
        intent.putExtra("aspectX", 1.6);//x和y是否等比缩放
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 316);//剪切后图片的尺寸
        intent.putExtra("return-data", true);//是否把剪切后的图片通过data返回
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());//图片的输出格式
        intent.putExtra("noFaceDetection", true);  //关闭面部识别
        //设置剪切的图片保存位置
        cropUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,cropUri);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }


}
