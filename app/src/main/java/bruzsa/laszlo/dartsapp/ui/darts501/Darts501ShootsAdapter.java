package bruzsa.laszlo.dartsapp.ui.darts501;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.R;

public class Darts501ShootsAdapter extends RecyclerView.Adapter<Darts501ShootsAdapter.ViewHolder> {

    private final List<Darts501Shoot> mDataSet;

    @SuppressLint("NotifyDataSetChanged")
    public void clearAllData() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView shootTextView;
        private Darts501Shoot shoot;

        public ViewHolder(View v) {
            super(v);
            v.setOnLongClickListener(v1 -> {
                int position = mDataSet.indexOf(shoot);
                notifyItemRemoved(position);
                return true;
            });
            shootTextView = v.findViewById(R.id.darts501ShootItem);
        }

        public void setShoot(Darts501Shoot shoot) {
            this.shoot = shoot;
        }
    }

    public Darts501ShootsAdapter(List<Darts501Shoot> shoots, Consumer<Darts501Shoot> toRemove) {
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
        viewHolder.setShoot(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
