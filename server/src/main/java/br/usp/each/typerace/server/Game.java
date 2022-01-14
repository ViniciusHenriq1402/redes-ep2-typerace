package br.usp.each.typerace.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Game {

    private long timeStart;
    private long timeFinish;
    private String sentence;
    private String backupSentence = "the size and age of the cosmos are beyond ordinary human understanding lost somewhere between immensity eternity is our tiny planetary home";
    private List<String> words;

    // Cria lista de palavras a partir de string sentence, quando o jogo é iniciado
    public Game(){
        words = new ArrayList<String>();
        // Caso haja erro randomizando as palavras use a frase de backup:
        if(!randomizeWords()) {
            words = Arrays.asList(this.backupSentence.split(" "));
            sentence = backupSentence;
        }
    }

    // Método que randomiza a lista de palavras (retorna false caso o API de timeout)
    private Boolean randomizeWords(){
        Random rand = new Random();
        int numberOfWords = rand.nextInt(11) + 20;

        try {
            // Pega i palavras aleatórias do arquivo lista_de_palavras.txt e as adiciona na lista de palavras
            for(int i = 0; i < numberOfWords; i++) {
                File textFile = new File("../lista_de_palavras.txt");
                Scanner fileScanner = new Scanner(textFile);
                int line = rand.nextInt(370103); int n = 0;

                while(n < line) {
                    fileScanner.nextLine();
                    n++;
                }

                words.add(fileScanner.nextLine());
                fileScanner.close();
            }
        } catch (FileNotFoundException e) {
            return false; // Caso não encontre o arquivo retorna false
        }

        // Junta todas as palavras em uma String
        sentence = words.get(0);
        for(int i = 1; i < words.size(); i++) {
            sentence += " " + words.get(i);
        }

        return true;
    }

    // Método que retorna a lista de palavras a ser digitado
    public List<String> getWords(){
        return words;
    }

    // Método que retorna a string de palavras
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
        // Re-randomiza as palavras para o próximo jogo
        if(!randomizeWords()) {
            words = Arrays.asList(this.backupSentence.split(" "));
            sentence = backupSentence;
        }
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
