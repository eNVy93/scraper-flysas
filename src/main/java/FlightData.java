import java.util.Arrays;

public class FlightData {

    //only direct or connection ant Oslo
    private String departureAirport;
    private String arrivalAirport;
    private String connectionAirport;
    private String departureTime;
    private String arrivalTime;
    private String[] flightPrices;
//    private String cheapestPrice;
//    private String taxes;
//
//    public FlightData(String departureAirport, String arrivalAirport, String connectionAirport, String departureTime, String arrivalTime, String cheapestPrice, String taxes) {
//        this.departureAirport = departureAirport;
//        this.arrivalAirport = arrivalAirport;
//        this.connectionAirport = connectionAirport;
//        this.departureTime = departureTime;
//        this.arrivalTime = arrivalTime;
//        this.cheapestPrice = cheapestPrice;
//        this.taxes = taxes;
//    }


    public FlightData(String[] flightPrices,String departureAirport, String arrivalAirport, String connectionAirport, String departureTime, String arrivalTime) {
        this.flightPrices = flightPrices;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.connectionAirport = connectionAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getConnectionAirport() {
        return connectionAirport;
    }

    public void setConnectionAirport(String connectionAirport) {
        this.connectionAirport = connectionAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "FlightData{" +
                "\n departureAirport='" + departureAirport + '\'' +
                ",\n arrivalAirport='" + arrivalAirport + '\'' +
                ",\n connectionAirport='" + connectionAirport + '\'' +
                ",\n departureTime='" + departureTime + '\'' +
                ",\n arrivalTime='" + arrivalTime + '\'' +
                ",\n flightPrices=" + Arrays.toString(flightPrices) +
                '}';
    }

    //    public String getCheapestPrice() {
//        return cheapestPrice;
//    }
//
//    public void setCheapestPrice(String cheapestPrice) {
//        this.cheapestPrice = cheapestPrice;
//    }
//
//    public String getTaxes() {
//        return taxes;
//    }
//
//    public void setTaxes(String taxes) {
//        this.taxes = taxes;
//    }
}
