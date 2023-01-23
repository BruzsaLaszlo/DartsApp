package bruzsa.laszlo.dartsapp.ui.x01;

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
import bruzsa.laszlo.dartsapp.model.dartsX01.X01Throw;

public class X01ThrowAdapter extends RecyclerView.Adapter<X01ThrowAdapter.ViewHolder> {

    private List<X01Throw> mDataSet;
    private final MutableLiveData<X01Throw> selectedForRemove = new MutableLiveData<>();

    @SuppressLint("NotifyDataSetChanged")
    public void refreshAll(List<X01Throw> throwList) {
        mDataSet = throwList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView shootTextView;
        private X01Throw x01Throw;

        public ViewHolder(View v) {
            super(v);
            v.setOnLongClickListener(v1 -> {
                selectedForRemove.setValue(x01Throw);
                return true;
            });
            shootTextView = v.findViewById(R.id.dartsX01ThrowItem);
        }

        public void setThrow(X01Throw shoot) {
            this.x01Throw = shoot;
        }
    }

    public X01ThrowAdapter(List<X01Throw> dartsThrows) {
        mDataSet = dartsThrows;
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
        viewHolder.shootTextView.setText(x01Throw.toString());
        if (!x01Throw.isNotHandicap())
            viewHolder.shootTextView.setTextColor(Color.rgb(100, 100, 0));
        else if (!x01Throw.isValid())
            viewHolder.shootTextView.setTextColor(Color.RED);
        viewHolder.setThrow(x01Throw);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public MutableLiveData<X01Throw> getSelectedForRemove() {
        return selectedForRemove;
    }

    public void remove(int position) {
        notifyItemRemoved(position);
    }
}
