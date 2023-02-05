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
        String stationFileName = "csvFiles/Stations.csv";
        readFile(stationFileName, "station");
        String firstJourneyFileName = "csvFiles/2021-05.csv";
        readFile(firstJourneyFileName, "journey");
        String secondJourneyFileName = "csvFiles/2021-06.csv";
        readFile(secondJourneyFileName, "journey");
        String thirdJourneyFileName = "csvFiles/2021-07.csv";
        readFile(thirdJourneyFileName, "journey");
        //System.out.println("\\q");
    }

    public static void readFile(String fileName, String fileType) throws IOException, CsvException, URISyntaxException {
        CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new InputStreamReader(
                new BOMInputStream(new FileInputStream(fileName))));
        System.out.println("BEGIN;");

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
                if (row.get("FID").isEmpty() || row.get("ID").isEmpty()) {
                    continue;
                }
                SQLInsert.append(row.get("FID"));
                SQLInsert.append(", ");
                SQLInsert.append(row.get("ID"));
                SQLInsert.append(", '");
                SQLInsert.append(row.get("Nimi").replace("'", "''"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Namn").replace("'", "''"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Name").replace("'", "''"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Osoite").replace("'", "''"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Adress").replace("'", "''"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Kaupunki").replace("'", "''"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Stad").replace("'", "''"));
                SQLInsert.append("', '");
                SQLInsert.append(row.get("Operaattor").replace("'", "''"));
                SQLInsert.append("', ");
                SQLInsert.append(row.get("Kapasiteet"));
                SQLInsert.append(", ");
                SQLInsert.append(row.get("x"));
                SQLInsert.append(", ");
                SQLInsert.append(row.get("y"));
                SQLInsert.append("); ");

                System.out.println(SQLInsert);
            } else if (fileType.equals("journey")) {
                String duration = row.get("Duration (sec.)");
                String distance = row.get("Covered distance (m)");
                String departure_time = row.get("Departure").replace("T", " ");
                String return_time = row.get("Return").replace("T", " ");

                if (duration.isEmpty() || distance.isEmpty()
                        || departure_time.isEmpty() || return_time.isEmpty()) {
                    continue;
                }
                if (Double.valueOf(duration) >= 10
                        && Double.valueOf(distance) >= 10) {

                    SQLInsert.append("'");
                    SQLInsert.append(departure_time.replace("T", " "));
                    SQLInsert.append("', '");
                    SQLInsert.append(return_time.replace("T", " "));
                    SQLInsert.append("', ");
                    SQLInsert.append(row.get("Departure station id"));
                    SQLInsert.append(", ");
                    //SQLInsert.append(row.get("Departure station name"));
                    //SQLInsert.append("', ");
                    SQLInsert.append(row.get("Return station id"));
                    SQLInsert.append(", ");
                    //SQLInsert.append(row.get("Return station name"));
                    //SQLInsert.append("', ");
                    SQLInsert.append(distance);
                    SQLInsert.append(", ");
                    SQLInsert.append(duration);
                    SQLInsert.append("); ");
                    System.out.println(SQLInsert);
                }
            }
        }
        System.out.println("COMMIT;");
        reader.close();
    }
}
