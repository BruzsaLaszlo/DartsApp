package bruzsa.laszlo.dartsapp.ui.x01;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import bruzsa.laszlo.dartsapp.InputViews;
import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.databinding.FragmentX01Binding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.model.home.GameMode;
import bruzsa.laszlo.dartsapp.model.x01.X01ChangeType;
import bruzsa.laszlo.dartsapp.model.x01.X01SummaryStatistics;
import bruzsa.laszlo.dartsapp.model.x01.X01ViewModel;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputType;

public class X01Fragment extends Fragment {

    private X01ViewModel dViewModel;
    private SharedViewModel sharedViewModel;
    private FragmentX01Binding binding;

    private Map<TeamPlayer, Button> playerTextNameBindings;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dViewModel = new ViewModelProvider(this).get(X01ViewModel.class);
    }

    @SuppressLint({"SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_x01, container, false);
        binding.setViewModel(dViewModel);

        if (dViewModel.onPlayerChange().getValue() == X01ChangeType.NO_GAME) {
            dViewModel.setSettings(sharedViewModel.getSettings());
            startNewGame();
        }

        setPlayerTextNameBindings();


        dViewModel.onPlayerChange().observe(getViewLifecycleOwner(), this::onChanged);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InputViews inputs = new InputViews(this, InputType.NUMPAD, binding.includedInputs);

        inputs.setOnReadyAction(value -> dViewModel.newThrow(value));

        if (sharedViewModel.getGameMode() == GameMode.PLAYER) {
            binding.textNamePlayer12.setVisibility(View.GONE);
            binding.textNamePlayer22.setVisibility(View.GONE);
        }


        refreshNamesStyle(sharedViewModel.getSelectedPlayersMap());

        binding.listThrowsPlayer1.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        binding.listThrowsPlayer2.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));

    }

    private void startNewGame() {
        dViewModel.newGame(sharedViewModel.getSelectedPlayersMap());
    }

    private void setPlayerTextNameBindings() {
        playerTextNameBindings = new EnumMap<>(TeamPlayer.class);
        playerTextNameBindings.put(PLAYER_1_1, binding.textNamePlayer11);
        playerTextNameBindings.put(PLAYER_2_1, binding.textNamePlayer21);
        if (sharedViewModel.getGameMode() == GameMode.TEAM) {
            playerTextNameBindings.put(PLAYER_1_2, binding.textNamePlayer12);
            playerTextNameBindings.put(PLAYER_2_2, binding.textNamePlayer22);
        }
    }


    private void refreshNamesStyle(Map<TeamPlayer, Player> players) {
        playerTextNameBindings.forEach((player, button) -> {
            if (dViewModel.getActivePlayer() == player) {
                button.setTextColor(RED);
                button.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                button.setTextColor(BLACK);
                button.setTypeface(Typeface.DEFAULT);
            }

            button.setText(players.get(player).getName());

            button.setOnClickListener(v -> dViewModel.setActivePlayer(player));
        });
    }

    private void refreshGUI() {
        refreshGUI(TEAM1, binding.textScorePlayer1,
                binding.textStatPlayer1, binding.listThrowsPlayer1);
        refreshGUI(TEAM2, binding.textScorePlayer2,
                binding.textStatPlayer2, binding.listThrowsPlayer2);
        refreshNamesStyle(sharedViewModel.getSelectedPlayersMap());
    }

    @SuppressLint("SetTextI18n")
    private void refreshGUI(Team team, TextView playerScoreText,
                            TextView playerStatText, RecyclerView listPlayerThrows) {
        if (dViewModel.isOut(team))
            playerScoreText.setTextColor(Color.GREEN);
        else
            playerScoreText.setTextColor(BLUE);
        playerStatText.setText(getStatAsString(team));
        listPlayerThrows.smoothScrollToPosition(100);
        binding.textSetLegPlayer1.setText(dViewModel.getSet(team) + "\n" + dViewModel.getLeg(team));
    }

    private String getStatAsString(Team team) {
        X01SummaryStatistics stat = dViewModel.getStat(team);
        if (stat.getDartCount() == 0) return "";
        return String.format(Locale.ENGLISH, """
                        %d
                        %d
                        %d
                        %d
                        %d
                        %s""",
                stat.getHundredPlus(),
                stat.getSixtyPlus(),
                (int) stat.getAverage(),
                stat.getMax(),
                stat.getMin(),
                stat.getHighestCheckout().map(Object::toString).orElse("-"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @SuppressLint("SetTextI18n")
    private void onChanged(X01ChangeType changeType) {
        refreshGUI();
        if (changeType == X01ChangeType.GAME_OVER) {
            // Todo
        }
    }
}