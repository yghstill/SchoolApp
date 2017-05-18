package ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolwq.BaseActivity;
import com.example.schoolwq.MainActivity;
import com.example.schoolwq.R;


/**
 * Created by Y-GH on 2017/5/12.
 */

public class PeripheryActivity extends BaseActivity {
    private TextView play,eat,more;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.periphery);
        initView();
    }


    private void initView() {
        getSupportActionBar().setTitle("校园周边");
        play = (TextView) findViewById(R.id.play);
        eat = (TextView) findViewById(R.id.eat);
        more = (TextView) findViewById(R.id.more);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeripheryActivity.this, PlayListActivity.class);
                startActivity(intent);
            }
        });

        eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeripheryActivity.this, StoreListActivity.class);
                startActivity(intent);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PeripheryActivity.this,"敬请期待...",Toast.LENGTH_SHORT).show();
            }
        });



    }

}
