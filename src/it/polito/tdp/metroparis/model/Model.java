package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private class EdgeTraversedGraphListener implements TraversalListener<Fermata, DefaultEdge> {
		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {	
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
			
	/*		back codifica relazioni del tipo child --> parent
	 * 
	 *      per un nuovo vertice child scoperto devo avere che:
	 *      - child è ancora sconosciuto (non ancora trovato)
	 *      - parent è già stato visitato
	*/		
			Fermata sourceVertex = grafo.getEdgeSource(e.getEdge());
			Fermata targetVertex = grafo.getEdgeTarget(e.getEdge());
			
	/*      se il grafo è orientao, allora source==parent, target==child
	 *      se il grafo non è orientato, potrebbe essere al contrario...
	 */
			if (!backVisit.containsKey(targetVertex) && backVisit.containsKey(sourceVertex))
				backVisit.put(targetVertex, sourceVertex);
			else if (!backVisit.containsKey(sourceVertex) && backVisit.containsKey(targetVertex))
				backVisit.put(sourceVertex, targetVertex);
			
		}

		@Override
		public void vertexFinished(VertexTraversalEvent<Fermata> arg0) {	
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<Fermata> arg0) {	
		}
		
	}
	
	private Graph<Fermata, DefaultEdge> grafo;
	private List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIdMap;
	private Map<Fermata, Fermata> backVisit;
	
	public void creaGrafo() {
		
//      creo il grafo
		grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		
//      aggiungo i vertici
		MetroDAO dao = new MetroDAO();
		fermate = dao.getAllFermate();
		Graphs.addAllVertices(grafo, fermate);
		
//      creo idMap
		fermateIdMap = new HashMap<>() ;
		for (Fermata f : fermate)
			fermateIdMap.put(f.getIdFermata(), f) ;
		
//      aggiungo gli archi:
		
/*      1° opzione: per ogni coppia di vertici fare una query per determinare se esiste l'arco o meno
		for (Fermata partenza : grafo.vertexSet())
			for (Fermata arrivo : grafo.vertexSet())
				if (dao.esisteConnessione(partenza, arrivo))
					grafo.addEdge(partenza, arrivo);
*/
		
//		2° opzione: per ogni vertice fare una query che restituisca l'elenco di tutti i vertici che dovranno essere adiacenti
		for (Fermata partenza : grafo.vertexSet()) {
			List<Fermata> arrivi = dao.stazioniArrivo(partenza, fermateIdMap);
			
			for (Fermata arrivo : arrivi)
				grafo.addEdge(partenza, arrivo);
		}
		
//      3° opzione: singola query che produce "subito" tutto il grafo
		
	}
	
	public List<Fermata> fermateRaggiungibili(Fermata source) {
		List<Fermata> result = new ArrayList<Fermata>();
		backVisit = new HashMap<>();
		
//		GraphIterator<Fermata, DefaultEdge> it = new BreadthFirstIterator<>(grafo, source);
		GraphIterator<Fermata, DefaultEdge> it = new DepthFirstIterator<>(grafo, source);
		
		it.addTraversalListener(new Model.EdgeTraversedGraphListener());
		
		backVisit.put(source, null);  // inseriamo la radice: non ha nessun padre
		
		while (it.hasNext()) {
			result.add(it.next());
		}
		
//		System.out.println(backVisit);
		
		return result;
	}
	
	public List<Fermata> percorsoFinoA(Fermata target) {
		if (!backVisit.containsKey(target)) {
//			il target non è raggiungibile dalla source
			return null;
		}
		
		List<Fermata> percorso = new LinkedList<>();
		Fermata f = target;
		
		while (f != null) {
			percorso.add(0, f);
			f = backVisit.get(f);
		}
		
		return percorso;
	}
	
	public Graph<Fermata, DefaultEdge> getGrafo() {
		return grafo;
	}
	
	public List<Fermata> getFermate() {
		return fermate;
	}

}
