package bruzsa.laszlo.dartsapp.ui.webgui;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.model.cricket.Stat;

public class WebGuiCricket {

    private final TemplateEngine templateEngine = new TemplateEngine();
    private final String htmlTemplate;
    private final Map<TeamPlayer, Player> players;
    private final List<Integer> activeNumbers;

    public WebGuiCricket(String htmlTemplate, Map<TeamPlayer, Player> players, List<Integer> activeNumbers) {
        this.htmlTemplate = htmlTemplate;
        this.players = players;
        this.activeNumbers = activeNumbers;
        templateEngine.setTemplateResolver(new StringTemplateResolver());
    }

    public String createHtml(Stat stat) {
        var variables = new HashMap<String, Object>();

        players.forEach((teamPlayer, player) -> variables.put(teamPlayer.toString(), player.getName()));
        stat.getScores().forEach((team, score) -> variables.put(team.toString() + "_SCORE", score));

        ArrayList<Integer> sorted = new ArrayList<>(activeNumbers);
        sorted.sort(Integer::compareTo);
        for (int i = 0; i < sorted.size(); i++) {
            int key = sorted.get(i);
            variables.put("N" + i, key);
            int finalI = i;
            stat.getStatMap().forEach((team, scores) -> {
                Integer value = scores.get(key);
                variables.put(team + "_" + finalI, getMark(Objects.requireNonNull(value)));
            });
        }

        Context ct = new Context();
        ct.setVariables(variables);
        return templateEngine.process(htmlTemplate, ct);
    }

    private String getMark(Integer count) {
        return switch (count) {
            case 0 -> "<hh><br></hh>";
            case 1 -> "<hh>I<br></hh>";
            case 2 -> "<hh>X<br></hh>";
            default -> """
                    <svg width="80px" height="87px">
                        <circle cx="40" cy="43" r="30" stroke="green" stroke-width="10" fill="white" />
                        Sorry, your browser does not support inline SVG.
                        <text fill="#000000" font-size="60" font-family="sans-serif"
                              x="25%" y="75%">X</text>
                    </svg><br>""";
        };
    }
}
