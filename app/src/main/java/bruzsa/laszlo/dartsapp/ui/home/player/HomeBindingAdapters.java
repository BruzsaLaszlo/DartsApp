package bruzsa.laszlo.dartsapp.ui.home.player;

import androidx.databinding.BindingAdapter;

import com.google.android.material.chip.Chip;

import java.util.Map;

import bruzsa.laszlo.dartsapp.enties.Player;
import bruzsa.laszlo.dartsapp.model.SharedViewModel.Settings;
import bruzsa.laszlo.dartsapp.model.TeamPlayer;
import bruzsa.laszlo.dartsapp.model.home.GameMode;
import bruzsa.laszlo.dartsapp.model.home.GameType;

public final class HomeBindingAdapters {

    private HomeBindingAdapters() {
    }

    @BindingAdapter({"mode", "settings"})
    public static void setOnClickListener(Chip chip, GameMode mode, Settings settings) {
        if (settings.getGameMode() == mode && !chip.isChecked()) {
            chip.setChecked(true);
        }
        chip.setOnClickListener(v -> {
            if (chip.isChecked()) settings.setGameMode(mode);
        });
    }

    @BindingAdapter({"game_type", "settings"})
    public static void setGameType(Chip chip, GameType type, Settings settings) {
        if (settings.getGameType() == type && !chip.isChecked()) {
            chip.setChecked(true);
        }
        chip.setOnClickListener(v -> {
            if (chip.isChecked()) settings.setGameType(type);
        });
    }

    @BindingAdapter({"selected_players", "team_player"})
    public static void setPlayerChip(Chip chip, Map<TeamPlayer, Player> selectedPlayers, TeamPlayer teamPlayer) {
        chip.setOnCloseIconClickListener(v -> {
            selectedPlayers.remove(teamPlayer);
            setChipStyle(chip, true, null);
        });
        Player player = selectedPlayers.get(teamPlayer);
        if (player != null) {
            setChipStyle(chip, false, player.getName());
        }
    }

    private static void setChipStyle(Chip chip, boolean empty, String text) {
        chip.setChipIconVisible(empty);
        chip.setCloseIconVisible(!empty);
        chip.setText(empty ? "add player" : text);
    }

}
