import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ReversoParser parser = new ReversoParser();
        Scanner scanner = new Scanner(System.in);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection;
        String verb = "";
        while (!verb.equals("0")) {
            System.out.print("Entrez le verbe: ");
            verb = scanner.nextLine();
            if (verb.equals("0")) {
                continue;
            }
            String forms = parser.getForms(verb);

            if (!forms.isEmpty()) {
                selection = new StringSelection(forms);
                clipboard.setContents(selection, null);
            }
        }
        scanner.close();

    }
}
