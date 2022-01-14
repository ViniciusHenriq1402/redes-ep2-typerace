package br.usp.each.typerace.server;

import java.util.ArrayList;
import java.util.List;


public class Player implements Comparable<Player> {

    private final Integer id;
    private int correct;
    private int wrong;
    public ArrayList<String> wordsRemaining = new ArrayList<>();


    public Player (Integer id, List<String> words){
        this.id = id;
        this.correct = 0;
        this.wrong = 0;
        copyWords(words);
    }

    // Método que retorna id do jogador
    public Integer getId(){
        return this.id;
    }

    // Método que retorna quantidade de acertos do jogador
    public int getCorrect(){
        return this.correct;
    }

    // Método que retorna quantidade de erros do jogador	
    public int getWrong(){
        return this.wrong;
    }

    // Método que define a quantidade de acertos do jogador
    public void setCorrect(int correct){
        this.correct = correct;
    }
    
    // Método que define a quantidade de erros do jogador	
    public void setWrong(int wrong){
        this.wrong = wrong;
    }

    // Método que verifica se a palavra digitada está correta
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

    // Método que limpa wordsRemaining e copia novas palavras
    public void copyWords(List<String> words) {
        if(wordsRemaining.size() != 0)
            wordsRemaining.clear();
        wordsRemaining.addAll(words);
    }

    // Método que retorna se ainda há palavras a serem digitadas
    public boolean playerStatus(){

        if (wordsRemaining.isEmpty()) return true;
        return false;
    }

    // Para ordenar os jogadores por quantidade decrescente de acertos
    @Override
	public int compareTo(Player pl) {
		return Integer.compare(this.getCorrect(), pl.getCorrect());
	}

}
