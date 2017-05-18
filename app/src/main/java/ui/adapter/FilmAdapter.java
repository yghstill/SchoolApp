package ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.schoolwq.R;

import java.util.List;

import Bean.FilmInformation;
import ui.tools.ImageLoader;

/**
 * Created by Chen on 2017/4/2.
 */
public class FilmAdapter extends ArrayAdapter<FilmInformation> implements AbsListView.OnScrollListener{

    private int resourceId;
    private int start,end;
    public static String[] URLS;
    private ImageLoader imageLoader;
    private boolean flag;

    public FilmAdapter(Context context, int resource, List<FilmInformation> objects, ListView listView) {
        super(context, resource, objects);
        resourceId = resource;
        imageLoader = new ImageLoader(listView);
        URLS = new String[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            URLS[i] = objects.get(i).getPoster();
        }
        flag = true;
        listView.setOnScrollListener(this);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilmInformation filmInfo = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.filmImage = (ImageView) convertView.findViewById(R.id.film_image);
            viewHolder.filmName = (TextView) convertView.findViewById(R.id.film_name);
            viewHolder.filmType = (TextView) convertView.findViewById(R.id.film_type);
            viewHolder.filmActor = (TextView) convertView.findViewById(R.id.film_actor);
            viewHolder.filmSynopsis = (TextView) convertView.findViewById(R.id.film_synopsis);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.filmName.setText(filmInfo.getName());
//        viewHolder.filmImage.setImageResource(R.mipmap.ic_launcher);
        viewHolder.filmImage.setTag(filmInfo.getPoster());
        imageLoader.showImageByAsyncTask(viewHolder.filmImage,filmInfo.getPoster());
        viewHolder.filmType.setText(filmInfo.getType());
        viewHolder.filmActor.setText("主演：" + filmInfo.getPlayer());
        viewHolder.filmSynopsis.setText("剧情简介：" + filmInfo.getSynopsis());
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            //加载可见项
            imageLoader.loadImages(start,end);
        }else{
            //停止任务
            imageLoader.cancleAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        start = firstVisibleItem;
        end = firstVisibleItem + visibleItemCount;
        //第一次显示的时候调用
        if(flag && visibleItemCount > 0){
            imageLoader.loadImages(start,end);
            flag = false;
        }
    }

    class ViewHolder{
        ImageView filmImage;
        TextView filmName;
        TextView filmType;
        TextView filmActor;
        TextView filmSynopsis;
    }
}
