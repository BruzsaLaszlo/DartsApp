package bruzsa.laszlo.dartsapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Speech {

    private final SpeechRecognizer speechRecognizer;
    private final Intent speechRecognizerIntent;
    private static final int REQUEST_RECORD_PERMISSION = 100;
    private static final String TAG = "Speech";


    private final MutableLiveData<List<String>> resultLiveData = new MutableLiveData<List<String>>();

    public Speech(Context context) {
        Log.i(TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(context));
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale("hu", "HU"));

        speechRecognizer.setRecognitionListener(new RecognitionListener() {

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
                Log.d(TAG, "onRmsChanged: " + rmsdB);
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
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.d(TAG, "onResults: " + data);
                List<String> numbers = data.stream()
                        .filter(s -> {
                            try {
                                Integer.parseInt(s);
                                return true;
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
                resultLiveData.setValue(numbers);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                Log.d(TAG, "onPartialResults");
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.d(TAG, "onEvent: " + eventType);

            }
        });
    }

    public SpeechRecognizer getSpeechRecognizer() {
        return speechRecognizer;
    }

    public LiveData<List<String>> getResultLiveData() {
        return resultLiveData;
    }

    public void startListening() {
        speechRecognizer.startListening(speechRecognizerIntent);
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

}
