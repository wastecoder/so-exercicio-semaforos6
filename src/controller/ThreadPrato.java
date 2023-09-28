package controller;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadPrato extends Thread {
    private int id;
    private Semaphore semaforoCozimento;
    private Semaphore semaforoEntrega;
    private String nomeDoPrato;
    private int tempoDePreparo;

    public ThreadPrato(int id, Semaphore semaforoCozimento, Semaphore semaforoEntrega) {
        this.id = id;
        this.semaforoCozimento = semaforoCozimento;
        this.semaforoEntrega = semaforoEntrega;
    }

    @Override
    public void run() {
        tipoDePrato();

        try {
            semaforoCozimento.acquire();
            cozinharPrato();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaforoCozimento.release();

            entregarPrato();
        }
    }

    private void tipoDePrato() {
        if (id % 2 == 0) {
            nomeDoPrato = "Sopa de Cebola";
            tempoDePreparo = gerarTempoAleatorio(500, 800);
        } else {
            nomeDoPrato = "Lasanha a Bolonhesa";
            tempoDePreparo = gerarTempoAleatorio(600, 1200);
        }
    }

    private void cozinharPrato() {
        int tempoPercorrido = 0;
        while (tempoPercorrido < tempoDePreparo) {
            tempoPercorrido += 100;

            if (tempoPercorrido > tempoDePreparo) {
                tempoPercorrido = tempoDePreparo;
            }
            exibirPercentual(tempoPercorrido);
        }
    }

    private void entregarPrato() {
        try {
            semaforoEntrega.acquire();
            System.out.println("#" + id + " [" + nomeDoPrato + "] ficou pronto em " + tempoDePreparo + "ms e sera entregue em 5s");

            int TEMPO_DE_ENTREGA = 500;
            fazerEsperar(TEMPO_DE_ENTREGA);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaforoEntrega.release();
        }
    }

    private int gerarTempoAleatorio(int minimo, int maximo) {
        return ThreadLocalRandom.current().nextInt(minimo, ++maximo);
    }

    private void fazerEsperar(int tempoDeEspera) {
        try {
            sleep(tempoDeEspera);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void exibirPercentual(float tempoPercorrido) {
        float percentual = (tempoPercorrido / tempoDePreparo) * 100;
        System.out.printf("#%d [%s] esta em %d%%\n", id, nomeDoPrato, (int) percentual);

        fazerEsperar(100);
    }
}
