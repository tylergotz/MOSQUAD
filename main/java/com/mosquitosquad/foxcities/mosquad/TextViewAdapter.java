package com.mosquitosquad.foxcities.mosquad;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Tyler Gotz on 2/25/2016.
 */
public class TextViewAdapter extends BaseAdapter {
    private Context context;
    private final String[] textViewValues;
    private final String[] subViewValues;

    public TextViewAdapter(Context context, String[] textViewValues, String[] subViewValues) {
        this.context = context;
        this.textViewValues = textViewValues;
        this.subViewValues = subViewValues;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = inflater.inflate(R.layout.item, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(textViewValues[position]);
            final TextView textView1 = (TextView) gridView.findViewById(R.id.grid_sub);
            textView1.setText(subViewValues[position]);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    textView1.setTextColor(Color.BLACK);
                }
            });


        } else {
            gridView = convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return textViewValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}