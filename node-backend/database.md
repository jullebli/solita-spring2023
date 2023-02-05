# Planning database tables and data import to database

## Recommmendations in instructions
- Import data from the CSV files to a database or in-memory storage
- Validate data before importing
- Don't import journeys that lasted for less than ten seconds
- Don't import journeys that covered distances shorter than 10 meters

## Journey csv

fields:
- Departure
- Return
- Departure station id
- Departure station name
- Return station id
- Return station name
- Covered distance (m)
- Duration (sec.)

## Table: Journey

### Validation to imported journeys
Only import if a record has duration >= 10 seconds and distance >= 10 meters.

### journey table fields:
- id (made by database)
- departure_station_id
- return_station_id
- departure_time
- return_time
- duration (at least 10 seconds)
- distance (at least 10 meters)

## Station csv

information also on https://public-transport-hslhrt.opendata.arcgis.com/datasets/HSLHRT::helsingin-ja-espoon-kaupunkipy%C3%B6r%C3%A4asemat-avoin/about

fields: 
- FID
- ID, (connected to Journey csv), type:Text
- Nimi, type: Text
- Namn, type: Text
- Name, type: Text
- Osoite, type: Text
- Adress, (Swedish address actually) type: Text
- Kaupunki, type: Text
- Stad, type: Text
- Operaattor, type: Text
- Kapasiteet, type: Number
- x (longitude), type: Number
- y (latitude), type: Number

## station table fields
- FID
- ID (has to be the same as in csv, connected to Journey csv)
- name
- name_FI
- name_SWE
- address
- adress_SWE
- city
- city_SWE
- operator
- capacity
- longitude
- latitude

same types as in the csv (all text except last three and FID are Numberic)

## create table SQL commands

CREATE TABLE station (
FID Numeric UNIQUE NOT NULL,
ID Text UNIQUE NOT NULL,
name Text,
name_FI Text,
name_SWE Text,
address Text,
address_SWE Text,
city Text,
city_SWE Text,
operator Text,
capacity Numeric,
longitude Numeric,
latitude Numeric,
PRIMARY KEY (ID)
);

CREATE TABLE journey (
id SERIAL PRIMARY KEY,
departure_time TIMESTAMP NOT NULL,
return_time TIMESTAMP NOT NULL,
departure_station_id Text,
return_station_id Text,
duration Numeric,
distance Numeric
);
