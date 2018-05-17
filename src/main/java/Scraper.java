import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

    public void iterateOverRows(Elements els) {

        Elements rows = els.select("tr");
        int rowCounter = 0;
        for (Element row : rows) {
            System.out.println("Row " + rowCounter);
            Elements cols = row.select("td");
            int colCounter = 0;
            for (Element col : cols) {
//                System.out.println("\nCol " + colCounter + " : " + col.text() );
                System.out.println(col.getElementsByAttribute("data-price"));
                System.out.println(col.getElementsByClass("time"));
                colCounter++;
            }
            rowCounter++;
        }
    }

    // take one row, map all cols of the row
    public void getData(Elements elements) {
        Elements rows = elements.select("tr");
        Elements cols = rows.select("td");
//        System.out.println(cols.select("[id^=price]"));
//        System.out.println(cols.select("[class=time]"));

    }

    public List<FlightData> getFlightDataRefactored(Elements elements) {
        List<FlightData> flightList = new ArrayList<FlightData>();
        Elements rows = elements.select("tr");
        for (int i = 0; i < 18; i++) {
            Elements mainDataRow = rows.select("#idLine_0_" + i);
            Elements detailsRow = elements.select("#toggleId_0_" + i);

            String[] flightType = generateFlightClassNameList();

            String goLightFlight = mainDataRow.select("#reco_0_" + i + flightType[0]).text();
            String sasGoFlight = mainDataRow.select("#reco_0_" + i + flightType[1]).text();
            String plusSaverFlight = mainDataRow.select("#reco_0_" + i + flightType[2]).text();
            String plusFlight = mainDataRow.select("#reco_0_" + i + flightType[3]).text();

            String depTime = mainDataRow.select("td.time > span:nth-child(1)").text();
            String arrTime = mainDataRow.select("td.time > span:nth-child(3)").text();

            String depAir = mainDataRow.select("td.airport.last > acronym:nth-child(1) > span").text();
            String arrAir = mainDataRow.select("td.airport.last > acronym:nth-child(3) > span").text();

            String departureAirport = detailsRow.select("table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(1) > span.location").text();
            // if connection exists
            // TODO think of a flow controll for connection airport. If the flight is direct, there is no connection airport(empty) .
            //TODO also if connection airport is not Oslo -> discard.
            String arrivalAirport = detailsRow.select("table > tbody > tr:nth-child(5) > td.route.last > span:nth-child(3) > span.location").text();
            String connectionAirport = detailsRow.select("table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(3) > span").text();
            if(connectionAirport.contains("London")){
                arrivalAirport = connectionAirport;
                connectionAirport = "";
            }
//            String noConnectionSelector = "table > tbody > tr.flight > td.route.last > span:nth-child(3) > span.location";

            String[] flightPrices = {goLightFlight,sasGoFlight,plusSaverFlight,plusFlight};

            if(connectionAirport.length()!=0 && !connectionAirport.contains("Oslo")){

                //do nothing
            } else {
                flightList.add(new FlightData(flightPrices,departureAirport,arrivalAirport,connectionAirport,depTime,arrTime));
            }

        }
        return flightList;


    }

    //REFACTOR
    public FlightData getFlightData(Elements elements) {
        Elements rows = elements.select("tr");
//        for(int i=0;i<19;i++){
//            Elements mainDataRow = rows.select("#idLine_0_"+i);
//            Elements detailsRow = elements.select("#toggleId_0_"+i);
//            System.out.println(mainDataRow.text());
//            System.out.println(detailsRow.text());
//
//        }
        Elements mainDataRow = rows.select("#idLine_0_1");
        Elements detailsRow = elements.select("#toggleId_0_1");
        String[] flightType = generateFlightClassNameList();
        // working with first data row
        // get all four prices for four flight types
        Elements goLightFlight = mainDataRow.select("#reco_0_1" + flightType[0]);
        Elements sasGoFlight = mainDataRow.select("#reco_0_1" + flightType[1]);
        Elements plusSaverFlight = mainDataRow.select("#reco_0_1" + flightType[2]);
        Elements plusFlight = mainDataRow.select("#reco_0_1" + flightType[3]);
        // replaceAll bullshit for generating numerical values from string
//        System.out.println(
//                goLightFlight.text().replaceAll("\\s","").replaceAll("€","").replaceAll("[^\\\\.0123456789]",".")
//                + ", " + sasGoFlight.text().replaceAll("\\s","").replaceAll("€","").replaceAll("[^\\\\.0123456789]",".")
//                + ", " + plusSaverFlight.text().replaceAll("\\s","").replaceAll("€","").replaceAll("[^\\\\.0123456789]",".")
//                + ", " + plusFlight.text().replaceAll("\\s","").replaceAll("€","").replaceAll("[^\\\\.0123456789]",".")
//        );
        // get departure and arrival times
        Elements depTime = mainDataRow.select("td.time > span:nth-child(1)");
        Elements arrTime = mainDataRow.select("td.time > span:nth-child(3)");

//        System.out.println(depTime.text() + " - " + arrTime.text());

        Elements depAir = mainDataRow.select("td.airport.last > acronym:nth-child(1) > span");
        Elements arrAir = mainDataRow.select("td.airport.last > acronym:nth-child(3) > span");
        System.out.println(depAir.text() + "-" + arrAir.text());

        // now lets do the second row
        Elements dont = detailsRow.select("table > tbody > tr:nth-child(2) > td.route.last");
        // if dont2 not empty && !contains.text("Oslo") -> dont include
        Elements dont2 = detailsRow.select("table > tbody > tr:nth-child(5) > td.route.last");
//        System.out.println(dont.text());
//        System.out.println(dont2.text());
        Elements departureAirport = detailsRow.select("table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(1) > span.location");
        // if connection exists
        // TODO think of a flow controll for connection airport. If the flight is direct, there is no connection airport.
        //TODO also if connection airport is not Oslo -> discard.
        Elements arrivalAirport = detailsRow.select("table > tbody > tr:nth-child(5) > td.route.last > span:nth-child(3) > span.location");
        Elements connectionAirport = detailsRow.select("table > tbody > tr:nth-child(2) > td.route.last > span:nth-child(3) > span");

//        System.out.println(departureAirport.text());
//        System.out.println(connectionAirport.text());
//        System.out.println(arrivalAirport.text());

        //map all the shit
        String[] flightPrices = {
                goLightFlight.text().replaceAll("\\s", ""),
                sasGoFlight.text().replaceAll("\\s", ""),
                plusSaverFlight.text().replaceAll("\\s", ""),
                plusFlight.text().replaceAll("\\s", "")
        };
        String depT = depTime.text();
        String arrT = arrTime.text();
        String dAir = depAir.text();
        String aAir = arrAir.text();
        String departAirport = departureAirport.text().replaceAll("\\s", "");
        String arrivAirport = arrivalAirport.text().replaceAll("\\s", "");
        String connAirport = connectionAirport.text();

        return new FlightData(flightPrices, departAirport, arrivAirport, connAirport, depT, arrT);


    }

    public String[] generateFlightClassNameList() {
        String goLight = "_ECONBG";
        String sasGo = "_ECOA";
        String plusSaver = "_PREMN";
        String plus = "_PREMB";
        return new String[]{goLight, sasGo, plusSaver, plus};

    }

    public void generateIds() {
        String idLine = "idLine_0_";
    }

    //TODO
    //Access table
    //Get rows

}
