package edu.up.cs301.hearts;

import java.util.ArrayList;

import android.graphics.Color;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

public class HeartsMainActivity extends GameMainActivity {
	public static final int PORT_NUMBER = 4752;

	@Override
	public GameConfig createDefaultConfig() {
		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

		playerTypes.add(new GamePlayerType("Human Player") {
			public GamePlayer createPlayer(String name) {
				return new HeartsHumanPlayer(name);
			}
		});
		// Create a game configuration class for Hearts
		GameConfig defaultConfig = new GameConfig(playerTypes, 2, 2, "Hearts",
				PORT_NUMBER);

		// Add the default players
		defaultConfig.addPlayer("Human", 0);
		defaultConfig.addPlayer("Fredrick", 0);

		// Set the initial information for the remote player
		defaultConfig.setRemoteData("Guest", "", 0);

		// done!
		return defaultConfig;

	}

	@Override
	public LocalGame createLocalGame() {

		return new HeartsLocalGame();
	}

}
