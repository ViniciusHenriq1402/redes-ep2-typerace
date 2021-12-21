package br.usp.each.typerace.server;

import java.util.List;


public class Player implements Comparable<Player> {

    private final Integer id;
    private int correct;
    private int wrong;
    public List<String> wordsRemaining;


    public Player (Integer id, List<String> words){
        this.id = id;
        this.correct = 0;
        this.wrong = 0;
        this.wordsRemaining = words;
    }

    public Integer getId(){
        return this.id;
    }

    public int getCorrect(){
        return this.correct;
    }

    public int getWrong(){
        return this.wrong;
    }

    public boolean wordTyped(String word){

        if (wordsRemaining.contains(word)){
            wordsRemaining.remove(word);
            this.correct++;
        }
        else {
            this.wrong++;
        }

        return playerStatus();
    }

    // status do jogador
    public boolean playerStatus(){

        if (wordsRemaining.isEmpty()) return true;
        return false;
    }

    @Override
	public int compareTo(Player pl) {
		return Integer.compare(this.getCorrect(), pl.getCorrect());
	}

}