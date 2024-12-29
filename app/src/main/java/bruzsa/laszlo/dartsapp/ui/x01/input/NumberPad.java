package bruzsa.laszlo.dartsapp.ui.x01.input;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.NumberPadChipBinding;

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
        createChips();
    }

    public void setInputTextNumber(InputTextNumber inputTextNumber) {
        this.inputTextNumber = inputTextNumber;
    }

    private void createChips() {
        for (int i = 0; i <= 2; i++) {
            TableRow row = createTableRow();
            for (int j = 1; j <= 3; j++) {
                int number = i * 3 + j;
                addChip("  " + number + "  ", number, row);
            }
            addView(row);
        }
        TableRow row = createTableRow();
        addChip("OK", OK, row);
        addChip("  0  ", 0, row);
        addChip(" <- ", BACK, row);
        addView(row);
    }

    private void addChip(String text, int key, TableRow parent) {
        View inflate = LayoutInflater.from(context)
                .inflate(R.layout.number_pad_chip, parent, true);
        NumberPadChipBinding binding = NumberPadChipBinding.bind(inflate);
        binding.numpadChip.setLayoutParams(getTableRowLayoutParams());
        binding.numpadChip.setText(text);
        binding.numpadChip.setId(key);
        binding.numpadChip.setOnClickListener(v -> getChipOnClickListener(key));
        binding.numpadChip.setOnLongClickListener(v -> getChipOnLongClickListener(key));
    }

    private boolean getChipOnLongClickListener(int key) {
        switch (key) {
            case OK -> inputTextNumber.setReady();
            case BACK -> inputTextNumber.clear();
            default -> inputTextNumber.plusAdd(key);
        }
        return true;
    }

    private void getChipOnClickListener(int key) {
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
        lp.setMargins(5, 0, 5, 0);
        return lp;
    }

}
