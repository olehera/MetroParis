package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata, DefaultEdge> grafo;
	private List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIdMap;
	
	public void creaGrafo() {
		
		// creo il grafo
		grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		// aggiungo i vertici
		MetroDAO dao = new MetroDAO();
		fermate = dao.getAllFermate();
		Graphs.addAllVertices(grafo, fermate);
		
		// creo idMap
		fermateIdMap = new HashMap<>() ;
		for (Fermata f : fermate)
			fermateIdMap.put(f.getIdFermata(), f) ;
		
		// aggiungo gli archi:
		
		/* 1° opzione: per ogni coppia di vertici fare una query per determinare se esiste l'arco o meno
		for (Fermata partenza : grafo.vertexSet())
			for (Fermata arrivo : grafo.vertexSet())
				if (dao.esisteConnessione(partenza, arrivo))
					grafo.addEdge(partenza, arrivo);
		*/
		
		/* 2° opzione: per ogni vertice fare una query che restituisca l'elenco di tutti i vertici che dovranno essere adiacenti
		for (Fermata partenza : grafo.vertexSet()) {
			List<Fermata> arrivi = dao.stazioniArrivo(partenza, fermateIdMap);
			
			for (Fermata arrivo : arrivi)
				grafo.addEdge(partenza, arrivo);
		}
		*/
		
		// 3° opzione: singola query che produce "subito" tutto il grafo
		
		
		
	}
	
	public Graph<Fermata, DefaultEdge> getGrafo() {
		return grafo;
	}
	
	public List<Fermata> getFermate() {
		return fermate;
	}

}
