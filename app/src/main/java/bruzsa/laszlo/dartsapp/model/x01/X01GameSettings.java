package bruzsa.laszlo.dartsapp.model.x01;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class X01GameSettings {

    private int firstTo = 1;
    private int startScore = 501;
    private X01MatchType x01MatchType = X01MatchType.SINGLE_LEG;

    public static X01GameSettings getDefault() {
        return new X01GameSettings();
    }

}
