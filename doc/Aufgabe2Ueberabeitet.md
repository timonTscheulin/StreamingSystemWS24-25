Aufgabe 2
Überlegungen zur Aufteilung von Verantwortlichkeiten (Write-Side, Grober Ablauf):

Auf der Write-Seite werden Kommandos von der API erzeugt und an den Kommando-Dispatcher übergeben. Der Dispatcher ruft einen zugehörigen Handler auf, der die benötigten Aggregate aus den Repositorys lädt. Diese Aggregate liefern die passenden Events, die vom Handler an den Event Store weitergeleitet werden. Am Ende meldet der Command-Handler den erfolgreichen Abschluss oder einen Fehler zurück.

Wichtiger Hinweis: Es wird angenommen, dass ein einziger Writer im Single-Threaded-Betrieb läuft, während mehrere Query-Instanzen genutzt werden. Alle Instanzen sind jedoch Single-Threaded und stellen daher keine Anforderungen an die Thread-Sicherheit der Klassen. Diese Annahme dient der Vereinfachung der Implementierung und hilft, sich auf die wesentlichen Aspekte der Vorlesung zu konzentrieren. Multithreaded-Systeme würden Performanz-Messungen erschweren, da der deterministische Ablauf des Systems beeinträchtigt werden könnte.
Detaillierte Beschreibung der Verantwortlichkeiten und der Architektur:
Command:

Ein Command ist ein einfaches Java Record.
CommandDispatcher:

Der Command-Dispatcher nutzt Reflections, um die Commands mit den zugehörigen Command-Handler-Objekten zu verbinden. So wird sichergestellt, dass für jedes verfügbare Command genau ein Handler existiert. Dies minimiert Fehler, insbesondere wenn unerfahrene Entwickler am System arbeiten, da das System die Richtigkeit der neuen Handler automatisch prüft.
Command Handler:

Die Verantwortlichkeiten der Command-Handler-Objekte umfassen:

    Laden der benötigten Aggregate.
    Überwachen der Bearbeitung und Wiederherstellung im Fehlerfall.
    Übergabe der erzeugten Events an den Event Store.
    Rückmeldung, ob das Command akzeptiert wurde.

Beispielhafter Kontrollfluss:

    Übergabe des Commands: Der Command wird an den Handler übergeben und verarbeitet.
    Abruf der benötigten Aggregate: Der Handler lädt die Aggregate über Repositories.
    Verarbeitung des Commands: Der Command wird an die Aggregate delegiert.
    Prüfung auf Fehler: Eventuelle Fehler werden abgefangen und behandelt.

Erfolgreicher Ablauf: 5. Einsammeln der Events: Die erzeugten Events werden gesammelt. 6. Commit der Events: Die Events werden im Event Store persistiert. 7. Rückmeldung: Der Handler bestätigt den erfolgreichen Abschluss.

Fehlerhafter Ablauf: 8. Rollback: Änderungen werden zurückgenommen, falls ein Fehler auftritt. 9. Fehlermeldung: Es wird zurückgemeldet, dass das Command nicht ausgeführt werden konnte.
Entscheidung für eine synchrone API:

Ich habe mich bewusst für eine synchrone API entschieden, da bei einer asynchronen API der Produzent von Kommandos zusätzliche Logik zur Fehlerbehandlung implementieren müsste. Asynchrone APIs bergen das Risiko, dass chronologische Abhängigkeiten zwischen Kommandos zu Anomalien führen können.
Aggregate Repository:

Das Aggregate Repository kapselt das Replay für den Aufbau der angeforderten Aggregate. Jedes Repository verwaltet einen bestimmten Aggregat-Typ und ist als Singleton implementiert, um Anomalien durch unterschiedliche Zustände zu vermeiden. Hinzugefügte Passage Anfang: Diese Architektur ist aktuell nicht thread-safe, da die Write-Seite nicht auf Skalierbarkeit ausgelegt ist. Sollte in Zukunft eine Skalierung nötig sein, müsste ein Synchronisationsmechanismus implementiert werden, um parallele Kommandobearbeitung zu verhindern. Optimistic Concurrency wäre eine mögliche Lösung, jedoch sehr komplex im Event Sourcing Kontext. Hinzugefügte Passage Ende.
Aggregate:

Das Aggregate kapselt die Anwendungslogik, die Entities und die Value Objects. Es führt Validierungen durch und gibt bei erfolgreicher Ausführung Events zurück. Hinzugefügte Passage Anfang: Eine einfache Verbesserung wäre, dass das Aggregate die Events direkt an einen Event Publisher übergibt, statt sie nur zurückzugeben. Hinzugefügte Passage Ende.
Event Store:

Der Event Store soll die Speichertechnologie abstrahieren, um verschiedene Systeme wie Kafka, JMS oder Pulsar unterstützen zu können. Ein generisches Event Store Interface kapselt die Infrastrukturlogik, während der Connector für die konkrete Implementierung zuständig ist. Dies ermöglicht einen einfachen Austausch der Speichertechnologie. Hinzugefügte Passage Anfang: Für die Praxis ist diese Architektur besonders nützlich, da sie eine einfache Erweiterung und Austauschbarkeit der zugrundeliegenden Systeme ermöglicht. Zudem kann die Event-Store-Logik konsistent bleiben, während Änderungen in den Connectoren isoliert bleiben. Hinzugefügte Passage Ende.
Events:

Die Events enthalten ausschließlich Informationen zur Serialisierung und keine Anwendungslogik. Sie sind in einem gemeinsamen Package organisiert, welches von der Write- und Read-Seite als Contract genutzt wird. Änderungen an den Events sollten von beiden Seiten erkannt und getestet werden, um sicherzustellen, dass die Kompatibilität erhalten bleibt.