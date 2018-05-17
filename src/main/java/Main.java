import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.util.NameValuePair;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String departureAirport = "ARN";
        String arrivalAirport = "LHR";
        String homeAdress = "https://www.flysas.com/en/lt/";
        Scraper scraper = new Scraper();
        WebClient webClient = scraper.setUpWebClient();
        HtmlPage page = webClient.getPage(homeAdress);
        HtmlPage afterClick = scraper.clickSearchButton(page);
        HtmlPage modifiedPage = scraper.setInterestPointValues(page);
//        scraper.getPageInfo(modifiedPage);
        HtmlPage pageAfterFormSubmit = scraper.submitForm(modifiedPage);

        HtmlPage pleaseWaitPage = scraper.clickSearchButton(pageAfterFormSubmit); // WOKRS !!!!!!!!!!!!!!!!

        List<NameValuePair> responseHeaders = pleaseWaitPage.getWebResponse().getResponseHeaders();


        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(1500);
        webClient.waitForBackgroundJavaScriptStartingBefore(1500);

        List<HtmlPage> windowList = scraper.savePagesFromWindows(webClient);

        //Save files to the machine
//        for(HtmlPage p: windowList){
//            scraper.saveHtmlFile(p);
//        }

        //THIS IS THE RESULT I NEED !!!!!!!!!!
        HtmlPage targetPage = windowList.get(0);
        scraper.getTargetTable(targetPage);
//        System.out.println(targetPage.asXml());
//
//        System.out.println(pleaseWaitPage.getTitleText());



    }

}
