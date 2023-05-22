package com.onicolian.drawer.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.onicolian.drawer.Activity.ChangeSetActivity;
import com.onicolian.drawer.CustomListViewSets;
import com.onicolian.drawer.DBHelper;
import com.onicolian.drawer.Entity.Clothes;
import com.onicolian.drawer.Entity.Sets;
import com.onicolian.drawer.R;

import java.util.ArrayList;

public class SetsFragment  extends Fragment {

    ListView userList;
    DBHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursorClothes;
    CustomListViewSets userAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sets, container, false);

        userList = view.findViewById(R.id.list);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ChangeSetActivity.class);

                Sets item = (Sets) parent.getAdapter().getItem(position);

                //int str =Integer.parseInt((String) ((TextView) view.findViewById(R.id.idText)).getText());
                intent.putExtra("id", item.getId());
                startActivity(intent);
            }
        });

        Button button = view.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeSetActivity.class);
                startActivity(intent);
            }
        });

        databaseHelper = new DBHelper(getActivity().getApplicationContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        cursorClothes = db.rawQuery("select * from " + DBHelper.SET, null);

        ArrayList<Sets> rowItems = new ArrayList<>();
        if (cursorClothes.getCount() > 0) {
            cursorClothes.moveToFirst();
            do {
                @SuppressLint("Range") Sets item = new Sets(cursorClothes.getLong(cursorClothes.getColumnIndex("_id")),
                        cursorClothes.getString(cursorClothes.getColumnIndex("setList")),
                        cursorClothes.getString(cursorClothes.getColumnIndex("desk")));
                rowItems.add(item);
            } while (cursorClothes.moveToNext());
        }

        // создааем адаптер и настраиваем список
        userAdapter = new CustomListViewSets(getActivity(), R.layout.sets_item, rowItems);
        userList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        cursorClothes.close();
    }
}
