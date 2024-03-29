import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

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

        /*System.out.println("Num jogos " + jogos.size());
        for (JogoInfo jogo : jogos.values()) {
            System.out.println("Saída = ");
            System.out.println(Arrays.toString(jogo.saida));
            for (int[] entrada : jogo.estados) {
                System.out.println(Arrays.toString(entrada));
            }
        }*/

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

            JogoInfo info = simular(proxEstado, (-1) * jogador);

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

        dados.simularJogos(1000);

        MultiLayerPerceptron mlp = new MultiLayerPerceptron(9, 50, 9);

        // MultilayerPerceptron mlp = new MultilayerPerceptron(camadaEntrada,
        // camadaOculta, camadaSaida);

        DataSet conjuntoTreinamento = new DataSet(9, 9);

        HashMap<String, JogoInfo> jogos = dados.getDados();

        for (JogoInfo jogo : jogos.values()) {
            for (int[] estado : jogo.estados) {
                double entrada[] = new double[9];
                double saida[] = new double[9];
                for (int i = 0; i < estado.length; i++) {
                    entrada[i] = (double) estado[i];
                    saida[i] = (double) jogo.saida[i];
                }
                conjuntoTreinamento.add(new DataSetRow(entrada, saida));
            }

        }

        BackPropagation bkp = mlp.getLearningRule();

        bkp.setMaxIterations(100);

        System.out.println("Treinando...");
        mlp.learn(conjuntoTreinamento, bkp);
        System.out.println("Fim do treinamento...");

        Double pesos[] = mlp.getWeights();

        String modeloArq = "C:\\Users\\Marcos\\Desktop\\Git\\MLPJogoDaVelha\\modelo";

        try {
            ObjectOutputStream arq = new ObjectOutputStream(new FileOutputStream(modeloArq));

            arq.writeObject(pesos);

            arq.close();

        } catch (Exception e) {

            e.printStackTrace();

        } 

    }

	private HashMap<String, JogoInfo> getDados() {
		return jogos;
	}

}
