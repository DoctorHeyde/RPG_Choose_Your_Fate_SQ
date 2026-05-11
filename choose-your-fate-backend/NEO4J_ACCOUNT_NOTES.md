# Neo4j Account Notes

Denne note opsummerer det vi lavede den 11.-12. maj 2026 på Neo4j account-delen:

- hvad problemet var
- hvad vi ændrede
- hvorfor ændringerne var vigtige
- og hvilke forbedringer vi tog med ud fra Copilot-feedback

## Baggrund

Vi havde en situation hvor Neo4j account-funktionaliteten ikke opførte sig stabilt via Spring Data Neo4j repository-hydrering. Særligt account-listningen fejlede, selv om de samme data godt kunne læses direkte via Cypher.

Det viste sig, at den mest stabile løsning var:

- at bruge `Neo4jClient` til account-queries
- at holde query-logikken i et repository-lag
- og lade service-laget stå for forretningslogik og mapping til API-DTO'er

## Hovedændringer

### 1. Vi droppede SDN repository-hydrering til account-listning

I stedet for at stole på Spring Data Neo4j's automatiske mapping af `AccountNode` ved repository-kald, flyttede vi account-adgang over på eksplicitte Cypher-queries via `Neo4jClient`.

#### Hvorfor?

Den automatiske repository-hydrering var ustabil i vores setup. Direkte Neo4j-queries fungerede derimod stabilt.

#### Resultat

Account-flowet i Neo4j bruger nu:

- eksplicit Cypher
- tydelig mapping
- og mere kontrolleret adfærd

### 2. Query-logikken blev flyttet ned i repository-laget

Først lå `Neo4jClient`-queries direkte i `Neo4jAccountService`, men bagefter blev de flyttet ned i `AccountNodeRepository`.

#### Hvorfor?

Det giver en pænere lagdeling:

- repository = databaseadgang og Cypher
- service = validering, orkestrering og forretningslogik

#### Resultat

`Neo4jAccountService` er nu lettere at læse og mere konsistent med resten af projektets struktur.

### 3. DTO'er blev fjernet fra repository-laget

Repositoryet returnerer ikke længere `AccountResponseDTO` direkte. I stedet returnerer det interne records som fx:

- `AccountData`
- `AccountSnapshot`
- `CreateAccountData`
- `UpdateAccountData`

Service-laget mapper derefter til `AccountResponseDTO`.

#### Hvorfor?

Det holder API-laget og persistence-laget bedre adskilt.

#### Resultat

Repositoryet arbejder nu med interne dataformer, mens service-laget former API-responsen.

## Copilot-feedback vi tog med

Nedenfor er de vigtigste ændringer vi lavede på baggrund af Copilot-kommentarer.

### 1. `max(id) + 1` blev erstattet med en counter-node

Før blev næste account-id fundet ved:

```cypher
MATCH (a:Account)
RETURN coalesce(max(a.id), 0) + 1
```

#### Problem

Det er race-prone under concurrency:

- to requests kan læse samme max-id
- begge kan forsøge at oprette samme næste id

#### Løsning

Vi indførte en `Counter`-node til account-id'er.

#### Hvorfor det var vigtigt

Det gør id-genereringen mere robust og mere sikker under samtidige requests.

### 2. Counteren fik en unik constraint

Copilot pegede korrekt på, at:

```cypher
MERGE (counter:Counter {name: 'account'})
```

kun er sikkert, hvis `Counter.name` er unikt.

#### Problem

Uden unik constraint kunne to transaktioner i princippet oprette to forskellige `Counter`-noder med samme `name`.

#### Løsning

Vi tilføjede:

```cypher
CREATE CONSTRAINT counter_name_unique IF NOT EXISTS
FOR (c:Counter)
REQUIRE c.name IS UNIQUE
```

#### Hvorfor det var vigtigt

Det gør selve counter-mønstret concurrency-sikkert.

### 3. Counteren blev gjort robust over for migrerede data

Copilot pegede også på, at counteren kunne komme bagud, hvis der senere blev indsat accounts med højere ids, fx via migration.

#### Problem

Hvis `counter.value` er lavere end den højeste eksisterende `Account.id`, kan næste id blive genbrugt og fejle på unik constraint.

#### Første løsning

Vi gjorde først counter-logikken i stand til at sammenligne sig med `maxExistingId` og løfte sin værdi op, hvis nødvendigt, før den incrementerer.

#### Endelig løsning

Senere rykkede vi selve counter-sync væk fra runtime create-queryen og over i migrationsflowet. Det var vigtigt, fordi counteren ellers enten:

- skulle scanne alle `Account`-noder ved hver oprettelse
- eller kunne blive synkroniseret på et forkert tidspunkt

Den endelige model er derfor:

- migration opretter/vedligeholder constraints
- migration synkroniserer `Counter {name: 'account'}` efter account-noder er migreret
- runtime create bruger kun counteren direkte uden at scanne alle accounts

#### Hvorfor det var vigtigt

Det gør løsningen robust, også hvis migrationer eller andre dataloads kører senere, samtidig med at runtime-oprettelse bliver billigere.

### 4. Id-allokering og account-create blev slået sammen i én query

Copilot pegede på, at create-flowet før bestod af to runder:

1. hent næste id
2. opret account

#### Problem

Det gav:

- ekstra latency
- og mulighed for "ubrugte" ids, hvis counteren blev incrementeret men selve create fejlede bagefter

#### Løsning

Vi flyttede id-allokeringen ind i selve `createAccount(...)` queryen, så:

- counter-opdatering
- og account-oprettelse

sker i samme databasekald.

#### Hvorfor det var vigtigt

Det gør create-flowet mere atomisk og mere effektivt.

### 5. Username og email blev beskyttet med unikke constraints

Copilot pegede på, at vores pre-checks for username/email ikke er nok under concurrency.

#### Problem

To requests kan begge nå at se:

- "username findes ikke"

og derefter begge prøve at oprette samme værdi.

#### Løsning

Vi tilføjede Neo4j constraints for:

- `Account.id`
- `Account.username`
- `Account.email`

#### Hvorfor det var vigtigt

Det gør dataintegriteten reel og ikke kun "best effort".

### 6. Pre-checks blev beholdt, men constraint failures oversættes pænt

Vi beholdt `ensureUniqueUsername(...)` og `ensureUniqueEmail(...)`, men vi lagde også en fallback på, så Neo4j constraint-fejl bliver oversat til `DuplicateResourceException`.

#### Hvorfor?

Det giver:

- pænere fejl til klienten
- men stadig rigtig DB-integritet som sikkerhedsnet

### 7. Den originale Neo4j-fejl bevares nu som cause

Copilot pegede på, at vi tidligere oversatte duplicate-fejl uden at bevare den originale exception.

#### Problem

Så blev debugging og logging sværere.

#### Løsning

`DuplicateResourceException` fik en constructor med:

```java
DuplicateResourceException(String message, Throwable cause)
```

og den oprindelige exception sendes nu med videre som cause.

#### Hvorfor det var vigtigt

Det gør logs og fejlfinding langt bedre, uden at ødelægge de pæne fejlbeskeder udadtil.

### 8. Constraint-oprettelse blev flyttet fra repository startup til migration

Vi havde midlertidigt constraint-oprettelse i repository-beanen via startup-logik.

#### Problem

Det var arkitektonisk forkert, fordi:

- repositoryet begyndte at lave schema-ændringer
- startup kunne fejle i miljøer uden schema-rettigheder
- og ansvaret lå både i repository og migration

#### Løsning

Constraint creation blev flyttet over i `Neo4jMigrationService#createConstraints()`.

#### Hvorfor det var vigtigt

Det giver en renere ansvarsdeling:

- migration = schema og constraints
- repository = queries
- service = forretningslogik

### 9. Counter-sync skulle ske efter node-migration, ikke før

Vi ramte en vigtig rækkefølgefejl efter at have kørt migration med `clearExistingData=true`.

#### Problem

Hvis counteren blev synkroniseret inde i `createConstraints()`, så skete det før account-noderne var migreret ind igen. I et tomt miljø blev counteren derfor sat til `0`, og efterfølgende account-oprettelse prøvede at genbruge `id = 1`.

#### Løsning

Vi flyttede counter-sync til en separat metode i migrationen, som først kører efter `migrateNodes(...)`.

#### Hvorfor det var vigtigt

Det sikrer, at counteren bliver synkroniseret mod de accounts der faktisk findes i grafen efter migrationen, ikke mod en midlertidigt tom database.

## Hvad der er vigtigt at forstå

### Repositoryet er nu et query-repository

Det er ikke længere et klassisk "automatisk SDN entity repository". Det er i stedet et custom Neo4j query-repository med eksplicitte Cypher-queries.

Det passer bedre til account-delen i vores projekt.

### Service-laget er stadig ansvarligt for forretningslogik

Selv om Neo4j-queryerne ligger i repositoryet, er det stadig service-laget der:

- laver uniqueness-prechecks
- encoder password
- bygger de interne create/update-dataobjekter
- mapper interne data til API-DTO'er

### `record`s bruges bevidst

Vi bruger Java `record`s til små interne dataformer som:

- `AccountData`
- `AccountSnapshot`
- `CreateAccountData`
- `UpdateAccountData`

Det er valgt fordi de er:

- små
- immutable
- og primært bruges til dataoverførsel internt mellem lag

## Kort eksamensforklaring

En mulig kort formulering kunne være:

> Vi oplevede, at Spring Data Neo4j's automatiske repository-mapping ikke var stabil nok til account-delen, så vi flyttede Neo4j account-adgangen over på eksplicitte Cypher-queries via `Neo4jClient`, men beholdt en ren lagdeling ved at placere query-logikken i repository-laget. Derefter gjorde vi account-oprettelsen mere robust på baggrund af Copilot-feedback: vi erstattede `max(id) + 1` med en counter-node, sikrede counteren med en unik constraint, flyttede id-allokering og create ind i samme query, tilføjede unikke constraints på username og email, og sørgede for at counteren bliver synkroniseret i migrationsflowet efter account-noderne er oprettet. Samtidig beholdt vi venlige pre-checks og oversatte eventuelle Neo4j-constraint-fejl til domænespecifikke fejlbeskeder.

## Kort status

Efter ændringerne er Neo4j account-delen:

- funktionel
- bedre opdelt i lag
- mere robust under konkurrence
- og lettere at forklare teknisk
