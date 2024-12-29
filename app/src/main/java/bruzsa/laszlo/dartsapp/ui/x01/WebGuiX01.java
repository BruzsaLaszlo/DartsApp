package bruzsa.laszlo.dartsapp.ui.x01;

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

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.model.x01.Stat;
import bruzsa.laszlo.dartsapp.model.x01.X01Settings;
import bruzsa.laszlo.dartsapp.model.x01.X01TeamScores;

public class WebGuiX01 {

    private final TemplateEngine templateEngine = new TemplateEngine();
    private final String htmlTemplate;
    private static final int MAX_THROW = 6;
    private final Map<TeamPlayer, Player> playerMap;
    private final Map<Team, X01TeamScores> teamScoresMap;
    private final X01Settings x01Settings;

    public WebGuiX01(String htmlTemplate, Map<TeamPlayer, Player> playerMap, Map<Team, X01TeamScores> teamScoresMap, X01Settings x01Settings) {
        this.htmlTemplate = htmlTemplate;
        this.playerMap = playerMap;
        this.teamScoresMap = teamScoresMap;
        this.x01Settings = x01Settings;
        templateEngine.setTemplateResolver(new StringTemplateResolver());
    }

    public String createHtml(Map<Team, Stat> statMap, TeamPlayer active) {
        var variables = new HashMap<String, Object>();
        playerMap.forEach((teamPlayer, player) -> {
            String element = active == teamPlayer ? "playerActive" : "playerInactive";
            String formatted = "<%s> %s </%s>".formatted(element, player.getName(), element);
            variables.put(teamPlayer.toString(), formatted);
        });
        statMap.forEach((team, stat) -> {
            variables.put(team + "_HC", stat.getHighestCheckout().or(() -> of(0)).map(integer -> integer == 0 ? "-" : integer.toString()).get());
            variables.put(team + "_P100", stat.getPlus100());
            variables.put(team + "_P60", stat.getPlus60());
            variables.put(team + "_MIN", stat.getMin() == Integer.MAX_VALUE ? "-" : stat.getMin());
            variables.put(team + "_MAX", stat.getMax() == 0 ? "-" : stat.getMax());
            variables.put(team + "_AVERAGE", stat.getAverage() == 0f ? "-" : format(US, "%.0f", stat.getAverage()));
            variables.put(team + "_SCORE", stat.getScore());
        });
        teamScoresMap.forEach((team, teamScores) -> {
            int size = teamScores.getThrowsList().size();
            for (int i = 1; i <= MAX_THROW; i++) {
                String s = "";
                if (size - i >= 0)
                    s = teamScores.getThrowsList().get(size - i).toString();
                variables.put(team + "_T" + i, s);
            }
            if (x01Settings.getMatchType() == SET) {
                variables.put(team + "_SET", teamScores.getSets());
                variables.put("S", "S");
            } else {
                variables.put(team + "_SET", "");
                variables.put("S", "");
            }
            if (x01Settings.getMatchType() != LEG || x01Settings.getCount() != 1) {
                variables.put(team + "_LEG", teamScores.getLegs());
                variables.put("L", "L");
            } else {
                variables.put("L", "");
                variables.put(team + "_LEG", "");
            }
        });

        Context ct = new Context();
        ct.setVariables(variables);
        return templateEngine.process(htmlTemplate, ct);
    }

}
