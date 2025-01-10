package bruzsa.laszlo.dartsapp.ui.x01.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Optional;

class InputValidatorTest {


    InputValidator validator = new InputValidator();

    @Test
    void validateInput() {
        assertNull(validator.validateInput(null, null));
        assertEquals("", validator.validateInput("", null));
        assertNull(validator.validateInput(null, ""));
        assertNull(validator.validateInput(null, "+"));
        assertEquals("", validator.validateInput("", "+"));
        assertEquals("6", validator.validateInput("", "+6"));
        assertEquals("66", validator.validateInput("", "66"));
        assertEquals("", validator.validateInput("", "666"));
        assertEquals("6+9", validator.validateInput("6", "+9"));
        assertEquals("25", validator.validateInput("2", "5"));
    }

    @Test
    void getValidNumber() {
        assertEquals(100, validator.getValidNumber("100+"));
        assertEquals(123, validator.getValidNumber("100+23"));
        assertEquals(123, validator.getValidNumber("100+23+"));
        assertEquals(126, validator.getValidNumber("100+23+3"));
    }

    @Test
    void isValidScore() {
        assertFalse(validator.isValidScore(null));
        assertFalse(validator.isValidScore(""));
        assertFalse(validator.isValidScore("+"));
        assertFalse(validator.isValidScore("181"));
        assertFalse(validator.isValidScore("179"));
        assertFalse(validator.isValidScore("66++"));
        assertFalse(validator.isValidScore("100+56+19"));
        assertTrue(validator.isValidScore("5"));
        assertTrue(validator.isValidScore("55"));
        assertTrue(validator.isValidScore("5+"));
        assertTrue(validator.isValidScore("5+5"));
        assertTrue(validator.isValidScore("66+66+6"));
        assertTrue(validator.isValidScore("66+66+"));
    }

    @Test
    void getValidThrow() {
        assertEquals(Optional.empty(), validator.getValidThrow(null));
        assertEquals(Optional.empty(), validator.getValidThrow(""));
        assertEquals(Optional.empty(), validator.getValidThrow("+"));
        assertEquals(Optional.empty(), validator.getValidThrow("181"));
        assertEquals(Optional.empty(), validator.getValidThrow("179"));
        assertEquals(Optional.empty(), validator.getValidThrow("66++"));
        assertEquals(Optional.empty(), validator.getValidThrow("100+56+19"));
        assertEquals(Optional.of(5), validator.getValidThrow("5"));
        assertEquals(Optional.of(55), validator.getValidThrow("55"));
        assertEquals(Optional.of(5), validator.getValidThrow("5+"));
        assertEquals(Optional.of(10), validator.getValidThrow("5+5"));
        assertEquals(Optional.of(138), validator.getValidThrow("66+66+6"));
        assertEquals(Optional.of(132), validator.getValidThrow("66+66+"));
    }

}