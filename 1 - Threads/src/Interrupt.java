public class Interrupt {

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Thread foi interrompida! Encerrando...");
                    break;
                }

                System.out.println("Executando tarefa...");
                try {
                    Thread.sleep(1000);

                }
                // Capturar InterruptedException desliga a flag de interrupção
                catch (InterruptedException e) {
                    System.out.println("Interrompido durante o sleep!");

                    // Testa para ver se a flag de interrupção está ativa
                    System.out.println(Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupt();
                    // reinterrompe após o catch
                    System.out.println(Thread.currentThread().isInterrupted());

                }
            }
        });

        worker.start();

        // O sleep lança um interrupted exception
        Thread.sleep(3000); // espera 3 segundos
        System.out.println("Interrompendo a thread...");
        worker.interrupt(); // envia sinal de interrupção
    }
}

// Thread.interrupted() desliga a flag de interrupção, verifica se o próprio thread foi interrompido
// Thread.interrupt() ativa a flag de interrupção
// Thread.isInterrupted() retorna true se a thread foi interrompida