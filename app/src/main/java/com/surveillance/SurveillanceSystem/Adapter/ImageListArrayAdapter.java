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
import android.widget.TextView;

import com.surveillance.SurveillanceSystem.R;
import com.surveillance.SurveillanceSystem.SecondFace;

import java.util.List;


public class ImageListArrayAdapter extends ArrayAdapter<SecondFace> {

    List<SecondFace> lists;
    Context context;

    public ImageListArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<SecondFace> lists) {
        super(context, resource, lists);
        this.context = context;
        this.lists = lists;
    }

    private class ViewHolder {
        LinearLayout imageContainer;
        TextView tvSeconds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.from(context).inflate(R.layout.row_image_list, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
//            holder.iv.add((ImageView) convertView.findViewById(R.id.iv1));
//            holder.iv.add((ImageView) convertView.findViewById(R.id.iv2));
//            holder.iv.add((ImageView) convertView.findViewById(R.id.iv3));
//            holder.iv.add((ImageView) convertView.findViewById(R.id.iv4));
//            holder.iv.add((ImageView) convertView.findViewById(R.id.iv5));
//            holder.iv.add((ImageView) convertView.findViewById(R.id.iv6));
//            holder.iv.add((ImageView) convertView.findViewById(R.id.iv7));

//            for (int i = 0; i < holder.iv.size(); i++) {
//                holder.iv.get(i).setImageBitmap(images.get(i));
//            }
            holder.tvSeconds = (TextView) convertView.findViewById(R.id.tvSecond);
            holder.imageContainer = (LinearLayout) convertView.findViewById(R.id.imageContainer);

//            holder.thumbView = (ImageView) convertView.findViewById(R.id.thumbView);
//            holder.tvVideoName = (TextView) convertView.findViewById(R.id.tvVideoName);
//            holder.tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
//            holder.tvRecordDate = (TextView) convertView.findViewById(R.id.tvRecordDate);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SecondFace sf = lists.get(position);
        List<Bitmap> images = sf.getBitmaps();
        holder.tvSeconds.setText(sf.getSeconds() + " Second");
        for (Bitmap image : images) {
            ImageView iv = new ImageView(context);
            iv.setImageBitmap(image);
            holder.imageContainer.addView(iv);
//                LinearLayout.LayoutParams vp =
//                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return lists.size();
    }
//    public List<ReportRecord> getReportRecords() {
//        return reportRecords;
//    }
}
