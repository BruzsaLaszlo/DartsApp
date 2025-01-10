package bruzsa.laszlo.dartsapp.ui.cricket;

import static androidx.window.layout.WindowMetricsCalculator.getOrCreate;

import android.util.Size;

import androidx.fragment.app.FragmentActivity;
import androidx.window.layout.WindowMetricsCalculator;

public class ScreenSize {

    private final FragmentActivity activity;
    private final WindowMetricsCalculator windowMetricsCalculator = getOrCreate();

    public ScreenSize(FragmentActivity activity) {
        this.activity = activity;
    }

    public Size getSize() {
        var windowMetrics = windowMetricsCalculator.computeCurrentWindowMetrics(activity);
        return new Size(windowMetrics.getBounds().width(), windowMetrics.getBounds().height());
    }

}
