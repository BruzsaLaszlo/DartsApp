package bruzsa.laszlo.dartsapp.ui.x01.input;


import static android.provider.CallLog.Calls.NUMBER;
import static com.google.android.material.R.style.Widget_Material3_Chip_Assist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class InputText extends Chip implements InputTextNumber {


    private Consumer<Chip> iconEvent;

    private IntConsumer onReady;

    public InputText(@NonNull Context context) {
        super(context, null, 0);
    }

    public InputText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public InputText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, Widget_Material3_Chip_Assist);
    }

    public Optional<Integer> plusAdd(int number) {
        return add("+" + number);
    }

    public Optional<Integer> add(int number) {
        return add(String.valueOf(number));
    }

    private Optional<Integer> add(String character) {
        String s = getText().toString().concat(character);
        InputValidator inputValidator = new InputValidator();
        if (!inputValidator.isInvalidScore(s)) {
            if (s.contains("+")) {
                int sum = Arrays.stream(s.split("[+]"))
                        .filter(s1 -> s1.matches(NUMBER))
                        .mapToInt(Integer::parseInt)
                        .sum();
                setText(s);
                return Optional.of(sum);
            }
            setText(s);
        }
        return Optional.empty();
    }

    public void removeLast() {
        if (getText() != null && !getText().toString().isEmpty())
            setText(getText().subSequence(0, getText().length() - 1));
    }

    public void clear() {
        setText("");
    }

    public Optional<Integer> getThrow() {
        if (getText() == null || getText().toString().isEmpty())
            return Optional.empty();
        else return Optional.of(Arrays.stream(getText().toString().split("[+]"))
                .mapToInt(Integer::parseInt)
                .sum());
    }

    public void setOnReadyAction(IntConsumer callback) {
        this.onReady = callback;
    }

    @Override
    public void setReady() {
        getThrow().ifPresent(value -> {
            onReady.accept(value);
            clear();
        });
    }

    public void setOnIconClickEvent(Consumer<Chip> iconEvent) {
        this.iconEvent = iconEvent;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getX() <= getTotalPaddingLeft()) {
            if (iconEvent != null)
                iconEvent.accept(this);
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }
}

class InputValidator {

    private static final int MAX_NAME_LENGTH = 20;
    public static final String NUMBER = "^\\d+$";

    public boolean isInvalidScore(String s) {
        if (s.length() == 0) return false;

        if (!s.matches("^\\d*\\+?\\d+\\+?\\d*$")) return true;

        int sum = Arrays.stream(s.split("[+]"))
                .mapToInt(Integer::parseInt)
                .sum();

        return sum > 180 || Set.of(179, 178, 176, 175, 173, 172, 169, 166, 163).contains(sum);
    }

    public Optional<Integer> getValidThrow(EditText scoreEditText) {
        if (scoreEditText.getText().toString().isBlank()) return Optional.empty();
        if (scoreEditText.getText().toString().matches(NUMBER)) {
            return Optional.of(Integer.parseInt(scoreEditText.getText().toString()));
        } else if (scoreEditText.getError() != null &&
                scoreEditText.getError().toString().matches(NUMBER)) {
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



