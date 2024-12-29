package bruzsa.laszlo.dartsapp.model.cricket;

import android.graphics.Color;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum State {

    OPEN(Color.BLUE),
    CAN_SCORE(Color.GREEN),
    OPPONENT_CAN_SCORE(Color.RED),
    CLOSE(Color.BLACK);

    private final int color;

    public static State getState(Integer multiply, Integer multiplyOpponent) {
        if (multiply == null || multiply < 3) {
            if (multiplyOpponent == null || multiplyOpponent < 3) {
                return OPEN;
            } else {
                return OPPONENT_CAN_SCORE;
            }
        } else if (multiplyOpponent == null || multiplyOpponent < 3) {
            return CAN_SCORE;
        } else {
            return CLOSE;
        }
    }

}
