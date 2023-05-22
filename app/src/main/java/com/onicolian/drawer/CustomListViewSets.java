package com.onicolian.drawer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onicolian.drawer.Entity.Clothes;
import com.onicolian.drawer.Entity.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomListViewSets extends ArrayAdapter<Sets> {

    Context context;
    Cursor cursorClothes;
    DBHelper sqlHelper;
    SQLiteDatabase db;

    public CustomListViewSets(Context context, int resourceId,
                               List<Sets> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        LinearLayout layout;
        TextView txtTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Sets rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sets_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.tvText);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.txtTitle.setText(rowItem.getDesk());



        String set = rowItem.getList().replace(" ", ", ");

        sqlHelper = new DBHelper(context);
        db = sqlHelper.getWritableDatabase();
        cursorClothes = db.rawQuery("select * from " + DBHelper.CLOTHES + " where " +
                DBHelper.COLUMN_ID + " in (" + set + ")", new String[]{});
        cursorClothes.moveToFirst();

        ArrayList<Clothes> rowItems = new ArrayList<>();
        if (cursorClothes.getCount() > 0) {
            cursorClothes.moveToFirst();
            do {
                @SuppressLint("Range") Clothes item = new Clothes(cursorClothes.getLong(cursorClothes.getColumnIndex("_id")),
                        cursorClothes.getBlob(cursorClothes.getColumnIndex("image")),
                        cursorClothes.getString(cursorClothes.getColumnIndex("category")),
                        cursorClothes.getString(cursorClothes.getColumnIndex("date")),
                        cursorClothes.getDouble(cursorClothes.getColumnIndex("prise")),
                        cursorClothes.getString(cursorClothes.getColumnIndex("desk")),
                        cursorClothes.getString(cursorClothes.getColumnIndex("isFavorite")));

                rowItems.add(item);
            } while (cursorClothes.moveToNext());
        }

        for (Clothes cloth : rowItems) {
            ImageView myImage = new ImageView(context);
            Bitmap bitmap = BitmapFactory.decodeByteArray(cloth.getImage(), 0, cloth.getImage().length);
            myImage.setImageBitmap(bitmap);
            holder.layout.addView(myImage);
        }
        return convertView;
    }
}
