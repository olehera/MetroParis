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
		cbxPartenza.getItems().addAll(model.getFermate());
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Fermata> cbxPartenza;

    @FXML
    private Button btnFermate;

    @FXML
    private ChoiceBox<Fermata> cbxArrivo;

    @FXML
    private Button btnPercorso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaFermate(ActionEvent event) {
    	Fermata source = cbxPartenza.getValue();
    	
    	List<Fermata> fermate = model.fermateRaggiungibili(source);
    	cbxArrivo.getItems().addAll(fermate);
    	
    	txtResult.clear();
    	for (Fermata f : fermate)
    		txtResult.appendText(f.getNome()+"\n");
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
        Fermata target = cbxArrivo.getValue();
        Fermata source = cbxPartenza.getValue();
    	
    	List<Fermata> fermate = model.trovaCamminoMinimo(source, target);
    	
    	txtResult.clear();
    	for (Fermata f : fermate)
    		txtResult.appendText(f.getNome()+"\n");
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