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
import java.util.Random;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.databinding.FragmentPlayerBinding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;

public class PlayerFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private TeamPlayer selectedTeamPlayer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
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
        List<Player> players = sharedViewModel.getAllPlayers().stream()
                .filter(player -> stream(args.getPlayerIds()).noneMatch(id -> player.getId().equals(id)))
                .collect(toList());
        selectedTeamPlayer = args.getSelectedTeamPlayer();

        Random random = new Random();
        binding.buttonAdd.setOnClickListener(v -> moveToHome(new Player(
                random.nextLong(),
                binding.editTextName.getText().toString())));

        recyclerView.setAdapter(new PlayerAdapter(players, this::moveToHome));
        return binding.getRoot();
    }

    private void moveToHome(Player selectedPlayer) {
        sharedViewModel.addPlayer(selectedTeamPlayer, selectedPlayer);
        Navigation.findNavController(requireView()).navigate(R.id.action_playerFragment_to_homeFragment);
    }

}