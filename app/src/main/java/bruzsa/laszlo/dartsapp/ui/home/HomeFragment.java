package bruzsa.laszlo.dartsapp.ui.home;

import static android.view.View.VISIBLE;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.PlayersEnum.PLAYER_2_2;
import static bruzsa.laszlo.dartsapp.ui.home.HomeFragmentDirections.actionHomeFragmentToPlayerFragment;
import static bruzsa.laszlo.dartsapp.ui.home.HomeFragmentDirections.actionHomeFragmentToSingleX01Fragment;
import static bruzsa.laszlo.dartsapp.util.IpAddress.getIPv4Address;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentHomeBinding;
import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.home.GameMode;
import bruzsa.laszlo.dartsapp.model.home.GameType;
import bruzsa.laszlo.dartsapp.model.home.HomeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requireActivity().findViewById(R.id.toolbar).setVisibility(VISIBLE);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setHomeViewModel(homeViewModel);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void choosePlayerFor(PlayersEnum playersEnum) {
        long[] playerIds = homeViewModel.getSelectedPlayersMap().values().stream()
                .filter(player -> Objects.nonNull(player.getId()))
                .mapToLong(Player::getId)
                .toArray();
        navigateToFragment(actionHomeFragmentToPlayerFragment(playerIds, playersEnum));
    }

    private boolean validSelectedPlayers(GameMode gameMode) {
        Map<PlayersEnum, Player> players = homeViewModel.getSelectedPlayersMap();
        return switch (gameMode) {
            case SINGLE -> players.get(PLAYER_1_1) != null;
            case PLAYER -> players.get(PLAYER_1_1) != null && players.get(PLAYER_2_1) != null;
            case TEAM -> players.get(PLAYER_1_1) != null && players.get(PLAYER_2_1) != null
                    && players.get(PLAYER_1_2) != null && players.get(PLAYER_2_2) != null;
        };
    }

    public void showDialogX01StartScore(View v) {
        var x01 = Map.of(
                0, "201",
                1, "301",
                2, "501",
                3, "701",
                4, "1001",
                5, "1501");
        var sorted = new TreeMap<>(x01);
        new MaterialAlertDialogBuilder(v.getContext())
                .setItems(sorted.values().toArray(new String[0]), (dialog, which) -> {
                    homeViewModel.getSettings().getX01Settings().setStartScore(parseInt(sorted.get(which)));
                    ((TextView) v).setText(sorted.get(which));
                })
                .create().show();
    }

    private static final int MAX_LEGSET = 13;

    @SuppressLint("SetTextI18n")
    public void showDialogSetLegCount(View v) {
        String[] s = new String[13];
        for (int i = 0; i < MAX_LEGSET; i++) {
            s[i] = valueOf(i + 1);
        }
        new MaterialAlertDialogBuilder(v.getContext())
                .setItems(s, (dialog, which) -> {
                    homeViewModel.getSettings().getX01Settings().setCount(which + 1);
                    ((TextView) v).setText(valueOf(which + 1));
                })
                .create().show();
    }

    private void showSnack(String message) {
        Snackbar.make(requireView(), message, BaseTransientBottomBar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void startGame() {
        GameMode mode = homeViewModel.getSettings().getGeneralSettings().getGameMode();
        if (!validSelectedPlayers(mode)) {
            showSnack("Please choose players!");
            return;
        }

        GameType type = homeViewModel.getSettings().getGeneralSettings().getGameType();
        switch (type) {
            case NO_GAME -> showSnack("Please, set game mode!");
            case X01 -> {
                switch (mode) {
                    case TEAM, PLAYER ->
                            navigateToFragment(R.id.action_homeFragment_to_X01Fragment);
                    case SINGLE -> navigateToFragment(actionHomeFragmentToSingleX01Fragment());
                }
            }
            case CRICKET -> {
                homeViewModel.getSettings().getCricketSettings().setRandomNumbers(binding.chipCricketSettingsRandom.isChecked());
                switch (mode) {
                    case TEAM, PLAYER ->
                            navigateToFragment(R.id.action_homeFragment_to_cricketFragment);
                    case SINGLE -> {// TODO
                    }
                }
            }
        }
    }

    private void navigateToFragment(NavDirections navDirections) {
        Navigation.findNavController(requireView()).navigate(navDirections);
    }

    private void navigateToFragment(int resId) {
        Navigation.findNavController(requireView()).navigate(resId);
    }

    public void changeMatchType(View view) {
        if (view instanceof TextView text) {
            homeViewModel.getSettings().getX01Settings().setMatchType(homeViewModel.getSettings().getX01Settings().getMatchType().opposit());
            text.setText(homeViewModel.getSettings().getX01Settings().getMatchType().name());
        }
    }

    public void changeFirstBest(View view) {
        if (view instanceof TextView text) {
            homeViewModel.getSettings().getX01Settings().setFirstToBestOf(homeViewModel.getSettings().getX01Settings().getFirstToBestOf().opposit());
            text.setText(homeViewModel.getSettings().getX01Settings().getFirstToBestOf().getLabel());
        }
    }

    public void setIpAddress(View v) {
        if (v instanceof TextView t) t.setText("WebGUI: " + getIpAddress());
    }

    public String getIpAddress() {
        return getIPv4Address(requireContext()).orElse("Can not find ip address");
    }

}