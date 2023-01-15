package bruzsa.laszlo.dartsapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.dao.Player;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {

    private final List<Player> players;

    public PlayersAdapter(List<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_players, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersAdapter.ViewHolder holder, int position) {
        holder.player.setText(players.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView player;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            player = itemView.findViewById(R.id.playerItem);
        }
    }
}
