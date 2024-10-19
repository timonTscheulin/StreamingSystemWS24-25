# Aufgabe2

## Überlegungen zur Aufteilung von Verantwortlichkeiten Write Side(Grober Ablauf):

Auf deer write Seite werden Kommandos von der api erzeugt und an den KommandoDispatcher übergeben. 
Der Kommandodispatcher ruft einen handler auf der wiederum aus aggregate repositories die passenden aggregates läd.
Die Aggragates liefern passend events welche an den handler zurückgegeben werden, die wiederum von dem Handler 
an den eventstor weitergeleitet werden. Zum schluss meldet der commandhandler die erfolgreiche bearbeitung des 
commands oder einen fehler.

Generell wird davon ausgegangen, dass ein writer im singeltreaded betrieb un dmehrere query instanzen genutzt werden.
Alle instanzen sind aber singelthreaded und stellen keine anforderungen an thread safety der klassen. 
Dies ist eine annahme, die zur vereinfachung der implementierung dient und es ermöglichen soll sich auf die 
wesentlichen aspekte aus der vorlesung zu konzentrieren. Zudem erschweren multithreaded system performanz messungen,
da eventuell der deterministische ablauf des systems nicht mehr gegeben ist.

## Detailierte beschreibung der Verantwortlichkeiten und der architektur:

### Command
Ist ein einfaches java record.

### CommandDispatcher
Der Command Dispatcher nutzt Reflections, um die commands mit dem zugehörigen Command Handler Objekt zu verbinden.
So ist sichergestellt, Das zu einem verfügbaren Handler genau ein Handler existiert. Dies minimiert fehler wenn unerfahrene
entwickler an dem System arbeiten, da das System die Richtigkeit der neuen handler mit deren Commands so weit wie möglich sicherstellt.

### Command Handler Objelkt
Die Verantwortung der command Handler objekte sind die folgenden Punkte:

1. Laden der benötigten Aggregate die zur bearbeitung des commands benötigt werden.
2. Überwachen der bearbeitung der Aggregate und realisieren eines Wiederherstellungsmechanismus im fehlerfall, sodass das domain model wieder in einen Konsitenten zustand zurück überführt wird.
3. Übergabe der erzeugten events an den Event Store
4. Rückmelden ob das Command akzeptiert wurde oder eben nicht.

Beispielhafter Kontrollflus:

    1. Übergabe des Commands an den Handler:
        Der Command wird vom Command Handler empfangen und weiter verarbeitet.

    2. Abruf der benötigten Aggregates über die Repositories:
        Der Handler ruft die benötigten Aggregates über die jeweiligen Repositories ab. Diese Repositories sind typischerweise für das Laden des aktuellen Zustands der Aggregate verantwortlich.
        Falls ein benötigtes Aggregate nicht gefunden wird, kann der Handler sofort abbrechen und einen entsprechenden Fehler zurückgeben.

    3. Übergabe der Command-Informationen an die Aggregates:
        Der Command Handler delegiert den Command an die beteiligten Aggregates.
        Jedes Aggregate führt dabei seine eigenen Prüfungen und Logik aus und erzeugt bei erfolgreicher Verarbeitung entsprechende Events, die dann gesammelt werden können.

    4. Prüfung auf Fehler während der Verarbeitung:
        Falls eines der Aggregates die Ausführung des Commands verweigert (z. B. durch das Auslösen einer Exception), stoppt der Prozess und der Fehler wird behandelt.

Erfolgreicher Ablauf:

    5. Einsammeln der Events aus den beteiligten Aggregates:
        Alle durch die Aggregates generierten Events werden gesammelt und zur Verarbeitung bereitgestellt.

    6. Commit der Events (Persistenz in den Event Store):
        Der Handler übergibt die Events an den Event Store (z. B. durch Transaktions-Commit).
        Falls der Commit erfolgreich ist, wird der neue Zustand in den Aggregates als dauerhaft und final betrachtet.

    7. Rückgabe der Information, dass der Command akzeptiert wurde:
        Der Handler gibt zurück, dass das Command erfolgreich bearbeitet wurde.

Fehlerhafter Ablauf:

    8. Rollback der bereits erfolgreich veränderten Aggregates:
        Falls während der Verarbeitung ein Fehler auftritt, führt der Handler ein Rollback durch, um bereits vorgenommene Änderungen in den Aggregates rückgängig zu machen.
        Im Event Sourcing-Kontext bedeutet dies oft, dass kompensierende Events ausgelöst oder dass die Transaktion vollständig abgebrochen wird, sodass die Änderungen gar nicht erst persistiert werden.

    9. Rückgabe eines Fehlers, dass das Command nicht durchgeführt werden konnte:
        Der Handler gibt eine Fehlermeldung zurück, dass der Command nicht akzeptiert wurde und keine Änderung am Zustand vorgenommen wurde.

An dieser Stelle habe ich mich für eine Sychron API entschieden. Es wäre ebenfalls eine asynchrone api auf der write Seite denkbar gewesen, die ein bearbeitung eines commands über ein dezidiertes query model auf der query seite zurückgibt.
Dies führt jedoch zu dem Problem, dass der Produzent von kommands entscheiden muss, ob er ein nachfolgendes Kommand schickt oder ob er das vorangegangene kommand wiederholt, da es aus irgendeinem grund nicht verarbeitet werden konnte.
Bei einer Asynchron api gibt es keine garantien, wann das kommand terminiert. Somit kann der Produzent nur mit einem timeout arbeiten, was bei commands die chronologische abhngikeiten besizen zu anomalien fhren kann.

### Aggregate Repository:

Das Aggregate repository kapselt das replay für den Aufbau der angeforderten aggregates. In userem fall ist das zwar erst einmal irelevant aber es soll hier trotzdem festgehalten werden. 
Jedes Repository kappselt dabei ein bestimmten aggregate typ. Das repo ist als singelton umgesetzt, um mögliche anomalien durch das validieren gegen verschiedene repositories des selben typs aber mit unterschiedlichen zuständen zu vermeiden.
Das Repo ist absichtlich nicht thread safe ausgelegt(z.B. weglassen des syncronized bei singeltons zugunsten der performanz), da dies die komplexität der klassen deutlich steigern würde. Dies trifft auf alle klassen der writer site zu. Zudem ist der laod (wie auch in der Vorlesung erwähnt)
typischerweise um größenordnungen kleiner als auf der read seite, wesshalb hier eine einzige writer instanz als ausreichend angenommen wird.

Wenn später skaliert werden soll müsste man sich einen syncronistations mechanismus überlegen, damit nicht zwei konkurierende kommands parallel ausgeführt werden.
Dafür gäbe es zwei optione:
1. Entweder wird ein lock auf dem aggregate implementiert, so dass das aggregate nur von einem command gleichzeitig verändert werden kann. Das birgt aber die gefahr eines deadlocks besonders dann wenn mehrere aggregates an einem command beteiligt sind.
2. Eine Alternative wäre optimistic concurancy, bei dem eine dritte instan anschliessend die konflikte auflösst, was aber bei einem event sourcing system vermutlich sehr schwer ist...

Ein weiterer punkt der bedacht werden sollte ist ein rollback mechanismus, der verhindert, dass im fehlerfall partielle änderungen von einem aggregate das erfolgreich verändert wurde im 
system verbleiben, wenn ein zweites aggregate nicht erfolgreich verändert werden konnte.

In unserem fall ist dies erst einmal nicht so wichtig, da es nur das aggregate vehicle gibt welches immer alleine verändert wird.

Ein einfacher mechanismus zum lösen diese problem wäre, dass events in einer transaction nur dann in den eventstore geschrieben werden, wenn alle aggregates
erfolgreich verändert wurden. dies ervordert aber eine trennung des event store und des repositories, so dass das aggregate welches verändert wurde seine events 
an den handler übergibt, welcher im erfolgsfall alle events in den eventstore schreibt. beim nächsten event liesst das repository die events wiedrer ein und erzuegt so aktuelle aggregates.
Das erschwärt jedoch die umsetzung eines möglichen snapshot mechanismus in dem repository.

### Aggregate:

Das Aggregate kappselt die apllication logic und die entities und value objects. Gegen die aggregates werden später durch den komandhandler validierungen iniziert.
Bei einer erfolgreichen durchführung geben diese die zugehörigen events zurück. Bei einem fehler wird eine Exception geworfen, die im commandhandler abgefangen werden muss, um das system fehlertollerant zu halten.

Die in dem Aggregate erzeugten events werden aktuell über eine Liste zurückgegeben was ok ist. In einer erweiterten version könnten diese an 
einen EventPublischer direkt im aggregate übergeben werden. Dies ist aber nur eine optimierungs idee.Dies kann auch problematisch sein, da   
dies die logik wie dinge zusammen hängen verstecken kann.

### Event Store

Der Event Store besteht aus mehreren teilen, um die speicher technologie austauschbar zu machen. Zum beispiel kafka, jms , pulsar. 
Ich dachte es wäre eine sinnvolle architektur, eine Event store Interface klasse anzubieten, in die ein connector injiziert wird, 
der von einem connector  interface abgeleitet ist. Das Event Store Interface enthält genrische logik retry mechanismen, logging und error handling. 
Der connector enthält die spezifische übersetzung auf das jeweilige speicher system.

Vorteile dieses Ansatzes in der Praxis

    * Austauschbarkeit des Speichersystems: Um auf ein anderes Event-System zu wechseln, muss nur ein neuer Connector erstellt werden. Das Event Store Interface bleibt unverändert.
    * Einfache Erweiterbarkeit: Zusätzliche Speichersysteme können leicht unterstützt werden, indem man neue Connector-Implementierungen erstellt.
    * Kapselung der Infrastrukturlogik: Die Event Store-Logik bleibt konsistent, und die Infrastrukturabhängigkeiten sind auf die Connectoren beschränkt.

Auf transaktionen wird erst einmal verzichtet, da diese nicht gerade einfach für die einzelnen subsysteme umzusetzen sind. Zudem muss darüber nachgedacht werden, ob die anforderungen
und das einsazzenario den einsatz von transaktionen rechtfertigen,da sie einen nicht unerheblichen einfluss auf die Performanz des system haben.

### Events

Die Events wissen wie sie serialisiert werden können und sind einfache java objekte ohne applikationslogik. Die Events sind mit dem Eventstore
in einem eigenen package organisiert, welches gemeinsam von der writer und der reader seite genutzt wird als eine art contract. Tests von writer und reader 
testen gegen die events, sodass relevante änderungen an den bestehenden events auffallen, wenn diese geändert werden, ohne das 
die andere seite darüber benachrichtigt worden ist. So sollte weitestgehend sichergestellt sein, dass beide seiten sich immer verstehen können.
Um die kohäsion der reader und writer packete nicht zu schwächen nutzen diese eigene domain events die die logik nethalten, um die events in die store events zu übersetzen.