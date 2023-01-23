package bruzsa.laszlo.dartsapp.ui.singlex01;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.Helper;
import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentSinglex01Binding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.singleX01.SingleX01ViewModel;
import bruzsa.laszlo.dartsapp.ui.Speech;
import bruzsa.laszlo.dartsapp.ui.x01.NumberPad;
import bruzsa.laszlo.dartsapp.ui.x01.X01ThrowAdapter;

public class SingleX01Fragment extends Fragment {

    private SingleX01ViewModel sViewModel;
    private SharedViewModel sharedViewModel;
    private X01ThrowAdapter playerThrowAdapter;
    private FragmentSinglex01Binding binding;
    private Speech speech;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requireActivity().setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        requireActivity().findViewById(R.id.toolbar).setVisibility(GONE);
        sViewModel = new ViewModelProvider(this).get(SingleX01ViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        binding = FragmentSinglex01Binding.inflate(inflater, container, false);
        if (sViewModel.getThrowsList().isEmpty())
            sViewModel.start(sharedViewModel.getPlayer(PLAYER_1_1), sharedViewModel.getSettings().getStartScore());
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.buttonSingleX01Ok.setVisibility(GONE);
        binding.imageMicrophone.setVisibility(
                Helper.isRecordPermissionGranted(this) ? VISIBLE : INVISIBLE);
        binding.imageMicrophone.setOnClickListener(v -> {
            binding.numberPadSingle.setVisibility(GONE);
            binding.imageMicrophone.setImageResource(R.drawable.microphone);
            speech.startListening();
        });

        binding.inputText.setOnLongClickListener(v -> {
            binding.numberPadSingle.setVisibility(VISIBLE);
            return true;
        });

        binding.inputText.setOnClickListener(v -> {
            binding.numberPadSingle.setVisibility(VISIBLE);
        });
        binding.numberPadSingle.setOnClickListener((Consumer<Integer>) number -> {
            if (number == NumberPad.OK) onOKClick();
            else if (number == NumberPad.BACK) binding.inputText.removeLast();
            else binding.inputText.add(String.valueOf(number));
        });
        binding.numberPadSingle.setOnLongClickListener((Consumer<Integer>) number -> binding.inputText.add("+"));

        speech = new Speech(requireContext());
        speech.getResultLiveData().observe(getViewLifecycleOwner(),
                strings -> {
                    binding.inputText.setText(String.join("+", strings));
                    binding.imageMicrophone.setImageResource(R.drawable.mute);
                });

        binding.buttonSingleX01Ok.setOnClickListener(v -> onOKClick());

        playerThrowAdapter = new X01ThrowAdapter(sViewModel.getThrowsList());
        playerThrowAdapter.getSelectedForRemove().observe(getViewLifecycleOwner(), x01Throw ->
                playerThrowAdapter.remove(sViewModel.removeThrow(x01Throw)));
        binding.listThrowsSingleX01.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, true));
        binding.listThrowsSingleX01.setAdapter(playerThrowAdapter);

        refreshGui(sharedViewModel.getSettings().getStartScore());
    }

    private void onOKClick() {
        binding.inputText.getThrow()
                .ifPresent(dartsThrow -> {
                    if (binding.textScorePlayer.getText().equals(dartsThrow.toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                        builder.setTitle("How many darts has been thrown?");
                        builder.setItems(new CharSequence[]{"1 dobás", "2 dobás", "3 dobás"}, (dialog, which) ->
                                refreshGui(sViewModel.newThrow(dartsThrow, which + 1)));
                        builder.create().show();
                    } else {
                        refreshGui(sViewModel.newThrow(dartsThrow));
                    }
                });
    }

    private void refreshGui(int score) {
        binding.textScorePlayer.setText(String.valueOf(score));
        binding.textStatPlayer.setText(sViewModel.getStat());
        binding.inputText.clear();
//        binding.textInputSingleX01.setError(null);
        playerThrowAdapter.inserted();
        binding.listThrowsSingleX01.smoothScrollToPosition(100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

}