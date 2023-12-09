# bookshop-backend
Die Repository dient zum Lernen von Java / Spring Boot im Backend.
Das Backend und die MySQL Datenbank werden mit Docker in getrennten Containern bereitgestellt.

# How to Start
    cd into project
    make fresh
    make up
Die Application ist dann unter localhost:8081 erreichbar.
Die Daten der Datenbank werden lokal im Ordner db-data abgelegt.

# Makefile
- make fresh,
Stoppt die Container und baut diese neu. Der db-data Order wird hierbei nicht gelöscht
- make up,
Startet die Container
- make down,
Stoppt die Container


# Technologie
- Java / Spring Boot
- MySQL
- Docker

## Funktionalitäten
- Auslesen aller Bücher 
- Auslesen eines Buches anhand der Isbn
- Einpflegen neuer Bücher
- Löschen eines Buches anhand der Isbn

### TODO
- Aktualisieren von Informationen
- Autoren anlegen, auslesen, bearbeiten, löschen
- Autoren zu Büchern zuweisen
