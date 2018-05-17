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
        String departureAirport = "ARN";
        String arrivalAirport = "LHR";
        String homeAdress = "https://www.flysas.com/en/lt/";
        Utilities utils = new Utilities();
        Scraper scraper = new Scraper();
        WebClient webClient = utils.setUpWebClient();
        HtmlPage page = webClient.getPage(homeAdress);
        HtmlPage afterClick = utils.clickSearchButton(page);
        HtmlPage modifiedPage = utils.setInterestPointValues(page);
//        scraper.getPageInfo(modifiedPage);
        HtmlPage pageAfterFormSubmit = utils.submitForm(modifiedPage);

        HtmlPage pleaseWaitPage = utils.clickSearchButton(pageAfterFormSubmit); // WOKRS !!!!!!!!!!!!!!!!

        List<NameValuePair> responseHeaders = pleaseWaitPage.getWebResponse().getResponseHeaders();


        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(1500);
        webClient.waitForBackgroundJavaScriptStartingBefore(1500);

        List<HtmlPage> windowList = utils.savePagesFromWindows(webClient);

        //Save files to the machine
//        for(HtmlPage p: windowList){
//            scraper.saveHtmlFile(p);
//        }

        //THIS IS THE RESULT I NEED !!!!!!!!!!
        HtmlPage targetPage = windowList.get(0);
        Document doc =utils.parsePageToDocument(targetPage);
        Elements departureTable = scraper.getDepartureTable(doc);
//        scraper.getData(departureTable);
        List<FlightData> flightDataList = scraper.getFlightDataRefactored(departureTable);

        for(FlightData flight: flightDataList){
            System.out.println(flight);
        }

//        System.out.println(targetPage.as
// Xml());
//
//        System.out.println(pleaseWaitPage.getTitleText());



    }

}
