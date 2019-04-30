package it.polito.tdp.metroparis.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata, DefaultEdge> grafo;
	private List<Fermata> fermate;
	
	public void creaGrafo() {
		
		// creo il grafo
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		// aggiungo i vertici
		MetroDAO dao = new MetroDAO();
		this.fermate = dao.getAllFermate();
		Graphs.addAllVertices(this.grafo, this.fermate);
		
		// aggiungo gli archi:
		
		/* 1° opzione
		for (Fermata partenza : this.grafo.vertexSet())
			for (Fermata arrivo : this.grafo.vertexSet())
				if (dao.esisteConnessione(partenza, arrivo))
					this.grafo.addEdge(partenza, arrivo);
		*/
		
		/* 2° opzione
		for (Fermata partenza : this.grafo.vertexSet()) {
			List<Fermata> arrivi = dao.stazioniArrivo(partenza);
			
			for (Fermata arrivo : arrivi)
				this.grafo.addEdge(partenza, arrivo);
		}
		*/
		
		// 3° opzione
		
		
		
	}
	
	public Graph<Fermata, DefaultEdge> getGrafo() {
		return this.grafo;
	}
	
	public List<Fermata> getFermate() {
		return this.fermate;
	}

}
