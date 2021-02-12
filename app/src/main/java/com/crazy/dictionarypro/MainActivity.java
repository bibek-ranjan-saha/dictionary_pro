package com.crazy.dictionarypro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.*;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    String url;
    EditText t1;
    Button b1;
    ImageView mic, spk, cut;
    public TextView t2;
    private TextToSpeech textToSpeech;
    ProgressBar pr;
    private AdView adView;
    String wordr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




//        AudienceNetworkAds.initialize(this);
//        // Instantiate an AdView object.
//        // NOTE: The placement ID from the Facebook Monetization Manager identifies your App.
//        // To get test ads, add IMG_16_9_APP_INSTALL# to your placement id. Remove this when your app is ready to serve real ads.
//
//        adView = new AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
//
//        // Find the Ad Container
//        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
//
//        // Add the ad view to your activity layout
//        adContainer.addView(adView);
//
//        // Request an ad
//        adView.loadAd();


        t1 = findViewById(R.id.wordtxt);
        t2 = findViewById(R.id.txtans);
        b1 = findViewById(R.id.srchbtn);
        mic = findViewById(R.id.micbtn);
        spk = findViewById(R.id.spkbtn);
        pr = findViewById(R.id.prgbar);
        cut = findViewById(R.id.cutbtn);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!t1.getText().toString().equals("") && !t1.getText().toString().contains(" ")) {
                        wordr = t1.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                requestApiButtonClick(b1);

            }
        };
        t1.addTextChangedListener(textWatcher);

        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!t1.getText().toString().equals(""))
                {
                    t1.setText("");
                }
                else
                {
                    Snackbar.make(v,"nothing to clear",Snackbar.LENGTH_SHORT).show();
                }
            }
        });


        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        spk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(t2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speak the word to get meaning");
                try {
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


        public void requestApiButtonClick(View view) {
            MyDictionaryRequest r = new MyDictionaryRequest(MainActivity.this);
            url = dictionaryEntries();
            r.execute(url);

        }

        private String dictionaryEntries() {
            final String language = "en-gb";
            final String word = wordr;
            final String fields = "definitions";
            final String strictMatch = "false";
            final String word_id = word.toLowerCase();
            return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        t1.setText(t1.getText() + result.get(0));
                        requestApiButtonClick(b1);
                    }
                    break;
            }
        }
    }