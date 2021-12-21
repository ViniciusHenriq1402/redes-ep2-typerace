package br.usp.each.typerace.server;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Game {

    private long timeStart;
    private long timeFinish;
    private String sentence = "the size and age of the cosmos are beyond ordinary human understanding lost somewhere between immensity eternity is our tiny planetary home";
    private List<String> words;

    public Game(){
        words = new ArrayList<String>(Arrays.asList(this.sentence.split(" ")));
    }

    public List<String> getWords(){
        return words;
    }

    public String getSentence(){
        return sentence;
    }

    public void countTime(){
        timeStart = System.currentTimeMillis();
    }

    public void stopTime(){
        timeFinish = System.currentTimeMillis();
    }

    public double timeElapsedSeconds(){
        return (timeFinish - timeStart) / 1000;
    }

    public List<Player> ranking(Map<Integer, Player> players){

        List<Player> pl = new ArrayList<Player>(players.values());
        Collections.sort(pl, Collections.reverseOrder());

        return pl;
    }


}