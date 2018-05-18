import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.ranges.Range;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Utilities {
    private static final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER);

    public WebClient setUpWebClient() throws IOException {

        // Turn off warnings
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyPort", "8888");
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.waitForBackgroundJavaScript(5000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setRedirectEnabled(true);
        setCookies(webClient);

        return webClient;
    }

    private void setCookies(WebClient client) {
        CookieManager cookieManager = client.getCookieManager();
        String domain = "www.flysas.com";
        String name = "SASLastSearch";
        String value = "%7B%22origin%22:%22ARN%22,%22destination%22:%22LHR%22,%22outward%22:%2220180604%22,%22inward%22:%2220180610%22,%22adults%22:%221%22,%22children%22:%220%22,%22infants%22:%220%22,%22youths%22:%22NaN%22,%22lpc%22:%22false%22,%22oneway%22:%22false%22,%22rtf%22:%22false%22,%22rcity%22:%22false%22%7D";
        Cookie lastSearch = new Cookie(domain, name, value);
        cookieManager.addCookie(lastSearch);

    }

    public HtmlPage clickSearchAnchor(HtmlPage page) throws IOException {
        String anchorHref = "javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(\"ctl00$FullRegion$MainRegion$ContentRegion$ContentFullRegion$ContentLeftRegion$CEPGroup1$CEPActive$cepNDPRevBookingArea$Searchbtn$ButtonLink\", \"\", true, \"\", \"\", false, true))";
        HtmlAnchor submitAnchor = page.getAnchorByHref(anchorHref);


        return submitAnchor.click();
    }

    public void saveHtmlFile(HtmlPage page) throws FileNotFoundException {

        String filename = "OutputHTML";
        String extension = ".html";
        int counter = 1;
        File outputFile = new File("C:\\Users\\nevyt\\IdeaProjects\\scraper-flysas\\src\\" + filename + extension);
        PrintWriter writer;
        if (outputFile.exists()) {
            outputFile = new File("C:\\Users\\nevyt\\IdeaProjects\\scraper-flysas\\src\\" + filename + counter + extension);
            writer = new PrintWriter(outputFile);
            writer.write(page.asXml());
            writer.close();
        } else {
            writer = new PrintWriter(outputFile);
            writer.write(page.asXml());
            writer.close();
        }

    }

    public HtmlPage fillSearchForm(HtmlPage page) {
        String buttonId = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_Searchbtn_ButtonLink";
        String fromId = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_predictiveSearch_hiddenFrom";
        String toId = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_predictiveSearch_hiddenTo";
        String adultId = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_cepPassengerTypes_passengerTypeAdult";
        String childId = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_cepPassengerTypes_passengerTypeChild211";
        String infantId = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_cepPassengerTypes_passengerTypeInfant";
        String outboundId = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_cepCalendar_hiddenOutbound";
        String returnId = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_cepCalendar_hiddenReturn";

        HtmlAnchor submitButton = (HtmlAnchor) page.getElementById(buttonId);
        HtmlHiddenInput departureAir = (HtmlHiddenInput) page.getElementById(fromId);
        HtmlHiddenInput arrivalAir = (HtmlHiddenInput) page.getElementById(toId);
        HtmlSelect adultCount = (HtmlSelect) page.getElementById(adultId);
        HtmlSelect childCount = (HtmlSelect) page.getElementById(childId);
        HtmlSelect infantCount = (HtmlSelect) page.getElementById(infantId);
        HtmlHiddenInput outbound = (HtmlHiddenInput) page.getElementById(outboundId);
        HtmlHiddenInput ret = (HtmlHiddenInput) page.getElementById(returnId);

        //set values
        departureAir.setValueAttribute("ARN");
        arrivalAir.setValueAttribute("LHR");
        adultCount.getOptionByValue("1");
        childCount.getOptionByValue("0");
        infantCount.getOptionByValue("0");
        outbound.setValueAttribute("2018-06-04");
        ret.setValueAttribute("2018-06-10");

        return page;

    }

    public HtmlPage submitForm(HtmlPage page) {
        HtmlForm form = page.getForms().get(0);
        page = (HtmlPage) form.fireEvent(Event.TYPE_SUBMIT).getNewPage();
        return page;
    }

    public List<HtmlPage> savePagesFromWindows(WebClient webClient) {
        List<HtmlPage> pageList = new ArrayList<HtmlPage>();
        List<WebWindow> windowCount = webClient.getWebWindows();
        for (WebWindow window :
                windowCount) {
            pageList.add(((HtmlPage) window.getEnclosedPage()));
//            System.out.println("Window url: " + window.getEnclosedPage().getUrl().toExternalForm());
        }
        return pageList;
    }

    public Document parsePageToDocument(HtmlPage page) {
        String pageAsXML = page.asXml();
        return Jsoup.parse(pageAsXML);


    }

}


