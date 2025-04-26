package questoes;

public class qTotalDeSaidas extends Thread {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("A");

        qTotalDeSaidas t1 = new qTotalDeSaidas();
        t1.start();
        System.out.println("B");
        t1.join();
        System.out.println("C");
    }

    public void run(){
        System.out.println("D");
        System.out.println("E");
    }
}

// Qual a quantidade total de saídas?
// A B D E C
// A D E B C
// A D B E C

// O "A" sempre é impresso primeiro e o "C" sempre é impresso por último
// O B pode ou nao ser impresso antes do D ou do E