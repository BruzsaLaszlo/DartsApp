package bruzsa.laszlo.dartsapp.ui.darts501;

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
import bruzsa.laszlo.dartsapp.databinding.FragmentDarts501SinglePlayerBinding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;
import bruzsa.laszlo.dartsapp.model.darts501.Darts501SinglePlayerViewModel;
import bruzsa.laszlo.dartsapp.ui.darts501.input.InputType;

public class Darts501SinglePlayerFragment extends Fragment {

    private Darts501SinglePlayerViewModel mViewModel;
    private SharedViewModel sharedViewModel;
    private Darts501ThrowAdapter playerThrowAdapter;
    private final MutableLiveData<InputType> inputType = new MutableLiveData<>(InputType.THROW);
    private FragmentDarts501SinglePlayerBinding binding;

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
        mViewModel = new ViewModelProvider(this).get(Darts501SinglePlayerViewModel.class);
        binding = FragmentDarts501SinglePlayerBinding.inflate(inflater, container, false);
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