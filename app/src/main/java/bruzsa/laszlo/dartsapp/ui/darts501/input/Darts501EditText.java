package bruzsa.laszlo.dartsapp.ui.darts501.input;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.MutableLiveData;

import java.util.Optional;

public class Darts501EditText extends AppCompatEditText {

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
                case SHOOT -> setShootSettings();
                case NEW_GAME -> setNewGameSettings();
                case NAME1, NAME2 -> setNameSettings();
            }
        });
    }

    private void setNewGameSettings() {
        setHint("Restart!");
    }

    private void setShootSettings() {
        setError(null);
        setHint("");
        setText("");
        scoreInputWatcher.setIgnoreValidation(false);
        setInputType(EditorInfo.TYPE_CLASS_PHONE);
    }

    private void setNameSettings() {
        scoreInputWatcher.setIgnoreValidation(true);
        setError(null);
        setHint("name");
        setText("");
        setInputType(EditorInfo.TYPE_CLASS_TEXT);
    }

    public Optional<Integer> getShoot() {
        return inputValidator.getValidShoot(this);
    }

    public Optional<String> getName() {
        return inputValidator.validateName(this);
    }

}
