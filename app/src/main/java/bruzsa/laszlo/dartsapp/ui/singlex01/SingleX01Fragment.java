package bruzsa.laszlo.dartsapp.ui.singlex01;

import static android.view.View.GONE;
import static bruzsa.laszlo.dartsapp.Helper.X01_WEB_GUI;
import static bruzsa.laszlo.dartsapp.Helper.getHtmlTemplate;
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
import java.util.concurrent.atomic.AtomicInteger;

import bruzsa.laszlo.dartsapp.Helper;
import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentSinglex01Binding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.ui.x01.X01WebGUI;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputViews;

public class SingleX01Fragment extends Fragment {

    private SingleX01ViewModel viewModel;
    private SharedViewModel sharedViewModel;
    private FragmentSinglex01Binding binding;

    private X01WebGUI webGUI;


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
        viewModel = new ViewModelProvider(this).get(SingleX01ViewModel.class);
        webGUI = new X01WebGUI(getHtmlTemplate(requireContext(), X01_WEB_GUI));
        viewModel.setOnGuiChangeEventListener((player, x01SummaryStatistics) -> {
            String html = webGUI.getHTML(sharedViewModel.getSelectedPlayersMap(), Map.of(TEAM1, viewModel.getSummaryStatistics()));
            sharedViewModel.setWebServerContent(html);
        });
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

        viewModel.startOrCountinue(sharedViewModel.getPlayer(PLAYER_1_1), sharedViewModel.getSettings().getStartScore());

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        InputViews inputViews = new InputViews(this, binding.includedInputs);
        binding.includedInputs.setInputViews(inputViews);
        binding.includedInputs.setLifecycleOwner(getViewLifecycleOwner());
        inputViews.setOnReadyAction(this::newThrow);

//        binding.listThrows.setLayoutManager(new LinearLayoutManager(
//                requireContext(), LinearLayoutManager.VERTICAL, true));
        binding.listThrows.setLayoutManager(new GridLayoutManager(
                requireContext(), 3));

    }

    private void newThrow(Integer dartsThrow) {
        viewModel.newThrow(dartsThrow, this::onCheckout);
        binding.listThrows.smoothScrollToPosition(viewModel.getThrowsAdapter().getItemCount());
    }

    private int onCheckout() {
        AtomicInteger dartCount = new AtomicInteger();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("How many darts has been thrown?");
        builder.setItems(new CharSequence[]{"1 dart", "2 darts", "3 darts"},
                (dialog, which) -> dartCount.set(which + 1));
        builder.create().show();
        return dartCount.get();
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