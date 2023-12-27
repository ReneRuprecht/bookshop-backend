# bookshop-backend
Die Repository dient zum Lernen von Java / Spring Boot im Backend.
Das Backend und die MySQL Datenbank werden mit Docker in getrennten Containern bereitgestellt.

# How to Start
    cd into project
    make fresh
    make up

Die Application ist dann unter localhost:8081 erreichbar.
Die Daten der Datenbank werden lokal im Ordner db-data abgelegt.

# How to run Tests 
## Innerhalb des Docker Containers
1. Per Terminal auf den Backend Container verbinden.  

        docker exec -it bookshop-backend bash  
2. Mit Gradle die Tests ausführen.  

        gradle clean test

## Innerhalb der IDE
Sollen die Tests local ausgeführt werden, so muss die application-test.yml angepasst werden.
Statt "testing-db:3306" muss hier "127.0.0.1:3307" eingetragen werden.
So können die Tests per IDE ausgeführt werden.

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
Die Endpunkte befinden sich hinter /api/v1/.
Alle Endpunkte bis auf /auth benötigen einen gültigen JWT.
Ein JWT wird nach erfolgreichen Login übermittelt

### Authentication
- Registrierung
- Login

### Inventory

- Auslesen aller Bücher 
- Auslesen eines Buches anhand der Isbn
- Einpflegen neuer Bücher
- Löschen eines Buches anhand der Isbn

### TODO
- Aktualisieren von Informationen
- Autoren anlegen, auslesen, bearbeiten, löschen
- Autoren zu Büchern zuweisen
