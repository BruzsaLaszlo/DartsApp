package bruzsa.laszlo.dartsapp.ui.x01.input;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;

public class NumberPad extends TableLayout {

    private final Context context;
    private InputTextNumber inputTextNumber;

    public static final int OK = 10;
    public static final int BACK = 11;

    public NumberPad(Context context) {
        this(context, null);
    }

    public NumberPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setLayoutParams(new ConstraintLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        setPadding(0, 0, 0, 0);
        ((ConstraintLayout.LayoutParams) getLayoutParams()).setMargins(0, 0, 0, 0);
        createChips09();
    }

    public void setInputTextNumber(InputTextNumber inputTextNumber) {
        Log.d("NumberPad", "setInputTextNumber: " + inputTextNumber);
        this.inputTextNumber = inputTextNumber;
    }

    private void createChips09() {
        for (int i = 0; i < 3; i++) {
            TableRow row = createTableRow();
            for (int j = 1; j <= 3; j++) {
                int number = i * 3 + j;
                row.addView(getChip("  " + number + "  ", number), getTableRowLayoutParams());
            }
            addView(row, getTableLayoutParams());
        }
        TableRow row = createTableRow();
        row.addView(getChip("OK", OK));
        row.addView(getChip("  0  ", 0));
        row.addView(getChip("<-", BACK));
        addView(row, getTableLayoutParams());
    }

    private Chip getChip(String text, int id) {
        Chip chip = new Chip(context);
        chip.setOnClickListener(v -> getChipOnClickListener(id));
        chip.setOnLongClickListener(v -> getChipOnLongClickListener(id));
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(context,
                null,
                0,
                com.google.android.material.R.style.Widget_Material3_Chip_Assist);
        chip.setChipDrawable(chipDrawable);
        chip.setLayoutParams(getTableRowLayoutParams());
        chip.setText(text);
        chip.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        chip.setTextSize(50);
        return chip;
    }

    private boolean getChipOnLongClickListener(int key) {
        switch (key) {
            case OK -> inputTextNumber.setReady();
            case BACK -> inputTextNumber.clear();
            default -> inputTextNumber.plusAdd(key);
        }
        return false;
    }

    private void getChipOnClickListener(int key) {
        Log.d("NumberPad", "getChipOnClickListener: " + inputTextNumber);
        switch (key) {
            case OK -> inputTextNumber.setReady();
            case BACK -> inputTextNumber.removeLast();
            default -> inputTextNumber.add(key);
        }
    }

    private TableRow createTableRow() {
        TableRow row = new TableRow(context);
        row.setLayoutParams(getTableLayoutParams());
        return row;
    }

    private LayoutParams getTableLayoutParams() {
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        lp.weight = 0.5f;
        lp.setMargins(0, 0, 0, 0);
        return lp;
    }

    private TableRow.LayoutParams getTableRowLayoutParams() {
        TableRow.LayoutParams lp = new TableRow.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        lp.weight = 0.5f;
        lp.setMargins(5, 0, 5, 0);
        return lp;
    }

}
