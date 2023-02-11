package bruzsa.laszlo.dartsapp.ui.cricket;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;

import java.util.List;

import bruzsa.laszlo.dartsapp.model.Team;
import bruzsa.laszlo.dartsapp.model.cricket.CricketTeam;

@SuppressWarnings("ConstantConditions")
public class CricketWebGui {

    private final String htmlTemplate;

    public CricketWebGui(String htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }

    public String getHTML(List<CricketTeam> cricketTeams) {
        String html = getStat(htmlTemplate, Team.TEAM1, cricketTeams.get(0));
        if (cricketTeams.size() == 2)
            html = getStat(html, Team.TEAM2, cricketTeams.get(1));
        return html;
    }

    @SuppressLint("DefaultLocale")
    private String getStat(String htmlTemplate, Team team, CricketTeam cricketTeam) {
        String output = htmlTemplate
                .replaceAll(team.name() + "_PLAYER_NAME", cricketTeam.toString())
                .replaceAll(team.name() + "_SCORE", valueOf(cricketTeam.getPoints()));
        for (Integer number : CricketTable.activeNumbers) {
            int count = cricketTeam.getScores().getOrDefault(number, 0);
            String pattern = String.format("%s_%d", team.name(), number);
            output = output.replaceAll(pattern, getMark(count));
        }
        return output;
    }

    private String getMark(final int count) {
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
