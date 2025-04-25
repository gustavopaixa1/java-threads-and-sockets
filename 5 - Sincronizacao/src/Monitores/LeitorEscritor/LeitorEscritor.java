package Monitores.LeitorEscritor;

public class LeitorEscritor {

    // Classe que representa o banco de dados compartilhado
    static class SharedDB {
        private int nr = 0; // número de leitores ativos
        private String dataBase;

        public SharedDB(String valorInicial) {
            this.dataBase = valorInicial;
        }

        // Escritor: só pode escrever se não houver leitores ativos
        public void write(String value, int id, int count) {
            synchronized (this) {
                while (nr != 0) {
                    try {
                        System.out.println("⏳ Escritor " + id + " esperando (leitores ativos)");
                        wait(); // espera leitores terminarem
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                // Escrita protegida
                dataBase = value;
                System.out.println("✍️ Escritor " + id + " escreveu: " + value + " (contagem: " + count + ")");

                notify(); // notifica um leitor ou escritor em espera
            }
        }

        // Leitor: pode ler mesmo com outros leitores lendo, mas não com escritores
        public String read(int id) {
            String v;

            // Primeira seção crítica: marca que um leitor entrou
            synchronized (this) {
                nr++;
            }

            // Leitura acontece fora do bloco synchronized (simula leitura)
            v = dataBase;
            System.out.println("📖 Leitor " + id + " leu: " + v);

            // Segunda seção crítica: marca saída do leitor
            synchronized (this) {
                nr--;
                if (nr == 0) {
                    notify(); // avisa escritor se ele estiver esperando
                }
            }

            return v;
        }
    }

    // Thread Leitor
    static class Reader extends Thread {
        private final SharedDB sdb;
        private final int id;

        public Reader(SharedDB s, int id) {
            this.sdb = s;
            this.id = id;
        }

        public void run() {
            while (true) {
                sdb.read(id);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Thread Escritor
    static class Writer extends Thread {
        private final SharedDB sdb;
        private final String value;
        private final int id;
        private int count;

        public Writer(SharedDB s, String value, int id) {
            this.sdb = s;
            this.value = value;
            this.id = id;
            this.count = 0;
        }

        public void run() {
            while (true) {
                sdb.write(value, id, count);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // Programa principal
    public static void main(String[] args) {
        SharedDB banco = new SharedDB("valor_inicial");

        // Inicia leitores
        for (int i = 0; i < 3; i++) {
            new Reader(banco, i).start();
        }

        // Inicia escritores
        for (int i = 0; i < 1; i++) {
            new Writer(banco, "Escrito" + i, i).start();
        }
    }
}
