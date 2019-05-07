package it.polito.tdp.metroparis;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MetroController {
	
	private Model model;
	
	public void setModel(Model model) {
		this.model = model;
		model.creaGrafo();
		List<Fermata> fermate = model.getFermate();
		cbxPartenza.getItems().addAll(model.getElencoFermate(fermate));
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> cbxPartenza;

    @FXML
    private Button btnFermate;

    @FXML
    private ChoiceBox<String> cbxArrivo;

    @FXML
    private Button btnPercorso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaFermate(ActionEvent event) {
    	Fermata source = null;
    	
    	for (Fermata f : model.getFermate())
    		if (f.getNome().equals(cbxPartenza.getValue()))
    			source = f;
    	
    	List<Fermata> fermate = model.fermateRaggiungibili(source);
    	List<String> s = model.getElencoFermate(fermate);
    	cbxArrivo.getItems().addAll(s);
    	
    	txtResult.clear();
    	for (String st : s)
    		txtResult.appendText(st+"\n");

    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
        Fermata target = null;
    	
    	for (Fermata f : model.getFermate())
    		if (f.getNome().equals(cbxArrivo.getValue()))
    			target = f;
    	
    	List<Fermata> fermate = model.percorsoFinoA(target);
    	List<String> s = model.getElencoFermate(fermate);
    	
    	txtResult.clear();
    	for (String st : s)
    		txtResult.appendText(st+"\n");
    }

    @FXML
    void initialize() {
        assert cbxPartenza != null : "fx:id=\"cbxPartenza\" was not injected: check your FXML file 'Metro.fxml'.";
        assert btnFermate != null : "fx:id=\"btnFermate\" was not injected: check your FXML file 'Metro.fxml'.";
        assert cbxArrivo != null : "fx:id=\"cbxArrivo\" was not injected: check your FXML file 'Metro.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Metro.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Metro.fxml'.";
    }

}