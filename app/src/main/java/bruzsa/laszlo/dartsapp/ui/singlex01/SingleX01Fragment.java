package bruzsa.laszlo.dartsapp.ui.singlex01;

import static android.view.View.GONE;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.Map;

import bruzsa.laszlo.dartsapp.Helper;
import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentSinglex01Binding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.ui.x01.X01WebGUI;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputType;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputViews;

public class SingleX01Fragment extends Fragment {

    private SingleX01ViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private FragmentSinglex01Binding binding;


    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        if (!Helper.isRecordInternetGranted(this)) {
            Log.d("SingleX01Fragment", "onCreate: Internet Permission Granted!");
            Helper.requestInternetPermission(this);
        }

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.setWebGui(new X01WebGUI(requireContext()));
        viewModel = new ViewModelProvider(this).get(SingleX01ViewModel.class);
        viewModel.setOnGuiChangeEvent((player, x01SummaryStatistics) ->
                sharedViewModel.setWebServerContent(Map.of(TEAM1, viewModel.getSummaryStatistics())));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        requireActivity().findViewById(R.id.toolbar).setVisibility(GONE);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_singlex01, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        binding.setSharedViewModel(sharedViewModel);
        binding.includedInputs.setSharedViewModel(sharedViewModel);

        viewModel.startOrCountinue(sharedViewModel.getPlayer(PLAYER_1_1), sharedViewModel.getSettings().getStartScore());

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        InputViews inputs = new InputViews(this, InputType.NUMPAD, binding.includedInputs);

        inputs.setOnReadyAction(this::newThrow);

//        binding.listThrows.setLayoutManager(new LinearLayoutManager(
//                requireContext(), LinearLayoutManager.VERTICAL, true));
        binding.listThrows.setLayoutManager(new GridLayoutManager(
                requireContext(), 3));

    }

    private void newThrow(Integer dartsThrow) {
        if (viewModel.getScore().getValue().equals(dartsThrow)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("How many darts has been thrown?");
            builder.setItems(new CharSequence[]{"1 dart", "2 darts", "3 darts"}, (dialog, which) ->
                    viewModel.newThrow(dartsThrow, which + 1));
            builder.create().show();
        } else {
            viewModel.newThrow(dartsThrow, 3);
        }
        binding.listThrows.smoothScrollToPosition(viewModel.getThrowsAdapter().getItemCount());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }


    private void showAlertDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title);
        builder.setNeutralButton("OK", null);
        builder.create().show();
    }


}