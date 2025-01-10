package bruzsa.laszlo.dartsapp.util;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HandleBackButton extends OnBackPressedCallback {

    private final FragmentActivity activity;

    public HandleBackButton(FragmentActivity activity) {
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
                    activity.getOnBackPressedDispatcher().onBackPressed();
                })
                .setNegativeButton("CANCEL", (dialog, which) -> handleOnBackCancelled())
                .show();
    }

}
