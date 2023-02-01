package bruzsa.laszlo.dartsapp.model.singlex01;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.time.LocalDate.now;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bruzsa.laszlo.dartsapp.dao.Player;

class SingleX01ViewModelTest {

    static final Player player = new Player(1L, "Joe", "J", now(), null);
    static final int startScore = 501;
    SingleX01ViewModel viewModel = new SingleX01ViewModel();

    @BeforeEach
    void setUp() {
        viewModel.start(player, startScore);
        viewModel.newThrow(180);
        viewModel.newThrow(180);
        viewModel.newThrow(180);
        viewModel.newThrow(141);

        viewModel.newThrow(200);
        viewModel.newThrow(167);
    }

    @Test
    void newThrow() {
        assertEquals(90, viewModel.newThrow(44).getValue());
        assertEquals(501, viewModel.newThrow(90).getValue());
    }

    @Test
    void getScore() {
        assertEquals(134, viewModel.getScore().getValue());
    }

    @Test
    void getThrowsList() {
        assertThat(viewModel.getThrowsList())
                .hasSize(6);
    }
}