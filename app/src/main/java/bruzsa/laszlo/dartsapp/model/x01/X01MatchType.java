package bruzsa.laszlo.dartsapp.model.x01;

public enum X01MatchType {

    LEG,
    SET;

    public X01MatchType opposit() {
        return this == LEG ? SET : LEG;
    }

}
