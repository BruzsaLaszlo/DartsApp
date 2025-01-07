package bruzsa.laszlo.dartsapp.ui.webgui;

import static java.lang.String.format;
import static java.util.Locale.US;
import static java.util.Optional.of;
import static bruzsa.laszlo.dartsapp.model.x01.X01MatchType.LEG;
import static bruzsa.laszlo.dartsapp.model.x01.X01MatchType.SET;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import bruzsa.laszlo.dartsapp.AppSettings;
import bruzsa.laszlo.dartsapp.HtmlTemplateReader;
import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.enties.x01.X01TeamScores;
import bruzsa.laszlo.dartsapp.model.PlayersEnum;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.x01.CheckoutTable;
import bruzsa.laszlo.dartsapp.model.x01.Stat;
import bruzsa.laszlo.dartsapp.model.x01.X01Service;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
import bruzsa.laszlo.dartsapp.model.x01.X01Throw;
import dagger.hilt.android.scopes.ViewModelScoped;

@ViewModelScoped
public class WebGuiX01 {

    private final TemplateEngine templateEngine = new TemplateEngine();
    private final String htmlTemplate;
    private static final int MAX_THROW = 5;
    private final Map<PlayersEnum, Player> playerMap;
    private final Map<Team, X01TeamScores> teamScoresMap;
    private final X01Settings x01Settings;

    @Inject
    public WebGuiX01(HtmlTemplateReader htmlTemplateReader, AppSettings appSettings, X01Service service) {
        this.htmlTemplate = htmlTemplateReader.getX01Template();
        this.playerMap = appSettings.getSelectedPlayers();
        this.teamScoresMap = service.getTeamScores();
        this.x01Settings = appSettings.getX01Settings();
        templateEngine.setTemplateResolver(new StringTemplateResolver());
    }

    public String createHtml(Map<Team, Stat> statMap, PlayersEnum active) {
        var variables = new HashMap<String, Object>();
        for (PlayersEnum playersEnum : PlayersEnum.values()) {
            if (playerMap.containsKey(playersEnum)) {
                String element = active == playersEnum ? "active" : "inactive";
                String name = Objects.requireNonNull(playerMap.get(playersEnum)).getName();
                String formatted = String.format("<%s> %s </%s>", element, name, element);
                variables.put(playersEnum.toString(), formatted);
            } else {
                variables.put(playersEnum.toString(), "");
            }
        }
        statMap.forEach((team, stat) -> {
            variables.put(team + "_HC", stat.getHighestCheckout().or(() -> of(0)).map(integer -> integer == 0 ? "-" : integer.toString()).get());
            variables.put(team + "_P100", stat.getPlus100());
            variables.put(team + "_P60", stat.getPlus60());
            variables.put(team + "_MIN", stat.getMin() == Integer.MAX_VALUE ? "-" : stat.getMin());
            variables.put(team + "_MAX", stat.getMax() == 0 ? "-" : stat.getMax());
            variables.put(team + "_AVERAGE", Double.isNaN(stat.getAverage()) ? "-" : format(US, "%d", (int) stat.getAverage()));
            variables.put(team + "_SCORE", stat.getScore());
            variables.put(team + "_CHECKOUT", CheckoutTable.getCheckoutFor(stat.getScore()));
        });

        variables.put("TEAM2_SET", "");
        variables.put("TEAM2_LEG", "");
        variables.put("S", "");
        variables.put("L", "");
        teamScoresMap.forEach((team, teamScores) -> {
            int size = teamScores.getThrowsList().size();
            for (int i = 1, n = 0; n < MAX_THROW && size - i >= 0; i++) {
                X01Throw x01Throw = teamScores.getThrowsList().get(size - i);
                if (x01Throw.isNotRemoved()) {
                    variables.put(team + "_LAST_THROW" + i, x01Throw.toString());
                    n++;
                }
            }
            if (x01Settings.getMatchType() == SET) {
                variables.put(team + "_SET", teamScores.getSets());
                variables.put("S", "S");
            } else {
                variables.put(team + "_SET", "");
            }
            if (x01Settings.getMatchType() != LEG || x01Settings.getCount() != 1) {
                variables.put(team + "_LEG", teamScores.getLegs());
                variables.put("L", "L");
            } else {
                variables.put(team + "_LEG", "");
            }
        });

        Context ct = new Context();
        ct.setVariables(variables);
        return templateEngine.process(htmlTemplate, ct);
    }

}
