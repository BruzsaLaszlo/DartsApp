package bruzsa.laszlo.dartsapp.ui.practice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import bruzsa.laszlo.dartsapp.R;
import bruzsa.laszlo.dartsapp.databinding.FragmentSinglePracticeBinding;
import bruzsa.laszlo.dartsapp.databinding.InputViewsBinding;
import bruzsa.laszlo.dartsapp.ui.x01.input.InputText;
import bruzsa.laszlo.dartsapp.ui.x01.input.NumberPad;

public class SinglePracticeFragment extends Fragment {

    private SinglePracticeViewModel spViewModel;
    private FragmentSinglePracticeBinding binding;
    private InputViewsBinding inputs;

    public static SinglePracticeFragment newInstance() {
        return new SinglePracticeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        spViewModel = new ViewModelProvider(this).get(SinglePracticeViewModel.class);
        binding = FragmentSinglePracticeBinding.inflate(inflater, container, false);
        inputs = InputViewsBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_single_practice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        X01ThrowAdapter adapter = new X01ThrowAdapter(spViewModel.getThrowList());

        binding.listThrowsSinglePractice.setLayoutManager(
                new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true));
//        binding.listThrowsSinglePractice.setAdapter(adapter);

        InputText inputText = view.findViewById(R.id.inputText);
        NumberPad numberPad = view.findViewById(R.id.numPad);
        numberPad.setInputTextNumber(inputText);
        inputText.setOnReadyAction(this::onOKClick);

//        inputs.numPad.setInputTextNumber(inputs.inputText);
//        inputs.inputText.setOnReadyAction(this::onOKClick);

//        binding.inputTextSinglePractice.getThrow()
    }

    private void onOKClick(Integer integer) {
        // TODO
    }
}