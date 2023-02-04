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
import java.util.function.BiPredicate;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;

public class X01ThrowAdapter extends RecyclerView.Adapter<X01ThrowAdapter.ViewHolder> {

    private final List<X01Throw> mDataSet;
    private final BiPredicate<X01Throw, Team> removeCallback;
    private final Team team;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView throwTextView;

        public ViewHolder(View v) {
            super(v);
            throwTextView = v.findViewById(R.id.dartsX01ThrowItem);
        }

    }

    public X01ThrowAdapter(List<X01Throw> mDataSet, BiPredicate<X01Throw, Team> removeCallback, Team team) {
        this.mDataSet = mDataSet;
        this.removeCallback = removeCallback;
        this.team = team;
    }

    public void inserted() {
        notifyItemInserted(mDataSet.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_x01_throws, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        X01Throw x01Throw = mDataSet.get(position);
        viewHolder.throwTextView.setText(x01Throw.toString());
        if (!x01Throw.isNotHandicap()) {
            viewHolder.throwTextView.setTextColor(Color.rgb(100, 100, 0));
        } else if (!x01Throw.isValid()) {
            viewHolder.throwTextView.setTextColor(Color.RED);
        }
        if (x01Throw.isRemoved()) {
            viewHolder.throwTextView.setTypeface(null, Typeface.ITALIC);
            viewHolder.throwTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        viewHolder.throwTextView.setOnLongClickListener(v1 -> {
            if (removeCallback.test(x01Throw, team)) {
                notifyItemChanged(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) return 0;
        return mDataSet.size();
    }

}
