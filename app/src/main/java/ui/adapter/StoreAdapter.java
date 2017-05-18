package ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.schoolwq.R;

import java.util.List;

import Bean.NewsBean;
import Bean.StoreBean;


public class StoreAdapter extends ArrayAdapter<StoreBean> {

	private int resourceId;

	public StoreAdapter(Context context, int textViewResourceId,
                        List<StoreBean> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StoreBean store = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) view.findViewById(R.id.newstitle);
			viewHolder.content = (TextView) view.findViewById(R.id.content);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.title.setText(store.getStorename());
		viewHolder.content.setText(store.getAddress());
		return view;
	}

	class ViewHolder {
		

		TextView title;
		TextView content;


	}
}
