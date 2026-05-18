package logic;

import model.GameState;

import java.io.*;

public class PersistenceManager {

    public static void saveGame(GameState state, String fileName){
        try{
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(state);

            out.close();
            fileOut.close();
            System.out.println("Game saved successfully to " + fileName);
        } catch(IOException e){
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public static GameState loadGame(String fileName){
        try{
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            GameState state = (GameState) in.readObject();

            fileIn.close();
            in.close();
            return state;
        } catch(IOException | ClassNotFoundException e){
            System.out.println("Something went wrong: " + e.getMessage());
            return null;
        }
    }
}
