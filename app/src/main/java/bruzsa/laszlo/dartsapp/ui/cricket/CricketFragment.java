package bruzsa.laszlo.dartsapp.ui.cricket;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static android.view.View.GONE;
import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static bruzsa.laszlo.dartsapp.Helper.CRICKET_WEB_GUI;
import static bruzsa.laszlo.dartsapp.Helper.getHtmlTemplate;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.cricket.CricketViewModel;
import bruzsa.laszlo.dartsapp.ui.cricket.CricketTable.TouchValue;

public class CricketFragment extends Fragment {

    private static final String TAG = "CricketFragment";
    private CricketViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private FragmentCricketBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CricketViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setScreenOrientation(SCREEN_ORIENTATION_LANDSCAPE);

        requireActivity().findViewById(R.id.toolbar).setVisibility(GONE);

        binding = FragmentCricketBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel.getCricketSettings().updateSize(requireActivity());
        binding.cricketTable1.inic(sharedViewModel.getCricketSettings(), this::getOnTouchEventListener);
        binding.cricketTable2.inic(sharedViewModel.getCricketSettings(), this::getOnTouchEventListener);

        binding.listThrows.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));

        viewModel.startNewGameOrContinue(
                sharedViewModel.getSelectedPlayersMap(),
                sharedViewModel.getCricketSettings(),
                binding.listThrows::setAdapter,
                getHtmlTemplate(requireContext(), CRICKET_WEB_GUI)
        );

    }

//    private void onRemoveListener(CricketThrow cricketThrow) {
//        new AlertDialog.Builder(requireActivity())
//                .setTitle("Do you want to delete?   " + cricketThrow.toString())
//                .setPositiveButton("Yes", (dialog, which) -> removeThrow(cricketThrow))
//                .setNegativeButton("No", null)
//                .create()
//                .show();
//    }

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

    private void getOnTouchEventListener(Team team, TouchValue touchValue) {
        viewModel.newThrow(touchValue.getMultiplier(), touchValue.getValue(), team, this::gameOver);
    }

    private void gameOver() {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(0xFF00FF00); // Changes this drawbale to use a single color instead of a gradient
        gd.setCornerRadius(5);
        gd.setStroke(1, 0xFF000000);

        binding.textCricketScore.setBackground(gd);
        binding.textCricketScore.setTextColor(Color.RED);

        binding.cricketTable1.setEnabled(false);
        binding.cricketTable2.setEnabled(false);
    }

}