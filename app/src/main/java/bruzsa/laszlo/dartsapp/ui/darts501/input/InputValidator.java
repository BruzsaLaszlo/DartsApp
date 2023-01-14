package bruzsa.laszlo.dartsapp.ui.darts501.input;

import android.text.Editable;
import android.widget.EditText;

import java.util.Optional;

public class InputValidator {

    private static final int MAX_NAME_LENGTH = 20;

    public boolean isInvalidScore(Editable s) {
        if (s.length() == 0) return false;

        if (s.toString().matches("\\d+")) {
            int input = Integer.parseInt(s.toString());
            return input > 180 && input % 100 != 0;
        }
        return !s.toString().matches("\\d+|\\d+\\+\\d+|\\d+\\+\\d+\\+\\d+|\\d+\\+|\\d+\\+\\d+\\+");
    }

    public Optional<Integer> getValidThrow(EditText scoreEditText) {
        if (scoreEditText.getText().toString().isBlank()) return Optional.empty();
        if (scoreEditText.getText().toString().matches("^\\d+$")) {
            return Optional.of(Integer.parseInt(scoreEditText.getText().toString()));
        } else if (scoreEditText.getError() != null &&
                scoreEditText.getError().toString().matches("^[0-9]+$")) {
            return Optional.of(Integer.parseInt(scoreEditText.getError().toString()));
        }
        return Optional.empty();
    }

    public Optional<String> validateName(EditText nameText) {
        String name = nameText.getText().toString()
                .strip()
                .replaceFirst(" ", "\n");
        if (name.isBlank()) return Optional.empty();
        name = name.split(" ")[0];
        if (name.length() > MAX_NAME_LENGTH) {
            return Optional.of(name.substring(0, MAX_NAME_LENGTH));
        }
        return Optional.of(name);
    }

}
