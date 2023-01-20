package bruzsa.laszlo.dartsapp.ui.darts501;

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
import bruzsa.laszlo.dartsapp.model.darts501.Darts501Throw;

public class Darts501ThrowAdapter extends RecyclerView.Adapter<Darts501ThrowAdapter.ViewHolder> {

    private List<Darts501Throw> mDataSet;
    private final MutableLiveData<Darts501Throw> selectedForRemove = new MutableLiveData<>();

    @SuppressLint("NotifyDataSetChanged")
    public void refreshAll(List<Darts501Throw> throwList) {
        mDataSet = throwList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView shootTextView;
        private Darts501Throw shoot;

        public ViewHolder(View v) {
            super(v);
            v.setOnLongClickListener(v1 -> {
                selectedForRemove.setValue(shoot);
                return true;
            });
            shootTextView = v.findViewById(R.id.darts501ThrowItem);
        }

        public void setThrow(Darts501Throw shoot) {
            this.shoot = shoot;
        }
    }

    public Darts501ThrowAdapter(List<Darts501Throw> shoots) {
        mDataSet = shoots;
    }

    public void inserted() {
        notifyItemInserted(mDataSet.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_darts501, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Darts501Throw darts501Throw = mDataSet.get(position);
        viewHolder.shootTextView.setText(darts501Throw.toString());
        if (!darts501Throw.isNotHandicap())
            viewHolder.shootTextView.setTextColor(Color.rgb(100, 100, 0));
        else if (!darts501Throw.isValid())
            viewHolder.shootTextView.setTextColor(Color.RED);
        viewHolder.setThrow(darts501Throw);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public MutableLiveData<Darts501Throw> getSelectedForRemove() {
        return selectedForRemove;
    }

    public void remove(int position) {
        notifyItemRemoved(position);
    }
}
