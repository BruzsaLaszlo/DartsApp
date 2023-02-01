package bruzsa.laszlo.dartsapp.ui.practice;

import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import bruzsa.laszlo.dartsapp.model.x01.X01Throw;

public class SinglePracticeViewModel extends ViewModel {

    private final List<X01Throw> throwList = new LinkedList<>();

    public void addThrow(X01Throw x01Throw) {
        throwList.add(x01Throw);
    }

    public List<X01Throw> getThrowList() {
        return Collections.unmodifiableList(throwList);
    }

}