package questoes;

public class MoedaWhile extends Thread{
    String face;

    public MoedaWhile(String f) {
        this.face = f;
    }

    public void run(){
        System.out.println(face);
    }

    public static void main(String[] args) {
        while(true){
            MoedaWhile m1 = new MoedaWhile("cara...");
            MoedaWhile m2 = new MoedaWhile("coroa...");
            m1.start();
            m2.start();
        }
    }
}
