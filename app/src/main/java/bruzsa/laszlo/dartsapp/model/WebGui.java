package bruzsa.laszlo.dartsapp.model;

import java.util.Map;

import bruzsa.laszlo.dartsapp.dao.Player;
import bruzsa.laszlo.dartsapp.model.x01.X01SummaryStatistics;

public interface WebGui {

    String getHTML(Map<TeamPlayer, Player> playerMap, Map<Team, X01SummaryStatistics> statMap);

}
