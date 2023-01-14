package bruzsa.laszlo.dartsapp.ui.darts501;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static bruzsa.laszlo.dartsapp.model.Darts501ViewModel.PLAYER_1;
import static bruzsa.laszlo.dartsapp.model.Darts501ViewModel.PLAYER_2;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.databinding.FragmentDarts501Binding;
import bruzsa.laszlo.dartsapp.model.ChangeType;
import bruzsa.laszlo.dartsapp.model.Darts501ViewModel;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.ui.darts501.input.InputType;

public class Darts501Fragment extends Fragment {

    private Darts501ViewModel darts501ViewModel;
    private SharedViewModel sharedViewModel;
    private FragmentDarts501Binding binding;

    private Darts501ThrowAdapter player1ThrowAdapter;
    private Darts501ThrowAdapter player2ThrowAdapter;
    private final MutableLiveData<InputType> inputType = new MutableLiveData<>(InputType.SHOOT);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        darts501ViewModel = new ViewModelProvider(this).get(Darts501ViewModel.class);
        darts501ViewModel.setStartScore(sharedViewModel.getStartScore());
    }

    @SuppressLint({"SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        if (darts501ViewModel.onPlayerChange().getValue() == ChangeType.NO_GAME) {
            darts501ViewModel.newGame(sharedViewModel.getPlayer1(), sharedViewModel.getPlayer2());
            darts501ViewModel.restartGame();
        }
        setObservers();

        binding = FragmentDarts501Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPlayerNamesStyle(binding.player1NameText, darts501ViewModel.getPlayer(PLAYER_1), 1, InputType.NAME1);
        setPlayerNamesStyle(binding.player2NameText, darts501ViewModel.getPlayer(PLAYER_2), 2, InputType.NAME2);

        binding.player1ScoreText.setTextColor(Color.BLUE);
        binding.player2ScoreText.setTextColor(Color.BLUE);

        inicThrowList(binding.listPlayer1Throws, PLAYER_1);
        inicThrowList(binding.listPlayer2Throws, PLAYER_2);

        binding.inputText.setInputType(inputType);

        binding.okButton.setOnClickListener(this::onClickOkButton);

        view.findViewById(R.id.textViewStat).setOnLongClickListener(v -> {
            inputType.setValue(InputType.NEW_GAME);
            return true;
        });
    }

    private void inicThrowList(RecyclerView listPlayerThrows, int player) {
        listPlayerThrows.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        Darts501ThrowAdapter shootAdapter = new Darts501ThrowAdapter(darts501ViewModel.getThrows(player));
        if (player == PLAYER_1) player1ThrowAdapter = shootAdapter;
        else player2ThrowAdapter = shootAdapter;
        shootAdapter.getSelectedForRemove().observe(getViewLifecycleOwner(), shoot -> darts501ViewModel.removeThrow(shoot));
        listPlayerThrows.setAdapter(shootAdapter);
    }

    private void setPlayerNamesStyle(TextView playerNameText, Player player, int i, InputType inputType) {
        playerNameText.setText(player.getName());
        playerNameText.setOnClickListener(v -> darts501ViewModel.setActivePlayer(i));
        playerNameText.setOnLongClickListener(v -> {
            this.inputType.setValue(inputType);
            return true;
        });
    }

    private void onClickOkButton(View button) {

        switch (Objects.requireNonNull(inputType.getValue())) {
            case SHOOT ->
                    binding.inputText.getThrow().ifPresent(shoot -> darts501ViewModel.newThrow(shoot));
            case NEW_GAME -> darts501ViewModel.restartGame();
            case NAME1 ->
                    binding.player1NameText.setText(binding.inputText.getName().orElse("Player 1"));
            case NAME2 ->
                    binding.player2NameText.setText(binding.inputText.getName().orElse("Player 2"));
        }

        inputType.setValue(InputType.SHOOT);
    }


    @SuppressLint("SetTextI18n")
    private void setObservers() {
        darts501ViewModel.onPlayerChange().observe(getViewLifecycleOwner(), changeType -> {
            switch (changeType) {
                case GAME_OVER -> {
                    binding.okButton.setText("Winner: " + darts501ViewModel.getPlayer(darts501ViewModel.getActivePlayer()).getName());
                    binding.inputText.setInputType(EditorInfo.TYPE_NULL);
                    binding.inputText.clearFocus();
                }
                case NEW_GAME -> {
                    player1ThrowAdapter.clearAllData();
                    player2ThrowAdapter.clearAllData();
                    refreshGUI();
                    binding.inputText.requestFocus();
                }
                case CHANGE_ACTIVE_PLAYER -> { // nothing only refresh
                }
                case ADD_SHOOT -> {
                    if (darts501ViewModel.getActivePlayer() == PLAYER_1)
                        player1ThrowAdapter.inserted();
                    else player2ThrowAdapter.inserted();
                }
                case ADD_SHOOTS -> {
                    player1ThrowAdapter.inserted();
                    player2ThrowAdapter.inserted();
                }
                case REMOVE_SHOOT -> {
                    player1ThrowAdapter.remove(darts501ViewModel.getLastRemoved());
                    player2ThrowAdapter.remove(darts501ViewModel.getLastRemoved());
                }
            }
            refreshGUI();
        });
    }

    private void refreshGUI() {
        refreshGUI(darts501ViewModel.getStat(PLAYER_1), binding.player1ScoreText,
                binding.player1StatText, binding.listPlayer1Throws);
        refreshGUI(darts501ViewModel.getStat(PLAYER_2), binding.player2ScoreText,
                binding.player2StatText, binding.listPlayer2Throws);
        if (darts501ViewModel.getActivePlayer() == PLAYER_1)
            setPlayerNamesStyle(binding.player1NameText, binding.player2NameText);
        else setPlayerNamesStyle(binding.player2NameText, binding.player1NameText);
    }

    private void refreshGUI(Darts501SummaryStatistics statPlayer, TextView playerScoreText,
                            TextView playerStatText, RecyclerView listPlayerThrows) {
        playerScoreText.setText(String.valueOf(statPlayer.getScore()));
        if (statPlayer.isOut())
            playerScoreText.setTextColor(Color.GREEN);
        else
            playerScoreText.setTextColor(Color.BLUE);
        if (statPlayer.isEmpty())
            playerStatText.setText("");
        else
            playerStatText.setText(statPlayer.getStat());
        listPlayerThrows.smoothScrollToPosition(100);
    }

    private void setPlayerNamesStyle(TextView player1NameText, TextView player2NameText) {
        player1NameText.setTypeface(Typeface.DEFAULT_BOLD);
        player1NameText.setTextColor(Color.RED);
        player2NameText.setTypeface(Typeface.DEFAULT);
        player2NameText.setTextColor(Color.BLACK);
    }

}