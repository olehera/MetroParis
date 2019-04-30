# Esercizio "Metro Paris"

![Metro Paris](db/database.png "Metro Paris database")

Si vuole costruire un grafo che permetta la ricerca dei percorsi all'interno della Metropolitana di Parigi.

Il grafo dovrà  avere le seguenti caratteristiche:
- il grafo sarà  semplice, orientato, non pesato
- ciascuna `Fermata` corrisponde ad un vertice del grafo
- due vertici saranno collegati da un arco se e solo se esiste almeno una `Connessione` tra le due fermate.

## Costruzione del grafo

Si sperimentino tre diverse modalità  di costruzione degli archi del grafo:

1.  doppio loop sulle coppie di vertici, per ogni coppia fare una query per determinare se esiste l'arco o meno
1.  singolo loop sui vertici, per ogni vertice fare una query che restituisca l'elenco di tutti i vertici che dovranno essere adiacenti
1.  nessun loop, singola query che produce "subito" tutto il grafo.

## Visita del grafo

- Permettere all'utente di selezionare una stazione di partenza.
- Visitare il grafo partendo da tale stazione
     - provare sia con la visita in ampiezza, che in profondità 
- Stampare l'elenco dei vertici raggiunti nella visita


## Estensioni possibili

Si considerino le seguenti possibili estensioni:
- il grafo è un grafo pesato, in cui il peso rappresenta il numero di `Linee` che connettono le due `Fermate`
- il grafo è un multi-grafo, in cui se esistono più `Linee` che collegano due `Fermate`, allora saranno aggiunti altrettanti archi.
     - il multi-grafo viene pesato, calcolando come peso il tempo di percorrenza tra le due `Fermate`. Per tale calcolo si utilizzi la `velocita` dichiarata per la linea, tenendo conto della distanza in linea d'aria tra le due `Fermate`

## Classi del Model
     
![Model Metro Paris](db/model%20classes.png "Model Metro Paris")
    