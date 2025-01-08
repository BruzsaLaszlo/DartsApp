package bruzsa.laszlo.dartsapp.ui.home.player;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.function.BiConsumer;

import bruzsa.laszlo.dartsapp.databinding.AdapterPlayerBinding;
import bruzsa.laszlo.dartsapp.enties.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private final List<Player> players;
    private final BiConsumer<Player, Action> selected;

    public PlayerAdapter(List<Player> players, BiConsumer<Player, Action> onClick) {
        this.players = players;
        this.selected = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterPlayerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.mContentView.setOnClickListener(v -> selected.accept(player, Action.ADD));
        holder.mContentView.setOnLongClickListener(v -> {
            new MaterialAlertDialogBuilder(v.getContext())
                    .setTitle("Remove Player")
                    .setMessage("Are you sure you delete: " + player.getName())
                    .setPositiveButton("DELETE", (dialog, which) -> {
                        selected.accept(player, Action.REMOVE);
                        players.remove(player);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                    })
                    .show();
            return true;
        });
        holder.mContentView.setText(player.getName());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;

        public ViewHolder(@NonNull AdapterPlayerBinding binding) {
            super(binding.getRoot());
            mContentView = binding.textPlayerName;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}