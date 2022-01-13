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

    // Cria lista de palavras a partir de string sentence, quando o jogo é iniciado
    public Game(){
        words = new ArrayList<String>(Arrays.asList(this.sentence.split(" ")));
    }

    // Método que retorna a lista de palavras a ser digitado
    public List<String> getWords(){
        return words;
    }

    // Método que retorna o string de palavras
    public String getSentence(){
        return sentence;
    }

    // Método que inicia a contagem de tempo
    public void countTime(){
        timeStart = System.currentTimeMillis();
    }

    // Método que finaliza a contagem de tempo
    public void stopTime(){
        timeFinish = System.currentTimeMillis();
    }

    // Método que calcula o tempo decorrido em segundos
    public double timeElapsedSeconds(){
        return (timeFinish - timeStart) / 1000;
    }

    // Método que ordena os jogadores segundo a quantidade de acertos
    public List<Player> ranking(Map<Integer, Player> players){

        List<Player> pl = new ArrayList<Player>(players.values());
        Collections.sort(pl, Collections.reverseOrder());

        return pl;
    }


}
