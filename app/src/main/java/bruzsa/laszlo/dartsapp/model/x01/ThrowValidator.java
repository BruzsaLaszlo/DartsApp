package bruzsa.laszlo.dartsapp.model.x01;

import java.util.Set;

public class ThrowValidator {

    public static boolean isValidCheckout(int number) {
        return number > 1 && (number <= 158 || CheckoutTable.getCheckoutMap().containsKey(number));
    }

    public static boolean isValidThrow(int number) {
        return number >= 0 &&
                number <= 180 &&
                !Set.of(179, 178, 176, 175, 173, 172, 169, 166, 163).contains(number);
    }

}
