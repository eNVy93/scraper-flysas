import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Scraper {

    //TODO
    public Elements getDepartureTable(Document doc) {
        return doc.select("#WDSEffect_table_0 > tbody");

    }

    //TODO
    public Elements getArrivalTable(Document doc) {
        return doc.select("#WDSEffect_table_1 > tbody");

    }


    public List<FlightData> getFlightDataRefactored(Elements elements, int tableType,String taxes) {
        List<FlightData> flightList = new ArrayList<FlightData>();
        Elements rows = elements.select("tr");
        for (int i = 0; i < 18; i++) {
            Elements mainDataRow = rows.select("#idLine_" + tableType + "_" + i);
            Elements detailsRow = elements.select("#toggleId_" + tableType + "_" + i);

            String[] flightType = generateFlightClassNameList();

            String goLightFlight = mainDataRow.select("#reco_" + tableType + "_" + i + flightType[0]).text();
            goLightFlight = goLightFlight.replaceAll("€", "").replaceAll("[^\\\\.0123456789^\\s^-]", ".").replaceAll("\\s", "");
            String sasGoFlight = mainDataRow.select("#reco_" + tableType + "_" + i + flightType[1]).text();
            sasGoFlight = sasGoFlight.replaceAll("€", "").replaceAll("[^\\\\.0123456789^\\s^-]", ".").replaceAll("\\s", "");
            String plusSaverFlight = mainDataRow.select("#reco_" + tableType + "_" + i + flightType[2]).text();
            plusSaverFlight = plusSaverFlight.replaceAll("€", "").replaceAll("[^\\\\.0123456789^\\s^-]", ".").replaceAll("\\s", "");
            String plusFlight = mainDataRow.select("#reco_" + tableType + "_" + i + flightType[3]).text();
            plusFlight = plusFlight.replaceAll("€", "").replaceAll("[^\\\\.0123456789^\\s^-]", ".").replaceAll("\\s", "");

            String depTime = mainDataRow.select("td.time > span:nth-child(1)").text();
            String arrTime = mainDataRow.select("td.time > span:nth-child(3)").text();

            String depAir = mainDataRow.select("td.airport.last > acronym:nth-child(1) > span").text();
            String arrAir = mainDataRow.select("td.airport.last > acronym:nth-child(3) > span").text();

            String departureAirport = detailsRow.select("table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(1) > span.location").text();
            String connectionAirport = detailsRow.select("table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(3) > span.location").text();
            String arrivalAirport = detailsRow.select("table > tbody > tr:nth-child(5) > td.route.last > span:nth-child(3) > span.location").text();
            // table 1
            // #toggleId_0_15 > table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(1) > span.location ----- departure     STOCKHOLM
            // #toggleId_0_15 > table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(3) > span    ----- connection          OSLO
            // #toggleId_0_15 > table > tbody > tr:nth-child(5) > td.route.last > span:nth-child(3) > span.location ----- arrival       LONDON
            // table 2
            // #toggleId_1_11 > table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(1) > span.location ----- departure     LONDON
            // #toggleId_1_11 > table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(3) > span.location ----- connection    SOME OTHER
            // #toggleId_1_11 > table > tbody > tr:nth-child(5) > td.route.last > span:nth-child(3) > span.location ----- arrival       STOCKHOLM
            if (connectionAirport.contains("London") || connectionAirport.contains("Stockholm")) {
                arrivalAirport = connectionAirport;
                connectionAirport = "";
            }
            String[] flightPrices = {goLightFlight, sasGoFlight, plusSaverFlight, plusFlight};
            String cheapestPrice;

            //TODO find cheapest flight
            if (flightPrices[0].length() == 1) {
                cheapestPrice = flightPrices[1];
            } else {
                cheapestPrice = flightPrices[0];
            }

            if (connectionAirport.length() != 0 && !connectionAirport.contains("Oslo")) {
                //do nothing
            } else {
                flightList.add(new FlightData(taxes, cheapestPrice, flightPrices, departureAirport, arrivalAirport, connectionAirport, depTime, arrTime));
            }

        }
        return flightList;


    }
    public String getTaxes(Document doc){
        String taxes = doc.select("#taxesAndFees").text();
        return taxes;
    }


    public String[] generateFlightClassNameList() {
        String goLight = "_ECONBG";
        String sasGo = "_ECOA";
        String plusSaver = "_PREMN";
        String plus = "_PREMB";
        return new String[]{goLight, sasGo, plusSaver, plus};

    }


}
