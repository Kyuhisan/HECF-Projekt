# HECF-SmartSearch

# FRONTEND:
## Orodja:
- Vite (v6.3.5)
- ReactTS (v19.9.0)
- Axios (v1.9.0)

### Priprava:
1. V ukazni vrstici pojdi v mapo ```01_Frontend```. 
2. Namesti odvisnost ```npm install```.
3. Zeženi aplikacijo ```npm run dev```.
4. Aplikacijo je dostopna na ```http://localhost:5173```.


# BACKEND:
## Orodja:
- Java (v23)
- Maven (v4.0.0)
- SpringBoot (v3.4.5)
- Lombok


### Priprava:
1. V ```src/main/resources/application.properties``` nastavi ```spring.data.mongodb.uri=mongodb+srv://<uporabnik>:<geslo>@<cluster-url>/<ime-baze>?retryWrites=true&w=majority&appName=<appName>```.
2. V ukazni vrstici se premakni v mapo ```02_Backend```.
3. Zazeni aplikacijo ```mvn spring-boot:run```.


# DATABASE:
## Orodja:
- MongoDB Atlas (cloud hosting)


# Zagon celotnega projekta:
1. V ukazni vrstici se premakni v mapo ```01_Frontend```.
2. Napisi in pozeni ```npm run dev```.
3. V ukazni vrstici se premakni v mapo ```02_Backend```.
4. Napisi in pozeni ```mvn spring-boot:run```.
