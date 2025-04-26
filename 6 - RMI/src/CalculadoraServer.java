import javax.naming.Context;
import javax.naming.InitialContext;

public class CalculadoraServer {
    public static void main(String[] args) {
        try {
            // 🔵 Cria a instância do objeto remoto que será exposto
            CalculatorImpl calc = new CalculatorImpl();

            // 🔵 Obtém o contexto inicial JNDI para registrar o objeto
            Context nameContext = new InitialContext();

            // 🔵 Registra (bind) o objeto no RMI registry usando JNDI
            nameContext.rebind("rmi://127.0.0.1:1099/calculator", calc);

            System.out.println("✅ CalculatorService registrado no JNDI com sucesso!");
        } catch (Exception e) {
            // ❗ Captura qualquer erro de exportação, registro ou configuração
            e.printStackTrace();
        }
    }
}
