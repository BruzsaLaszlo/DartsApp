package bruzsa.laszlo.dartsapp.ui.dartsX01;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static android.text.InputType.TYPE_NULL;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.databinding.FragmentDartsx01Binding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.model.dartsX01.ChangeType;
import bruzsa.laszlo.dartsapp.model.dartsX01.DartsX01ViewModel;
import bruzsa.laszlo.dartsapp.model.home.GameMode;
import bruzsa.laszlo.dartsapp.ui.Speech;
import bruzsa.laszlo.dartsapp.ui.dartsX01.input.InputType;

public class DartsX01Fragment extends Fragment {

    private DartsX01ViewModel dViewModel;
    private FragmentDartsx01Binding binding;
    private SharedViewModel sharedViewModel;

    private DartsX01ThrowAdapter player1ThrowAdapter;
    private DartsX01ThrowAdapter player2ThrowAdapter;

    private TextView lastOnLongClicked;

    private Map<TeamPlayer, Button> playerTextNameBindings;
    private final MutableLiveData<InputType> inputType = new MutableLiveData<>(InputType.THROW);

    private Speech speech;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dViewModel = new ViewModelProvider(this).get(DartsX01ViewModel.class);

        if (checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PERMISSION_DENIED) {
            Log.d("DartsX01Fragment", "onCreate: RECORD_AUDIO permission denied");
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
//                showInContextUI(...);
            } else {
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
                }).launch(Manifest.permission.RECORD_AUDIO);
            }
        }
        speech = new Speech(requireContext());
    }

    @SuppressLint({"SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        binding = FragmentDartsx01Binding.inflate(inflater, container, false);

        if (dViewModel.onPlayerChange().getValue() == ChangeType.NO_GAME) {
            dViewModel.setSettings(sharedViewModel.getSettings());
            startNewGame();
        }

        setPlayerTextNameBindings();

        setObservers();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (sharedViewModel.getGameMode() == GameMode.PLAYER) {
            binding.textNamePlayer12.setVisibility(View.GONE);
            binding.textNamePlayer22.setVisibility(View.GONE);
        }

        refreshNamesStyle(sharedViewModel.getSelectedPlayersMap());

        inicThrowList(binding.listThrowsPlayer1, TEAM1);
        inicThrowList(binding.listThrowsPlayer2, TEAM2);

        binding.textInput.setInputType(inputType);

        binding.buttonOk.setOnClickListener(this::onClickButtonOk);

        binding.textViewStat.setOnLongClickListener(v -> {
            inputType.setValue(InputType.RESTART_GAME);
            binding.buttonOk.setText(R.string.ok);
            return true;
        });
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

    private void inicThrowList(RecyclerView listPlayerThrows, Team team) {
        listPlayerThrows.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        DartsX01ThrowAdapter throwAdapter = new DartsX01ThrowAdapter(dViewModel.getThrows(team));
        if (team.isTeam1()) player1ThrowAdapter = throwAdapter;
        else player2ThrowAdapter = throwAdapter;
        throwAdapter.getSelectedForRemove().observe(getViewLifecycleOwner(), shoot -> dViewModel.removeThrow(shoot, team));
        listPlayerThrows.setAdapter(throwAdapter);
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

            button.setOnLongClickListener(v -> {
                lastOnLongClicked = button;
                inputType.setValue(InputType.NAME);
                return true;
            });

            button.setOnClickListener(v -> dViewModel.setActivePlayer(player));
        });
    }

    private void onClickButtonOk(View button) {
        switch (Objects.requireNonNull(inputType.getValue())) {
            case THROW ->
                    binding.textInput.getThrow().ifPresent(shoot -> dViewModel.newThrow(shoot));
            case RESTART_GAME -> dViewModel.newGame(sharedViewModel.getSelectedPlayersMap());
            case NAME -> lastOnLongClicked.setText(binding.textInput.getName().orElse("Player"));
        }
        inputType.setValue(InputType.THROW);
    }

    @SuppressLint("SetTextI18n")
    private void setObservers() {
        binding.buttonSpeach.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                speech.startListening();
            } else {
                speech.stopListening();
            }
        });

        speech.getResultLiveData().observe(getViewLifecycleOwner(), numbers ->
                binding.textInput.setText(String.join("+", numbers)));

        binding.textScorePlayer1.setOnClickListener(v -> inputType.setValue(InputType.THROW));
        binding.textScorePlayer2.setOnClickListener(v -> inputType.setValue(InputType.THROW));

        dViewModel.onPlayerChange().observe(getViewLifecycleOwner(), changeType -> {
            switch (changeType) {
                case GAME_OVER -> {
                    binding.buttonOk.setText("Winner: " + sharedViewModel.getPlayer(dViewModel.getActivePlayer()).getName());
                    binding.textInput.setInputType(TYPE_NULL);
                    binding.textInput.clearFocus();
                }
                case NEW_GAME -> {
                    player1ThrowAdapter.refreshAll(dViewModel.getThrows(TEAM1));
                    player2ThrowAdapter.refreshAll(dViewModel.getThrows(TEAM2));
                    binding.textInput.requestFocus();
                }
                case CHANGE_ACTIVE_PLAYER -> { // nothing only refresh
                }
                case NO_GAME -> { // never happens here
                }
                case ADD_SHOOT -> {
                    if (Set.of(PLAYER_2_1, PLAYER_2_2).contains(dViewModel.getActivePlayer()))
                        player1ThrowAdapter.inserted();
                    else player2ThrowAdapter.inserted();
                }
                case ADD_SHOOTS -> {
                    player1ThrowAdapter.inserted();
                    player2ThrowAdapter.inserted();
                }
                case REMOVE_SHOOT -> {
                    player1ThrowAdapter.remove(dViewModel.getLastRemovedIndex());
                    player2ThrowAdapter.remove(dViewModel.getLastRemovedIndex());
                }
            }
            refreshGUI();
        });
    }

    private void refreshGUI() {
        refreshGUI(TEAM1, binding.textScorePlayer1,
                binding.textStatPlayer1, binding.listThrowsPlayer1);
        refreshGUI(TEAM2, binding.textScorePlayer2,
                binding.textStatPlayer2, binding.listThrowsPlayer2);
        refreshNamesStyle(sharedViewModel.getSelectedPlayersMap());
    }

    private void refreshGUI(Team team, TextView playerScoreText,
                            TextView playerStatText, RecyclerView listPlayerThrows) {
        playerScoreText.setText(String.valueOf(dViewModel.getScore(team)));
        if (dViewModel.isOut(team))
            playerScoreText.setTextColor(Color.GREEN);
        else
            playerScoreText.setTextColor(BLUE);
        playerStatText.setText(dViewModel.getStat(team));
        listPlayerThrows.smoothScrollToPosition(100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}