package bruzsa.laszlo.dartsapp.ui.darts501.input;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Arrays;

public class ScoreInputWatcher implements TextWatcher {

    private final InputValidator inputValidator;
    private final EditText scoreEditText;
    private boolean ignoreValidation;

    public ScoreInputWatcher(EditText scoreEditText, InputValidator inputValidator) {
        this.scoreEditText = scoreEditText;
        this.inputValidator = inputValidator;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!ignoreValidation && inputValidator.isInvalidScore(s)) {
            if (s.charAt(s.length() - 1) == '+') s.replace(s.length() - 1, s.length(), "");
            else s.replace(s.length() - 1, s.length(), "+");
        } else if (s.toString().contains("+")) {
            int sum = Arrays.stream(s.toString().split("[+]"))
                    .filter(s1 -> s1.matches("[0-9]+"))
                    .mapToInt(Integer::parseInt)
                    .sum();
            scoreEditText.setError(String.valueOf(sum));
        }
    }

    public void setIgnoreValidation(boolean ignoreValidation) {
        this.ignoreValidation = ignoreValidation;
    }
}
