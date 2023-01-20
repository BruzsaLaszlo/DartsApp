package bruzsa.laszlo.dartsapp.ui.X01.input;

import android.text.Editable;
import android.widget.EditText;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class InputValidator {

    private static final int MAX_NAME_LENGTH = 20;

    public boolean isInvalidScore(Editable s) {
        if (s.length() == 0) return false;

        if (!s.toString().matches("^\\d*\\+?\\d+\\+?\\d*$")) return true;

        int sum = Arrays.stream(s.toString().split("[+]"))
                .mapToInt(Integer::parseInt)
                .sum();

        return sum > 180 || Set.of(179, 178, 176, 175, 173, 172, 169, 166, 163).contains(sum);
    }

    public Optional<Integer> getValidThrow(EditText scoreEditText) {
        if (scoreEditText.getText().toString().isBlank()) return Optional.empty();
        if (scoreEditText.getText().toString().matches("^\\d+$")) {
            return Optional.of(Integer.parseInt(scoreEditText.getText().toString()));
        } else if (scoreEditText.getError() != null &&
                scoreEditText.getError().toString().matches("^\\d+$")) {
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
