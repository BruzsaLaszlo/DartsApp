package bruzsa.laszlo.dartsapp.ui.home.player;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentPlayerBinding;
import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.home.HomeViewModel;

public class PlayerFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private PlayersEnum selectedPlayersEnum;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPlayerBinding binding = FragmentPlayerBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.list;
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),
                requireActivity().getRequestedOrientation() == SCREEN_ORIENTATION_LANDSCAPE ? 3 : 2));
//        if (M_COLUMN_COUNT <= 1) {
//            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        } else {
//            recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
//        }

        PlayerFragmentArgs args = PlayerFragmentArgs.fromBundle(requireArguments());
        List<Player> players = homeViewModel.getAllPlayers()
                .stream()
                .filter(player -> stream(args.getPlayerIds()).noneMatch(id -> player.getId().equals(id)))
                .collect(toList());
        selectedPlayersEnum = args.getSelectedPlayersEnum();

        binding.buttonAdd.setOnClickListener(v -> moveToHome(new Player(binding.editTextName.getText().toString()), Action.ADD));

        recyclerView.setAdapter(new PlayerAdapter(players, this::moveToHome));
        return binding.getRoot();
    }

    private void moveToHome(Player selectedPlayer, Action action) {
        if (action == Action.REMOVE) {
            homeViewModel.removePlayer(selectedPlayer);
        } else {
            homeViewModel.selectPlayer(selectedPlayersEnum, selectedPlayer);
            Navigation.findNavController(requireView()).navigate(R.id.action_playerFragment_to_homeFragment);
        }
    }

}