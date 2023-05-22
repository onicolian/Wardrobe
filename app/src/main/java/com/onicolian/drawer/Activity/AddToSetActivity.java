package com.onicolian.drawer.Activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.onicolian.drawer.Entity.Clothes;
import com.onicolian.drawer.CustomListViewCloth;
import com.onicolian.drawer.DBHelper;
import com.onicolian.drawer.Entity.Sets;
import com.onicolian.drawer.R;

import java.util.ArrayList;
import java.util.Objects;

public class AddToSetActivity extends AppCompatActivity {

    ListView userList;
    DBHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursorClothes;
    Cursor setCursor;
    CustomListViewCloth userAdapter;
    String desk;
    long setId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtoset);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setId = extras.getLong("setID");
            desk = extras.getString("desk");
        }

        userList = findViewById(R.id.list);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setCursor = db.rawQuery("select * from " + DBHelper.SET + " where " +
                        DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(setId)});
                setCursor.moveToFirst();

                String set;
                Clothes item = (Clothes) parent.getAdapter().getItem(position);
                id = item.getId();
                if (setCursor != null && setCursor.moveToFirst()){
                    set = setCursor.getString(1) + " " + Objects.toString(id, null);
                }
                else set = Objects.toString(id, null);
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.COLUMN_SETLIST, set);
                cv.put(DBHelper.COLUMN_DESK, desk);
                if (setId > 0) {
                    db.update(DBHelper.SET, cv, DBHelper.COLUMN_ID + "=" + setId, null);
                } else {
                    db.insert(DBHelper.SET, null, cv);
//
//                    setCursor = db.rawQuery("select * from " + DBHelper.SET + " ORDER BY " +
//                            DBHelper.COLUMN_ID + " DESC LIMIT 1", new String[]{String.valueOf(setId)});
//                    setCursor.moveToFirst();
//                    setId = Long.parseLong(setCursor.getString(0));
                }
                goHome();

            }
        });

        databaseHelper = new DBHelper(getApplicationContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();
        //получаем данные из бд в виде курсора
        cursorClothes = db.rawQuery("select * from " + DBHelper.CLOTHES, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
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

        // создааем адаптер и настраиваем список
        userAdapter = new CustomListViewCloth(this, R.layout.cloth_item, rowItems);
        userList.setAdapter(userAdapter);
    }

    // по нажатию на кнопку запускаем UserActivity для добавления данных
    public void add(View view) {
        Intent intent = new Intent(this, ChangeClothActivity.class);
        startActivity(intent);
    }

    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, ChangeSetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent.putExtra("id", setId);
        startActivity(intent);
    }
}
