package bruzsa.laszlo.dartsapp.ui.darts501.input;

import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.text.InputType.TYPE_CLASS_TEXT;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Optional;

public class Darts501EditText extends TextInputEditText {

    private final InputValidator inputValidator = new InputValidator();
    private final ScoreInputWatcher scoreInputWatcher = new ScoreInputWatcher(this, inputValidator);

    public Darts501EditText(@NonNull Context context) {
        super(context);
    }

    public Darts501EditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Darts501EditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addTextChangedListener(scoreInputWatcher);
    }

    public void setInputType(MutableLiveData<InputType> inputTypeMutableLiveData) {
        inputTypeMutableLiveData.observeForever(inputType -> {
            switch (inputType) {
                case THROW -> setThrowSettings();
                case RESTART_GAME -> setNewGameSettings();
                case NAME -> setNameSettings();
            }
        });
    }

    private void setNewGameSettings() {
        setHint("Restart!");
    }

    private void setThrowSettings() {
        setError(null);
        setHint("");
        setText("");
        scoreInputWatcher.setIgnoreValidation(false);
        setInputType(TYPE_CLASS_PHONE);
    }

    private void setNameSettings() {
        scoreInputWatcher.setIgnoreValidation(true);
        setError(null);
        setHint("name");
        setText("");
        setInputType(TYPE_CLASS_TEXT);
    }

    public Optional<Integer> getThrow() {
        return inputValidator.getValidThrow(this);
    }

    public Optional<String> getName() {
        return inputValidator.validateName(this);
    }

}
