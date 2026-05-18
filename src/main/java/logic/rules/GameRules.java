package logic.rules;

import logic.TurnManager;
import model.GameState;
import model.Player;

public interface GameRules {
    void setupMatch(GameState state, TurnManager turnManager);
    void playTurn(GameState state, InGameUI ui, TurnManager turnManager);
    void calculatePoints(GameState state, Player winner);
}
