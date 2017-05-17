package com.example.schoolwq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.MultipartBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import Bean.PictureBean;
import Internet.MyOkhttp;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ui.adapter.GridAdapter;
import ui.main.AddPictureActivity;
import ui.me.LogoutDialogFragment;
import ui.me.MymsgActivity;
import ui.me.SchoolListActivity;
import ui.tools.SnackbarUtil;

import static Internet.NormalPostRequest.mURL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private static RecyclerView recyclerview;
    private CoordinatorLayout coordinatorLayout;
    private GridAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private ItemTouchHelper itemTouchHelper;
    private int lastVisibleItem ;
    private List<PictureBean> picture;
    private SharedPreferences pref;
    private LinearLayout lay;
    private TextView name,xueyuan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        setListener();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);  //个性图标得到解放~嘿嘿
        View headerView = navigationView.getHeaderView(0);
        //初始化头像
        ImageView head = (ImageView) headerView.findViewById(R.id.image_head);
        head.setImageResource(R.mipmap.main_2_more);
        //初始化名字
        name = (TextView) headerView.findViewById(R.id.username);
        xueyuan = (TextView) headerView.findViewById(R.id.xueyuan);
        name.setText(pref.getString("username",""));
        xueyuan.setText(pref.getString("xueyuan",""));
        lay = (LinearLayout) headerView.findViewById(R.id.img);
        lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MymsgActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.staggered_coordinatorLayout);
        recyclerview=(RecyclerView)findViewById(R.id.staggered_recycler);
        mLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.staggered_swipe_refresh) ;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark,R.color.colorAccent);
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPictureActivity.class);
                startActivity(intent);
                finish();
            }
        });
        pref = getSharedPreferences("user", MODE_PRIVATE);
        new GetData().execute(mURL+":8080/wanqing/getimg/1");
    }

    private void setListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetData().execute(mURL+":8080/wanqing/getimg/1");
            }
        });

        itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags=0;
                if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
                    dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                }
                return makeMovementFlags(dragFlags,0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from=viewHolder.getAdapterPosition();
                int to=target.getAdapterPosition();
                PictureBean moveItem=picture.get(from);
                picture.remove(from);
                picture.add(to,moveItem);
                mAdapter.notifyItemMoved(from,to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        });

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +2>=mLayoutManager.getItemCount()) {
                    new GetData().execute(mURL+":8080/wanqing/getimg/1");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] positions= mLayoutManager.findLastVisibleItemPositions(null);
                lastVisibleItem = Math.max(positions[0],positions[1]);
            }
        });
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {

        } else if (id == R.id.nav_school) {
            Intent intent = new Intent(MainActivity.this, SchoolListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_tongzhi) {

        } else if (id == R.id.nav_daohang) {

        } else if (id == R.id.nav_zhoubian) {

        }else if (id == R.id.nav_more) {

        }else if (id == R.id.nav_fankui) {

        }else if (id == R.id.nav_about) {

        }else if (id == R.id.nav_exit) {
            LogoutDialogFragment fragment = new LogoutDialogFragment();
            fragment.show(getFragmentManager(), null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class GetData extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {

            return MyOkhttp.get(params[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!TextUtils.isEmpty(result)&&result.length()>2){
                Gson gson=new Gson();
                if(picture==null||picture.size()==0){
                    picture= gson.fromJson(result, new TypeToken<List<PictureBean>>() {}.getType());
                    PictureBean pages=new PictureBean();
                    picture.add(pages);
                }

                if(mAdapter==null){
                    recyclerview.setAdapter(mAdapter = new GridAdapter(MainActivity.this,picture));

                    //设置点击监听
                    mAdapter.setOnItemClickListener(new GridAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            int position=recyclerview.getChildAdapterPosition(view);
                            SnackbarUtil.ShortSnackbar(coordinatorLayout,"上传者："+picture.get(position).getUserid()+"   时间："+picture.get(position).getImgtime()+"\n描述："+picture.get(position).getImgdesc(),SnackbarUtil.Info).show();

                        }

                        @Override
                        public void onItemLongClick(View view) {
                            itemTouchHelper.startDrag(recyclerview.getChildViewHolder(view));
                        }
                    });

                    itemTouchHelper.attachToRecyclerView(recyclerview);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
