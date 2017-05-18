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
import Bean.PlaygroundBean;


public class playAdapter extends ArrayAdapter<PlaygroundBean> {

	private int resourceId;

	public playAdapter(Context context, int textViewResourceId,
                       List<PlaygroundBean> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PlaygroundBean play = getItem(position);
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
		viewHolder.title.setText(play.getFunningname());
		viewHolder.content.setText(play.getAddress());
		return view;
	}

	class ViewHolder {
		

		TextView title;
		TextView content;


	}
}
