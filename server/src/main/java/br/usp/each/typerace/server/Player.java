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
