package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private class EdgeTraversedGraphListener implements TraversalListener<Fermata, DefaultWeightedEdge> {
		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {	
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> e) {		
			Fermata sourceVertex = grafo.getEdgeSource(e.getEdge());
			Fermata targetVertex = grafo.getEdgeTarget(e.getEdge());
	 
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
	
	private Graph<Fermata, DefaultWeightedEdge> grafo;
	private List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIdMap;
	private Map<Fermata, Fermata> backVisit;
	
	public void creaGrafo() {
		
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		MetroDAO dao = new MetroDAO();
		fermate = dao.getAllFermate();
		Graphs.addAllVertices(grafo, fermate);
		
		fermateIdMap = new HashMap<>() ;
		for (Fermata f : fermate)
			fermateIdMap.put(f.getIdFermata(), f) ;
		
		for (Fermata partenza : grafo.vertexSet()) {
			List<Fermata> arrivi = dao.stazioniArrivo(partenza, fermateIdMap);
			
			for (Fermata arrivo : arrivi)
				grafo.addEdge(partenza, arrivo);
		}
		
//      aggiungo i pesi agli archi:
		for (ConnessioneVelocita cp : dao.getConnessioneVelocita()) {
			Fermata partenza = fermateIdMap.get(cp.getStazP());
			Fermata arrivo = fermateIdMap.get(cp.getStazA());
			
			double distanza = LatLngTool.distance(partenza.getCoords(), arrivo.getCoords(), LengthUnit.KILOMETER);
			double peso = distanza / cp.getVelocita() * 3600;  // calcolo tempo in secondi
			
			grafo.setEdgeWeight(partenza, arrivo, peso);
		}
		
	}
	
	public List<Fermata> fermateRaggiungibili(Fermata source) {
		List<Fermata> result = new ArrayList<Fermata>();
		backVisit = new HashMap<>();
		
		GraphIterator<Fermata, DefaultWeightedEdge> it = new DepthFirstIterator<>(grafo, source);
		
		it.addTraversalListener(new Model.EdgeTraversedGraphListener());
		
		backVisit.put(source, null);  
		
		while (it.hasNext()) {
			result.add(it.next());
		}
		
		return result;
	}
	
	public List<Fermata> percorsoFinoA(Fermata target) {
		if (!backVisit.containsKey(target)) {
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
	
	public List<Fermata> trovaCamminoMinimo(Fermata partenza, Fermata arrivo) {
		DijkstraShortestPath<Fermata, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(grafo);
		
		GraphPath<Fermata, DefaultWeightedEdge> path = dijkstra.getPath(partenza, arrivo);
		
		return path.getVertexList();
	}
	
	public Graph<Fermata, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public List<Fermata> getFermate() {
		return fermate;
	}

}
