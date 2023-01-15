package bruzsa.laszlo.dartsapp.ui.cricket;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentCricketBinding;
import bruzsa.laszlo.dartsapp.model.CricketViewModel;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;

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
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.startNewGameCricket().observe(getViewLifecycleOwner(),newGame ->
                cViewModel.newGame(sharedViewModel.getPlayer1(), sharedViewModel.getPlayer2()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCricketBinding.inflate(inflater, container, false);
        setScreenOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requireActivity().findViewById(R.id.toolbar).setVisibility(View.INVISIBLE);
        }


        if (cViewModel.isGameNotStarted()) {
            cViewModel.newGame(sharedViewModel.getPlayer1(), sharedViewModel.getPlayer2());
        }

        sharedViewModel.startNewGameCricket().observe(getViewLifecycleOwner(), this::newGame);

        cViewModel.isGameOver().observe(getViewLifecycleOwner(), isGameOver ->
                binding.scoreTextView.setTextColor(Boolean.TRUE.equals(isGameOver) ? Color.RED : Color.BLACK));

        setOnClickListenersForTable(binding.cricketTable1, 1);
        setOnClickListenersForTable(binding.cricketTable2, 2);


        binding.shootList.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        cricketThrowsAdapter = new CricketThrowsAdapter(
                cViewModel.getThrows(),
                cricketThrow -> {
                    cViewModel.shootRemove(cricketThrow);
                    refreshGUI();
                });
        binding.shootList.setAdapter(cricketThrowsAdapter);
        binding.shootList.setPadding(0, 0, 0, 0);

        refreshGUI();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshGUI() {
        binding.cricketTable1.refreshState(cViewModel.getPlayerScore(1), cViewModel.getPlayerScore(2));
        binding.cricketTable2.refreshState(cViewModel.getPlayerScore(2), cViewModel.getPlayerScore(1));
        binding.scoreTextView.setText(cViewModel.updatePoints());
        cricketThrowsAdapter.notifyDataSetChanged();
        binding.shootList.smoothScrollToPosition(100);
    }

    private void setOnClickListenersForTable(CricketTable table, int i) {
        table.setOnClickListener(v -> {
            cViewModel.newThrow(1, ((CricketTable) v).getTouchedValue(), i);
            refreshGUI();
        });
        table.setOnLongClickListener(v -> {
            int value = ((CricketTable) v).getTouchedValue();
            cViewModel.newThrow(value == 25 ? 2 : 3, value, i);
            refreshGUI();
            return true;
        });
    }

    private void newGame(boolean isNewGame) {
        cViewModel.newGame(sharedViewModel.getPlayer1(), sharedViewModel.getPlayer2());
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