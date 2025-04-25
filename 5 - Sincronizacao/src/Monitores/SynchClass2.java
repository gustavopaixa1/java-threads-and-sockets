package Monitores;

public class SynchClass2 {

    // Classe com 3 tipos de blocos sincronizados
    public static class SynchClass {
        private final Object lock1 = new Object(); // monitor separado 1
        private final Object lock2 = new Object(); // monitor separado 2

        // Usa o monitor do próprio objeto (this)
        public void synchThisMethod(int id) {
            synchronized (this) {
                System.out.println("🔒 [this] Thread " + id + " está executando synchThisMethod");
                sleep();
            }
        }

        // Usa o monitor do objeto lock1 (independente de this)
        public void synchLock1Method(int id) {
            synchronized (lock1) {
                System.out.println("🔒 [lock1] Thread " + id + " está executando synchLock1Method");
                sleep();

            }
        }

        // Usa o monitor do objeto lock2 (independente de this)
        public void synchLock2Method(int id) {
            synchronized (lock2) {
                System.out.println("🔒 [lock2] Thread " + id + " está executando synchLock2Method");
                sleep();

            }
        }

        private void sleep() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Cada thread vai executar um dos métodos sincronizados
    public static class SynchImpl extends Thread {
        private final int id;
        private final SynchClass sc;

        public SynchImpl(int id, SynchClass sc) {
            this.id = id;
            this.sc = sc;
        }

        @Override
        public void run() {
            while (true) {
                switch (id) {
                    case 0, 1:
                        sc.synchThisMethod(id); // usam o mesmo monitor: this
                        break;
                    case 2, 3:
                        sc.synchLock1Method(id); // usam o mesmo monitor: lock1
                        break;
                    case 4, 5:
                        sc.synchLock2Method(id); // usam o mesmo monitor: lock2
                        break;
                }
            }
        }
    }

    // Classe principal que cria e inicia as threads
    public static void main(String[] args) {
        SynchClass sc = new SynchClass();

        // Cria 6 threads, 2 para cada tipo de monitor
        for (int i = 0; i < 6; i++) {
            new SynchImpl(i, sc).start();
        }
    }
}
