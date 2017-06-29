package ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Picture;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.schoolwq.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Bean.PictureBean;
import Internet.NormalPostRequest;
import ui.login.ResignActivity;
import ui.tools.SnackbarUtil;

import static Internet.NormalPostRequest.mURL;

/**
 * Created by Y-GH on 2017/5/10.
 *
 */

public  class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private List<PictureBean> datas;
    private String httpurl1 = mURL+":8080/wanqing/deleteimg";

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
    public void addItem(PictureBean pic, int position,RecyclerView recyclerview) {
        datas.add(position, pic);
        notifyItemInserted(position);
        recyclerview.scrollToPosition(position);
    }

    public void removeItem(final int position, final RecyclerView recyclerview, final CoordinatorLayout coordinatorLayout,Context context) {
        final PictureBean removed=datas.get(position);
        onDelete(datas.get(position).getImgid(),context);
        datas.remove(position);
        notifyItemRemoved(position);

        SnackbarUtil.ShortSnackbar(coordinatorLayout,"你删除了第"+position+"个item",SnackbarUtil.Warning).setAction("撤销", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(removed,position,recyclerview);
                SnackbarUtil.ShortSnackbar(coordinatorLayout,"撤销了删除第"+position+"个item",SnackbarUtil.Confirm).show();
            }
        }).setActionTextColor(Color.WHITE).show();
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

    private void onDelete(final String imgid, final Context context) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("imgid", imgid);
        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        String json = new Gson().toJson(response);
                        Log.e("成功", response.toString() );
                        try {
                            Log.e("====>>>>>",response.getString("status"));
                            if (response.getString("status").equals("success")) {
                                Toast.makeText(context,"删除成功", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "连接服务器错误", Toast.LENGTH_SHORT).show();

            }
        }, params);
        requestQueue.add(request);
    }

}
