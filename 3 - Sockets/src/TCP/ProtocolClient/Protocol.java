package TCP.ProtocolClient;

public class Protocol {
    public String processLine(String input){
        if (input.equals("Bye")){
            return "Bye";
        }
        else if (input.equals("Hello")){
            return "Hello";
        }
        else if (input.equals("How are you?")){
            return "I am fine";
        }
        else if (input.equals("What is your name?")){
            return "My name is Protocol";
        }
        else {
            return "Unknown command";
        }
    }
}
