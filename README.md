# Utopia-Library

# Kafka developer guide

Er zijn een aantal stappen die men moet doorlopen voor het introduceren van Kafka in een nieuwe microservice.
## **1. Events Produceren**
### 1.1 Topic Creation
Per microservice vraagt Kafka om een Topic aan te maken, wat events categoriseert.
Het aanmaken van een Topic is geautomatiseerd en vergt alleen de volgende code in de file _Application.java_, ofwel de klasse die geannoteerd is met _@SpringBootApplication_.

```
import utopia.library.events.TopicFactory;


@Bean
public NewTopic init(){
	return TopicFactory.initializeKafka("Topic");
}
```

### 1.2 Initialiseren van de EventProducer
Om events te kunnen aanmaken hebben we een event producer nodig. Deze komt uit de library.
Voordat we de event producer kunnen gebruiken moet deze als volgt geïnitialiseerd worden.

```
import utopia.library.events.EventProducer;

private final EventProducer eventProducer = new EventProducer();
}
```
### 1.3 Sturen van events
Men stuurt een event naar de Kafka broker wanneer een relevante use case met succes is afgerond. Dit gebeurt in de service laag van de microservice, per use case methode.

```
import utopia.library.events.ApplicationEvent;

public void useCase(){
    ....
    this.eventProducer.Send("Topic", ApplicationEvent.create(dto, "EventString"));    
}
```

**Topic** refereert naar het Topic van de microservice dat ook al eens gedefinieerd werd in het hoofdstuk hierboven. Het is aangeraden om hier een constante van te maken binnen de service laag.

**DTO** refereert naar de informatie die meegestuurd moet worden met het event. Bij het aanmaken en updaten van een entiteit is dit meestal het object gemapped op een DTO. Bijvoorbeeld bij het registreren van een bedrijf zendt men een _CompanyDto _mee.
Bij het verwijderen is er niet veel meer mee te geven in een DTO, en is gekozen om alleen het id van de verwijderde entiteit mee te sturen. 
> Let Op! Indien de verwijderde entiteit een een relatief Id heeft, wordt er wél een bijbehorende DTO aangemaakt. Deze bevat echter enkel de Id's van eventuele parents die nodig zijn om de entiteit eenduidig te kunnen identificeren.

**EventString** refereert naar de naam van het event. Het geeft antwoord op de vraag: Wat is er precies veranderd? De eventString wordt altijd in de Engelse 'past simple' opgesteld. Bijvoorbeeld het registreren van een bedrijf geeft de EventString _RegisteredCompany_.

## **2. Events Consumeren**
### 2.1 Initialiseren van de EventConsumer
Het volstaat volgende annotatie toe te voegen aan de applicatie klasse _Application.java_, ofwel de klasse die geannoteerd is met @SpringBootApplication.
```
@Import(ConsumerConfiguration.class)
public class Application {
//SOME CODE HERE
}
```
### 2.2 Consumeren van een Event
Een event consumeren gebeurt bij voorkeur in de Service (Application) laag.
```
@KafkaListener(topics = "[TOPIC]", containerFactory = "kafkaListenerContainerFactory")
public void consumeEventExample(ApplicationEvent<[TYPE]> event){
    //We can check _event.eventType_
    //And/or do something with _event.data_
}
```
* Vul [TOPIC] in met het topic(s) van het event(s) wat geconsumeerd moet worden.
* Vul [TYPE] in met een type wat voldoet aan ApplicationEvent.data. Wanneer er bijvoorbeeld een complexe dto vervat zit in het event, zal type bij voorkeur een zelf gedefinieerde input dto zijn.


## 3. Extra info  

Een ApplicationEvent<T> heeft volgend formaat wanneer het geserialiseerd werd:
```
{  
	"eventType": "[Naam van event]" //bv. "TaskPublishedEvent",  
	"timeStamp": [Tijdstip in LocalDateTime formaat],  
	"data":  
	{  
		[Geserialiseerd formaat van meegegeven Dto]  
		//Bv. "id": 5, "name": "naam", "description": "Dit is een voorbeeld beschrijving"
	}  
}  
```  



# Exception guide

## Exception throwing
Wanneer er iets fout kan gaan tijdens uitvoering zorgen we dat er een exception gegooid wordt op dit slechte pad. Deze exception bubbelt omhoog tot de Controller laag, waar deze afgehandeld wordt. 

Geef altijd zo veel mogelijk informatie mee aan de exception zodat we nadien kunnen nagaan wat er exact is fout gegaan.

### Exceptions in Guards
De Guard klasse uit de library is een van de plaatsen waar veel exceptions kunnen gegooid worden. Maar, Guards zijn ook een functionaliteit die in veel services gebruikt zal worden. Zodanig moet de Guard informatie mee krijgen van buitenaf, over wie hem oproept.

**Voorbeeld : Guard.title(string title, string caseOfError)**

In _Station.java_ wordt deze Guard gebruikt om de title van een station te testen. De Guard krijgt de volgende caseOfError string mee:

`"Setting title in Station.java with id " + this.getId() + ":"`

Vervolgens voegt de desbetreffende Guard daar nog meer informatie aan toe:

`" Exception during title Guard: " + title + " does not conform to standards"`

Met de volledige error string weten we dus wat er fout gaat, waarom het fout gaat en in welke klasse.

## Exception handling
Exception handling gebeurt op globaal niveau in de klasse _GlobalExceptionHandler.java_ binnen de library. Deze activeert wanneer de Controller een exception tegenkomt voordat de aanvraag afgehandeld kan worden.

> **Catch dus nooit exceptions door middel van een try-catch, tenzij in héél uitzonderlijke gevallen!**
> Als uitzondering zie bijvoorbeeld Guard.email. Hier wordt de Address exception gevangen en omgezet tot een IllegalArgumentException zodat deze niet in de throws clause van iedere hogere functie vernoemt hoeft te worden.


De exceptionHandler heeft voor iedere exception die wij in de code gooien, alsook voor de default clause _Exception.class_, een functie die de error afhandelt. Hij neemt hier dus de taak van de controller over wanneer de uitvoering op een 'slecht' pad terechtkomt.

Concreet komt dit er op neer dat voor iedere exception een HTTP error code is gekozen die wordt terug gestuurd wanneer de Controller een exception tegenkomt.
In de praktijk is dit meestal een 400, ofwel Bad Request. Dit komt doordat de meeste exceptions, waaronder die in de Guard, gegooid worden naar aanleiding van een (intentionele) fout van de gebruiker.

Naast 400 kan de service op dit moment ook een 500, ofwel Internal Server Error, teruggeven wanneer er zich een bug voordoet of iets onvoorzien fout loopt.

Concreet betekent dit dus:

* _NoSuchElementException_ - **400** - Wordt gegooid tijdens het zoeken naar een bepaalde entiteit binnen een lijst, wanneer de gevraagde entiteit niet bestaat
* _IllegalArgumentException_ - **400** - Wordt gegooid wanneer een Guard niet slaagt
* _EntityNotFoundException_ - **400** - Wordt gegooid wanneer een Repository query getById niet gevonden wordt
* _Exception_ - **500** - Iedere andere exception wordt op dit moment gezien als onvoorzien en zou niet mogen voorkomen tijdens production

Wanneer er een nieuwe exception handling vereist moet de Core, en deze lijst, uitgebreid worden.

## Exception logging
Wanneer de exceptionHandler gevraagd wordt om een exception af te handelen, logt de exceptionHandler de exception alvorens verder te communiceren met de front-end. Hij gebruikt hierbij de error message die is meegegeven wanneer de exception gegooid is. Het is dus erg belangrijk dat deze zo veel mogelijk informatie bevat.

Naast de error message worden ook de access token en sessionId van de uitvoerende gebruiker gelogd (indien beschikbaar).


