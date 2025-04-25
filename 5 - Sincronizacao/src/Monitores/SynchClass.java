package Monitores;

public class SynchClass {

    // Monitor: usado nos métodos sincronizados de instância
    public synchronized void synchMethod(int id) {
        while (true) {
            System.out.println(">> [Instância - Sync] Thread " + id + " está executando synchMethod");
            sleepUmPouco();
        }
    }

    // NÃO usa monitor: múltiplas threads podem acessar ao mesmo tempo
    public void nonSynchMethod(int id) {
        while (true) {
            System.out.println(">> [Instância - Normal] Thread " + id + " está executando nonSynchMethod");
            sleepUmPouco();
        }
    }

    // Usa o monitor da CLASSE (SynchClass.class)
    public static synchronized void synchStaticMethod(int id) {
        while (true) {
            System.out.println(">> [Classe - Sync Static] Thread " + id + " está executando synchStaticMethod");
            sleepUmPouco();
        }
    }

    // NÃO usa monitor: acesso livre
    public static void nonSynchStaticMethod(int id) {
        while (true) {
            System.out.println(">> [Classe - Normal Static] Thread " + id + " está executando nonSynchStaticMethod");
            sleepUmPouco();
        }
    }

    private static void sleepUmPouco() {
        try {
            Thread.sleep(1000); // pausa para visualização clara da concorrência
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Classe que representa cada Thread que executa um métdo diferente
class SynchImpl extends Thread {
    private final int id;
    private final SynchClass sc;

    public SynchImpl(int id, SynchClass sc) {
        this.id = id;
        this.sc = sc;
    }

    @Override
    public void run() {
        switch (id) {
            case 0:
            case 1:
                sc.synchMethod(id); // métdo sincronizado de instância: usa monitor do objeto
                break;

            case 2:
            case 3:
                sc.nonSynchMethod(id); // métdo comum de instância: concorrência livre
                break;

            case 4:
            case 5:
                SynchClass.synchStaticMethod(id); // métdo sincronizado static: usa monitor da classe
                break;

            case 6:
            case 7:
                SynchClass.nonSynchStaticMethod(id); // método static comum: concorrência livre
                break;
        }
    }
}

// Ponto de entrada do programa
class Main {
    public static void main(String[] args) {
        SynchClass sc = new SynchClass(); // objeto compartilhado entre threads 0-3

        for (int i = 0; i < 8; i++) {
            new SynchImpl(i, sc).start(); // cria e inicia 8 threads
        }
    }
}
