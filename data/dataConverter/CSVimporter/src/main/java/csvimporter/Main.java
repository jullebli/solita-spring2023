package csvimporter;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.commons.io.input.BOMInputStream;

/**
 *
 * @author bergmjul
 */
public class Main {

    public static void main(String[] args) throws IOException, CsvException, URISyntaxException {
        String stationFileName = "csvFiles/testStations.csv";
        readFile(stationFileName, "station");
        String firstJourneyFileName = "csvFiles/testJourneys.csv";
        readFile(firstJourneyFileName, "journey");
    }

    /*
    public static List<String[]> readFile(String fileName) throws IOException, CsvException {
        List<String[]> list = new ArrayList<>();
        try ( CSVReader reader = new CSVReader(new FileReader(fileName))) {
            list = reader.readAll();
            list.forEach(x -> System.out.println(Arrays.toString(x)));
        } catch (Error e) {
            System.out.println("There has been an error: " + e);
        }
        
        return list;
    } 
     */
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
 /*
            //System.out.println(row.values());
            //System.out.println(row.keySet());
            for (Object value : row.values()) {

                System.out.println("value = " + value);
                System.out.println("class = " + value.getClass());
                for (Object key : row.keySet()) {
                    if (row.get(key).equals(value.toString())) {
                        System.out.println("key = " + key);
                        System.out.println("key class = " + key.getClass());
                        System.out.println("");
                    }
                    
                }
            }
             */
            if (fileType.equals("station")) {
                SQLInsert.append(row.get("FID"));
                SQLInsert.append(", ");
                SQLInsert.append(row.get("ID"));
                //System.out.println(SQLInsert);
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
                //System.out.println("was journey type");
                //System.out.println("row = " + row);
                if (Integer.parseInt(row.get("Duration (sec.)")) >= 10
                        || Integer.parseInt(row.get("Covered distance (m)")) >= 10) {
                    //need to think if names are necessary. Could be join table with id
                    //System.out.println("was long enough and lasted long enough");
                    
                    
                    //SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
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
                    //System.out.println("");
                }
            }

            /*
            for (String value : row.values()) {
                System.out.println(value);
            }
             */
        }

        reader.close();

        /*
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();
        
        Path path = Paths.get(ClassLoader.getSystemResource("data/Stations.csv").toURI());
        CSVReader CSVreader = new CSVReaderBuilder(Files.newBufferedReader(path))
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();
        
        for (String[] nextLine : CSVreader.readAll()) {
            Arrays.toString(nextLine);
        }
         */
    }
}
