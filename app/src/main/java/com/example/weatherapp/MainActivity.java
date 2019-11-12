package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

   EditText input ;
   TextView weather;


   public void findOut (View view){

       Log.i("city",input.getText().toString());

       Download dwn = new Download();

       dwn.execute("https://api.openweathermap.org/data/2.5/weather?q="+input.getText().toString()+"&APPID=6fd2e5e662f36a2428a0aa8515d75780");


   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        weather =(TextView) findViewById(R.id.weather);






    }






    class Download extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            URL url;
            HttpsURLConnection connection;

            try {
                url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader read = new InputStreamReader(in);

                int data=read.read();

                while (data !=-1){
                    char datachar = (char) data;

                    result+=datachar;

                    data=read.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsn = new JSONObject(result);
                String weatherino = jsn.getString("weather");
                Log.i("Website",weatherino);


                JSONArray arr = new JSONArray(weatherino);
                String message ="";
                for(int i = 0;i<arr.length();i++){

                    JSONObject part = arr.getJSONObject(i);

                    String main =part.getString("main");
                    String description = part.getString("description");


                    if(main != "" && description != ""){
                    message += "Main: "+ main + "\r\n" + "Description: " + description + "\r\n" + "Temperature: "  + "\r\n" ;}

                }

                if (message != "") {
                    weather.setText(message);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
