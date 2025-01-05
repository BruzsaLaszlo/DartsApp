package bruzsa.laszlo.dartsapp;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.speech.tts.TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntConsumer;

import bruzsa.laszlo.dartsapp.ui.webgui.WebServer;

public final class Helper {

    private Helper() {
    }

    public static boolean requestRecordPermission(@NonNull Fragment fragment, String permission) {
        AtomicBoolean result = new AtomicBoolean(false);
        Log.i("Speech", "requestRecordPermission: ");
        if (checkSelfPermission(fragment.requireContext(), permission) == PERMISSION_DENIED) {
            Log.d("Speech", permission + ": permission denied");
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
//                showInContextUI(...);
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                            if (isGranted) {
                                Log.i("Speech", permission + " : GRANTED");
                                result.set(true);
                                // Permission is granted. Continue the action or workflow in your
                                // app.
                            } else {
                                Log.e("Speech", permission + " : ACCESS DENIED");
                                // Explain to the user that the feature is unavailable because the
                                // feature requires a permission that the user has denied. At the
                                // same time, respect the user's decision. Don't link to system
                                // settings in an effort to convince the user to change their
                                // decision.
                            }
                        })
                        .launch(permission);
            }
        } else {
            Log.d("Speech-Permission", permission + ": ok");
            return true;
        }
        return result.get();
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


    public static Optional<String> getIPv4Address(Context context) {
        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager instanceof ConnectivityManager cm) {
            LinkProperties link = cm.getLinkProperties(cm.getActiveNetwork());
            Log.i("IPAddress List", link.getLinkAddresses().toString());

            // return only one IPv4Address
            return link.getLinkAddresses().stream()
                    .filter(linkAddress -> linkAddress.getAddress().getAddress().length == 4)
                    .findFirst()
                    .map(LinkAddress::toString)
                    .map(s -> s.substring(0, s.indexOf("/")) + ":" + WebServer.WEBSERVER_PORT);
        }
        return Optional.empty();
    }

    public static final String WEB_GUI_X01 = "webgui/webgui_x01.html";
    public static final String CRICKET_WEB_GUI = "webgui/webgui_cricket.html";

    public static String getHtmlTemplate(Context context, String assetFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(assetFile), StandardCharsets.UTF_8))) {
            return reader.lines()
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        } catch (IOException ioException) {
            throw new IllegalArgumentException("File can not read", ioException);
        }
    }

    public static void showCheckoutDialog(int throwValue, Context context, IntConsumer onClickListener) {
        if (throwValue > 110 || List.of(109, 108, 106, 105, 103, 102).contains(throwValue)) {
            onClickListener.accept(3);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("How many darts has been thrown?");
            if ((throwValue > 40 && throwValue != 50) || throwValue % 2 == 1) {
                builder.setItems(new CharSequence[]{"2 darts", "3 darts"},
                        (dialog, which) -> onClickListener.accept(which + 2));
            } else {
                builder.setItems(new CharSequence[]{"1 dart", "2 darts", "3 darts"},
                        (dialog, which) -> onClickListener.accept(which + 1));
            }
            builder.create().show();
        }
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

    private void showAlertDialog(String title, Context context, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setNeutralButton("OK", onClickListener)
                .create()
                .show();
    }
}
