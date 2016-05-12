package com.mosquitosquad.foxcities.mosquad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by Tyler Gotz on 4/29/2016.
 */
public class LinearLayoutListView extends LinearLayout
{
    ListView listView;

    public LinearLayoutListView(Context context)
    {
        super(context);
    }

    public LinearLayoutListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LinearLayoutListView(Context context, AttributeSet attrs, int defStyleAttrs)
    {
        super(context, attrs, defStyleAttrs);
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
