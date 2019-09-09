import java.util.ArrayList;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.neuroph.nnet.MultiLayerPerceptron;

public class JogoVelha extends JFrame {

    // Representa qual o jogador atual em um jogo.
    private int jogadorAtual;

    // Botões para representar cada uma das 9 posições em um jogo da velha.
    private JButton posicao0;
    private JButton posicao1;
    private JButton posicao2;
    private JButton posicao3;
    private JButton posicao4;
    private JButton posicao5;
    private JButton posicao6;
    private JButton posicao7;
    private JButton posicao8;

    // Botões agurpados em um arraylist para facilitar na implementação de eventos.
    private ArrayList<JButton> posicoes;

    private double estado[];

    private MultiLayerPerceptron mlp;

    public JogoVelha() {

        // Por padrão quem começa é o X.
        jogadorAtual = Jogo.JOGADOR_X;

        // Inicializa e posiciona os botões na janela gráfica.
        inicializaPosicoes();

        // Configura eventos sobre os botões.
        setPosicoesEventos();

        // Configura a exibição da janela gráfica
        setJanelaGrafica();

        // Carregar o modelo
        carregarModelo();
    }

    public void carregarModelo() {

        String dirArq = "C:\\Users\\Marcos\\Desktop\\Git\\MLPJogoDaVelha\\modelo";

        try {

            ObjectInputStream modelo = new ObjectInputStream(new FileInputStream(dirArq));

            Double pesos[] = (Double[]) modelo.readObject();

            mlp = new MultiLayerPerceptron(9, 50, 9);

            double pesosDoublePrimitivos[] = new double[pesos.length];

            for(int i = 0; i < pesos.length; ++i) {
                pesosDoublePrimitivos[i] = (double) pesos[i];
            }

            mlp.setWeights(pesosDoublePrimitivos);

            estado = new double[9];

            mlp.setInput(estado);

            mlp.calculate();

            double saida[] = mlp.getOutput();




        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void fazerJogadaIA() {

        mlp.setInput(estado);
        mlp.calculate();

        double saida[] = mlp.getOutput();

        double maior = 0;
        int melhorPosicao = 0;

        for (int i = 0; i < saida.length; ++i) {
            if (maior < saida[i] && estado[i] == Jogo.LIVRE) {
                maior = saida[i];
                melhorPosicao = i;
            }
        }

        efetuaJogada(melhorPosicao, jogadorAtual);

    }

    // Inicializa os botões como posições na interface gráfica.
    private void inicializaPosicoes() {
        
        // Configura um layout 3x3.
        GridLayout layout = new GridLayout(3, 3);
        setLayout(layout);
        
        // Inicializa os botões.
        posicao0 = new JButton("");
        posicao1 = new JButton("");
        posicao2 = new JButton("");
        posicao3 = new JButton("");
        posicao4 = new JButton("");
        posicao5 = new JButton("");
        posicao6 = new JButton("");
        posicao7 = new JButton("");
        posicao8 = new JButton("");
        
        // Adiciona sequencialmente cada botão na janela gráfica conforme o grid 3x3.
        add(posicao0);
        add(posicao1);
        add(posicao2);
        add(posicao3);
        add(posicao4);
        add(posicao5);
        add(posicao6);
        add(posicao7);
        add(posicao8);  
        
        // Adiciona os botões (posições) na lista de posições do jogo da velha.
        posicoes = new ArrayList();
        posicoes.add(posicao0);
        posicoes.add(posicao1);
        posicoes.add(posicao2);
        posicoes.add(posicao3);
        posicoes.add(posicao4);
        posicoes.add(posicao5);
        posicoes.add(posicao6);
        posicoes.add(posicao7);
        posicoes.add(posicao8);
     
    }
    
    // Configura eventos de clique sobre os botões.
    private void setPosicoesEventos() {
        
        // Para cada um dos botões... configurar evento de clique.
        for(int i = 0; i < posicoes.size(); ++i) {
            
            final int posicao = i;
            JButton botao = posicoes.get(i);
            
            // Configre um evento de clique sobre o botão
            botao.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    efetuaJogada(posicao, jogadorAtual);
                    fazerJogadaIA();
                }
            });
        }
    }
    
    // Exibe a janela gráfica que representa o jogo.
    private void setJanelaGrafica() {
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
    }
    
    /**
     * Efetua uma jogada no jogo da velha.
     * @param pos posição a ser assinalada. As posições são sequencias.
     * @param jogador jogador que está fazendo a jogada.
     */
    public void efetuaJogada(int pos, int jogador) {
        if(jogador == Jogo.JOGADOR_X) {
            posicoes.get(pos).setText("X");
            jogadorAtual = Jogo.JOGADOR_O;
        }
        else {
            posicoes.get(pos).setText("O");
            jogadorAtual = Jogo.JOGADOR_X;
        }

        estado[pos] = jogador;
        
        // Valide as mudanças ocorridas na interface gráfica.
        validate();
    }
    
    /**
     * Obtém o estado atual de um jogo da velha.
     * @return um vetor de inteiros que corresponde ao 
     * estado atual de um jogo da velha. Uma posição 
     * está vazia se um elemento do vetor de inteiros é 0.
     */
    public int[] getEstado() {
        int jogo[] = new int[9];
        for(int i = 0; i < posicoes.size(); ++i) {
            if(posicoes.get(i).getText().equals("X")) {
                jogo[i] = Jogo.JOGADOR_X;
            }
            if(posicoes.get(i).getText().equals("O")) {
                jogo[i] = Jogo.JOGADOR_O;
            }
        }
        return jogo;
    }

    public static void main(String[] args) {
        JogoVelha jogo = new JogoVelha();    
    }
    
    
}
