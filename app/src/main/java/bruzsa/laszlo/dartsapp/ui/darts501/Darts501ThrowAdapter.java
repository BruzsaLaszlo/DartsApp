package bruzsa.laszlo.dartsapp.ui.darts501;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bruzsa.laszlo.dartsapp.R;

public class Darts501ThrowAdapter extends RecyclerView.Adapter<Darts501ThrowAdapter.ViewHolder> {

    private final List<Darts501Throw> mDataSet;
    private final MutableLiveData<Darts501Throw> selectedForRemove = new MutableLiveData<>();

    @SuppressLint("NotifyDataSetChanged")
    public void refreshAll() {
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
        viewHolder.shootTextView.setText(mDataSet.get(position).toString());
        viewHolder.setThrow(mDataSet.get(position));
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
