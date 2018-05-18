import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.List;


public class Main {


    public static void main(String[] args) throws IOException {
        Utilities utils = new Utilities();
        Scraper scraper = new Scraper();

        final int DEPARTURE_TABLE_INDEX = 0;
        final int ARRIVAL_TABLE_INDEX = 1;
        String homeAddress = "https://www.flysas.com/en/lt/";

        WebClient webClient = utils.setUpWebClient();
        HtmlPage page = webClient.getPage(homeAddress);
        HtmlPage modifiedPage = utils.fillSearchForm(page);
        HtmlPage pageAfterFormSubmit = utils.submitForm(modifiedPage);              // force submit form

        utils.clickSearchAnchor(pageAfterFormSubmit);
        // sync AJAX and wait for JavaScript
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(1500);
        webClient.waitForBackgroundJavaScriptStartingBefore(1500);

        List<HtmlPage> windowList = utils.savePagesFromWindows(webClient);

        HtmlPage targetPage = windowList.get(0);
        Document doc = utils.parsePageToDocument(targetPage);
        Elements departureTable = scraper.getDepartureTable(doc);
        Elements arrivalTable = scraper.getArrivalTable(doc);
        String taxes = scraper.getTaxes(doc);

        List<FlightData> departureFlightList = scraper.getFlightData(departureTable, DEPARTURE_TABLE_INDEX, taxes);

        for (FlightData flight : departureFlightList) {
            System.out.println("DEPARTURE FLIGHT");
            System.out.println("----------------");
            System.out.println(flight);
        }

        List<FlightData> arrivalFlightList = scraper.getFlightData(arrivalTable, ARRIVAL_TABLE_INDEX, taxes);

        for (FlightData flight : arrivalFlightList) {
            System.out.println("ARRIVAL FLIGHT");
            System.out.println("--------------");
            System.out.println(flight);
        }
        System.out.println();


    }
}
