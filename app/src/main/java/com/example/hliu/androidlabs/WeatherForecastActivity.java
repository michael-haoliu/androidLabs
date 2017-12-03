package com.example.hliu.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecastActivity extends Activity {
    private ProgressBar progressBar;// = findViewById(R.id.progressBar1_ID);
    private TextView curTemp;//        = findViewById(R.id.stringCurrentTemp_ID);
    private TextView maxTemp ;//       = findViewById(R.id.stringMaxTemp_ID);
    private TextView minTemp;//        = findViewById(R.id.stringMinTemp_ID);
    private ImageView imageView;//     = findViewById(R.id.imageWeather_ID);

    public final static String Tag = WeatherForecastActivity.class.getSimpleName();
    private final static String TempTag = "temperature";
    private final static String PicTag = "weather";

    private final static String TempAttr_value = "value";
    private final static String TempAttr_min = "min";
    private final static String TempAttr_max = "max";
    private final static String TempAttr_icon= "icon";

    private String urlWeather = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        //----------------
        progressBar = findViewById(R.id.progressBar1_ID);
        curTemp     = findViewById(R.id.stringCurrentTemp_ID);
        maxTemp     = findViewById(R.id.stringMaxTemp_ID);
        minTemp     = findViewById(R.id.stringMinTemp_ID);
        imageView   = findViewById(R.id.imageWeather_ID);

        //-------progress bar
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        ForcastQuery forcastQuery = new ForcastQuery();
        forcastQuery.execute(urlWeather);

    }

    public class ForcastQuery extends AsyncTask<String, Integer, String>{
        private String curTemp_str, minTemp_str, maxTemp_str;
        private Bitmap pic_icon;
        private String iconName;

        public ForcastQuery() {
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            curTemp.setText(curTemp_str + " C");
            minTemp.setText(minTemp_str + " C");
            maxTemp.setText(maxTemp_str + " C");

            imageView.setImageBitmap(pic_icon);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i("Tag", "weather in onProgressUpdate");
            progressBar.setProgress(values[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String url_string = strings[0];
            InputStream inputStream = downloadUrl(url_string);

            try{
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xmlPullParser = factory.newPullParser();
                //Sets the input stream the parser is going to process.
                // This call resets the parser state and sets the event type to the initial value START_DOCUMENT
                xmlPullParser.setInput(inputStream,"UTF-8");
                while(xmlPullParser.next() != XmlPullParser.END_DOCUMENT){
                    SystemClock.sleep(200);

                    if(xmlPullParser.getEventType() != XmlPullParser.START_TAG){
                        continue;
                    }
                    if(xmlPullParser.getName().equalsIgnoreCase(TempTag)){
                        curTemp_str = xmlPullParser.getAttributeValue(null, TempAttr_value);
                        publishProgress(25);
                        SystemClock.sleep(200);

                        minTemp_str = xmlPullParser.getAttributeValue(null, TempAttr_min);
                        publishProgress(50);

                        maxTemp_str = xmlPullParser.getAttributeValue(null, TempAttr_max);
                        publishProgress(75);

                        SystemClock.sleep(200);
                    }

                    //------------
                    if(xmlPullParser.getName().equalsIgnoreCase(PicTag)){
                        iconName = xmlPullParser.getAttributeValue(null, TempAttr_icon);
                        String picURL_str = "http://openweathermap.org/img/w/" + iconName + ".png";
                        String fileDirectory_icon = iconName + ".png";

                        if(fileExistance(fileDirectory_icon)){ // file exists

                            FileInputStream fis = null;
                            try {
                                fis = openFileInput(fileDirectory_icon);
                                Bitmap bm = BitmapFactory.decodeStream(fis);
                                pic_icon = bm;

                                Log.i(Tag, "weather Image already exists");
                                publishProgress(100);
                            }
                            catch (FileNotFoundException e) {
                                Log.i(Tag, "weather error Image file not exist/ can not open");
                                e.printStackTrace();
                            }

                        }else{
                            Bitmap bm = getImage(picURL_str);

                            if(bm !=null){
                                pic_icon = bm;
//                                Bitmap image  = HTTPUtils.getImage(ImageURL));

                                FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);

                                pic_icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                Log.i(Tag, "weather  Image not exists and loaded from URL");

                                publishProgress(100);

                                SystemClock.sleep(200);

                            }else{
                                Log.i(Tag, "weather error  Image not exists and can not be loaded url - Null");
                            }
                        }// exist or load from web
                    }// pic tag
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.e("Tag", "weather xmlpullparser exception ");
            }
            return null;
        }

        //---------------------------------
        private InputStream downloadUrl(String url_str){
            InputStream inputStream=null;
            try{
                URL url = new URL(url_str);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                //start query
                connection.connect();

                Log.i(Tag, "weather : open url  " + url_str);
                inputStream = connection.getInputStream();

            }catch(Exception e){
                //MalformedURLException
                // IO
                //IllegalStateException
                e.printStackTrace();
                Log.i(Tag, "weather error: can not open url  " + url_str);
            }
            return inputStream;
        }
        //-------------------------
        private boolean fileExistance(String fileName) {
            Log.i(Tag, "weather check fileExistance" );
            File file = getBaseContext().getFileStreamPath(fileName);
            Log.i(Tag, file.toString());
            return file.exists();
        }

        //---------------------------------
        private Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        private Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }





        private Bitmap downLoadImage(URL url) {
            Log.i(Tag, "Weather download Image");
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                int response = httpURLConnection.getResponseCode();
                if (response == 200) {
                    return BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }

    }// inner class
}// outter class
