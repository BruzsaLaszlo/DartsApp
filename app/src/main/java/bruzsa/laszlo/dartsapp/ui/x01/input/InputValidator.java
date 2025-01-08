package bruzsa.laszlo.dartsapp.ui.x01.input;

import static bruzsa.laszlo.dartsapp.model.x01.ThrowValidator.isValidThrow;

import android.widget.EditText;

import java.util.Arrays;
import java.util.Optional;

public class InputValidator {

    private static final int MAX_NAME_LENGTH = 20;
    public static final String NUMBER = "^\\d+$";

    public CharSequence validateInput(CharSequence text, String character) {
        if (character == null) return text;
        String newInput = text == null ? character : text.toString().concat(character);
        if (newInput.startsWith("+")) newInput = newInput.substring(1);
        if (isValidScore(newInput)) {
            return newInput;
        }
        return text;
    }

    public int getValidNumber(String inputText) {
        if (inputText.contains("+")) {
            return Arrays.stream(inputText.split("[+]"))
                    .filter(s1 -> s1.matches(NUMBER))
                    .mapToInt(Integer::parseInt)
                    .sum();
        }
        return Integer.parseInt(inputText);
    }

    public boolean isValidScore(String s) {
        if (s == null || s.isBlank()) return false;

        if (s.matches("^0{2}") || !s.matches("^\\d*\\+?\\d+\\+?\\d*$")) return false;

        int sum = Arrays.stream(s.split("[+]"))
                .mapToInt(Integer::parseInt)
                .sum();

        return isValidThrow(sum);
    }

    public Optional<Integer> getValidThrow(String text) {
        if (isValidScore(text))
            return Optional.of(getValidNumber(text));
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
