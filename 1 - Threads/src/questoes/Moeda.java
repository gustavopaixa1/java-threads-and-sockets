package questoes;

public class Moeda extends Thread {
    static String face;

    public void run() { face = "coroa";}

    public static void main(String[] args) throws InterruptedException {
        face = "cara";
        Moeda t1 = new Moeda();
        t1.start(); t1.join();
        System.out.println(face);
    }

}


// Cara nunca será mostrada na tela
// Coroa sempre será mostrada na tela
// O métdo join garante que a thread altere o valor estático de face para em seguida imprimir
