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

    private String[] generateFlightClassNameList() {
        String goLight = "_ECONBG";
        String sasGo = "_ECOA";
        String plusSaver = "_PREMN";
        String plus = "_PREMB";
        return new String[]{goLight, sasGo, plusSaver, plus};

    }

    public List<FlightData> getFlightData(Elements elements, int tableType, String taxes) {
        List<FlightData> flightList = new ArrayList<FlightData>();
        Elements rows = elements.select("tr");
        String separator = "_";
        String dataRowSelector = "#idLine_";
        String detailRowSelector = "#toggleId_";
        String priceSelector = "#reco_";
        for (int i = 0; i < 18; i++) {
            Elements mainDataRow = rows.select(dataRowSelector + tableType + separator + i);
            Elements detailsRow = elements.select(detailRowSelector + tableType + separator + i);

            String[] flightType = generateFlightClassNameList();
            // Select prices and replace symbols to get a (numeric-like) String value. (replace € and 'whitespace' with nothing and replace comma with dot).
            String goLightFlight = mainDataRow.select(priceSelector + tableType + separator + i + flightType[0]).text();
            goLightFlight = goLightFlight.replaceAll("€", "").replaceAll("[^\\\\.0123456789^\\s^-]", ".").replaceAll("\\s", "");

            String sasGoFlight = mainDataRow.select(priceSelector + tableType + separator + i + flightType[1]).text();
            sasGoFlight = sasGoFlight.replaceAll("€", "").replaceAll("[^\\\\.0123456789^\\s^-]", ".").replaceAll("\\s", "");

            String plusSaverFlight = mainDataRow.select(priceSelector + tableType + separator + i + flightType[2]).text();
            plusSaverFlight = plusSaverFlight.replaceAll("€", "").replaceAll("[^\\\\.0123456789^\\s^-]", ".").replaceAll("\\s", "");

            String plusFlight = mainDataRow.select(priceSelector + tableType + separator + i + flightType[3]).text();
            plusFlight = plusFlight.replaceAll("€", "").replaceAll("[^\\\\.0123456789^\\s^-]", ".").replaceAll("\\s", "");

            String depTime = mainDataRow.select("td.time > span:nth-child(1)").text();
            String arrTime = mainDataRow.select("td.time > span:nth-child(3)").text();

            String depAir = mainDataRow.select("td.airport.last > acronym:nth-child(1) > span").text();
            String arrAir = mainDataRow.select("td.airport.last > acronym:nth-child(3) > span").text();

            String departureAirport = detailsRow.select("table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(1) > span.location").text();
            String connectionAirport = detailsRow.select("table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(3) > span.location").text();
            String arrivalAirport = detailsRow.select("table > tbody > tr:nth-child(5) > td.route.last > span:nth-child(3) > span.location").text();

            //Fixes problem where the connection airport becomes arrival airport.
            if (connectionAirport.contains("London") || connectionAirport.contains("Stockholm")) {
                arrivalAirport = connectionAirport;
                connectionAirport = "";
            }
            String[] flightPrices = {goLightFlight, sasGoFlight, plusSaverFlight, plusFlight};
            String cheapestPrice;
            // Either goLightFlight is cheapest. If not present - the cheapest one will be Sas Go.
            if (flightPrices[0].length() == 1) {
                cheapestPrice = flightPrices[1];
            } else {
                cheapestPrice = flightPrices[0];
            }

            // If the flight is direct
            if (connectionAirport.length() != 0 && !connectionAirport.contains("Oslo")) {
                //do nothing
            } else {
                flightList.add(new FlightData(taxes, cheapestPrice, flightPrices, departureAirport, arrivalAirport, connectionAirport, depTime, arrTime));
            }

        }
        return flightList;


    }

    public String getTaxes(Document doc) {
        return doc.select("#taxesAndFees").text();
    }


}
