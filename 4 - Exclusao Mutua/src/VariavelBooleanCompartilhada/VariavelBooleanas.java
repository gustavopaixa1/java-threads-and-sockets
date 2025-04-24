package VariavelBooleanCompartilhada;

public class VariavelBooleanas {
    public static void main(String[] args) {
        Lock lock = new LockImpl();
        Thread t1 = new MyLockThread(lock, 0);
        Thread t2 = new MyLockThread(lock, 1);

        t1.start();
        t2.start();
    }

    static class MyLockThread extends Thread {
        Lock lock;
        int id;

        public MyLockThread(Lock lock, int id) {
            this.lock = lock;
            this.id = id;
        }

        public void run() {
            while (true) {
                lock.requestCS(id);
                System.out.println("Thread " + id + " entrou na seção crítica");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread " + id + " saiu da seção crítica");
                lock.releaseCS(id);
            }
        }
    }
}

interface Lock {
    public void requestCS(int id);
    public void releaseCS(int id);
}
class LockImpl implements Lock {

    volatile boolean wantCS[] = {false, false};

    // o wantCS[0] = true indica que o processo 0 quer entrar na seção crítica
    // Faz p rocesso 0 esperar enquanto o processo 1 estiver na seção crítica
    public void requestCS(int id) {

        // Pode ter deadlock se os dois processos forem true ao mesmo tempo, ambos
        // vao esperar o outro sair, wantCS[0] = true e wantCS[1] = true
        wantCS[id] = true;
        while (wantCS[1-id]);
    }

    // Indica que o processo em questao não quer mais acesso a entrar na seção crítica
    public void releaseCS(int id) {
        wantCS[id] = false;
    }
}

