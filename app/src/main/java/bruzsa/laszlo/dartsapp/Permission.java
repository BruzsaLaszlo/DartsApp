package bruzsa.laszlo.dartsapp;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.function.Consumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Permission {

    private final Fragment fragment;
    private ActivityResultLauncher<String> resultLauncher;
    @Getter
    private boolean result;

    public void checkAndRequestPermission(String permission, Consumer<Boolean> result) {
        Log.i("Speech", "requestRecordPermission: " + permission);
        if (!isPermissionEnable(permission)) {
            Log.w("Speech", permission + ": permission denied");
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
//                showInContextUI(...);
                new MaterialAlertDialogBuilder(fragment.requireContext())
                        .setTitle("Permission")
                        .setMessage("Microphone need for SpeechToText, please seclect to enable!")
                        .setPositiveButton("OK", (dialog, which) -> {
                            resultLauncher.launch(permission);
                            result.accept(this.result);
                            Log.i("TAG", "checkAndRequestPermission: " + this.result);
                        })
                        .show();
            } else {
//                 You can directly ask for the permission.
//                 The registered ActivityResultCallback gets the result of this request.
                Log.w("TAG", "shouldShowRequestPermissionRationale: false");
                new MaterialAlertDialogBuilder(fragment.requireContext())
                        .setTitle("Permission")
                        .setMessage("Microphone need for SpeechToText, please seclect to enable!")
                        .setPositiveButton("OK", (dialog, which) -> {
                            fragment.requireActivity().startActivity(new Intent(
                                    ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", "bruzsa.laszlo.dartsapp", null)));
                            result.accept(this.result);
                            Log.i("TAG", "checkAndRequestPermission: " + this.result);
                        })
                        .show();

            }
        } else {
            Log.i("Speech-Permission", permission + ": GRANTED without asking user");
            result.accept(true);
        }
    }

    public boolean isPermissionEnable(String permission) {
        return checkSelfPermission(fragment.requireContext(), permission) == PERMISSION_GRANTED;
    }

    public void registerForActivityResult() {
        resultLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                b -> result = b);
    }

    public Context getContext() {
        return fragment.requireContext();
    }

}
