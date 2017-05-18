package ui.me;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Bean.FilmInformation;
import Bean.schoolBean;
import Internet.NormalPostRequest2;
import ui.adapter.FilmAdapter;
import ui.adapter.SchoolAdapter;

import static Internet.NormalPostRequest.mURL;


/**
 * Created by Y-GH on 2016/6/13.
 */
public class MoreMovieActivity extends BaseActivity {
    private Toolbar toolbar;
//    private List<FilmInformation> List = new ArrayList<FilmInformation>();
    private List<FilmInformation> list;
    private ListView listview;
    private FilmAdapter Adapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private static String url = "http://op.juhe.cn/onebox/movie/pmovie?dtype=&city=%E6%AD%A6%E6%B1%89&key=f51f79c6a80e180e45af6564e506c6ce";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_list);
        initView();
        new FilmAsyncTask().execute(url);
    }

    private void initView() {
        getSupportActionBar().setTitle("最新电影资讯");
        listview = (ListView) findViewById(R.id.order_list_view);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                list.clear();
                new FilmAsyncTask().execute(url);
            }
        });

    }

    class FilmAsyncTask extends AsyncTask<String, Void, List<FilmInformation>> {
        @Override
        protected List<FilmInformation> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<FilmInformation> filmInformations) {
            super.onPostExecute(filmInformations);
            swipyRefreshLayout.setRefreshing(false);
            Adapter = new FilmAdapter(MoreMovieActivity.this,
                    R.layout.film_intro,list,listview);
            listview.setAdapter(Adapter);
        }
    }

    private List<FilmInformation> getJsonData(String url) {
        list = new ArrayList<>();
        FilmInformation film;
        String jsonString = null;
        try {
            jsonString = readStream(new URL(url).openStream());
            if (jsonString == null){
                //请求不到数据，网络连接失败
                Log.d("chen","请求不到数据");
            }
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject cityObject = jsonObject.getJSONObject("result");
            JSONArray cityArray = cityObject.getJSONArray("data");
            for (int i = 0; i < cityArray.length(); i++) {
                jsonObject = cityArray.getJSONObject(i);
                //正在上映或即将上映
                String name = jsonObject.getString("name");
                JSONArray filmArray = jsonObject.getJSONArray("data");
                for (int j = 0; j < filmArray.length(); j++) {
                    film = new FilmInformation();
                    JSONObject filmObject = filmArray.getJSONObject(j);
                    film.setName(filmObject.getString("tvTitle"));
                    film.setPoster(filmObject.getString("iconaddress"));
                    JSONObject object = filmObject.getJSONObject("star");
                    JSONObject object1 = object.getJSONObject("data");
                    for (int s = 0, t = 1; s < object1.length(); s += 2) {
                        JSONObject data = object1.getJSONObject(String.valueOf(t++));
                        if(film.getPlayer() != null) {
                            film.setPlayer(film.getPlayer() + "/" + data.getString("name"));
                        }else{
                            film.setPlayer(data.getString("name"));
                        }
                    }
                    object = filmObject.getJSONObject("type");
                    object1 = object.getJSONObject("data");
                    for (int t = 0; t < object1.length(); t++) {
                        JSONObject data = object1.getJSONObject(String.valueOf(t+1));
                        if(film.getType() != null) {
                            film.setType(film.getType() + "/" + data.getString("name") + " ");
                        }else{
                            film.setType(data.getString("name"));
                        }
                    }
                    object = filmObject.getJSONObject("story");
                    object1 = object.getJSONObject("data");
                    film.setSynopsis(object1.getString("storyBrief"));
                    list.add(film);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    private String readStream(InputStream is) {
        String result = "";
        try {
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



}
