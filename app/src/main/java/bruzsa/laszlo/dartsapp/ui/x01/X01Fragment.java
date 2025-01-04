package bruzsa.laszlo.dartsapp.ui.x01;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;
import static bruzsa.laszlo.dartsapp.Helper.getHtmlTemplate;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.Helper;
import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentX01Binding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.x01.X01ViewModel;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputViews;

public class X01Fragment extends Fragment {

    private X01ViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private FragmentX01Binding binding;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

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

        viewModel.startGameOrContinue(
                sharedViewModel.getSelectedPlayersMap(),
                sharedViewModel.getX01Settings(),
                getHtmlTemplate(requireContext(), Helper.WEB_GUI_X01));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InputViews inputViews = new InputViews(this, binding.includedInputs);
        binding.includedInputs.setInputViews(inputViews);
        binding.includedInputs.setLifecycleOwner(getViewLifecycleOwner());
        inputViews.setOnReadyAction(this::newThrow);
        inputViews.setOnPartialResultAction(value -> viewModel.newPartialValue(value));

        binding.listThrowsPlayer1.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));
        binding.listThrowsPlayer2.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, true));

    }

    private void newThrow(int value) {
        viewModel.newThrow(value, this::showDialogDartsCount, this::showGameOverScreen);
    }

    private void showDialogDartsCount(X01ViewModel.Darts darts, Consumer<Integer> dartsCount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("How many darts has been thrown?");
        CharSequence[] charSequences;
        if (darts == X01ViewModel.Darts.THREE) {
            charSequences = new CharSequence[]{"1 dart", "2 darts", "3 darts"};
            builder.setItems(charSequences, (dialog, which) -> dartsCount.accept(which + 1));
        } else {
            charSequences = new CharSequence[]{"2 darts", "3 darts"};
            builder.setItems(charSequences, (dialog, which) -> dartsCount.accept(which + 2));
        }
        builder.create().show();
    }

    private void showGameOverScreen(Team winner) {
        binding.includedInputs.inputText.setDisabled();
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("The Winner is : \n"
                        + sharedViewModel.getPlayer(winner.player1()).getName()
                        + (sharedViewModel.getSelectedPlayersMap().size() == 4 ? "\n" + sharedViewModel.getPlayer(winner.player2()).getName() : ""))
                .setPositiveButton("Ok", null)
                .create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

}