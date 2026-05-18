package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    private List<Meld> melds;

    public Board(){
        melds = new ArrayList<>();
    }

    public void addMeld(Meld newMeld){
        melds.add(newMeld);
    }

    public Meld getMeld(int index){
        return melds.get(index);
    }

    public List<Meld> getAllMelds(){
        return melds;
    }

    public int getMeldCount(){
        return melds.size();
    }

    public void clear(){
        melds.clear();
    }
}
