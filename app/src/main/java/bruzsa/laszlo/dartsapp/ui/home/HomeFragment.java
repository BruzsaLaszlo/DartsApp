package bruzsa.laszlo.dartsapp.ui.home;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentHomeBinding;
import bruzsa.laszlo.dartsapp.model.SharedViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        binding.newCricket.setOnClickListener(v -> sharedViewModel.newGameCricket());
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}