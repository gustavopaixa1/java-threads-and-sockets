package Monitores.LeitorEscritor;


public class LeitorEscritorEquidade {
    static class SharedDB {
        private final int[] nr = new int[2]; // leitores por lote
        private int nw = 0;                  // escritor ativo
        private int nwtotal = 0;             // total de escritores esperando
        private int thisBatch = 0;           // lote atual
        private int nextBatch = 1;           // lote seguinte
        private String dataBase;

        public SharedDB(String db) {
            dataBase = db;
        }

        public void write(String v, int id) {
            synchronized (this) {
                nwtotal++; // entra na fila de escritores
                while (nr[thisBatch] + nw != 0) {
                    try {
                        System.out.println("⏳ Escritor " + id + " esperando (leitores ou escritor ativo)");
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                nw = 1;
                nwtotal--;
            }

            // simula escrita
            System.out.println("✍️ Escritor " + id + " escrevendo: " + v);
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            synchronized (this) {
                dataBase = v;
                nw = 0;
                // troca o lote após escrita
                int tmp = thisBatch;
                thisBatch = nextBatch;
                nextBatch = tmp;
                notifyAll(); // acorda leitores e escritores
            }
        }

        public String read(int id) {
            String v;
            synchronized (this) {
                if (nwtotal == 0) {
                    nr[thisBatch]++;
                } else {
                    nr[nextBatch]++;
                    int myBatch = nextBatch;
                    while (thisBatch != myBatch) {
                        try {
                            System.out.println("⏳ Leitor " + id + " aguardando seu lote");
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            // leitura fora do monitor
            System.out.println("📖 Leitor " + id + " lendo: " + dataBase);
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            v = dataBase;

            synchronized (this) {
                nr[thisBatch]--;
                if (nr[thisBatch] == 0) {
                    notifyAll(); // acorda escritores aguardando o fim do lote
                }
            }

            return v;
        }
    }

    static class Leitor extends Thread {
        private final SharedDB db;
        private final int id;

        public Leitor(SharedDB db, int id) {
            this.db = db;
            this.id = id;
        }

        public void run() {
            while (true) {
                db.read(id);
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
            }
        }
    }

    static class Escritor extends Thread {
        private final SharedDB db;
        private final String valor;
        private final int id;

        public Escritor(SharedDB db, String valor, int id) {
            this.db = db;
            this.valor = valor;
            this.id = id;
        }

        public void run() {
            while (true) {
                db.write(valor + " - " + System.currentTimeMillis(), id);
                try { Thread.sleep(2500); } catch (InterruptedException e) {}
            }
        }
    }

    public static void main(String[] args) {
        SharedDB db = new SharedDB("inicial");

        // Cria leitores
        for (int i = 0; i < 3; i++) {
            new Leitor(db, i).start();
        }

        // Cria escritores
        for (int i = 0; i < 2; i++) {
            new Escritor(db, "Valor do escritor " + i, i).start();
        }
    }
}
