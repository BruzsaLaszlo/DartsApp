package bruzsa.laszlo.dartsapp.util;

import android.app.Activity;

import androidx.activity.OnBackPressedCallback;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HandleBackButton extends OnBackPressedCallback {

    private final Activity activity;

    public HandleBackButton(Activity activity) {
        super(true);
        this.activity = activity;
    }

    @Override
    public void handleOnBackPressed() {
        new MaterialAlertDialogBuilder(activity)
                .setTitle("Exit Game")
                .setMessage("Are you sure?")
                .setPositiveButton("EXIT", (dialog, which) -> {
                    remove();
                    activity.onBackPressed();
                })
                .setNegativeButton("CANCEL", (dialog, which) -> handleOnBackCancelled())
                .show();
    }

}
