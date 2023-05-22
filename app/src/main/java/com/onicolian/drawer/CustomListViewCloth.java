package com.onicolian.drawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.onicolian.drawer.Entity.Clothes;

import java.util.List;

public class CustomListViewCloth extends ArrayAdapter<Clothes> {

    Context context;

    public CustomListViewCloth(Context context, int resourceId,
                               List<Clothes> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView idTitle;
        TextView priceTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Clothes rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cloth_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.tvText);
            holder.idTitle = (TextView) convertView.findViewById(R.id.idText);
            holder.priceTitle = (TextView) convertView.findViewById(R.id.priseText);
            holder.imageView = (ImageView) convertView.findViewById(R.id.ivImg);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.txtTitle.setText(rowItem.getCategory());
        holder.idTitle.setText(Long.toString(rowItem.getId()));
        holder.priceTitle.setText(Double.toString(rowItem.getPrice()));
        Bitmap bitmap = BitmapFactory.decodeByteArray(rowItem.getImage(), 0, rowItem.getImage().length);
        holder.imageView.setImageBitmap(bitmap);

        return convertView;
    }
}