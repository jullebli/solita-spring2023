# Backend code for bike-app

This is the backend code for my Solita Dev Academy Finland 2023 pre-assignment application called bike-app. Database code, csvFiles and logfile are not in this github repository but maintained locally.

## Database

Database is a local postgreSQL database called bike-app-db. In database.md is information about how to create tables and CSV files' fields.

### How to import data from CSV into database

There is a jar file (CSVimporter-1.0-SNAPSHOT-jar-with-dependencies.jar) that reads csv file rows and makes one insert into SQL command for every row in he CSV file. All the inserts made of one CSV file are imported inside BEGIN-COMMIT transaction block.

You need some things for my code to work correctly:
 - files named: Stations.csv, 2021-05.csv, 2021-06.csv and 2021-07.csv in the folder node-backend/csvFiles. First line in these files should be the headers. Check Database.md for information about CSV fields/headers.
 - station and journey tables in your local database. Check Database.md for correct CREATE TABLE commands.

You can also modify the code in ../data/dataConverter/CSVimporter/csvimporter/Main to handle data named differently. But then you need to run mvn package in the CSVimporter directory to make a jar file to the target directory and copy the jar file to node-backend directory.

Run the Java jar file in the node-backend directory:
- java -cp CSVimporter-1.0-SNAPSHOT-jar-with-dependencies.jar csvimporter.Main | psql --dbname bike-app-db

Running will take some minutes.

Then you can check if all data was inserted into database
- psql --dbname bike-app-db (or what you chose as db name)
- SELECT COUNT(*) FROM station; (should be 457)
- SELECT COUNT(*) FROM journey; (should be 3128758)
