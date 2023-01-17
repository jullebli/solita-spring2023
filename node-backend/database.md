# Planning database tables and data import to database

## Recommmendations in instructions
Import data from the CSV files to a database or in-memory storage
Validate data before importing
Don't import journeys that lasted for less than ten seconds
Don't import journeys that covered distances shorter than 10 meters

## Journey csv
fields:
Departure
Return
Departure station id
Departure station name
Return station id
Return station name
Covered distance (m)
Duration (sec.)

## Table: Journey

### Validation to imported journeys
Only import if a record has duration >= 10 seconds and distance >= 10 meters.

### journey table fields:
id (made by database)
departure_station_id
return_station_id
departure_time
return_time
duration (at least 10 seconds)
distance (at least 10 meters)

## Station csv
fields: FID, ID (connected to Journey csv), Nimi, Namn, Name, Osoite, Adress (Swedish address actually), Kaupunki, Stad, Operaattor, Kapasiteet, x (longitude), y (latitude)

## station table fields
FID
ID (has to be the same as in csv, connected to Journey csv)
name
name_FI
name_SWE
address
adress_SWE
city
city_SWE
operator
capasity
longitude
latitude


FID
ID
Nimi
Namn
Name
Osoite
Adress
Kaupunki
Stad
Operaattor
Kapasiteet
x
y