package bruzsa.laszlo.dartsapp.ui.x01;

import static java.lang.String.valueOf;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM1;
import static bruzsa.laszlo.dartsapp.model.Team.TEAM2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_1_2;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_1;
import static bruzsa.laszlo.dartsapp.model.TeamPlayer.PLAYER_2_2;

import android.util.Log;

import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.model.x01.Stat;
import bruzsa.laszlo.dartsapp.model.x01.X01TeamScores;

@SuppressWarnings("ConstantConditions")
public class X01WebGUI {

    private static final int MAX_THROW = 6;
    private final String htmlTemplate;
    private final Map<TeamPlayer, Player> playerMap;
    private final Map<Team, X01TeamScores> teamScoresMap;

    public X01WebGUI(String htmlTemplate, Map<TeamPlayer, Player> playerMap, Map<Team, X01TeamScores> teamScoresMap) {
        this.htmlTemplate = htmlTemplate;
        this.playerMap = playerMap;
        this.teamScoresMap = teamScoresMap;
    }


    public String getHTML(Map<Team, Stat> statMap) {
        String html = htmlTemplate;

        if (statMap.size() < 2) {
            html = html
                    .replaceAll("<p2>.*</p2>", "")
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

    private String getOne(String html, Team team, String player, Stat stat) {
        String which = team == TEAM1 ? "1" : "2";

        if (stat.isEmpty()) {
            html = html.replaceAll("<p$>[\\s\\S]*?</p$>".replace("$", which), "");
            Log.d("X01WebGUI", "getOne: " + html);
        }

        Map<String, String> keyValue = Map.of(
                "$PLAYER_NAME", player,
                "$SCORE", valueOf(stat.getScore()),
                "$AVG", valueOf((int) stat.getAverage()),
                "$100+", valueOf(stat.getPlus100()),
                "$60+", valueOf(stat.getPlus100()),
                "$MAX", valueOf(stat.getMax()),
                "$MIN", valueOf(stat.getMin()),
                "$HC", stat.getHighestCheckout().map(String::valueOf).orElse("-")
        );

        for (Map.Entry<String, String> entry : keyValue.entrySet()) {
            html = html.replace(which + entry.getKey(), entry.getValue());
        }

        int size = teamScoresMap.get(team).getThrowsList().size();
        for (int i = 1; i <= MAX_THROW; i++) {
            String s = "";
            if (size - i >= 0)
                s = teamScoresMap.get(team).getThrowsList().get(size - i).toString();
            html = html.replace(which + "$L" + i, s);
        }

        return html;
    }

}
