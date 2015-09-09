package com.gmail.maxdiland.drebedengireports.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * author Max Diland
 */
public class ExtendedArrayAdapter<T> extends ArrayAdapter<T> {
    private LayoutInflater inflater;
    private int viewResourceId;

    public ExtendedArrayAdapter(Context context, int resource) {
        super(context, resource);
        init(context, resource);
    }

    public ExtendedArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        init(context, resource);
    }

    public ExtendedArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
        init(context, resource);
    }

    public ExtendedArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
        init(context, resource);
    }

    public ExtendedArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        init(context, resource);
    }

    public ExtendedArrayAdapter(Context context, int resource, int textViewResourceId,
                                List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        init(context, resource);
    }

    private void init(Context context, int resource) {
        inflater = LayoutInflater.from(context);
        viewResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(viewResourceId, null);
        }

        if ( propagateDataOnView(position, view) ) {
            return view;
        }
        return super.getView(position, convertView, parent);
    }

    protected boolean propagateDataOnView(int position, View view) {
        return false;
    }
}
