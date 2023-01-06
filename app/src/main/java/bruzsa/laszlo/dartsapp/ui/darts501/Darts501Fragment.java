package bruzsa.laszlo.dartsapp.ui.darts501;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

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

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.model.Darts501ViewModel;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.ui.darts501.input.Darts501EditText;
import bruzsa.laszlo.dartsapp.ui.darts501.input.InputType;

public class Darts501Fragment extends Fragment {

    private Darts501ViewModel mViewModel;
    private SharedViewModel sharedViewModel;

    Darts501ShootsAdapter player1ShootAdapter;
    Darts501ShootsAdapter player2ShootAdapter;
    private TextView player1ScoreText;
    private TextView player2ScoreText;
    private TextView player1StatText;
    private TextView player2StatText;
    private TextView player1NameText;
    private TextView player2NameText;
    private Darts501EditText inputText;
    private RecyclerView listPlayer1Shoots;
    private RecyclerView listPlayer2Shoots;
    private MaterialButton okButton;
    private final MutableLiveData<InputType> inputType = new MutableLiveData<>(InputType.SHOOT);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(Darts501ViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @SuppressLint({"SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        mViewModel = new ViewModelProvider(this).get(Darts501ViewModel.class);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        if (mViewModel.getActive().getValue() == null) {
            mViewModel.newGame(new Darts501Player(1L, "Player1"), new Darts501Player(2L, "Player2"));
            mViewModel.startGame();
        }
        setObservers();
        return inflater.inflate(R.layout.fragment_darts501, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listPlayer1Shoots = view.findViewById(R.id.listPlayer1Shoots);
        listPlayer1Shoots.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        player1ShootAdapter = new Darts501ShootsAdapter(mViewModel.getShoots(1), darts501Shoot -> {/*TODO*/});
        listPlayer1Shoots.setAdapter(player1ShootAdapter);

        listPlayer2Shoots = view.findViewById(R.id.listPlayer2Shoots);
        listPlayer2Shoots.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        player2ShootAdapter = new Darts501ShootsAdapter(mViewModel.getShoots(2), darts501Shoot -> {/*TODO*/});
        listPlayer2Shoots.setAdapter(player2ShootAdapter);

        mViewModel.setPlayerShootListener(player -> {
            if (player == 1) player1ShootAdapter.inserted();
            else player2ShootAdapter.inserted();
            refreshGUI();
        });

        inputText = view.findViewById(R.id.scoreEditText);
        inputText.setInputType(inputType);

        okButton = view.findViewById(R.id.okButton);
        okButton.setOnClickListener(this::onClickOkButton);

        player1ScoreText = view.findViewById(R.id.player1Score);
        player1ScoreText.setTextColor(Color.BLUE);

        player2ScoreText = view.findViewById(R.id.player2Score);
        player2ScoreText.setTextColor(Color.BLUE);


        player1StatText = view.findViewById(R.id.player1StatText);
        player2StatText = view.findViewById(R.id.player2StatText);

        player1NameText = setPlayerNamesStyle(view.findViewById(R.id.player1NameText), mViewModel.getPlayer1(), 1, InputType.NAME1);
        player2NameText = setPlayerNamesStyle(view.findViewById(R.id.player2NameText), mViewModel.getPlayer2(), 2, InputType.NAME2);

        view.findViewById(R.id.textViewStat).setOnLongClickListener(v -> {
            inputType.setValue(InputType.NEW_GAME);
            return true;
        });


    }

    private TextView setPlayerNamesStyle(TextView playerNameText, Darts501Player player, int i, InputType name1) {
        playerNameText.setText(player.getName());
        playerNameText.setOnClickListener(v -> mViewModel.setActivePlayer(i));
        playerNameText.setOnLongClickListener(v -> {
            inputType.setValue(name1);
            return true;
        });
        return playerNameText;
    }

    private void onClickOkButton(View button) {

        switch (Objects.requireNonNull(inputType.getValue())) {
            case SHOOT -> inputText.getShoot().ifPresent(shoot -> mViewModel.newShoot(shoot));
            case NEW_GAME -> mViewModel.startGame();
            case NAME1 -> player1NameText.setText(inputText.getName().orElse("Player 1"));
            case NAME2 -> player2NameText.setText(inputText.getName().orElse("Player 2"));
        }

        inputType.setValue(InputType.SHOOT);
    }

    private void refreshGUI() {
        refreshGUI(mViewModel.getPlayer1().getStat(), player1ScoreText,
                player1StatText, listPlayer1Shoots);
        refreshGUI(mViewModel.getPlayer2().getStat(), player2ScoreText,
                player2StatText, listPlayer2Shoots);
    }

    private void refreshGUI(Darts501SummaryStatistics statPlayer, TextView playerScoreText,
                            TextView playerStatText, RecyclerView listPlayerShoots) {
        playerScoreText.setText(String.valueOf(statPlayer.getScore()));
        if (statPlayer.isOut())
            playerScoreText.setTextColor(Color.GREEN);
        else
            playerScoreText.setTextColor(Color.BLUE);
        if (statPlayer.getCount() == 0)
            playerStatText.setText("");
        else
            playerStatText.setText(statPlayer.getStat());
        listPlayerShoots.smoothScrollToPosition(100);
    }

    @SuppressLint("SetTextI18n")
    private void setObservers() {
        mViewModel.getActive().observe(getViewLifecycleOwner(), darts501Player -> {
            if (mViewModel.whichActive() == 1) setPlayerNamesStyle(player1NameText, player2NameText);
            else setPlayerNamesStyle(player2NameText, player1NameText);

        });
        mViewModel.isGameOver().observe(getViewLifecycleOwner(), isGameOver -> {
            if (isGameOver) {
                okButton.setText("Winner: " + mViewModel.getActivePlayer().getName());
                inputText.setInputType(EditorInfo.TYPE_NULL);
                inputText.clearFocus();
            } else {
                player1ShootAdapter.clearAllData();
                player2ShootAdapter.clearAllData();
                refreshGUI();
                inputText.requestFocus();
            }
        });
    }

    private void setPlayerNamesStyle(TextView player1NameText, TextView player2NameText) {
        player1NameText.setTypeface(Typeface.DEFAULT_BOLD);
        player1NameText.setTextColor(Color.RED);
        player2NameText.setTypeface(Typeface.DEFAULT);
        player2NameText.setTextColor(Color.BLACK);
    }

}