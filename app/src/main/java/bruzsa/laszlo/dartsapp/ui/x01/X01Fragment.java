package bruzsa.laszlo.dartsapp.ui.x01;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentX01Binding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.x01.X01ViewModel;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputType;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputViews;

public class X01Fragment extends Fragment {

    private X01ViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private FragmentX01Binding binding;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel = new ViewModelProvider(this).get(X01ViewModel.class);
    }

    @SuppressLint({"SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_x01, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        binding.setSharedViewModel(sharedViewModel);
        binding.includedInputs.setSharedViewModel(sharedViewModel);

        viewModel.startGameOrContinue(sharedViewModel.getSelectedPlayersMap(), sharedViewModel.getSettings());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InputViews inputs = new InputViews(this, InputType.NUMPAD, binding.includedInputs);

        inputs.setOnReadyAction(value -> {
            viewModel.newThrow(value, () -> {
            }/* todo GAME OVER EVENT*/);
            binding.listThrowsPlayer1.smoothScrollToPosition(viewModel.getThrowsAdapterCount(TEAM1));
            binding.listThrowsPlayer1.smoothScrollToPosition(viewModel.getThrowsAdapterCount(TEAM2));
        });

        binding.listThrowsPlayer1.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        binding.listThrowsPlayer2.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

}