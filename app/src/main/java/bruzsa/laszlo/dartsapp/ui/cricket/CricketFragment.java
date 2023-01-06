package bruzsa.laszlo.dartsapp.ui.cricket;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.model.CricketViewModel;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;

public class CricketFragment extends Fragment {

    private CricketViewModel cViewModel;
    private int multiplier = 1;
    private SharedViewModel sharedViewModel;
    private CricketTable cricketTable1;
    private CricketTable cricketTable2;
    private TextView scoreTextView;
    private RecyclerView shootList;
    private CricketShootsAdapter cricketShootsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cricket, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requireActivity().findViewById(R.id.toolbar).setVisibility(View.INVISIBLE);
        }


        CricketPlayer cricketPlayer1 = new CricketPlayer("P1");
        CricketPlayer cricketPlayer2 = new CricketPlayer("P2 ");

        cViewModel = new ViewModelProvider(this).get(CricketViewModel.class);
        if (cViewModel.isGameNotStarted()) {
            cViewModel.newGame(cricketPlayer1, cricketPlayer2);
        }

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getNewGame().observe(getViewLifecycleOwner(), newGame -> {
            cViewModel.newGame(cricketPlayer1, cricketPlayer2);
            refreshGUI();
        });

        scoreTextView = view.findViewById(R.id.scoreTextView);
        cViewModel.isGameOver().observe(getViewLifecycleOwner(),isGameOver ->
                scoreTextView.setTextColor(isGameOver? Color.RED :Color.BLACK));

        cricketTable1 = view.findViewById(R.id.cricketTable1);
        setOnClickListenersForTable(cricketTable1, 1);
        cricketTable2 = view.findViewById(R.id.cricketTable2);
        setOnClickListenersForTable(cricketTable2, 2);


        shootList = view.findViewById(R.id.shootList);
        shootList.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        cricketShootsAdapter = new CricketShootsAdapter(
                cViewModel.getShoots(),
                cricketShoot -> {
                    cViewModel.shootRemove(cricketShoot);
                    refreshGUI();
                });
        shootList.setAdapter(cricketShootsAdapter);
        shootList.setPadding(0, 0, 0, 0);

        refreshGUI();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshGUI() {
        cricketTable1.refreshState(cViewModel.getPlayerScore(1), cViewModel.getPlayerScore(2));
        cricketTable2.refreshState(cViewModel.getPlayerScore(2), cViewModel.getPlayerScore(1));
        scoreTextView.setText(cViewModel.updatePoints());
        cricketShootsAdapter.notifyDataSetChanged();
        shootList.smoothScrollToPosition(100);
    }

    private void setOnClickListenersForTable(CricketTable table, int i) {
        table.setOnClickListener(v -> {
            cViewModel.newShoot(1, ((CricketTable) v).getTouchedValue(), i);
            refreshGUI();
        });
        table.setOnLongClickListener(v -> {
            int value = ((CricketTable) v).getTouchedValue();
            cViewModel.newShoot(value == 25 ? 2 : 3, value, i);
            refreshGUI();
            return true;
        });
    }

}