package app.wachirapong.emschecker;


import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wachirapong on 2/6/2016 AD.
 */
public class DateDetailAdapter extends ArrayAdapter<String> {

    List<String> item;

    public DateDetailAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public DateDetailAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        item = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.date_detail_recycle_view, null);
        }

        String p = getItem(position);

            TextView tt1 = (TextView) v.findViewById(R.id.tvTest);
        tt1.setText(p);


        return v;
    }

    @Override
    public int getCount() {
        return item.size();
    }
}

