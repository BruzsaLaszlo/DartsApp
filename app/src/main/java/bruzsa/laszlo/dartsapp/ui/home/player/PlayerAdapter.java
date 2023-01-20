package bruzsa.laszlo.dartsapp.ui.home.player;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.databinding.AdapterPlayerBinding;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private final List<Player> mValues;
    private final Consumer<Player> selected;

    public PlayerAdapter(List<Player> mValues, Consumer<Player> selected) {
        this.mValues = mValues;
        this.selected = selected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterPlayerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Player player = mValues.get(position);
        holder.mContentView.setOnClickListener(v -> {
            selected.accept(player);
            Navigation.findNavController(holder.itemView).navigate(R.id.action_playerFragment_to_nav_home);
        });
        holder.mIdView.setText(player.getId().toString());
        holder.mContentView.setText(player.getName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(@NonNull AdapterPlayerBinding binding) {
            super(binding.getRoot());
            mIdView = binding.textPlayerId;
            mContentView = binding.textPlayerName;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}