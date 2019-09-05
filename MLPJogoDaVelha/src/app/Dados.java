package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Dados {

    private HashMap<String, JogoInfo> jogos;

    public void simularJogos(int numJogos) {

        jogos = new HashMap<String, JogoInfo>();

        while (numJogos > 0) {

            int estadoInicial[] = new int[9];
            JogoInfo info = simular(estadoInicial, Jogo.JOGADOR_X);

            if (info != null) {
                if (!jogos.containsKey(info.chave)) {
                    jogos.put(info.chave, info);
                    numJogos--;
                }
            }

        }

        System.out.println("Num jogos "+jogos.size());
        for (JogoInfo jogo : jogos.values()) {
            System.out.println("Saída = ");
            System.out.println(Arrays.toString(jogo.saida));
            for(int[] entrada : jogo.estados) {
                System.out.println(Arrays.toString(entrada));
            }
        }

    }

    private JogoInfo simular(int estado[], int jogador) {

        int ganhador = Jogo.ganhador(estado);

        if (ganhador == Jogo.VELHA) {
            return null;
        }

        if (ganhador == Jogo.LIVRE) {

            ArrayList<Integer> livres = Jogo.posicoesLivres(estado);

            Random r = new Random();
            int indice = r.nextInt(livres.size());

            int proxEstado[] = Jogo.realizarjoada(estado, livres.get(indice), jogador);

            JogoInfo info = simular(proxEstado, (-1)*jogador);

            if (info != null) {
                int entrada[] = Jogo.espelharEstado(estado, info.ganhador);
                info.chave = info.chave + Arrays.toString(entrada);
                info.estados.add(estado);
                return info;

            } else {
                return null;
            }

        } else {

            int saida[] = Jogo.getSaida(estado, ganhador);   
            int entrada[] = Jogo.espelharEstado(estado, ganhador);         

            JogoInfo info = new JogoInfo();

            info.estados.add(entrada);
            info.saida = saida;
            info.chave = Arrays.toString(entrada);
            info.ganhador = ganhador;

            return info;

        }

    }

    public class JogoInfo {

        ArrayList<int[]> estados;

        int saida[];

        String chave;

        int ganhador;

        JogoInfo() {
            estados = new ArrayList<int[]>();
        }

    }
    
    public static void main(String h[]) {
        Dados dados = new Dados();

        dados.simularJogos(1);

    }

}