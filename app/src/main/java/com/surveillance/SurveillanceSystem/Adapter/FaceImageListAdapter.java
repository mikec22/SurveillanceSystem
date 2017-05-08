package com.surveillance.SurveillanceSystem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.surveillance.SurveillanceSystem.R;

import java.util.List;

/**
 * Created by Mike on 8/5/2017.
 */

public class FaceImageListAdapter extends ArrayAdapter<List<Bitmap>> {

    private Context context;
    private List<List<Bitmap>> lists;

    public FaceImageListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<List<Bitmap>> lists) {
        super(context, resource, lists);
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    private class ViewHolder {
        LinearLayout imageContainer;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.from(context).inflate(R.layout.row_face_img, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.imageContainer = (LinearLayout) convertView.findViewById(R.id.imageContainer);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        List<Bitmap> bitmaps = lists.get(position);
        for (Bitmap bitmap : bitmaps) {
            ImageView iv = new ImageView(context);
            holder.imageContainer.addView(iv);
        }
        return convertView;
    }

}

