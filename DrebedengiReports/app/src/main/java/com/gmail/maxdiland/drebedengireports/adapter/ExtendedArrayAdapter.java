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
    protected int viewResourceId;
    protected int dropDownViewResourceId;

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
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        dropDownViewResourceId = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = createViewIfNull(convertView, dropDownViewResourceId, parent);
        return propagateDataOnDropDownView(position, view) ?
                view : super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = createViewIfNull(convertView, viewResourceId, parent);
        return propagateDataOnView(position, view) ?
                view : super.getView(position, convertView, parent);
    }

    protected View createViewByResourceId(int resourceId, ViewGroup parent) {
        return inflater.inflate(resourceId, parent, false);
    }

    protected View createViewIfNull(View view, int resource, ViewGroup parent) {
        return (view == null) ? createViewByResourceId(resource, parent) : view;
    }

    protected boolean propagateDataOnView(int position, View view) {
        return false;
    }

    protected boolean propagateDataOnDropDownView(int position, View view) {
        return false;
    }
}
