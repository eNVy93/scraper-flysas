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

    // TODO set the input value to 'departureAirport'
    public HtmlPage clickSearchButton(HtmlPage page) throws IOException {
        String buttonId = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_Searchbtn_ButtonLink";
        String anchorHref = "javascript:WebForm_DoPostBackWithOptions(new WebForm_PostBackOptions(\"ctl00$FullRegion$MainRegion$ContentRegion$ContentFullRegion$ContentLeftRegion$CEPGroup1$CEPActive$cepNDPRevBookingArea$Searchbtn$ButtonLink\", \"\", true, \"\", \"\", false, true))";
        DomElement buttonElement = page.getElementById(buttonId);
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

    public HtmlPage setInterestPointValues(HtmlPage page) {
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

//      FOR OUTPUT DEBUGGING
//        System.out.println(
//                "Button: " + submitButton.toString()
//                        + "\nDeparture: " + departureAir.toString()
//                        + "\nArrival: " + arrivalAir.toString()
//                        + "\nAdultCount: " + adultCount.toString()
//                        + "\nChildCount: " + childCount.toString()
//                        + "\nInfantCount: " + infantCount.toString()
//                        + "\nDeparture date: " + outbound.toString()
//                        + "\nArrival date: " + ret.toString()
//        );
        return page;

    }

    public HtmlPage submitForm(HtmlPage page) {
        HtmlForm form = page.getForms().get(0);
        page = (HtmlPage) form.fireEvent(Event.TYPE_SUBMIT).getNewPage();
        // new bullshit
        return page;
    }

    public List<HtmlPage> savePagesFromWindows(WebClient webClient) {
        List<HtmlPage> pageList = new ArrayList<HtmlPage>();
        List<WebWindow> windowCount = webClient.getWebWindows();
        for (WebWindow window :
                windowCount) {
            pageList.add(((HtmlPage) window.getEnclosedPage()));
            System.out.println("Window url: " + window.getEnclosedPage().getUrl().toExternalForm());
//            System.out.println("DATA " + ((HtmlPage) window.getEnclosedPage()).asXml());
        }
        return pageList;
    }

    public Document parsePageToDocument(HtmlPage page){
//        HtmlTable targetTable = (HtmlTable) page.getElementById("WDSEffect_table_0");
        String pageAsXML = page.asXml();
        return Jsoup.parse(pageAsXML);


    }


    //DOES NOTHING

    public void setDepartureInput(HtmlPage page, String departureAirport) {
        String idFrom = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_predictiveSearch_hiddenFrom";

        DomElement departureInputElement = page.getElementById(idFrom);
        departureInputElement.setAttribute("value", departureAirport);
        System.out.println(departureInputElement.toString());
        System.out.println(departureInputElement.getAttribute("value"));

    }

    public void setArrivalInput(HtmlPage page, String arrivalAirport) {
        String idFrom = "ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_predictiveSearch_hiddenTo";

        DomElement arrivalInputElement = page.getElementById(idFrom);
        arrivalInputElement.setAttribute("value", arrivalAirport);
        System.out.println(arrivalInputElement.toString());
        System.out.println(arrivalInputElement.getAttribute("value"));

    }

    public void getPageInfo(HtmlPage page) {
        URL baseUrl = page.getBaseURL();
        String titleText = page.getTitleText();
        String contentType = page.getContentType();
        List<HtmlForm> forms = page.getForms();
        List<Range> selectionRanges = page.getSelectionRanges();
        List<NameValuePair> responseHeaders = page.getWebResponse().getResponseHeaders();

        System.out.println(
                "Base URL " + baseUrl.toExternalForm()
                        + "\nTitle text: " + titleText
                        + "\nContent-type: " + contentType
        );

//        for (HtmlForm form :
//                forms) {
//            System.out.println("Form : " + form.asXml());
//        }
        System.out.println("Form count: " + forms.size());
        for (Range range : selectionRanges) {
            System.out.println("Range: " + range.toString());
        }

        for (NameValuePair pair : responseHeaders) {
            System.out.println(pair.getName() + "->" + pair.getValue());
        }
    }

    public HtmlPage submitFinalForm(HtmlPage page) {
        HtmlForm form = page.getFormByName("form1");
        page = (HtmlPage) form.fireEvent(Event.TYPE_SUBMIT).getNewPage();
        return page;
    }

    public HtmlPage addFormSubmitButtonAndClick(HtmlPage page) throws IOException {
        // create a submit button - it doesn't work with 'input'
        DomElement button = page.createElement("button");
        button.setAttribute("type", "submit");

// append the button to the form
        HtmlElement form = page.getForms().get(0);
        form.appendChild(button);

// submit the form
        page = button.click();
        return page;
    }

    public void generatePostRequest(HtmlPage page) throws IOException {

        String baseURL = "book.flysas.com";
        String paramValue = " /pl/SASC/wds/Override.action?SO_SITE_EXT_PSPURL=https://classic.sas.dk/SASCredits/SASCreditsPaymentMaster.aspx&SO_SITE_TP_TPC_POST_EOT_WT=50000&SO_SITE_USE_ACK_URL_SERVICE=TRUE&WDS_URL_JSON_POINTS=ebwsprod.flysas.com%2FEAJI%2FEAJIService.aspx&SO_SITE_EBMS_API_SERVERURL=https%3A%2F%2F1aebwsprod.flysas.com%2FEBMSPointsInternal%2FEBMSPoints.asmx&WDS_SERVICING_FLOW_TE_SEATMAP=TRUE&WDS_SERVICING_FLOW_TE_XBAG=TRUE&WDS_SERVICING_FLOW_TE_MEAL=TRUE";
        String requestURL = "https://book.flysas.com/pl/SASC/wds/Override.action?SO_SITE_EXT_PSPURL=https://classic.sas.dk/SASCredits/SASCreditsPaymentMaster.aspx&SO_SITE_TP_TPC_POST_EOT_WT=50000&SO_SITE_USE_ACK_URL_SERVICE=TRUE&WDS_URL_JSON_POINTS=ebwsprod.flysas.com%2FEAJI%2FEAJIService.aspx&SO_SITE_EBMS_API_SERVERURL=https%3A%2F%2F1aebwsprod.flysas.com%2FEBMSPointsInternal%2FEBMSPoints.asmx&WDS_SERVICING_FLOW_TE_SEATMAP=TRUE&WDS_SERVICING_FLOW_TE_XBAG=TRUE&WDS_SERVICING_FLOW_TE_MEAL=TRUE";
        URL url = new URL(requestURL);
        WebRequest requestSettings = new WebRequest(url, HttpMethod.POST);

        requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        requestSettings.setAdditionalHeader("Referer", "https://www.flysas.com/en/lt/");
        requestSettings.setAdditionalHeader("Accept-Language", "en,lt;q=0.9,en-US;q=0.8,ru;q=0.7,pl;q=0.6,es;q=0.5");
        requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
        requestSettings.setAdditionalHeader("Cookie", "D_SID=84.240.13.12:qAgZT1vYP/iQhPH8NR6BWE7mxdUhnkzreqKNEvlcwHg; D_IID=05456754-5E10-3B7B-B884-98B928D5600B; D_UID=C84EFE24-C4B0-3D97-88E3-482C519C517A; D_ZID=9279EBCE-1A4B-3DF3-8F7D-C27D49600C7C; D_ZUID=0962D830-CCE5-3376-8E5D-581DA19E605E; D_HID=09E35B06-3130-30D5-94A8-7E9083D21693; JSESSIONID_SASC=VIlte_id8Lv5K3s3Ing53DKwI7NxwjY9zhGoJHoWuq9Y7ZxMg1I3!863071827!-339112673");
        requestSettings.setAdditionalHeader("Cache-Control", "max-age=0");
        requestSettings.setAdditionalHeader("Origin", "https://www.flysas.com");

        requestSettings.setRequestBody("/pl/SASC/wds/Override.action?SO_SITE_EXT_PSPURL=https://classic.sas.dk/SASCredits/SASCreditsPaymentMaster.aspx&SO_SITE_TP_TPC_POST_EOT_WT=50000&SO_SITE_USE_ACK_URL_SERVICE=TRUE&WDS_URL_JSON_POINTS=ebwsprod.flysas.com%2FEAJI%2FEAJIService.aspx&SO_SITE_EBMS_API_SERVERURL=https%3A%2F%2F1aebwsprod.flysas.com%2FEBMSPointsInternal%2FEBMSPoints.asmx&WDS_SERVICING_FLOW_TE_SEATMAP=TRUE&WDS_SERVICING_FLOW_TE_XBAG=TRUE&WDS_SERVICING_FLOW_TE_MEAL=TRUE");

        page = webClient.getPage(requestSettings);
        System.out.println(((HtmlPage) page).asXml());
    }


}


