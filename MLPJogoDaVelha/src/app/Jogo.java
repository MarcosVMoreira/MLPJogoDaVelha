package app;

import java.util.ArrayList;
import java.util.Arrays;

public class Jogo {

    public static final int JOGADOR_X = 1;
    public static final int JOGADOR_O = -1;
    public static final int LIVRE = 0;
    public static final int VELHA = 2;

    public static int[] realizarjoada(int estado[], int pos, int jogador) {

        // copiando o valor do estado atual pra um novo estado
        int novoEstado[] = Arrays.copyOfRange(estado, 0, estado.length);

        // adicionando a jogada do jogador no novoEstado
        novoEstado[pos] = jogador;

        return novoEstado;
    }

    public static int ganhador(int estado[]) {

        // verifica linhas
        if (estado[0] == estado[1] && estado[1] == estado[2] && estado[0] != LIVRE) {
            return estado[0];
        }

        if (estado[3] == estado[4] && estado[4] == estado[5] && estado[3] != LIVRE) {
            return estado[3];
        }

        if (estado[6] == estado[7] && estado[7] == estado[8] && estado[6] != LIVRE) {
            return estado[6];
        }

        // verifica colunas
        if (estado[0] == estado[3] && estado[3] == estado[6] && estado[0] != LIVRE) {
            return estado[0];
        }

        if (estado[1] == estado[4] && estado[4] == estado[7] && estado[1] != LIVRE) {
            return estado[1];
        }

        if (estado[2] == estado[5] && estado[5] == estado[8] && estado[2] != LIVRE) {
            return estado[2];
        }

        // verifica diagonais
        if (estado[0] == estado[4] && estado[4] == estado[8] && estado[0] != LIVRE) {
            return estado[0];
        }

        if (estado[2] == estado[4] && estado[4] == estado[6] && estado[2] != LIVRE) {
            return estado[2];
        }

        for (int i = 0; i < estado.length; ++i) {
            if (estado[i] == LIVRE) {
                return LIVRE;
            }
        }

        return VELHA;
    }

    public static ArrayList<Integer> posicoesLivres(int estado[]) {

        ArrayList<Integer> livres = new ArrayList<>();

        for (int i = 0; i < estado.length; i++) {
            if (estado[i] == LIVRE) {
                livres.add(i);
            }
        }

        return livres;
    }

    public static int[] getSaida(int estado[], int ganhador) {
        
        int saida[] = new int[9];
        
        for (int i = 0; i < estado.length; ++i) {
            if (estado[i] == ganhador) {
                saida[i] = 1;
            }
        }

        return saida;
    }

    public static int[] espelharEstado(int estado[], int ganhador) {
        if (ganhador == JOGADOR_O) {
            for(int i = 0; i < estado.length; ++i) {
                if (estado[i] == JOGADOR_O) {
                    estado[i] = JOGADOR_X;
                } else if (estado[i] == JOGADOR_X) {
                    estado[i] = JOGADOR_O;
                }
            }
        }
        return estado;
    }

}