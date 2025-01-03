package bruzsa.laszlo.dartsapp.ui.singlex01;

import static android.view.View.GONE;
import static bruzsa.laszlo.dartsapp.Helper.WEB_GUI_X01;
import static bruzsa.laszlo.dartsapp.Helper.getHtmlTemplate;
import static bruzsa.laszlo.dartsapp.Helper.showCheckoutDialog;

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
import androidx.recyclerview.widget.GridLayoutManager;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentSinglex01Binding;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputViews;

public class SingleX01Fragment extends Fragment {

    private SingleX01ViewModel viewModel;
    private FragmentSinglex01Binding binding;


    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        viewModel = new ViewModelProvider(this).get(SingleX01ViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        requireActivity().findViewById(R.id.toolbar).setVisibility(GONE);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_singlex01, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        SingleX01FragmentArgs args = SingleX01FragmentArgs.fromBundle(requireArguments());
        viewModel.startOrCountinue(
                args.getPlayer(),
                args.getStartScore(),
                getHtmlTemplate(requireContext(), WEB_GUI_X01));

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        InputViews inputViews = new InputViews(this, binding.includedInputs);
        binding.includedInputs.setInputViews(inputViews);
        binding.includedInputs.setLifecycleOwner(getViewLifecycleOwner());
        inputViews.setOnReadyAction(this::newThrow);

        binding.listThrows.setLayoutManager(new GridLayoutManager(
                requireContext(), 3));

    }

    private void newThrow(Integer dartsThrow) {
        viewModel.newThrow(dartsThrow, this::onCheckout);
    }

    private void onCheckout(int throwValue) {
        showCheckoutDialog(throwValue, requireContext(), dartCount ->
                viewModel.newThrow(throwValue, dartCount));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

}