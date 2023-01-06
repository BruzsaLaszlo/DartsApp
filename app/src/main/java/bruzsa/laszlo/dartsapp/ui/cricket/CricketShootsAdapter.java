package bruzsa.laszlo.dartsapp.ui.cricket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.R;

public class CricketShootsAdapter extends RecyclerView.Adapter<CricketShootsAdapter.ViewHolder> {

    private final List<CricketShoot> mDataSet;
    private final Consumer<CricketShoot> toRemove;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView shootTextView;
        private CricketShoot cricketShoot;

        public ViewHolder(View v) {
            super(v);
            v.setOnLongClickListener(v1 -> {
                int position = mDataSet.indexOf(cricketShoot);
                toRemove.accept(cricketShoot);
                cricketShoot.setRemoved();
                notifyItemRemoved(position);
                return true;
            });
            shootTextView = v.findViewById(R.id.cricketShootItem);
        }

        public void setCricketShoot(CricketShoot cricketShoot) {
            this.cricketShoot = cricketShoot;
        }
    }

    public CricketShootsAdapter(List<CricketShoot> shoots, Consumer<CricketShoot> toRemove) {
        mDataSet = shoots;
        this.toRemove = toRemove;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_cricket, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.shootTextView.setText(mDataSet.get(position).toString());
        viewHolder.setCricketShoot(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
