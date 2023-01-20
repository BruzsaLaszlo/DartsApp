package bruzsa.laszlo.dartsapp.ui.home.player;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.databinding.FragmentPlayerBinding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;

public class PlayerFragment extends Fragment {

    private static final int M_COLUMN_COUNT = 1;
    private SharedViewModel sharedViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPlayerBinding binding = FragmentPlayerBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.getRoot();
        Context context = recyclerView.getContext();
        if (M_COLUMN_COUNT <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        }

        List<Player> players = sharedViewModel.getAllPlayers();
        Log.d("PlayerFragment", "onCreateView: " + sharedViewModel.getSelectedPlayersCollection());
        players.removeAll(sharedViewModel.getSelectedPlayersCollection());

        recyclerView.setAdapter(new PlayerAdapter(players,
                selectedPlayer -> sharedViewModel.addPlayer(selectedPlayer)));
        return recyclerView;
    }

}