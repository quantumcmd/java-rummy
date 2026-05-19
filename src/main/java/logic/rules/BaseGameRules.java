package logic.rules;

import logic.PersistenceManager;
import logic.TurnManager;
import model.GamePiece;
import model.GameState;
import model.Player;
import ui.InGameUI;

public abstract class BaseGameRules implements GameRules{

    @Override
    public int calculatePoints(GameState state, Player winner) {
        int pointsWon = 0;
        for (Player player : state.getPlayers()) {
            if (!player.equals(winner)) {
                for (GamePiece piece : player.getHand()) {
                    pointsWon += piece.getNumericalValue();
                }
            }
        }
        return pointsWon;
    }

    protected void handleSave(GameState state, InGameUI ui, TurnManager turnManager) {
        String saveName = ui.promptSaveFileName();
        boolean saved = PersistenceManager.saveGame(state, saveName);
        if (saved) {
            ui.printMessage("Game saved successfully to " + saveName);
        } else {
            ui.printError("Error saving game!");
        }
        turnManager.getLogger().exportLogsToFile("match_log.txt");
    }
}
