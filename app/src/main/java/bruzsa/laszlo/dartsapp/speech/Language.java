package bruzsa.laszlo.dartsapp.speech;

import androidx.annotation.NonNull;

import java.util.Locale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Language {

    HUNGARIAN("hu"),
    ENGLISH(Locale.US.getDisplayLanguage());


    private final String countryCode;

    @NonNull
    @Override
    public String toString() {
        return this.name();
    }
}
