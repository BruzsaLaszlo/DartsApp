package bruzsa.laszlo.dartsapp.ui.x01;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;

import java.util.function.Consumer;

public class NumberPad extends TableLayout {

    private final Context context;
    Consumer<Integer> onClick;
    Consumer<Integer> onLongClick;

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
        row.addView(getChip("0", 0));
        row.addView(getChip("<x", BACK));
        addView(row, getTableLayoutParams());
    }

    private Chip getChip(String text, int id) {
        Chip chip = new Chip(context);
        chip.setOnClickListener(v -> onClick.accept(id));
        chip.setOnLongClickListener(v -> {
            onLongClick.accept(id);
            return true;
        });
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(context,
                null,
                0,
                com.google.android.material.R.style.Widget_Material3_Chip_Assist);
        chip.setChipDrawable(chipDrawable);
        chip.setLayoutParams(getTableRowLayoutParams());
        chip.setText(text);
        chip.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        chip.setTextSize(50);
//        chip.setTextColor(Color.BLACK);         // color
        return chip;
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

    public void setOnClickListener(Consumer<Integer> onClick) {
        this.onClick = onClick;
    }

    public void setOnLongClickListener(Consumer<Integer> onLongClick) {
        this.onLongClick = onLongClick;
    }

}
