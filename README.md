# 🚀 HECF-SmartSearch

**Pametno iskanje razpisov** – podpora pri iskanju in sledenju razpisom iz programa **Horizon Europe Cascade Funding**.  
Aplikacija omogoča filtriranje, pregledovanje in spremljanje aktualnih razpisov na enem mestu, z osredotočenostjo na uporabniku prijazno izkušnjo in hitro iskanje relevantnih informacij.

---

## 🌐 FRONTEND

### 🛠️ Orodja
- ⚡ **Vite** (v6.3.5)
- ⚛️ **React TypeScript** (v19.9.0)
- 📡 **Axios** (v1.9.0)

### ⚙️ Priprava
1. 📁 Odpri terminal in pojdi v mapo:  
   ```bash
   cd 01_Frontend
2. 📦 Namesti odvisnosti:
    ```bash
    npm install
     ```  

3. ▶️ Zaženi aplikacijo:
    ```bash
    npm run dev
    ```
4. 🌐 Odpri v brskalniku:
    ```bash
        http://localhost:5173
    ```
## 
## 🖥️ BACKEND

### 🛠️ Orodja
 - ☕ Java (v23)

- 🧰 Maven (v4.0.0)
- 🌱 Spring Boot (v3.4.5)
- 🧬 Lombok
- 🤖 Selenium

### ⚙️ Priprava
1. 📁 Ustvari datoteko **application.propertes** Dodaj MongoDB povezavo v datoteko:   **src/main/resources/application.properties**
    ```bash
    spring.data.mongodb.uri=mongodb+srv://<uporabnik>:<geslo>@<cluster-url>/<ime-baze>?retryWrites=true&w=majority&appName=<appName>
    ```

2. 📁Pojdi v mapo z ukazom:
    ```bash
    cd 02_Backend
    ```
    
3. ▶️ Zaženi aplikacijo:
    ```bash
    mvn spring-boot:run
    ```
##  
### 🗄️ DATABASE
### 🛠️ Orodja
 - ☁️ MongoDB Atlas (Cloud hosting)
##

## 🧪 Zagon celotnega projekta
V terminalu:
```bash
    cd 01_Frontend
```
```bash
    npm run dev
```
V novem terminalu:
```bash
    cd 02_Backend
```
```bash
    mvn spring-boot:run
```


##
### 🌐 Namestitev ChromeDriverja
### 🔧 Navodila:

 1. Pojdi v Chrome brskalnik in vpišite naslednje: 
 ```bash
    chrome://settings/help
 ```
 
 2. Poglej katero verzijo uporabljaš:   
 <img src="ReadmeIMG/image.png" alt="Chrome verzija" width="800"/>  

 3. Pojdi na tole povezavo in pritisni Chrome version plus:  
 <img src="ReadmeIMG/image-1.png" alt="Download stran" width="800"/>

 ```bash
    https://developer.chrome.com/docs/chromedriver/downloads
 ```
 
 4. Sedaj pa izberi pravilen chromeDriver za tvojo mašino:  
 <img src="ReadmeIMG/image-3.png" alt="Prenos datoteke" width="800"/>  

 5. Za konec pa vpiši ustrezen url v brskalnik naloži in razširi naložen chromeDriver
