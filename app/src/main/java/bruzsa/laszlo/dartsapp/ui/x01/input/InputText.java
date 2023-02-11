package bruzsa.laszlo.dartsapp.ui.x01.input;


import static com.google.android.material.R.style.Widget_Material3_Chip_Assist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class InputText extends Chip implements InputTextNumber {

    private Consumer<Chip> iconEvent;
    private IntConsumer onReady;

    private final InputValidator validator = new InputValidator();

    public InputText(@NonNull Context context) {
        super(context, null, 0);
    }

    public InputText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public InputText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, Widget_Material3_Chip_Assist);
    }

    public void plusAdd(int number) {
        add("+" + number);
    }

    public void add(int number) {
        add(String.valueOf(number));
    }

    private void add(@NonNull String character) {
        CharSequence validatedText = validator.validateInput(getText(), character);
        setText(validatedText);
    }

    public void removeLast() {
        if (getText() != null && !getText().toString().isEmpty())
            setText(getText().subSequence(0, getText().length() - 1));
    }

    public void clear() {
        setText("");
        setHint("");
    }

    public Optional<Integer> getThrow() {
        return validator.getValidThrow(getText().toString());
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




