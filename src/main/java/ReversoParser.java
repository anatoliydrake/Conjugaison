import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ReversoParser {
    private final ArrayList<String> formes;
    private final String LINK = "https://conjugueur.reverso.net/conjugaison-francais-verbe-";

    public ReversoParser() {
        this.formes = new ArrayList<>();
    }

    public void parseForms(String verb) throws IOException {
        String line;
        Document doc = Jsoup.connect(LINK + verb + ".html").get();
        Elements formes = doc.select("#ch_divSimple .blue-box-wrap[mobile-title=\"Indicatif Présent\"] li");
        for (Element f : formes) {
            line = f.text();
            if (line.contains("/")) {
                line = line.substring(0, line.indexOf('/')) + line.substring(line.indexOf(" "));
            }
            this.formes.add(line);
        }
        Elements participePasse = doc.select("#ch_divSimple .blue-box-wrap[mobile-title=\"Participe Passé\"] li i[class=\"verbtxt\"]");
        if (participePasse.size() == 4) {
            String mascSg = participePasse.get(0).text();
            String mascPl = participePasse.get(1).text();
            boolean isRegularForm = mascPl.startsWith(mascSg);
            String termination = "(e)(s)";
            if (isRegularForm) {
                this.formes.add(mascSg + termination);
            } else {
                this.formes.add(mascSg + "," + mascPl.substring(0, mascPl.length() - 1) + termination);
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = formes.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(";")
                        .append("\n");
            }
        }
        return builder.toString();
    }
}