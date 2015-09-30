package com.gmail.maxdiland.drebedengireports.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * author Max Diland
 */
public class ExtendedArrayAdapter<T> extends ArrayAdapter<T> {
    private String hintText = "";
    private String noSelectionText = "(Not selected)";
    private int defaultHintColor = Color.parseColor("#80000000"); //TODO retrieve from theme

    private LayoutInflater inflater;
    protected int viewResId;
    protected int dropDownViewResId;
    protected int textViewResId;
    private boolean allowsEmptyEntry;

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
        viewResId = resource;
    }

    @Override
    public T getItem(int position) {
        if (allowsEmptyEntry) {
            return (position == 0) ?  null : super.getItem(position - 1);
        } else {
            return super.getItem(position);
        }
    }

    @Override
    public int getCount() {
        return allowsEmptyEntry ? super.getCount() + 1 : super.getCount();
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        dropDownViewResId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = createViewIfNull(convertView, viewResId, parent);
        return handleView(position, view) ? view : super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = createViewIfNull(convertView, dropDownViewResId, parent);
        return handleDropDownView(position, view) ?
                view : super.getDropDownView(position, convertView, parent);
    }

    protected View createViewIfNull(View view, int resource, ViewGroup parent) {
        return (view == null) ? createViewByResourceId(resource, parent) : view;
    }

    protected View createViewByResourceId(int resourceId, ViewGroup parent) {
        return inflater.inflate(resourceId, parent, false);
    }

    protected boolean handleView(int position, View view) {
        return isEmptyEntry(position) ?
                propagateDataOnViewForEmpty(view) : propagateDataOnView(position, view);
    }

    protected boolean handleDropDownView(int position, View view) {
        return isEmptyEntry(position) ?
                propagateDataOnDropDownViewForEmpty(view) :
                propagateDataOnDropDownView(position, view);
    }

    protected boolean propagateDataOnView(int position, View view) {
        return false;
    }

    protected boolean propagateDataOnDropDownView(int position, View view) {
        return false;
    }


    protected boolean propagateDataOnViewForEmpty(View view) {
        propagateOnView(view, hintText, true);
        return true;
    }

    protected boolean propagateDataOnDropDownViewForEmpty(View view) {
        propagateOnView(view, noSelectionText, false);
        return true;
    }

    protected void propagateOnView(View view, Object object, boolean viewForHint) {
        TextView textView;
        try {
            textView = (textViewResId == 0) ?
                    (TextView) view : (TextView) view.findViewById(textViewResId);
        } catch (ClassCastException e) {
            //TODO Consider the log message and exception
            Log.e("ExtendedArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ExtendedArrayAdapter requires the resource ID to be a TextView", e);
        }
        textView.setText(object.toString());

        if (viewForHint) {
            textView.setTextColor(defaultHintColor);
        }
    }

    private boolean isEmptyEntry(int position) {
        return allowsEmptyEntry && position == 0;
    }

    public void setAllowsEmptyEntry(boolean allowsEmptyEntry) {
        this.allowsEmptyEntry = allowsEmptyEntry;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public void setNoSelectionText(String noSelectionText) {
        this.noSelectionText = noSelectionText;
    }
}
