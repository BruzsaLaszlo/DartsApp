package bruzsa.laszlo.dartsapp.model.x01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CheckoutTableTest {

    @Test
    void getCheckoutFor() {
        assertEquals("T10 D18", CheckoutTable.getCheckoutFor(66));
    }
}