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

    public Board cloneBoard(){
        Board copy = new Board();
        for(Meld m : this.melds){
            copy.addMeld(m.cloneMeld());
        }
        return copy;
    }

    public void restore(Board original){
        this.melds.clear();
        for(Meld m : original.getAllMelds()){
            this.melds.add(m.cloneMeld());
        }
    }

    public void removeEmptyMelds(){
        for(int i = melds.size() - 1; i >= 0; i--){
            if(melds.get(i).getPieces().isEmpty()){
                melds.remove(i);
            }
        }
    }

    @Override
    public String toString() {
        return "Board: " + (melds != null ? melds.toString() : "[]");
    }

}
