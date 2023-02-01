package bruzsa.laszlo.dartsapp;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Optional;
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

    public static boolean requestInternetPermission(@NonNull Fragment fragment) {
        AtomicBoolean result = new AtomicBoolean();
        if (checkSelfPermission(fragment.requireContext(), Manifest.permission.INTERNET) == PERMISSION_DENIED) {
            Log.d("DartsX01Fragment", "onCreate: RECORD_AUDIO permission denied");
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.INTERNET)) {
//                showInContextUI(...);
            } else {
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), result::set)
                        .launch(Manifest.permission.INTERNET);
            }
        }
        return result.get();
    }

    public static boolean isRecordInternetGranted(@NonNull Fragment fragment) {
        return checkSelfPermission(fragment.requireContext(), android.Manifest.permission.RECORD_AUDIO) == PERMISSION_GRANTED;
    }

    public static Optional<String> getIPv4Address(Context context) {
        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager instanceof ConnectivityManager cm) {
            LinkProperties link = cm.getLinkProperties(cm.getActiveNetwork());
            Log.e("IPAddress List", link.getLinkAddresses().toString());

            // return only one IPv4Address
            return link.getLinkAddresses().stream()
                    .filter(linkAddress -> linkAddress.getAddress().getAddress().length == 4)
                    .findFirst()
                    .map(LinkAddress::toString);
        }
        return Optional.empty();
    }
}
