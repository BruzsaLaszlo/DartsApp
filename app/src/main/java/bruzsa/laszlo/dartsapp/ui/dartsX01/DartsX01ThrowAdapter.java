package bruzsa.laszlo.dartsapp.ui.dartsX01;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.model.dartsX01.DartsX01Throw;

public class DartsX01ThrowAdapter extends RecyclerView.Adapter<DartsX01ThrowAdapter.ViewHolder> {

    private List<DartsX01Throw> mDataSet;
    private final MutableLiveData<DartsX01Throw> selectedForRemove = new MutableLiveData<>();

    @SuppressLint("NotifyDataSetChanged")
    public void refreshAll(List<DartsX01Throw> throwList) {
        mDataSet = throwList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView shootTextView;
        private DartsX01Throw shoot;

        public ViewHolder(View v) {
            super(v);
            v.setOnLongClickListener(v1 -> {
                selectedForRemove.setValue(shoot);
                return true;
            });
            shootTextView = v.findViewById(R.id.dartsX01ThrowItem);
        }

        public void setThrow(DartsX01Throw shoot) {
            this.shoot = shoot;
        }
    }

    public DartsX01ThrowAdapter(List<DartsX01Throw> shoots) {
        mDataSet = shoots;
    }

    public void inserted() {
        notifyItemInserted(mDataSet.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_dartsx01, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        DartsX01Throw dartsX01Throw = mDataSet.get(position);
        viewHolder.shootTextView.setText(dartsX01Throw.toString());
        if (!dartsX01Throw.isNotHandicap())
            viewHolder.shootTextView.setTextColor(Color.rgb(100, 100, 0));
        else if (!dartsX01Throw.isValid())
            viewHolder.shootTextView.setTextColor(Color.RED);
        viewHolder.setThrow(dartsX01Throw);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public MutableLiveData<DartsX01Throw> getSelectedForRemove() {
        return selectedForRemove;
    }

    public void remove(int position) {
        notifyItemRemoved(position);
    }
}
