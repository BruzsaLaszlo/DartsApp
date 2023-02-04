package bruzsa.laszlo.dartsapp.ui.singlex01;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.view.View.GONE;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import bruzsa.laszlo.dartsapp.Helper;
import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentSinglex01Binding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputType;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputViews;

public class SingleX01Fragment extends Fragment {

    private SingleX01ViewModel sViewModel;
    private FragmentSinglex01Binding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Helper.isRecordInternetGranted(this)) {
            Log.d("SingleX01Fragment", "onCreate: Internet Permission Granted!");
            Helper.requestInternetPermission(this);
        }
    }

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        requireActivity().setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        requireActivity().findViewById(R.id.toolbar).setVisibility(GONE);

        sViewModel = new ViewModelProvider(this).get(SingleX01ViewModel.class);
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_singlex01, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(sViewModel);
        binding.setSharedViewModel(sharedViewModel);
        binding.includedInputs.setSharedViewModel(sharedViewModel);

        sViewModel.startOrCountinue(sharedViewModel.getPlayer(PLAYER_1_1), sharedViewModel.getSettings().getStartScore());

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        InputViews inputs = new InputViews(this, InputType.NUMPAD, binding.includedInputs);

        inputs.setOnReadyAction(this::newThrow);

        binding.listThrows.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, true));

    }

    private void newThrow(Integer dartsThrow) {
        if (sViewModel.getScore().getValue().equals(dartsThrow)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("How many darts has been thrown?");
            builder.setItems(new CharSequence[]{"1 dart", "2 darts", "3 darts"}, (dialog, which) ->
                    sViewModel.newThrow(dartsThrow, which + 1));
            builder.create().show();
        } else {
            sViewModel.newThrow(dartsThrow, 3);
        }
        binding.listThrows.smoothScrollToPosition(sViewModel.getThrowsAdapter().getItemCount());
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