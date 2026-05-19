package logic;

import model.GameState;

import java.io.*;

public class PersistenceManager {

    public static boolean saveGame(GameState state, String fileName){
        try{
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(state);

            out.close();
            fileOut.close();
            return true;
        } catch(IOException e){
            return false;
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
            return null;
        }
    }
}
