package bruzsa.laszlo.dartsapp.ui.darts501;

import static android.text.InputType.TYPE_NULL;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static bruzsa.laszlo.dartsapp.model.darts501.Darts501ViewModel.PLAYER_1;
import static bruzsa.laszlo.dartsapp.model.darts501.Darts501ViewModel.PLAYER_2;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentDarts501Binding;
import bruzsa.laszlo.dartsapp.model.darts501.ChangeType;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.darts501.Darts501ViewModel;
import bruzsa.laszlo.dartsapp.ui.darts501.input.InputType;

public class Darts501Fragment extends Fragment {

    private Darts501ViewModel dViewModel;
    private SharedViewModel sharedViewModel;
    private FragmentDarts501Binding binding;

    private Darts501ThrowAdapter player1ThrowAdapter;
    private Darts501ThrowAdapter player2ThrowAdapter;
    private final MutableLiveData<InputType> inputType = new MutableLiveData<>(InputType.THROW);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dViewModel = new ViewModelProvider(this).get(Darts501ViewModel.class);
    }

    @SuppressLint({"SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        if (dViewModel.onPlayerChange().getValue() == ChangeType.NO_GAME) {
            dViewModel.setSettings(sharedViewModel.getSettings());
            dViewModel.newGame(sharedViewModel.getPlayer1(), sharedViewModel.getPlayer2());
        }

        binding = FragmentDarts501Binding.inflate(inflater, container, false);

        setObservers();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshNamesStyle(binding.player1NameText, dViewModel.getPlayer(PLAYER_1), PLAYER_1, InputType.NAME1);
        refreshNamesStyle(binding.player2NameText, dViewModel.getPlayer(PLAYER_2), PLAYER_2, InputType.NAME2);

        binding.player1ScoreText.setTextColor(Color.BLUE);
        binding.player2ScoreText.setTextColor(Color.BLUE);

        inicThrowList(binding.listPlayer1Throws, PLAYER_1);
        inicThrowList(binding.listPlayer2Throws, PLAYER_2);

        binding.inputText.setInputType(inputType);

        binding.okButton.setOnClickListener(this::onClickOkButton);

        view.findViewById(R.id.textViewStat).setOnLongClickListener(v -> {
            inputType.setValue(InputType.RESTART_GAME);
            binding.okButton.setText(R.string.ok);
            return true;
        });
    }

    private void inicThrowList(RecyclerView listPlayerThrows, int player) {
        listPlayerThrows.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        Darts501ThrowAdapter throwAdapter = new Darts501ThrowAdapter(dViewModel.getThrows(player));
        if (player == PLAYER_1) player1ThrowAdapter = throwAdapter;
        else player2ThrowAdapter = throwAdapter;
        throwAdapter.getSelectedForRemove().observe(getViewLifecycleOwner(), shoot -> dViewModel.removeThrow(shoot, player));
        listPlayerThrows.setAdapter(throwAdapter);
    }

    private void refreshNamesStyle(TextView playerNameText, String playerName, int i, InputType inputType) {
        playerNameText.setText(playerName);
        playerNameText.setOnClickListener(v -> dViewModel.setActivePlayer(i));
        playerNameText.setOnLongClickListener(v -> {
            this.inputType.setValue(inputType);
            return true;
        });
    }

    private void onClickOkButton(View button) {

        switch (Objects.requireNonNull(inputType.getValue())) {
            case THROW ->
                    binding.inputText.getThrow().ifPresent(shoot -> dViewModel.newThrow(shoot));
            case RESTART_GAME -> dViewModel.restartGame();
            case NAME1 ->
                    binding.player1NameText.setText(binding.inputText.getName().orElse("Player 1"));
            case NAME2 ->
                    binding.player2NameText.setText(binding.inputText.getName().orElse("Player 2"));
        }

        inputType.setValue(InputType.THROW);
    }


    @SuppressLint("SetTextI18n")
    private void setObservers() {
        binding.player1ScoreText.setOnClickListener(v -> inputType.setValue(InputType.THROW));
        binding.player2ScoreText.setOnClickListener(v -> inputType.setValue(InputType.THROW));
        sharedViewModel.getNewGame501().observe(getViewLifecycleOwner(), newGame ->
                dViewModel.newGame(sharedViewModel.getPlayer1(), sharedViewModel.getPlayer2()));
        dViewModel.onPlayerChange().observe(getViewLifecycleOwner(), changeType -> {
            switch (changeType) {
                case GAME_OVER -> {
                    binding.okButton.setText("Winner: " + dViewModel.getPlayer(dViewModel.getActivePlayer()));
                    binding.inputText.setInputType(TYPE_NULL);
                    binding.inputText.clearFocus();
                }
                case NEW_GAME -> {
                    player1ThrowAdapter.refreshAll(dViewModel.getThrows(PLAYER_1));
                    player2ThrowAdapter.refreshAll(dViewModel.getThrows(PLAYER_2));
                    binding.inputText.requestFocus();
                }
                case CHANGE_ACTIVE_PLAYER -> { // nothing only refresh
                }
                case NO_GAME -> { // never happens here
                }
                case ADD_SHOOT -> {
                    if (dViewModel.getActivePlayer() == PLAYER_2)
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
        refreshGUI(PLAYER_1, binding.player1ScoreText,
                binding.player1StatText, binding.listPlayer1Throws);
        refreshGUI(PLAYER_2, binding.player2ScoreText,
                binding.player2StatText, binding.listPlayer2Throws);
        refreshNamesStyle(binding.player1NameText, binding.player2NameText);
    }

    private void refreshGUI(int player, TextView playerScoreText,
                            TextView playerStatText, RecyclerView listPlayerThrows) {
        playerScoreText.setText(String.valueOf(dViewModel.getScore(player)));
        if (dViewModel.isOut(player))
            playerScoreText.setTextColor(Color.GREEN);
        else
            playerScoreText.setTextColor(Color.BLUE);
        playerStatText.setText(dViewModel.getStat(player));
        listPlayerThrows.smoothScrollToPosition(100);
    }

    private void refreshNamesStyle(TextView player1NameText, TextView player2NameText) {
        if (dViewModel.getActivePlayer() == PLAYER_2) {
            var temp = player1NameText;
            player1NameText = player2NameText;
            player2NameText = temp;
        }
        player1NameText.setTypeface(Typeface.DEFAULT_BOLD);
        player1NameText.setTextColor(Color.RED);
        player2NameText.setTypeface(Typeface.DEFAULT);
        player2NameText.setTextColor(Color.BLACK);
    }

}