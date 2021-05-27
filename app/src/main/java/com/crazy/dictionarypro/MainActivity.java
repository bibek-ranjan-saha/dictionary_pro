package com.crazy.dictionarypro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
    String wordr;
    private SpeechRecognizer speechRecognizer;
    public static final Integer RequestAudioRequestCode = 1;
    AlertDialog.Builder alertspeechd;
    AlertDialog alertDialog;
//    private int percentage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = findViewById(R.id.wordtxt);
        t2 = findViewById(R.id.txtans);
        b1 = findViewById(R.id.srchbtn);
        mic = findViewById(R.id.micbtn);
        spk = findViewById(R.id.spkbtn);
        pr = findViewById(R.id.prgbar);
        cut = findViewById(R.id.cutbtn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestApiButtonClick();
            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
        {
            checkPermissionn();
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speak the word to get meaning");
//                try {
//                    startActivityForResult(intent, 1);
//                } catch (ActivityNotFoundException e) {
//                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                }
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.speechlayout,viewGroup,false);
                alertspeechd = new AlertDialog.Builder(MainActivity.this);
                alertspeechd.setMessage("Listening....");
                alertspeechd.setView(dialogView);
                alertDialog = alertspeechd.create();
                alertDialog.show();
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                mic.setImageResource(R.drawable.mic);
                ArrayList<String> arrayList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                ArrayList<String> confidence = results.getStringArrayList(SpeechRecognizer.CONFIDENCE_SCORES);
                t1.setText(arrayList.get(0));
//                percentage = Integer.parseInt(confidence.get(0));
//                Toast.makeText(MainActivity.this, "confidence level : "+percentage+"%", LENGTH_SHORT).show();
                alertDialog.dismiss();
                requestApiButtonClick();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


//        TextWatcher textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if (!t1.getText().toString().equals("") && t1.getText().toString().contains(" ")) {
//                        wordr = t1.getText().toString();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                requestApiButtonClick(b1);
//
//            }
//        };
//        t1.addTextChangedListener(textWatcher);

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

        mic.setOnTouchListener((v, event) -> {
            if (event.getAction() == event.ACTION_UP)
            {
                speechRecognizer.stopListening();
            }

            if (event.getAction() == event.ACTION_DOWN)
            {
                mic.setImageResource(R.drawable.mic);
                speechRecognizer.startListening(intent);
            }
            return false;
        });
    }

    private void checkPermissionn() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RequestAudioRequestCode);
    }


    public void requestApiButtonClick() {
            if (!t1.getText().toString().isEmpty())
            {
                wordr = t1.getText().toString();
                MyDictionaryRequest r = new MyDictionaryRequest(MainActivity.this);
                url = dictionaryEntries();
                r.execute(url);
            }
            else {
                Toast.makeText(this, "write something", LENGTH_SHORT).show();
            }
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
                        requestApiButtonClick();
                    }
                    break;
            }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RequestAudioRequestCode && grantResults.length>0)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "mic permission granted", LENGTH_SHORT).show();
            }
        }

    }
}