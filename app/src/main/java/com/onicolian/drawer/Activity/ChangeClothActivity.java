package com.onicolian.drawer.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onicolian.drawer.DBHelper;
import com.onicolian.drawer.Fragment.ClothesFragment;
import com.onicolian.drawer.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class ChangeClothActivity extends AppCompatActivity {

    TextView category;
    Spinner categorySpinner;
    EditText price;
    EditText desk;
    CheckBox isFavorite;
    Button delButton;
    Button saveButton;
    String[] countries = {"Футболка", "Свитер", "Толстовка", "Рубашка", "Пиджак", "Платье", "Юбка", "Брюки", "Шарф", "Шапка", "Верхняя одежда", "Обувь", "Сумка"};
    DBHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    Button buttonLoadImage;
    ImageView imgView;

    long userId = -1;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changecloth);

        category = findViewById(R.id.category);

        categorySpinner = findViewById(R.id.categorySpinner);
        price = findViewById(R.id.price);
        desk = findViewById(R.id.desk);
        isFavorite = findViewById(R.id.isFavorite);
        delButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        imgView = findViewById(R.id.imgView);
        buttonLoadImage = findViewById(R.id.buttonLoadPicture);
        imgView = findViewById(R.id.imgView);


        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });

        sqlHelper = new DBHelper(this);
        db = sqlHelper.getWritableDatabase();

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                category.setText(item);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };
        categorySpinner.setOnItemSelectedListener(itemSelectedListener);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        // если 0, то добавление
        if (userId >= 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + DBHelper.CLOTHES + " where " +
                    DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            byte[] image = userCursor.getBlob(1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imgView.setImageBitmap(bitmap);
//            for (int i=0; i<countries.length;i++)
//                if (userCursor.getString(2) == countries[i]){
//                    categorySpinner.setSelection(i);
//                    break;
//                }

            String compareValue = userCursor.getString(2);
            if (compareValue != null) {
                int spinnerPosition = adapter.getPosition(compareValue);
                categorySpinner.setSelection(spinnerPosition);
            }


            category.setText(categorySpinner.getSelectedItem().toString());
            price.setText(String.valueOf(userCursor.getInt(4)));
            desk.setText(String.valueOf(userCursor.getString(5)));
            if (userCursor.getInt(6) == 1)
                isFavorite.setChecked(true);
            userCursor.close();
        } else {
            delButton.setVisibility(View.GONE);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imgView.setImageURI(selectedImageUri);

                }
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            imgView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//        }
//    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_CATEGORY, category.getText().toString());
        cv.put(DBHelper.COLUMN_DATE, String.valueOf(new Date()));
        if (price.getText().toString().equals(""))
            cv.put(DBHelper.COLUMN_PRISE, 0);
        else
            cv.put(DBHelper.COLUMN_PRISE, Integer.parseInt(price.getText().toString()));
        cv.put(DBHelper.COLUMN_DESK, desk.getText().toString());
        if (isFavorite.isChecked())
            cv.put(DBHelper.COLUMN_FAVORITE, 1);
        else
            cv.put(DBHelper.COLUMN_FAVORITE, 0);

        Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        cv.put(DBHelper.COLUMN_IMAGE, imageInByte);

        if (userId >= 0) {
            db.update(DBHelper.CLOTHES, cv, DBHelper.COLUMN_ID + "=" + userId, null);
        } else {
            db.insert(DBHelper.CLOTHES, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DBHelper.CLOTHES, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, ClothesFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
