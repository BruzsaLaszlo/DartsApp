package bruzsa.laszlo.dartsapp.model.x01;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ThrowValidatorTest {

    @Test
    void isValidCheckout() {
        assertTrue(ThrowValidator.isValidCheckout(2));
        assertTrue(ThrowValidator.isValidCheckout(158));
        assertTrue(ThrowValidator.isValidCheckout(170));
        assertTrue(ThrowValidator.isValidCheckout(167));
        assertTrue(ThrowValidator.isValidCheckout(161));
        assertTrue(ThrowValidator.isValidCheckout(164));

        assertFalse(ThrowValidator.isValidCheckout(159));
        assertFalse(ThrowValidator.isValidCheckout(1));
        assertFalse(ThrowValidator.isValidCheckout(180));
        assertFalse(ThrowValidator.isValidCheckout(177));
        assertFalse(ThrowValidator.isValidCheckout(0));
    }

    @Test
    void isValidThrow() {
        assertTrue(ThrowValidator.isValidThrow(0));
        assertTrue(ThrowValidator.isValidThrow(1));
        assertTrue(ThrowValidator.isValidThrow(180));
        assertTrue(ThrowValidator.isValidThrow(159));
        assertTrue(ThrowValidator.isValidThrow(177));

        assertFalse(ThrowValidator.isValidThrow(163));
        assertFalse(ThrowValidator.isValidThrow(-1));
        assertFalse(ThrowValidator.isValidThrow(181));
        assertFalse(ThrowValidator.isValidThrow(169));

    }
}