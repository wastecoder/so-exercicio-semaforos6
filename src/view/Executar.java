package view;

import controller.ThreadPrato;

import java.util.concurrent.Semaphore;

public class Executar {
    public static void main(String[] args) {
        final int COZIMENTO_SIMULTANEOS = 5;
        final int ENTREGAS_POR_VEZ = 1;
        Semaphore semaforoCozimento = new Semaphore(COZIMENTO_SIMULTANEOS);
        Semaphore semaforoEntrega = new Semaphore(ENTREGAS_POR_VEZ);

        ThreadPrato[] pratos = new ThreadPrato[10];
        for (int i = 0; i < pratos.length; i++) {
            pratos[i] = new ThreadPrato(i, semaforoCozimento, semaforoEntrega);
            pratos[i].start();
        }
    }
}
