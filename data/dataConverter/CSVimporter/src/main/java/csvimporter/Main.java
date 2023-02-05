package csvimporter;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Map;
import org.apache.commons.io.input.BOMInputStream;

/**
 *
 * @author bergmjul
 */
public class Main {

    public static void main(String[] args) throws IOException, CsvException, URISyntaxException {
        //String stationFileName = "csvFiles/Stations.csv";
        //readFile(stationFileName, "station");
        String firstJourneyFileName = "csvFiles/2021-05.csv";
        readFile(firstJourneyFileName, "journey");
        //String secondJourneyFileName = "csvFiles/2021-06.csv";
        //readFile(secondJourneyFileName, "journey");
        //String thirdJourneyFileName = "csvFiles/2021-07.csv";
        //readFile(thirdJourneyFileName, "journey");
        //System.out.println("\\q");
    }

    public static void readFile(String fileName, String fileType) throws IOException, CsvException, URISyntaxException {
        CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new InputStreamReader(
                new BOMInputStream(new FileInputStream(fileName))));

        while (true) {
            StringBuilder SQLInsert = new StringBuilder("INSERT INTO ");
            if (fileType.equals("station")) {
                SQLInsert.append("station (FID, ID, name_FI, name_SWE, name,  address, address_SWE, city, city_SWE, operator, capacity, longitude, latitude) VALUES (");
            } else if (fileType.equals("journey")) {
                SQLInsert.append("journey (departure_time, return_time, departure_station_id, return_station_id,  duration, distance) VALUES (");
            }
            Map<String, String> row = reader.readMap();
            if (row == null) {
                break;
            }

            /*
            FID, 
            ID (connected to Journey csv), 
            Nimi, 
            Namn, 
            Name, 
            Osoite, 
            Adress (Swedish address actually), 
            Kaupunki, 
            Stad, 
            Operaattor, 
            Kapasiteet, 
            x (longitude), 
            y (latitude)
             */

            if (fileType.equals("station")) {
                SQLInsert.append(row.get("FID"));
                SQLInsert.append(", ");
                SQLInsert.append(row.get("ID"));
                SQLInsert.append(", '");
                SQLInsert.append(row.get("Nimi"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Namn"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Name"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Osoite"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Adress"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Kaupunki"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Stad"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Operaattor"));
                SQLInsert.append("', ");
                SQLInsert.append(row.get("Kapasiteet"));
                SQLInsert.append(", ");
                SQLInsert.append(row.get("x"));
                SQLInsert.append(", ");
                SQLInsert.append(row.get("y"));
                SQLInsert.append("); ");

                System.out.println(SQLInsert);
            } else if (fileType.equals("journey")) {
                if (Integer.parseInt(row.get("Duration (sec.)")) >= 10
                        && Integer.parseInt(row.get("Covered distance (m)")) >= 10) {

                    SQLInsert.append("'");
                    SQLInsert.append(row.get("Departure").replace("T", " "));
                    SQLInsert.append("', '");
                    SQLInsert.append(row.get("Return").replace("T", " "));
                    SQLInsert.append("', ");
                    SQLInsert.append(row.get("Departure station id"));
                    SQLInsert.append(", ");
                    //SQLInsert.append(row.get("Departure station name"));
                    //SQLInsert.append("', ");
                    SQLInsert.append(row.get("Return station id"));
                    SQLInsert.append(", ");
                    //SQLInsert.append(row.get("Return station name"));
                    //SQLInsert.append("', ");
                    SQLInsert.append(row.get("Covered distance (m)"));
                    SQLInsert.append(", ");
                    SQLInsert.append(row.get("Duration (sec.)"));
                    SQLInsert.append("); ");
                    System.out.println(SQLInsert);
                }
            }
        }
        reader.close();
    }
}
