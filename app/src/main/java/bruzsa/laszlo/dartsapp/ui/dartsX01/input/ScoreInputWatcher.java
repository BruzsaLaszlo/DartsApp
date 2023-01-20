package bruzsa.laszlo.dartsapp.ui.dartsX01.input;

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
        // default implementation ignored
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // default implementation ignored
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!ignoreValidation && inputValidator.isInvalidScore(s)) {
            if (s.charAt(s.length() - 1) == '+') s.replace(s.length() - 1, s.length(), "");
            else s.replace(s.length() - 1, s.length(), "+");
        } else if (s.toString().contains("+")) {
            int sum = Arrays.stream(s.toString().split("[+]"))
                    .filter(s1 -> s1.matches("\\d+"))
                    .mapToInt(Integer::parseInt)
                    .sum();
            scoreEditText.setError(String.valueOf(sum));
        }
    }

    public void setIgnoreValidation(boolean ignoreValidation) {
        this.ignoreValidation = ignoreValidation;
    }
}
