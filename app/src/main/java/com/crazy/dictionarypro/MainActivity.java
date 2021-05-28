package com.crazy.dictionarypro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    ConstraintLayout bg;
    String messgae;
    private SpeechRecognizer speechRecognizer;
    public static final Integer RequestAudioRequestCode = 1;
    AlertDialog.Builder alertspeechd;
    AlertDialog alertDialog;
//    private int percentage;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bg = findViewById(R.id.bg);

        TransitionManager.beginDelayedTransition(bg);

        t1 = findViewById(R.id.wordtxt);
        t2 = findViewById(R.id.txtans);
        b1 = findViewById(R.id.srchbtn);
        mic = findViewById(R.id.micbtn);
        spk = findViewById(R.id.spkbtn);
        pr = findViewById(R.id.prgbar);
        cut = findViewById(R.id.cutbtn);

        messgae = t1.getText().toString();

        if (savedInstanceState != null)
        {
            messgae = savedInstanceState.getString("msg");
            wordr = savedInstanceState.getString("word");
            t1.setText(wordr);
            t2.setText(messgae);
        }

        b1.setOnClickListener(v -> requestApiButtonClick());

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
                messgae = t1.getText().toString();
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

        cut.setOnClickListener(v -> {
            if (!t1.getText().toString().equals(""))
            {
                t1.setText("");
            }
            else
            {
                Snackbar.make(v,"nothing to clear",Snackbar.LENGTH_SHORT).show();
            }
        });


        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) textToSpeech.setLanguage(Locale.ENGLISH);
        });

        spk.setOnClickListener(v -> textToSpeech.speak(t2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null));

        mic.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                speechRecognizer.stopListening();
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN)
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

        @SuppressLint("SetTextI18n")
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1) {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    t1.setText(t1.getText() + result.get(0));
                    requestApiButtonClick();
                }
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("msg",messgae);
        outState.putString("word",wordr);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("you are about to leave me")
                .setTitle("dictionary app")
                .setNegativeButton("no", (dialog, which) -> Toast.makeText(getApplicationContext(),"lets continue", LENGTH_SHORT).show())
                .setPositiveButton("yes", (dialog, which) -> finishAffinity())
                .show();
    }
}