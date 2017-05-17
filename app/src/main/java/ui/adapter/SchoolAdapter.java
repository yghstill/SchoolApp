package ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.schoolwq.R;

import java.util.List;

import Bean.schoolBean;


public class SchoolAdapter extends ArrayAdapter<schoolBean> {

	private int resourceId;

	public SchoolAdapter(Context context, int textViewResourceId,
                         List<schoolBean> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		schoolBean school = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.school = (TextView) view.findViewById(R.id.schoolmsg);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.school.setText(school.getXueyuanid());
		return view;
	}

	class ViewHolder {
		

		TextView school;


	}
}
