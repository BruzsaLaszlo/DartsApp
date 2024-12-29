package bruzsa.laszlo.dartsapp.ui.cricket;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.model.cricket.CricketThrow;

public class CricketThrowsAdapter extends RecyclerView.Adapter<CricketThrowsAdapter.ViewHolder> {

    private final List<CricketThrow> mDataSet;
    private final Consumer<CricketThrow> removeEventListener;
    private RecyclerView recyclerView;

    public CricketThrowsAdapter(List<CricketThrow> mDataSet, Consumer<CricketThrow> removeEventListener) {
        this.mDataSet = mDataSet;
        this.removeEventListener = removeEventListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cricket, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CricketThrow cricketThrow = mDataSet.get(position);
        viewHolder.cricketThrow = cricketThrow;
        viewHolder.throwTextView.setText(cricketThrow.toString());
        if (cricketThrow.isRemoved()) {
            viewHolder.throwTextView.setTypeface(null, Typeface.ITALIC);
            viewHolder.throwTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.throwTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.throwTextView.setPaintFlags(
                    viewHolder.throwTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView throwTextView;
        private CricketThrow cricketThrow;

        public ViewHolder(View v) {
            super(v);
            v.setOnLongClickListener(v1 -> {
                removeEventListener.accept(cricketThrow);
                notifyItemChanged(mDataSet.indexOf(cricketThrow));
                return true;
            });
            throwTextView = v.findViewById(R.id.cricketThrowItem);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addNewThrow() {
        int position = getItemCount() - 1;
        notifyItemInserted(position);
        recyclerView.smoothScrollToPosition(position);
    }
}
