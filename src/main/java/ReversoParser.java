import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ReversoParser {
    public static final String INDICATIF_PRESENT_CSS_QUERRY = "#ch_divSimple .blue-box-wrap[mobile-title=\"Indicatif Présent\"] li";
    public static final String PARTICIP_PASSE_CSS_QUERRY = "#ch_divSimple .blue-box-wrap[mobile-title=\"Participe Passé\"] li i[class=\"verbtxt\"]";
    private final String URL = "https://conjugueur.reverso.net/conjugaison-francais-verbe-";

    private StringBuilder parseIndicatifPresent(Document doc) {
        String line;
        StringBuilder builder = new StringBuilder();
        Elements formes = doc.select(INDICATIF_PRESENT_CSS_QUERRY);
        for (Element f : formes) {
            line = f.text();
            if (line.contains("/")) {
                line = line.substring(0, line.indexOf('/')) + line.substring(line.indexOf(" "));
            }
            builder.append(line)
                    .append(";\n");
        }
        return builder;
    }

    private StringBuilder parseParticipePasse(Document doc) {
        StringBuilder builder = new StringBuilder();
        Elements participePasse = doc.select(PARTICIP_PASSE_CSS_QUERRY);
        if (participePasse.size() == 4) {
            String mascSg = participePasse.get(0).text();
            String mascPl = participePasse.get(1).text();
            boolean isRegularForm = mascPl.startsWith(mascSg);
            String termination = "(e)(s)";
            if (isRegularForm) {
                builder.append(mascSg).append(termination);
            } else {
                builder.append(mascSg).append(",").append(mascPl, 0, mascPl.length() - 1).append(termination);
            }
        }
        return builder;
    }

    public String getForms(String verb) throws IOException {
        Document doc;
        try {
            doc = Jsoup.connect(URL + verb + ".html").get();
        } catch (HttpStatusException e) {
            System.out.println("Il n'y a pas ce verbe: " + verb);
            return "";
        }

        StringBuilder indicatifPresent = parseIndicatifPresent(doc);
        StringBuilder participePasse = parseParticipePasse(doc);

        return indicatifPresent.append(participePasse).toString();
    }
}