package bruzsa.laszlo.dartsapp.speech;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.RECORD_AUDIO;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import bruzsa.laszlo.dartsapp.speech.events.ErrorEventListener;
import bruzsa.laszlo.dartsapp.speech.events.ResultEventListener;

public class Speech {

    public static final String NEED_PERMISSION_AUDIO = RECORD_AUDIO;
    public static final String NEED_PERMISSION_INTERNET = INTERNET;
    private TextToSpeech textToSpeech;

    private final SpeechRecognizer speechRecognizer;
    private Language language = Language.HUNGARIAN;
    private static final String TAG = "Speech";

    private final ResultEventListener resultEventListener;
    private final ErrorEventListener errorEventListener;

    private static Speech speech;

    public static Speech build(@NonNull Context context, ResultEventListener resultEventListener, ErrorEventListener errorEventListener) {
            if (speech == null)
                speech = new Speech(context, resultEventListener, errorEventListener);
        return speech;
    }

    private Speech(Context context, ResultEventListener resultEventListener, ErrorEventListener errorEventListener) {
        this.resultEventListener = resultEventListener;
        this.errorEventListener = errorEventListener;

        textToSpeech = new TextToSpeech(context, status -> {
            Log.d(TAG, "Speech: " + textToSpeech.getAvailableLanguages());
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(new Locale(language.getCountryCode()));
            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(getCustomRecognizerListener());
    }

    @NonNull
    private RecognitionListener getCustomRecognizerListener() {
        return new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "onReadyForSpeech");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                //Log.d(TAG, "onRmsChanged: " + rmsdB);
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                Log.d(TAG, "onBufferReceived: " + Arrays.toString(buffer));
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "onEndOfSpeech");
            }

            @Override
            public void onError(int error) {
                Log.d(TAG, "onError: " + getErrorText(error));
                errorEventListener.onError(getErrorText(error));
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.d(TAG, "onResults: " + data);
                List<String> numbers = Objects.requireNonNull(data).stream()
                        .flatMap(s -> Arrays.stream(s.split("\\s")))
                        .map(s -> switch (s.toLowerCase()) {
                            case "zero", "zérus", "semmi" -> "0";
                            case "egy", "one" -> "1";
                            case "kettő", "two" -> "2";
                            case "öt" -> "5";
                            case "hat" -> "6";
                            case "hatvan" -> "60";
                            default -> s;
                        })
                        .filter(s -> s.chars().anyMatch(Character::isDigit))
                        .collect(Collectors.toList());
                resultEventListener.onResult(numbers);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                Log.d(TAG, "onPartialResults");
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.d(TAG, "onEvent: " + eventType);

            }
        };
    }


    public void startListening() {
        speechRecognizer.startListening(getSpeechRecognizerIntent());
    }

    public void setLanguage(Language language) {
        this.language = language;
        textToSpeech.setLanguage(new Locale(language.getCountryCode()));
    }

    private Intent getSpeechRecognizerIntent() {
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if (language == null)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        else
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language.getCountryCode());
        return speechRecognizerIntent;
    }

    public void stopListening() {
        speechRecognizer.stopListening();
    }

    private String getErrorText(int errorCode) {
        return switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO -> "Audio recording error";
            case SpeechRecognizer.ERROR_CLIENT -> "Client side error";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions";
            case SpeechRecognizer.ERROR_NETWORK -> "Network error";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout";
            case SpeechRecognizer.ERROR_NO_MATCH -> "No match";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy";
            case SpeechRecognizer.ERROR_SERVER -> "error from server";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input";
            default -> "Didn't understand, please try again.";
        };
    }

    public void textToSpeech(CharSequence text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "valami");
    }

}
