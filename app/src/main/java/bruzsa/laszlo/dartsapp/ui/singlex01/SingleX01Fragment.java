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
import bruzsa.laszlo.dartsapp.InputViews;
import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentSinglex01Binding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.singlex01.SingleX01ViewModel;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputType;

public class SingleX01Fragment extends Fragment {

    private SingleX01ViewModel sViewModel;
    private SharedViewModel sharedViewModel;
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
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_singlex01, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(sViewModel);

        if (sViewModel.getThrowsList().isEmpty())
            sViewModel.start(sharedViewModel.getPlayer(PLAYER_1_1), sharedViewModel.getSettings().getStartScore());
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        InputViews inputs = new InputViews(this, InputType.NUMPAD, binding.includedInputs);

        inputs.setOnReadyAction(this::newThrow);

        binding.listThrowsSingleX01.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, true));

        refreshGui();
    }

    private void newThrow(Integer dartsThrow) {
        if (sViewModel.getScore().getValue().equals(dartsThrow)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("How many darts has been thrown?");
            builder.setItems(new CharSequence[]{"1 dart", "2 darts", "3 darts"}, (dialog, which) ->
                    newThrow(dartsThrow, which + 1));
            builder.create().show();
        } else {
            newThrow(dartsThrow, 3);
        }
    }

    private void newThrow(Integer dartsThrow, int dartCount) {
        sViewModel.newThrow(dartsThrow, dartCount);
//        playerThrowAdapter.inserted();
        refreshGui();
    }

    @SuppressLint("SetTextI18n")
    private void refreshGui() {
        binding.textStatPlayer.setText(sViewModel.getStat().toString());
//        binding.listThrowsSingleX01.smoothScrollToPosition(playerThrowAdapter.getItemCount());
        sharedViewModel.setWebServerContent("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Page Title</title>
                    <meta http-equiv="refresh" content="1" />
                </head>
                <body>
                                
                <h1 style="font-size:2.25rem;">My h1 heading </h1>
                <h1>$Player1Name</h1>
                <p></p>
                <h2>$Player1Score</h2>
                <p>This is a paragraph.</p>
                <h3>AVG  $AVG</h3>
                <h3>MAX  $MAX</h3>
                <h3>MIN  $MIN</h3>
                <h3>HC   $HC</h3>
                <h3>100+ $100+</h3>
                <h3>60+  $60+</h3>
                </body>
                </html>
                """
                .replace("$Player1Name", sharedViewModel.getPlayer(PLAYER_1_1).getName())
                .replace("$Player1Score", sViewModel.getScore().toString())
                .replace("$AVG", String.valueOf(sViewModel.getStat().getAverage()))
                .replace("$MAX", String.valueOf(sViewModel.getStat().getMax()))
                .replace("$MIN", String.valueOf(sViewModel.getStat().getMin()))
                .replace("$HC", sViewModel.getStat().getHighestCheckout().map(Object::toString).orElse(""))
                .replace("$100+", String.valueOf(sViewModel.getStat().getHundredPlus()))
                .replace("$60+", String.valueOf(sViewModel.getStat().getSixtyPlus()))
        );
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