# Ürituste planeerija RIK

Ürituste planeerija on rakendus, mis võimaldab manageerida nii üritusi kui ka osalejaid. Sellega on võimalik lisada uusi üritusi ja osavõtjaid, uuendada andmeid kui ka kustutada üritusele registreerituid.

## Kirjeldus

Rakendus kasutab Reacti raamistikku frontis ja Java Spring Booti backendis. See jookseb Postgres databaasi peal. RESTful API arhitektuur on loodud sobimaks rakendusele ja on vajadusel lihtsasti integreeritav teisetele süsteemidele.

## Nõuded

Java 21 `java -version`

Node.js 12.x või uuem`node -version`

npm 6.x või uuem `npm -version`

Maven peab olema installitud.

Kontrolli et docker oleks installitud`docker -version`

Kontrolli et docker jookseks`sudo systemctl status docker`

kui ei siis käivita
`sudo systemctl start docker`

Kontrolli et PostgreSQL oleks installitud.`psql -version`

## Installimine

Ava kaks akent, üks backend/eventsmanager ja teine frontend/eventsmanager-frontend kaustas.
Backend kaustas:

```
mvn clean install
```

Käivita Spring boot

```
mvn spring-boot:run
```

Frontend kaustas:

```
npm install
```

ja siis

```
npm start
```
