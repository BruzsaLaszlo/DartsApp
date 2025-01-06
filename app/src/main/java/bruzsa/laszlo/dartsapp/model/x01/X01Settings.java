package bruzsa.laszlo.dartsapp.model.x01;

import static bruzsa.laszlo.dartsapp.model.x01.FirstToBestOf.BEST_OF;
import static bruzsa.laszlo.dartsapp.model.x01.FirstToBestOf.FIRST_TO;
import static bruzsa.laszlo.dartsapp.model.x01.X01MatchType.LEG;
import static lombok.AccessLevel.NONE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class X01Settings {

    private int startScore = 501;
    private FirstToBestOf firstToBestOf = FIRST_TO;
    @Getter(NONE)
    private int count = 1;
    private X01MatchType matchType = LEG;
    private boolean winByTwoClearLeg = false;

    public int getCount() {
        if (firstToBestOf == BEST_OF) return count / 2;
        return count;
    }

}
