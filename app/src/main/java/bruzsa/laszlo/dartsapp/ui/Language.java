package bruzsa.laszlo.dartsapp.ui;

public enum Language {

    HUNGARIAN("hu"),
    ENGLISH("en");

    private final String countryCode;

    Language(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
