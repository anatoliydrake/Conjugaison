import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ReversoParser parser = new ReversoParser();
        parser.parseForms("devoir");
        System.out.println(parser);
    }
}
