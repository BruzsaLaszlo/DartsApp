package bruzsa.laszlo.dartsapp.ui.dartsX01single;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentDartsx01SinglePlayerBinding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.dartsX01single.DartsX01SinglePlayerViewModel;
import bruzsa.laszlo.dartsapp.ui.dartsX01.DartsX01ThrowAdapter;
import bruzsa.laszlo.dartsapp.ui.dartsX01.input.InputType;

public class DartsX01SinglePlayerFragment extends Fragment {

    private DartsX01SinglePlayerViewModel mViewModel;
    private SharedViewModel sharedViewModel;
    private DartsX01ThrowAdapter playerThrowAdapter;
    private final MutableLiveData<InputType> inputType = new MutableLiveData<>(InputType.THROW);
    private FragmentDartsx01SinglePlayerBinding binding;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DartsX01SinglePlayerViewModel.class);
        binding = FragmentDartsx01SinglePlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TODO document why this method is empty
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}