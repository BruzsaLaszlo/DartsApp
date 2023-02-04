package bruzsa.laszlo.dartsapp.ui.x01.input;

import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

public final class InputBindingAdapters {

    private InputBindingAdapters() {
    }

    @BindingAdapter("hideIfVoiceDisabled")
    public static void hideIfDisabledVoice(View view, MutableLiveData<Boolean> voiceEnabled) {
        view.setVisibility(Boolean.TRUE.equals(voiceEnabled.getValue()) ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("hideIfNumPadDisabled")
    public static void hideIfNumPadDisabled(View view, MutableLiveData<Boolean> voiceEnabled) {
        view.setVisibility(Boolean.TRUE.equals(voiceEnabled.getValue()) ? View.GONE : View.VISIBLE);
    }

}
