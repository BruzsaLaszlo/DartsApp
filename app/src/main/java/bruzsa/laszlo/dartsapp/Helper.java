package bruzsa.laszlo.dartsapp;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.concurrent.atomic.AtomicBoolean;

public class Helper {

    private Helper() {
    }

    public static boolean requestRecordPermission(@NonNull Fragment fragment) {
        AtomicBoolean result = new AtomicBoolean();
        if (checkSelfPermission(fragment.requireContext(), android.Manifest.permission.RECORD_AUDIO) == PERMISSION_DENIED) {
            Log.d("DartsX01Fragment", "onCreate: RECORD_AUDIO permission denied");
            if (fragment.shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)) {
//                showInContextUI(...);
            } else {
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result::set)
                        .launch(Manifest.permission.RECORD_AUDIO);
            }
        }
        return result.get();
    }

    public static boolean isRecordPermissionGranted(@NonNull Fragment fragment) {
        return checkSelfPermission(fragment.requireContext(), android.Manifest.permission.RECORD_AUDIO) == PERMISSION_GRANTED;
    }
}
