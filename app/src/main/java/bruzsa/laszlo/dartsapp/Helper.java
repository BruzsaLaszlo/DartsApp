package bruzsa.laszlo.dartsapp;

import static android.speech.tts.TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES;

import android.content.Intent;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

public final class Helper {

    private Helper() {
    }

    private void checkTtsData(Fragment fragment) {
        fragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                                Intent data = result.getData();
                                if (data != null) {
                                    var list = data.getCharSequenceArrayListExtra(EXTRA_AVAILABLE_VOICES);
                                    Log.d("MainActivity", "EXTRA_AVAILABLE_VOICES: " + list);
                                }
                            } else {
                                // missing data, install it
                                Intent installIntent = new Intent();
                                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                                fragment.startActivity(installIntent);
                            }
                        })
                .launch(new Intent().setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA));
    }


    public static void showError() {
        try {
            Class.forName("dalvik.system.CloseGuard")
                    .getMethod("setEnabled", boolean.class)
                    .invoke(null, true);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setStrictMode() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build());
        StrictMode.enableDefaults();  // <-- This includes warning on leaked closeables
    }

}
