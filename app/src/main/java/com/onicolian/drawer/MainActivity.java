package com.onicolian.drawer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.onicolian.drawer.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public String name;
    public Double temp;
    public String weatherPic;
    public Element el;
    public TextView tempView;
    public TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headerView = navigationView.getHeaderView(0);
        tempView = headerView.findViewById(R.id.textView);
        textView2 = headerView.findViewById(R.id.textView2);

        new NewThread().execute();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public class NewThread extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg) {
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

                JSONObject weather = (JSONObject) (((JSONArray) jsonObj.get("weather")).get(0));
                weatherPic = weather.getString("main");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected  void onPostExecute (String result) {
            tempView.append(String.valueOf(temp));
            textView2.append(String.valueOf(weatherPic));
        }
    }
}