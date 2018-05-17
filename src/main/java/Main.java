import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {


    public static void main(String[] args) throws IOException, InterruptedException {
        final int DEPARTURE_TABLE = 0;
        final int ARRIVAL_TABLE = 1;

        String homeAddress = "https://www.flysas.com/en/lt/";
        Utilities utils = new Utilities();
        Scraper scraper = new Scraper();
        WebClient webClient = utils.setUpWebClient();
        HtmlPage page = webClient.getPage(homeAddress);
        HtmlPage modifiedPage = utils.setInterestPointValues(page);
        HtmlPage pageAfterFormSubmit = utils.submitForm(modifiedPage);

        HtmlPage pleaseWaitPage = utils.clickSearchButton(pageAfterFormSubmit); // WOKRS !!!!!!!!!!!!!!!!


        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(1500);
        webClient.waitForBackgroundJavaScriptStartingBefore(1500);

        List<HtmlPage> windowList = utils.savePagesFromWindows(webClient);


        //THIS IS THE RESULT I NEED !!!!!!!!!!
        HtmlPage targetPage = windowList.get(0);
        Document doc = utils.parsePageToDocument(targetPage);
        Elements departureTable = scraper.getDepartureTable(doc);
        Elements arrivalTable = scraper.getArrivalTable(doc);
        String taxes = scraper.getTaxes(doc);
        List<FlightData> departureFlightList = scraper.getFlightDataRefactored(departureTable, DEPARTURE_TABLE, taxes);

        for (FlightData flight : departureFlightList) {
            System.out.println("DEPARTURE FLIGHT");
            System.out.println("_-_-_-_-_-_-_-_-");
            System.out.println(flight);
        }

        List<FlightData> arrivalFlightList = scraper.getFlightDataRefactored(arrivalTable, ARRIVAL_TABLE, taxes);
        for (FlightData flight : arrivalFlightList) {
            System.out.println("ARRIVAL FLIGHT");
            System.out.println("_-_-_-_-_-_-_-");
            System.out.println(flight);
        }
        System.out.println();


    }
}
