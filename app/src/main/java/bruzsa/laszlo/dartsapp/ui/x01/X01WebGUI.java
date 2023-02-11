package bruzsa.laszlo.dartsapp.ui.x01;

import static java.lang.String.valueOf;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.model.x01.X01SummaryStatistics;

@SuppressWarnings("ConstantConditions")
public class X01WebGUI {

    private final String htmlTemplate;

    public X01WebGUI(String htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }


    public String getHTML(Map<TeamPlayer, Player> playerMap, Map<Team, X01SummaryStatistics> statMap) {
        String html = htmlTemplate;

        if (statMap.size() == 1) {
            html = html
                    .replaceAll("<player2stat>.*</player2stat>", "")
                    .replaceAll("<player2score>.*</player2score>", "")
                    .replaceAll("<player2name>.*</player2name>", "");
        }

        String p1Name = playerMap.get(PLAYER_1_1).getName();
        String p2Name = "";
        if (playerMap.size() > 1)
            p2Name = playerMap.get(PLAYER_2_1).getName();
        if (playerMap.size() == 4) {
            p1Name += "<br>" + playerMap.get(PLAYER_1_2).getName();
            p2Name += "<br>" + playerMap.get(PLAYER_2_2).getName();
        }

        html = getOne(html, TEAM1, p1Name, statMap.get(TEAM1));
        if (playerMap.size() > 1)
            html = getOne(html, TEAM2, p2Name, statMap.get(TEAM2));

        return html;
    }

    private String getOne(String html, Team team, String player, X01SummaryStatistics stat) {
        String which = team == TEAM1 ? "1" : "2";

        Map<String, String> keyValue = Map.of(
                "$PLAYER_NAME", player,
                "$SCORE", valueOf(stat.getScoreLastLeg()),
                "$AVG", valueOf((int) stat.getAverage()),
                "$100+", valueOf(stat.getHundredPlus()),
                "$60+", valueOf(stat.getSixtyPlus()),
                "$MAX", valueOf(stat.getMax()),
                "$MIN", valueOf(stat.getMin()),
                "$HC", stat.getHighestCheckout().map(String::valueOf).orElse("-")
        );

        for (Map.Entry<String, String> entry : keyValue.entrySet()) {
            html = html.replace(which + entry.getKey(), entry.getValue());
        }

        if (stat.getThrowCountExceptHandicap() == 0) {
            html = html
                    .replaceAll("<player$stat>.*</player$stat>".replace("$", which), "");
        }

        return html;
    }

}
