package bruzsa.laszlo.dartsapp.ui.x01.input;

import static android.content.Context.MODE_PRIVATE;
import static bruzsa.laszlo.dartsapp.ui.x01.input.InputType.NUMPAD;
import static bruzsa.laszlo.dartsapp.ui.x01.input.InputType.VOICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntConsumer;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.InputViewsBinding;
import bruzsa.laszlo.dartsapp.speech.Language;
import bruzsa.laszlo.dartsapp.speech.Speech;
import lombok.Getter;

@SuppressWarnings("ConstantConditions")
public class InputViews {

    private Speech speech = null;
    @Getter
    private final MutableLiveData<InputType> inputType = new MutableLiveData<>(NUMPAD);
    private final InputText inputText;
    private final ImageView imageMicrophone;

    @SuppressLint("ClickableViewAccessibility")
    public InputViews(Fragment fragment, InputViewsBinding bindingInputs) {

        inputText = bindingInputs.inputText;

        imageMicrophone = bindingInputs.imageMicrophone;

        bindingInputs.numPad.setInputTextNumber(inputText);

        boolean success = inicSpeech(fragment);
        if (success) {
            imageMicrophone.setOnTouchListener(this::getOnTouchListener);
            inputType.setValue(loadInputType(fragment.getContext()));
        } else {
            inputType.setValue(NUMPAD);
        }
    }

    private boolean inicSpeech(Fragment fragment) {
        speech = Speech.build(
                fragment,
                this::onSpeechResult,
                this::onSpeechError);
        return speech != null;
    }

    private boolean getOnTouchListener(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            imageMicrophone.setImageResource(R.drawable.microphone);
            speech.startListening();
            Log.d("InputViews", "getOnTouchListener: down");
            return true;
        }
//        else if (event.getAction() == MotionEvent.ACTION_UP) {
//            Log.d("InputViews", "getOnTouchListener: up");
//            speech.stopListening();
//            return true;
//        }
        return false;
    }

    public void onOKClick() {
        inputText.setReady();
    }

    public boolean createAlertDialogChangeInputType(View view) {
        if (speech == null) {
            Log.e("onLongClick", "Speech is not available");
            return true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Control by : ");
        builder.setItems(new CharSequence[]{"Numpad", "Voice"}, (dialog, which) -> {
            if (which == 0) setInputToNumPad();
            else setInputToVoice();
            saveInputType(inputType.getValue(), view.getContext());
        });
        builder.create().show();
        return true;
    }

    private void setInputToNumPad() {
        inputType.setValue(NUMPAD);
        speech.stopListening();
    }

    private void setInputToVoice() {
        inputType.setValue(VOICE);
    }

    public boolean showLanguageChangerDialog(View v) {
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

    @BindingAdapter("src")
    public static void setImageResources(View view, MutableLiveData<InputType> inputType) {
        if (view instanceof ImageView mageMicrophone && (inputType == null || inputType.getValue() == NUMPAD)) {
            mageMicrophone.setImageResource(R.drawable.mute);
        }
    }

    @BindingAdapter("txt")
    public static void inicInputText(View view, MutableLiveData<InputType> inputType) {
        if (view instanceof InputText inputText) {
            if ((inputType == null || inputType.getValue() == NUMPAD)) {
                inputText.setHint("");
            } else {
                inputText.setHint("VOICE");
            }
            inputText.clear();
        }
    }

    private static final String INPUT_VIEWS_PREFERENCES = "InputViews";

    public void saveInputType(InputType inputType, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(INPUT_VIEWS_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(InputType.class.toString(), inputType.name());
        myEdit.apply();
    }

    public InputType loadInputType(Context context) {
        SharedPreferences sh = context.getSharedPreferences(INPUT_VIEWS_PREFERENCES, MODE_PRIVATE);
        String name = sh.getString(InputType.class.toString(), NUMPAD.name());
        return InputType.valueOf(name);
    }

    private void onSpeechResult(List<String> numbers) {
        String input = String.join("+", numbers);
        inputText.setText(input);
        imageMicrophone.setImageResource(R.drawable.mute);
        onOKClick();
        inputText.setHint(input);
        speech.textToSpeech(input);
    }

    private void onSpeechError(String error) {
        if (inputType.getValue() == VOICE) {
            inputText.setHint(error);
            imageMicrophone.setImageResource(R.drawable.mute);
        }
    }
}
