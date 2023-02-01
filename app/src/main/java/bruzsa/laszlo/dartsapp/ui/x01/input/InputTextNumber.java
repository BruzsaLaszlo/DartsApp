package bruzsa.laszlo.dartsapp.ui.x01.input;

import java.util.Optional;

public interface InputTextNumber {

    Optional<Integer> add(int number);

    Optional<Integer> plusAdd(int number);

    void removeLast();

    void clear();

    void setReady();
}
