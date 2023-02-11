package bruzsa.laszlo.dartsapp.ui.cricket;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static android.view.View.GONE;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static bruzsa.laszlo.dartsapp.Helper.CRICKET_WEB_GUI;
import static bruzsa.laszlo.dartsapp.Helper.getHtmlTemplate;
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
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.cricket.CricketThrow;
import bruzsa.laszlo.dartsapp.model.cricket.CricketViewModel;

public class CricketFragment extends Fragment {

    private static final String TAG = "CricketFragment";
    private CricketViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private CricketThrowsAdapter cricketThrowsAdapter;
    private FragmentCricketBinding binding;
    private CricketWebGui webGUI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        try {
//            Class.forName("dalvik.system.CloseGuard")
//                    .getMethod("setEnabled", boolean.class)
//                    .invoke(null, true);
//        } catch (ReflectiveOperationException e) {
//            throw new RuntimeException(e);
//        }
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CricketViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        webGUI = new CricketWebGui(getHtmlTemplate(requireContext(), CRICKET_WEB_GUI));
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

        viewModel.isGameOver().observe(getViewLifecycleOwner(), isGameOver ->
                binding.textCricketScore.setTextColor(Boolean.TRUE.equals(isGameOver) ? Color.RED : Color.BLACK));

        ScreenSize screenSize = new ScreenSize(requireActivity());
        binding.cricketTable1.inic(screenSize.getSize(), Team.TEAM1);
        binding.cricketTable1.setOnTouchEventListener(this::getOnTouchEventListener);
        binding.cricketTable2.inic(screenSize.getSize(), Team.TEAM2);
        binding.cricketTable2.setOnTouchEventListener(this::getOnTouchEventListener);

        binding.listThrows.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        cricketThrowsAdapter = new CricketThrowsAdapter(viewModel.getThrows(), this::onRemoveListener);
        binding.listThrows.setAdapter(cricketThrowsAdapter);
        binding.listThrows.setPadding(0, 0, 0, 0);

        if (viewModel.isGameNotStarted()) {
            startNewGame();
        } else {
            refreshGUI();
        }


        String player1 = sharedViewModel.getPlayer(PLAYER_1_1).getName();
        String player2 = sharedViewModel.getPlayer(PLAYER_2_1).getName();
        if (sharedViewModel.getSelectedPlayersMap().size() == 4) {
            player1 += "\n" + sharedViewModel.getPlayer(PLAYER_1_2).getName();
            player2 += "\n" + sharedViewModel.getPlayer(PLAYER_2_2).getName();
        }
        binding.textNameCricketPlayer1.setText(player1);
        binding.textNameCricketPlayer2.setText(player2);
    }

    private void onRemoveListener(CricketThrow cricketThrow) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Do you want to delete?   " + cricketThrow.toString());
        builder.setPositiveButton("Yes", (dialog, which) -> {
            if (viewModel.shootRemove(cricketThrow)) {
                refreshGUI();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshGUI() {
        binding.cricketTable1.refreshState(viewModel.getPlayerScore(1), viewModel.getPlayerScore(2));
        binding.cricketTable2.refreshState(viewModel.getPlayerScore(2), viewModel.getPlayerScore(1));

        binding.textCricketScore.setText(viewModel.updatePoints());

        cricketThrowsAdapter.notifyDataSetChanged();
        binding.listThrows.smoothScrollToPosition(cricketThrowsAdapter.getItemCount());

        String html = webGUI.getHTML(viewModel.getCricketTeams());
        sharedViewModel.setWebServerContent(html);
    }

    private void startNewGame() {
        viewModel.newGame(sharedViewModel.getSelectedPlayersMap());
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

    private void getOnTouchEventListener(Team team, Integer number) {
        viewModel.newThrow(1, number, team);
        cricketThrowsAdapter.notifyItemInserted(cricketThrowsAdapter.getItemCount());
        refreshGUI();
    }

}