package com.crazy.dictionarypro;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

    public class MyDictionaryRequest extends AsyncTask<String, Integer, String> {

        private WeakReference<MainActivity> activityWeakReference;

        MyDictionaryRequest(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

//        Context context;
//        TextView textView,t12;
//        ProgressBar prg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing())
            {
                return;
            }
            activity.pr.setVisibility(View.VISIBLE);
            activity.t2.setText("answer comming up shortly....");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            MainActivity activity = activityWeakReference.get();
//            if (activity == null || activity.isFinishing())
//            {
//                return;
//            }
//            activity.pr.setProgress(values[0]);
//            activity.t2.setText("............................");
        }

//        public MyDictionaryRequest(Context context, TextView t2, ProgressBar progressBar) {
//            this.context = context;
//            textView = t2;
//            prg = progressBar;
//        }

        //        final String app_id = "a12cb0c3";
//        final String app_key = "eaf7dd7f3a800d3a301a1af16568e469";
//        String myurl;
//        Context context;
//        TextView t1;
//
//        public MyDictionaryRequest(Context context, TextView t2) {
//            this.context = context;
//            t1 = t2;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            myurl = params[0];
//            try {
//                URL url = new URL(myurl);
//                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
//                urlConnection.setRequestProperty("Accept", "application/json");
//                urlConnection.setRequestProperty("app_id", app_id);
//                urlConnection.setRequestProperty("app_key", app_key);
//
//                // read the output from the server
//                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                StringBuilder stringBuilder = new StringBuilder();
//
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line + "\n");
//                }
//
//                return stringBuilder.toString();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return e.toString();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
//            String def;
//            try {
//                JSONObject js = new JSONObject(s);
//                JSONArray result = js.getJSONArray("results");
//
//                JSONObject lEntries = result.getJSONObject(0);
//                JSONArray lsarray = lEntries.getJSONArray("lexicalEntries");
//
//                JSONObject entries = lsarray.getJSONObject(0);
//                JSONArray e = entries.getJSONArray("entries");
//
//                JSONObject jsonObject = e.getJSONObject(0);
//                JSONArray sensesArray = jsonObject.getJSONArray("senses");
//
//                JSONObject de = sensesArray.getJSONObject(0);
//                JSONArray d = de.getJSONArray("definitions");
//
//                def = d.getString(0);
//                t1.setText(def);
//                Toast.makeText(context, def, Toast.LENGTH_LONG).show();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(context,e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
@Override
protected String doInBackground(String... params) {
    final String app_id = "a12cb0c3";
    final String app_key = "eaf7dd7f3a800d3a301a1af16568e469";
    try {
        URL url = new URL(params[0]);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setRequestProperty("app_id",app_id);
        urlConnection.setRequestProperty("app_key",app_key);

        // read the output from the server
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }

        return stringBuilder.toString();

    }
    catch (Exception e) {
        e.printStackTrace();
        return e.toString();
    }
}

        @Override
        protected void onPostExecute(String result) {
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing())
            {
                return;
            }
            activity.pr.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
            String def;
            try {
                JSONObject js = new JSONObject(result);
                JSONArray results = js.getJSONArray("results");

                JSONObject lEntries = results.getJSONObject(0);
                JSONArray lsarray = lEntries.getJSONArray("lexicalEntries");

                JSONObject entries = lsarray.getJSONObject(0);
                JSONArray e = entries.getJSONArray("entries");

                JSONObject jsonObject = e.getJSONObject(0);
                JSONArray sensesArray = jsonObject.getJSONArray("senses");

                JSONObject de = sensesArray.getJSONObject(0);
                JSONArray d = de.getJSONArray("definitions");

                def = d.getString(0);
                activity.t2.setText(def);
//                Toast.makeText(activity, def, Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
                activity.t2.setText("sorry try any thing else");
//                Toast.makeText(activity,"sorry try any thing else", Toast.LENGTH_LONG).show();
            }
//            Log.v("dictionary","json response comming up"+result);
        }
    }