# Strom-Kalkulator

I løpet av 2022 opplevde Norge rekordhøye strømpriser, noe som påvirket både
privatpersoner og bedrifter. På grunn av denne pågående situasjonen og den økende
bekymringen rundt energikostnader, har vi valgt å fokusere på Case 2: Strømpris-
kalkulator.

Vi mener at dette caset er spesielt relevant og aktuelt på grunn av den intense
diskusjonen og debatten som har pågått rundt strømprisene i det siste. Flere fakto-
rer, som værforhold, økende etterspørsel etter strøm og endringer i energimarkedet,
har bidratt til denne utfordrende situasjonen.

Ved å utvikle en strømpriskalkulator-app ønsker vi å hjelpe forbrukerne med å få
en bedre forståelse av hvordan strømprisene påvirker deres daglige liv og økonomi. En
slik app vil kunne gi dem innsikt i forbruksmønstrene sine og bidra til å identifisere
muligheter for energibesparelser. Dette kan potensielt føre til mer bærekraftige valg
og reduserte strømutgifter for både enkeltpersoner og bedrifter.

### API-nøkkel
For å få hentet data fra FROST og MET-apiene trenger man API-nøkkler.
Det er god standard å ha dise lokalt på egen maskin, og ikke
i en fil som pushes til github. For at appen skal kjøre, legg
til api-nøkkelene i local.properties slik:

```properties
apiKey=DIN_API_NØKKEL
frostKey=DIN_FROST_NØKKEL
```

Da burde de ligge i BuildConfig når du har bygget appen, 
og du kan hente de ut hvor som helst
i kotlin-koden ved å skirve `BuildConfig.apiKey` eller `BuildConfig.frostKey`

**ADVARSEL: DETTE GJEMMER IKKE API-NØKKELEN I APK-BUILDS AV 
PROSJEKTET. DETTE ER BARE EN LØSNING FOR Å SLIPPE Å LEGGE TIL NØKKELEN I GIT.**
