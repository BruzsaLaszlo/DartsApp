package bruzsa.laszlo.dartsapp.model.x01;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FirstToBestOf {

    FIRST_TO("first to"),
    BEST_OF("best of");

    @Getter
    private final String label;

    public FirstToBestOf opposit() {
        return this == FIRST_TO ? BEST_OF : FIRST_TO;
    }
}
