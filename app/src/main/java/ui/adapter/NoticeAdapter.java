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
import Bean.SchoolNoticesBean;


public class NoticeAdapter extends ArrayAdapter<SchoolNoticesBean> {

	private int resourceId;

	public NoticeAdapter(Context context, int textViewResourceId,
                         List<SchoolNoticesBean> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SchoolNoticesBean ns = getItem(position);
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
		viewHolder.title.setText(ns.getNoticename());
		viewHolder.content.setText(ns.getContent());
		return view;
	}

	class ViewHolder {
		

		TextView title;
		TextView content;


	}
}
