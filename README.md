# redes-ep2-typerace
Repositório para o EP2 de Redes de Computadores, EACH-USP - 2021/2
<br><br>

## Integrantes
* Maraiza Adami Pereira - 6836201
* Matheus Pecoraro de Carvalho Santos - 11917271
* Sungwon Yoon - 9822261
* Vinicius Almeida - 10816815
<br>


## Pré-requisitos
* JDK 11 ou maior (testado com a JDK11 OpenJDK)
* Gradle (incluso no repositório, não é necessário instalá-lo)
<br>

## Rodando
Para rodar o servidor
```sh
./gradlew server:run
```
ou
```sh
./gradlew server:run --console=plain
```

<br><br>

Para rodar um cliente
```sh
./gradlew client:run
```
ou
```sh
./gradlew client:run --console=plain
```

<br>

## Instruções do jogo

* Regras do Jogo

  *	Envie uma palavra por vez <br>
  *	Não há ordem específica para enviar palavras <br>
  *	Palavras podem ser maiúsculas ou minúsculas <br>
  *	Palavras erradas não tiram ponto <br>
  *	O jogo termina quando um jogador digitar todas as palavras corretamente <br>
  *	Jogador com mais palavras corretamente digitadas vence o jogo <br><br>
  
  
* Comandos

  *	Digite "start" para começar<br>
  *	Digite "sair" para desconectar<br>
