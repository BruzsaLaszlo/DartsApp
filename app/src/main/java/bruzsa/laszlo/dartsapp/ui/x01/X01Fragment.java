package bruzsa.laszlo.dartsapp.ui.x01;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.DialogSettingsBinding;
import bruzsa.laszlo.dartsapp.databinding.FragmentX01Binding;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.x01.X01ViewModel;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputViews;
import bruzsa.laszlo.dartsapp.util.HandleBackButton;
import bruzsa.laszlo.dartsapp.util.Permission;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class X01Fragment extends Fragment {

    private X01ViewModel viewModel;
    private InputViews inputViews;

    private FragmentX01Binding binding;
    private final Permission permission = new Permission(this);


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        viewModel = new ViewModelProvider(this).get(X01ViewModel.class);

        permission.registerForActivityResult();
    }

    @SuppressLint({"SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new HandleBackButton(requireActivity()));

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_x01, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        inputViews = new InputViews(permission, binding.includedInputs, this::showSettingsDialog);
        binding.includedInputs.setInputViews(inputViews);
        binding.includedInputs.setLifecycleOwner(getViewLifecycleOwner());
        inputViews.setOnReadyAction(this::newThrow);
        inputViews.setOnPartialResultAction(value -> viewModel.newPartialValue(value));

        viewModel.getWinnerTeam().observe(getViewLifecycleOwner(), this::showGameOverScreen);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.listThrowsPlayer1.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        binding.listThrowsPlayer2.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
    }

    private void newThrow(int value) {
        viewModel.newThrow(value, requireContext());
        inputViews.textToSpeech(value);
    }

    private void showSettingsDialog() {
        DialogSettingsBinding settingsDialog = DataBindingUtil.inflate(
                getLayoutInflater(), R.layout.dialog_settings,
                this.binding.constraintLayoutData, false);
        settingsDialog.setLifecycleOwner(getViewLifecycleOwner());
        settingsDialog.setInputViews(inputViews);
        new MaterialAlertDialogBuilder(requireContext())
                .setView(settingsDialog.inputSettingsDialog)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void showGameOverScreen(Team winner) {
        binding.includedInputs.inputText.setDisabled();
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("The Winner is : \n"
                        + viewModel.getPlayerMap().get(winner.player1()).getName()
                        + (viewModel.getPlayerMap().size() == 4 ? "\n" + viewModel.getPlayerMap().get(winner.player2()).getName() : ""))
                .setPositiveButton("Ok", null)
                .create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

}