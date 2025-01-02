package bruzsa.laszlo.dartsapp.model.singlex01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.time.LocalDate.now;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.ui.singlex01.SingleX01ViewModel;

@RunWith(MockitoJUnitRunner.class)
class SingleX01ViewModelTest {

    static final Player player = new Player(1L, "Joe", "J", now(), null);
    static final int startScore = 501;

    SingleX01ViewModel viewModel = new SingleX01ViewModel();

    @BeforeEach
    void setUp() {
        viewModel.startOrCountinue(player, startScore, "");
        viewModel.newThrow(180, 3);
        viewModel.newThrow(180, 3);
        viewModel.newThrow(180, 3);
        viewModel.newThrow(141, 3);

        viewModel.newThrow(200, 3);
        viewModel.newThrow(167, 3);
    }

    @Test
    void newThrow() {
        viewModel.newThrow(44, 3);
        assertEquals(90, viewModel.getScore().getValue());

        viewModel.newThrow(90, 3);
        assertEquals(501, viewModel.getScore().getValue());
    }

    @Test
    void getScore() {
        assertEquals(134, viewModel.getScore().getValue());
    }

}