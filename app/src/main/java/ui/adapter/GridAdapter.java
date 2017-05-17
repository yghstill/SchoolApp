package ui.adapter;

import android.content.Context;
import android.graphics.Picture;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.TextView;

import com.example.schoolwq.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Bean.PictureBean;

/**
 * Created by Y-GH on 2017/5/10.
 *
 */

public  class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private List<PictureBean> datas;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    public GridAdapter(Context context, List<PictureBean> datas) {
        mContext=context;
        this.datas=datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
            View view = LayoutInflater.from(mContext
                    ).inflate(R.layout.grid_msg_item, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(view);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            Picasso.with(mContext).load(datas.get(position).getUrl()).into(((MyViewHolder) holder).iv);
        }

    }

    @Override
    public int getItemCount()
    {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {

        //判断item是图还是显示页数（图片有URL）
//        if (!TextUtils.isEmpty(datas.get(position).getUrl())) {
            return 0;
//        } else {
//            return 1;
//        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {

            mOnItemClickListener.onItemClick(v);
        }

    }


    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemLongClick(v);
        }
        return false;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageButton iv;

        public MyViewHolder(View view)
        {
            super(view);
            iv = (ImageButton) view.findViewById(R.id.iv);
        }
    }

}
