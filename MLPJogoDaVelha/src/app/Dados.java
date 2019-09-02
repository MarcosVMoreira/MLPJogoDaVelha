package app;

import java.util.ArrayList;
import java.util.Random;

public class Dados {

    public void simularJogos(int numJogos) {

        while (numJogos > 0) {
            int estadoInicial[] = new int[9];

            simular(estadoInicial, Jogo.JOGADOR_X);

        }

    }

    private void simular(int estado[], int jogador) {

        int ganhador = Jogo.ganhador(estado);

        if (ganhador == Jogo.LIVRE) {

            ArrayList<Integer> livres = Jogo.posicoesLivres(estado);

            Random r = new Random();
            int indice = r.nextInt(livres.size());

            int proxEstado[] = Jogo.realizarjoada(estado, livres.get(indice), ganhador);

            simular(proxEstado, (-1)*jogador);

        } else {

            int saida[] = Jogo.getSaida(estado, ganhador);            

        }

    }

}