import org.jsoup.Jsoup;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class = wt]").first();
        Elements names = tableWth.select("tr[class = wth]");
        Elements values = tableWth.select("tr[valign = top]");
        show(names, values);
    }
    private static int printTempValues(Elements values, int index) {
        int a = 0;
        if (index == 0) {
            Element valueLine = values.get(index);
            switch (valueLine.selectFirst("td").text()) {
                case "Утро":
                    a = 0;
                    break;
                case "День":
                    a = 1;
                    break;
                case "Вечер":
                    a = 2;
                    break;
                case "Ночь":
                    a = 3;
                    break;
            }
        }

        for (; a < 4; a++) {
            Element valueLine = values.get(index);
            index++;
            for (Element td: valueLine.select("td")) {
                System.out.print(td.text() + "    ");
            }
            System.out.println();
        }
        return index;
    }
    private static void show(Elements names, Elements values) throws Exception {
        int index =0;

        for (Element name:names) {
            String dateString = name.select("th[id = dt]").text();
            String date = getDate(dateString);
            System.out.println(date + "    Явления     Температура      Давление     Влажность     Ветер");
            index = printTempValues(values, index);
        }
    }
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");
    private static String getDate(String date) throws Exception {
        Matcher matcher = pattern.matcher(date);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Cant extract date from String!");
    }
    private static Document getPage() throws IOException {
        String url = "http://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }
}