package bruzsa.laszlo.dartsapp.ui.singlex01;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.x01.X01SummaryStatistics;

public class SingleX01WebGUI {

    public String getHTML(Player player, X01SummaryStatistics stat) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Page Title</title>
                    <meta http-equiv="refresh" content="1" />
                </head>
                <body>
                                
                <h1 style="font-size:2.25rem;">My h1 heading </h1>
                <h1>$Player1Name</h1>
                <p></p>
                <h2>$Player1Score</h2>
                <p>This is a paragraph.</p>
                <h3>AVG  $AVG</h3>
                <h3>MAX  $MAX</h3>
                <h3>MIN  $MIN</h3>
                <h3>HC   $HC</h3>
                <h3>100+ $100+</h3>
                <h3>60+  $60+</h3>
                </body>
                </html>
                """
                .replace("$Player1Name", player.getName())
                .replace("$Player1Score", Integer.toString(stat.getScoreLastLeg()))
                .replace("$AVG", String.valueOf(stat.getAverage()))
                .replace("$MAX", String.valueOf(stat.getMax()))
                .replace("$MIN", String.valueOf(stat.getMin()))
                .replace("$HC", stat.getHighestCheckout().map(Object::toString).orElse(""))
                .replace("$100+", String.valueOf(stat.getHundredPlus()))
                .replace("$60+", String.valueOf(stat.getSixtyPlus())
                );
    }

}
