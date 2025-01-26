package bruzsa.laszlo.dartsapp.model.x01;

import java.util.Set;

public class ThrowValidator {

    public static boolean isValidCheckout(int score) {
        return score > 1 && score < 159 || Set.of(160, 161, 164, 167, 170).contains(score);
    }

    public static boolean isValidThrow(int number) {
        return number >= 0 &&
                number <= 180 &&
                !Set.of(179, 178, 176, 175, 173, 172, 169, 166, 163).contains(number);
    }

}
