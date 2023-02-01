package bruzsa.laszlo.dartsapp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static bruzsa.laszlo.dartsapp.ui.x01.input.InputType.VOICE;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.function.IntConsumer;

import bruzsa.laszlo.dartsapp.databinding.InputViewsBinding;
import bruzsa.laszlo.dartsapp.ui.Language;
import bruzsa.laszlo.dartsapp.ui.Speech;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputText;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputType;
import bruzsa.laszlo.dartsapp.ui.x01.input.NumberPad;

public class InputViews {

    private final Speech speech;
    private final InputText inputText;
    private final NumberPad numberPad;
    private final ImageView imageMicrophone;

    public InputViews(Fragment fragment, InputType inputType, InputViewsBinding bindingInputs) {
        inputText = bindingInputs.inputText;
        numberPad = bindingInputs.numPad;
        imageMicrophone = bindingInputs.imageMicrophone;

        speech = new Speech(fragment.getContext());

        numberPad.setInputTextNumber(inputText);

        inputText.setOnLongClickListener(this::createAlertDialogChangeInputType);

        if (inputType == InputType.NUMPAD) {
            setInputToNumPad();
        } else if (inputType == VOICE) {
            setInputToVoice();
        }


        if (Helper.isRecordPermissionGranted(fragment)) {
            imageMicrophone.setOnClickListener(v -> startSpeechRecognizer());
            imageMicrophone.setOnLongClickListener(this::showLanguageChangerDialog);
        }

        speech.getResultLiveData().observe(fragment.getViewLifecycleOwner(),
                strings -> {
                    String input = String.join("+", strings);
                    inputText.setText(input);
                    imageMicrophone.setImageResource(R.drawable.mute);
                    onOKClick();
                    inputText.setHint(input);
                    speech.textToSpeech(input);
                });
        speech.getErrorLiveData().observe(fragment.getViewLifecycleOwner(), s -> {
            inputText.setHint(s);
            imageMicrophone.setImageResource(R.drawable.mute);
        });
    }

    private void startSpeechRecognizer() {
        imageMicrophone.setImageResource(R.drawable.microphone);
        speech.startListening();
    }

    private void onOKClick() {
        inputText.setReady();
    }

    private void setInputToNumPad() {
        imageMicrophone.setVisibility(GONE);
        numberPad.setVisibility(VISIBLE);
        inputText.setHint("");
        inputText.clear();
        speech.stopListening();
    }

    private void setInputToVoice() {
        imageMicrophone.setVisibility(VISIBLE);
        imageMicrophone.setImageResource(R.drawable.mute);
        numberPad.setVisibility(GONE);
        inputText.setHint("VOICE");
        inputText.clear();
    }

    public boolean createAlertDialogChangeInputType(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Control by : ");
        builder.setItems(new CharSequence[]{"Numpad", "Voice"}, (dialog, which) -> {
            if (which == 0) setInputToNumPad();
            else setInputToVoice();
        });
        builder.create().show();
        return true;
    }

    private boolean showLanguageChangerDialog(View v) {
        showLanguageDialog(v.getContext());
        return true;
    }

    private void showLanguageDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose language");
        builder.setSingleChoiceItems(
                Arrays.stream(Language.values()).map(Language::name).toArray(String[]::new), 0,
                (dialog, which) -> speech.setLanguage(Language.values()[which]));
        builder.setNeutralButton("OK", null);
        builder.create().show();
    }

    public void setOnReadyAction(IntConsumer callback) {
        inputText.setOnReadyAction(callback);
    }
}
