package com.onicolian.drawer.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.onicolian.drawer.Entity.Clothes;
import com.onicolian.drawer.CustomListViewCloth;
import com.onicolian.drawer.DBHelper;
import com.onicolian.drawer.Fragment.SetsFragment;
import com.onicolian.drawer.R;

import java.util.ArrayList;

public class ChangeSetActivity extends AppCompatActivity {

    Button delButton;
    ListView list;
    EditText desk;
    DBHelper sqlHelper;
    SQLiteDatabase db;
    Cursor setCursor;
    Cursor cursorClothes;
    CustomListViewCloth userAdapter;

    long setId=0;
    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeset);

        delButton = findViewById(R.id.deleteButton);
        list = findViewById(R.id.list);
        desk = findViewById(R.id.desk);

        sqlHelper = new DBHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setId = extras.getLong("id");
        }
        // если 0, то добавление
        if (setId > 0) {
            // получаем элемент по id из бд
            setCursor = db.rawQuery("select * from " + DBHelper.SET + " where " +
                    DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(setId)});
            setCursor.moveToFirst();
            desk.setText(setCursor.getString(2));

            String set = setCursor.getString(1).replace(" ", ", ");

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

            // создааем адаптер и настраиваем список
            userAdapter = new CustomListViewCloth(this, R.layout.cloth_item, rowItems);
            list.setAdapter(userAdapter);
        } else {
            delButton.setVisibility(View.GONE);
        }
    }

    public void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setId = extras.getLong("id");
        }
        if (setId > 0) {
            // получаем элемент по id из бд
            setCursor = db.rawQuery("select * from " + DBHelper.SET + " where " +
                    DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(setId)});
            setCursor.moveToFirst();

            String set = setCursor.getString(1).replace(" ", ", ");

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

            // создааем адаптер и настраиваем список
            userAdapter = new CustomListViewCloth(this, R.layout.cloth_item, rowItems);
            list.setAdapter(userAdapter);

        } else {
            delButton.setVisibility(View.GONE);
        }
    }

    public void add(View view) {

        Intent intent = new Intent(this, AddToSetActivity.class);
        intent.putExtra("setID", setId);
        intent.putExtra("desk", desk.getText().toString());
        startActivity(intent);
    }

    public void delete(View view){
        db.delete(DBHelper.SET, "_id = ?", new String[]{String.valueOf(setId)});
        goHome();
    }
    private void goHome(){

        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, SetsFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

//    public void save(long str){
//        ContentValues cv = new ContentValues();
//        cv.put(DBHelper.COLUMN_DESK, desk.getText().toString());
//
//        if (str > 0) {
//            db.update(DBHelper.SET, cv, DBHelper.COLUMN_ID + "=" + str, null);
//        } else {
//            db.insert(DBHelper.SET, null, cv);
//        }
//    }
}
