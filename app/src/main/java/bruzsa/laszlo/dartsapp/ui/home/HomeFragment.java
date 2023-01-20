package bruzsa.laszlo.dartsapp.ui.home;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.databinding.FragmentHomeBinding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.model.home.GameMode;
import bruzsa.laszlo.dartsapp.model.home.GameType;
import bruzsa.laszlo.dartsapp.model.home.HomeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SharedViewModel sharedViewModel;
    private HomeViewModel homeViewModel;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requireActivity().findViewById(R.id.toolbar).setVisibility(VISIBLE);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicPlayerModeAndTypeChips();
        setVisibilityOfPlayers();
        refreshPlayerChips();

        binding.chipGroupGameTypes.setSelectionRequired(true);
        binding.chipGroupGameTypes.setOnCheckedStateChangeListener(getOnCheckedStateChangeListenerForGameTypes());

        binding.buttonStartGame.setOnClickListener(getOnClickListenerForStartButton());
        binding.chipGroupPlayersMode.setSingleSelection(true);
        binding.chipGroupPlayersMode.setOnCheckedStateChangeListener(getOnCheckedStateChangeListenerForGameModes());

        setOnClickListenerForChip(binding.chipTeam1Player1, PLAYER_1_1);
        setOnClickListenerForChip(binding.chipTeam1Player2, PLAYER_1_2);
        setOnClickListenerForChip(binding.chipTeam2Player1, PLAYER_2_1);
        setOnClickListenerForChip(binding.chipTeam2Player2, PLAYER_2_2);
    }

    private ChipGroup.OnCheckedStateChangeListener getOnCheckedStateChangeListenerForGameTypes() {
        return (group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                sharedViewModel.setGameType(GameType.NO_GAME);
            } else {
                int id = checkedIds.get(0);
                if (id == binding.chip501.getId()) {
                    sharedViewModel.setGameType(GameType.NORMAL_501);
                } else if (id == binding.chipCricket.getId()) {
                    sharedViewModel.setGameType(GameType.CRICKET);
                }
            }
        };
    }


    private void setOnClickListenerForChip(Chip chip, TeamPlayer teamPlayer) {
        chip.setOnClickListener(view -> {
            sharedViewModel.setSelectedPlayer(teamPlayer);
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_playerFragment);
        });
        chip.setOnCloseIconClickListener(view -> {
            setChipStyle(chip, true, null);
            sharedViewModel.removePlayerEnum(teamPlayer);
        });
    }

    private void refreshPlayerChips() {
        refreshPlayerChip(binding.chipTeam1Player1, PLAYER_1_1);
        refreshPlayerChip(binding.chipTeam1Player2, PLAYER_1_2);
        refreshPlayerChip(binding.chipTeam2Player1, PLAYER_2_1);
        refreshPlayerChip(binding.chipTeam2Player2, PLAYER_2_2);
    }

    private void refreshPlayerChip(Chip chip, TeamPlayer teamPlayer) {
        Player player = sharedViewModel.getPlayer(teamPlayer);
        if (player == null) {
            setChipStyle(chip, true, null);
        } else {
            setChipStyle(chip, false, player.getName());
        }
    }

    private void setChipStyle(Chip chip, boolean empty, String text) {
        chip.setChipIconVisible(empty);
        chip.setCloseIconVisible(!empty);
        chip.setText(empty ? "add player" : text);
    }

    @NonNull
    private View.OnClickListener getOnClickListenerForStartButton() {
        return v -> {
            GameType type = sharedViewModel.getGameType();
            GameMode mode = sharedViewModel.getGameMode();

            switch (type) {
                case NO_GAME -> showSnack("Please, set game mode!");
                case NORMAL_501 -> {
                    switch (mode) {
                        case TEAM, PLAYER ->
                                navigateToFragment(R.id.action_nav_home_to_nav_darts501Fragment, mode);
                        case SINGLE ->
                                navigateToFragment(R.id.action_nav_home_to_darts501SinglePlayer, mode);
                    }
                }
                case CRICKET -> {
                    switch (mode) {
                        case TEAM, PLAYER ->
                                navigateToFragment(R.id.action_nav_home_to_nav_cricketFragment, mode);
                        case SINGLE -> {
                        }
                    }
                }
            }

        };

    }

    private void navigateToFragment(int actionNavHomeToNavFragment, GameMode gameMode) {
        if (validSelectedPlayers(gameMode)) {
            Navigation.findNavController(requireView()).navigate(actionNavHomeToNavFragment);
        } else {
            showSnack("Please choose players!");
        }
    }

    private boolean validSelectedPlayers(GameMode gameMode) {
        Map<TeamPlayer, Player> players = sharedViewModel.getSelectedPlayersMap();
        return switch (gameMode) {
            case SINGLE -> players.get(PLAYER_1_1) != null;
            case PLAYER -> players.get(PLAYER_1_1) != null && players.get(PLAYER_2_1) != null;
            case TEAM -> players.get(PLAYER_1_1) != null && players.get(PLAYER_2_1) != null
                    && players.get(PLAYER_1_2) != null && players.get(PLAYER_2_2) != null;
        };
    }

    private void showSnack(String message) {
        Snackbar.make(requireView(), message, BaseTransientBottomBar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @NonNull
    private ChipGroup.OnCheckedStateChangeListener getOnCheckedStateChangeListenerForGameModes
            () {
        return (group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                sharedViewModel.setGameMode(GameMode.PLAYER);
            } else {
                int checked = checkedIds.get(0);
                if (checked == binding.chipTeamPlayMode.getId()) {
                    sharedViewModel.setGameMode(GameMode.TEAM);
                } else if (checked == binding.chipSinglePlayerMode.getId()) {
                    sharedViewModel.setGameMode(GameMode.SINGLE);
                    Log.d("HomeFragment", "getOnCheckedStateChangeListenerForGameModes: ");
                }
            }
            setVisibilityOfPlayers();
        };
    }

    private void inicPlayerModeAndTypeChips() {
        switch (sharedViewModel.getGameMode()) {
            case PLAYER -> binding.chipGroupPlayersMode.clearCheck();
            case SINGLE -> binding.chipSinglePlayerMode.setChecked(true);
            case TEAM -> binding.chipTeamPlayMode.setChecked(true);
        }

        switch (sharedViewModel.getGameType()) {
            case NORMAL_501 -> binding.chip501.setChecked(true);
            case CRICKET -> binding.chipCricket.setChecked(true);
            case NO_GAME -> binding.chipGroupGameTypes.clearCheck();
        }
    }

    private void setVisibilityOfPlayers() {
        switch (sharedViewModel.getGameMode()) {
            case PLAYER -> setChipPlayerVisibilityAndInfoText(
                    INVISIBLE, VISIBLE, INVISIBLE, "Player 1", "Player 2");
            case TEAM -> setChipPlayerVisibilityAndInfoText(
                    VISIBLE, VISIBLE, VISIBLE, "Team 1", "Team 2");
            case SINGLE -> setChipPlayerVisibilityAndInfoText(
                    INVISIBLE, INVISIBLE, INVISIBLE, "Player", null);
        }
    }

    private void setChipPlayerVisibilityAndInfoText(int visibilityOfPlayer12, int visibilityOfPlayer21,
                                                    int visibilityOfPlayer22, String textInfo1, String textInfo2) {
        binding.chipTeam1Player2.setVisibility(visibilityOfPlayer12);
        binding.chipTeam2Player1.setVisibility(visibilityOfPlayer21);
        binding.chipTeam2Player2.setVisibility(visibilityOfPlayer22);
        binding.textInfoPlayerTeam1.setText(textInfo1);
        binding.textInfoPlayerTeam2.setText(textInfo2);
        binding.divider.setVisibility(sharedViewModel.getGameMode() == GameMode.SINGLE ? INVISIBLE : VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}