package bruzsa.laszlo.dartsapp.ui.cricket;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.WindowInsets;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class ScreenSize {

    private final FragmentActivity activity;

    public ScreenSize(FragmentActivity activity) {
        this.activity = activity;
    }

    public Size getSize() {
        return new Size(getScreenWidth(), getScreenHeight());
    }

    private int getScreenWidth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Rect bounds = windowMetrics.getBounds();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(
                    WindowInsets.Type.systemBars()
            );

            if (activity.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE
                    && activity.getResources().getConfiguration().smallestScreenWidthDp < 600
            ) { // landscape and phone
                int navigationBarSize = insets.right + insets.left;
                return bounds.width() - navigationBarSize;
            } else { // portrait or tablet
                return bounds.width();
            }
        } else {
            DisplayMetrics outMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            return outMetrics.widthPixels;
        }
    }

    private int getScreenHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Rect bounds = windowMetrics.getBounds();
            Insets insets = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(
                    WindowInsets.Type.systemBars()
            );

            if (activity.getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE
                    && activity.getResources().getConfiguration().smallestScreenWidthDp < 600
            ) { // landscape and phone
                return bounds.height();
            } else { // portrait or tablet
                int navigationBarSize = insets.bottom;
                return bounds.height() - navigationBarSize;
            }
        } else {
            DisplayMetrics outMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            return outMetrics.heightPixels;
        }
    }

    public static Size getLegacySize(@NonNull Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            final WindowMetrics metrics = activity.getWindowManager().getCurrentWindowMetrics();
            // Gets all excluding insets
            final WindowInsets windowInsets = metrics.getWindowInsets();
            Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()
                    | WindowInsets.Type.displayCutout());

            int insetsWidth = insets.right + insets.left;
            int insetsHeight = insets.top + insets.bottom;

            // Legacy size that Display#getSize reports
            final Rect bounds = metrics.getBounds();
            return new Size(bounds.width() - insetsWidth,
                    bounds.height() - insetsHeight);
        } else {
            return null;
        }
    }
}
