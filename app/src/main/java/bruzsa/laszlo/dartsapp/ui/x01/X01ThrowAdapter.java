package bruzsa.laszlo.dartsapp.ui.x01;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;

public class X01ThrowAdapter extends RecyclerView.Adapter<X01ThrowAdapter.ViewHolder> {

    private final List<X01Throw> mDataSet;
    private final OnChangeThrow onChangeThrow;
    private RecyclerView recyclerView;

    public X01ThrowAdapter(List<X01Throw> mDataSet, OnChangeThrow onChangeThrow) {
        this.mDataSet = mDataSet;
        this.onChangeThrow = onChangeThrow;
    }

    public void inserted() {
        int pos = getItemCount() - 1;
        notifyItemInserted(pos);
        if (recyclerView != null)
            recyclerView.smoothScrollToPosition(pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_x01_throws, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        X01Throw x01Throw = mDataSet.get(position);
        viewHolder.throwTextView.setText(x01Throw.toString());

        viewHolder.throwTextView.setTypeface(null, Typeface.NORMAL);
        viewHolder.throwTextView.setPaintFlags(Paint.CURSOR_AFTER);
        switch (x01Throw.getStatus()) {
            case VALID -> viewHolder.throwTextView.setTextColor(Color.BLACK);
            case INVALID -> viewHolder.throwTextView.setTextColor(Color.RED);
            case REMOVED -> {
                viewHolder.throwTextView.setTypeface(null, Typeface.ITALIC);
                viewHolder.throwTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
        if (!viewHolder.throwTextView.hasOnClickListeners()) {
            viewHolder.throwTextView.setOnLongClickListener(v -> {
                if (x01Throw.isNotRemoved()) {
                    onChangeThrow.change(
                            x01Throw,
                            recyclerView.getContext(),
                            changed -> {
                                if (changed) notifyItemChanged(position);
                            });
                }
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) return 0;
        return mDataSet.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView throwTextView;

        public ViewHolder(View v) {
            super(v);
            throwTextView = v.findViewById(R.id.throwTextView);
        }

    }
}
