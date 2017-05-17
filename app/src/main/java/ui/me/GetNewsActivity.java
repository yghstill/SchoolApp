package ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.schoolwq.BaseActivity;
import com.example.schoolwq.R;


/**
 * Created by Y-GH on 2017/5/12.
 */

public class GetNewsActivity extends BaseActivity {
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
        provider.setText(bundle.getString("provider",null));
        time.setText(bundle.getString("time",null));
    }

    private void initView() {
        getSupportActionBar().setTitle("新闻详情");
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        provider = (TextView) findViewById(R.id.provider);
        time = (TextView) findViewById(R.id.time);



    }

}
