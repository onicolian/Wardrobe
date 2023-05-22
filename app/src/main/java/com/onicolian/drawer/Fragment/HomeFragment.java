package com.onicolian.drawer.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.onicolian.drawer.DBHelper;
import com.onicolian.drawer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HomeFragment extends Fragment {

    SQLiteDatabase db;
    DBHelper databaseHelper;
    Cursor cursorClothes;
    Cursor shoesC;

    double longitude = 0;
    double latitude = 0;
    public String name;
    public Double temp;
    public String weatherPic;
    public Element el;
    public TextView tempView;
    public TextView textView2;
    public LinearLayout my_root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tempView = view.findViewById(R.id.textView);
        textView2 = view.findViewById(R.id.textView2);
        my_root = view.findViewById(R.id.layout);
        databaseHelper = new DBHelper(getActivity().getApplicationContext());
        db = databaseHelper.getReadableDatabase();

//        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        longitude = location.getLongitude();
//        latitude = location.getLatitude();

        new NewThread().execute();

        return view;
    }

    public void getCloth(String str){
        shoesC = db.rawQuery("select * from " + DBHelper.CLOTHES + " where " +
                DBHelper.COLUMN_CATEGORY + "=?" + " ORDER BY RANDOM() LIMIT 1", new String[]{String.valueOf(str)});
        shoesC.moveToFirst();
        System.out.println("Bye world...");
        if(shoesC.getCount() > 0)
            randCursor(shoesC);
    }

    @SuppressLint("Range")
    public void randCursor(Cursor cursor){
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        final TextView textView = new TextView(getActivity());
//        textView.setLayoutParams(lparams);
//        textView.setText(cursor.getString(cursor.getColumnIndex("_id")));
//        my_root.addView(textView);

        final ImageView imgView = new ImageView(getActivity());
        imgView.setLayoutParams(lparams);

        @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        imgView.setImageBitmap(bitmap);
        my_root.addView(imgView);
    }


    public class NewThread extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg){
            Document doc;
            try {

                String myURL = "https://api.openweathermap.org/data/2.5/weather?q=Kazan&appid=860d812cd2b71d581d2ad0a8a162a10d&units=metric";
                doc = Jsoup.connect(myURL).ignoreContentType(true).get();

                Elements contentHeader = doc.select("body");
                el = contentHeader.get(0);

                String text = String.valueOf(el);
                JSONObject jsonObj = new JSONObject(text.substring(text.indexOf('>') + 1, text.indexOf("</")));

                name = jsonObj.getString("name");

                JSONObject main = (JSONObject) jsonObj.get("main");
                temp = Double.valueOf(main.getString("temp"));

                JSONObject weather = (JSONObject)(((JSONArray)jsonObj.get("weather")).get(0));
                weatherPic = weather.getString("main");
            }
            catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected  void onPostExecute (String result){
            tempView.append(String.valueOf(temp));
            textView2.append(String.valueOf(weatherPic));

            getCloth("Обувь");

            if (temp >= 20){

                int q = (int) (Math.random() * 2);

                switch(q){
                    case(0):{
                        getCloth("Платье");
                        break;
                    }

                    case(1):{
                        getCloth("Футболка");
                        q = (int) (Math.random() * 2);
                        switch(q){
                            case(0):{
                                getCloth("Юбка");
                                break;
                            }

                            case(1):{
                                getCloth("Брюки");
                                break;
                            }
                        }
                    }
                }
            }
            else if (temp < 20){

                getCloth("Верхняя одежда");
                getCloth("Шапка");

                if (temp >= 0){
                    int q = (int) (Math.random() * 2);

                    switch(q){
                        case(0):{
                            getCloth("Платье");
                            break;
                        }

                        case(1):{
                            q = (int) (Math.random() * 3);
                            switch(q){
                                case(0):{
                                    getCloth("Свитер");
                                    break;
                                }

                                case(1):{
                                    getCloth("Толстовка");
                                    break;
                                }

                                case(2):{
                                    getCloth("Футболка");
                                    break;
                                }
                            }
                            q = (int) (Math.random() * 2);
                            switch(q){
                                case(0):{
                                    getCloth("Юбка");
                                    break;
                                }

                                case(1):{
                                    getCloth("Брюки");
                                    break;
                                }
                            }
                        }
                    }
                }
                else{
                    getCloth("Брюки");
                    getCloth("Футболка");
                    int q = (int) (Math.random() * 2);
                    switch(q){
                        case(0):{
                            getCloth("Свитер");
                            break;
                        }

                        case(1):{
                            getCloth("Толстовка");
                            break;
                        }
                    }
                }

            }

        }

    }
}
