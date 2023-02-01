package bruzsa.laszlo.dartsapp.ui.cricket;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static android.view.View.GONE;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentCricketBinding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.cricket.CricketViewModel;

public class CricketFragment extends Fragment {

    private static final String TAG = "CricketFragment";
    private CricketViewModel cViewModel;
    private SharedViewModel sharedViewModel;
    private CricketThrowsAdapter cricketThrowsAdapter;
    private FragmentCricketBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cViewModel = new ViewModelProvider(this).get(CricketViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setScreenOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        binding = FragmentCricketBinding.inflate(inflater, container, false);
        requireActivity().findViewById(R.id.toolbar).setVisibility(GONE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cViewModel.isGameOver().observe(getViewLifecycleOwner(), isGameOver ->
                binding.textCricketScore.setTextColor(Boolean.TRUE.equals(isGameOver) ? Color.RED : Color.BLACK));

        ScreenSize screenSize = new ScreenSize(requireActivity());
        binding.cricketTable1.setSize(screenSize.getSize());
        binding.cricketTable1.setLeft(true);
        setOnClickListenersForTable(binding.cricketTable1, 1);
        binding.cricketTable2.setSize(screenSize.getSize());
        setOnClickListenersForTable(binding.cricketTable2, 2);

        binding.listThrows.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        cricketThrowsAdapter = new CricketThrowsAdapter(
                cViewModel.getThrows(),
                cricketThrow -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle("Do you want to delete?   " + cricketThrow.toString());
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        if (cViewModel.shootRemove(cricketThrow)) {
                            refreshGUI();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.create().show();
                });
        binding.listThrows.setAdapter(cricketThrowsAdapter);
        binding.listThrows.setPadding(0, 0, 0, 0);

        if (cViewModel.isGameNotStarted()) {
            startNewGame();
        } else {
            refreshGUI();
        }


        String player1 = sharedViewModel.getPlayer(PLAYER_1_1).getNickName();
        String player2 = sharedViewModel.getPlayer(PLAYER_2_1).getNickName();
        if (sharedViewModel.getSelectedPlayersMap().size() == 4) {
            player1 += "\n" + sharedViewModel.getPlayer(PLAYER_1_2).getNickName();
            player2 += "\n" + sharedViewModel.getPlayer(PLAYER_2_2).getNickName();
        }
        binding.textNameCricketPlayer1.setText(player1);
        binding.textNameCricketPlayer2.setText(player2);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshGUI() {
        binding.cricketTable1.refreshState(cViewModel.getPlayerScore(1), cViewModel.getPlayerScore(2));
        binding.cricketTable2.refreshState(cViewModel.getPlayerScore(2), cViewModel.getPlayerScore(1));
        binding.textCricketScore.setText(cViewModel.updatePoints());
        cricketThrowsAdapter.notifyDataSetChanged();
        binding.listThrows.smoothScrollToPosition(cricketThrowsAdapter.getItemCount());
    }

    private void setOnClickListenersForTable(CricketTable table, int player) {
        table.getTouchedValue().observe(getViewLifecycleOwner(), value -> {
            cViewModel.newThrow(1, value, player);
            cricketThrowsAdapter.notifyItemInserted(cricketThrowsAdapter.getItemCount());
            refreshGUI();
        });
    }

    private void startNewGame() {
        cViewModel.newGame(sharedViewModel.getSelectedPlayersMap());
        refreshGUI();
    }

    private void setScreenOrientation(int screenOrientation) {
        requireActivity().setRequestedOrientation(screenOrientation);
        Log.d(TAG, "SCREEN_ORIENTATION: " + screenOrientation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setScreenOrientation(SCREEN_ORIENTATION_UNSPECIFIED);
        binding = null;
    }
}