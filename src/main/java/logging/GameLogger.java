package logging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameLogger {
    private List<String> logs;

    public GameLogger(){
        this.logs = new ArrayList<>();
    }

    // Instead of writing to a file every single time a player moves (which is slow),
    // I'm storing the logs in a List in memory
    public void logAction(String action){
        logs.add(action);
    }

    public boolean exportLogsToFile(String fileName){
        try(FileWriter writer = new FileWriter(fileName)){
            for(String message : logs){
                writer.write(message + "\n");
            }
            return true;
        } catch (IOException e){
            return false;
        }
    }
}
