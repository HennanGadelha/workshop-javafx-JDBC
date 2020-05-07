package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import db.ValidationException;
import gui.Listeners.DataChangeListeners;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller seller;

	private SellerService service;

	private List<DataChangeListeners> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;

	@FXML
	private Label labelErroName;
	
	@FXML
	private Label labelErroEmail;
	
	@FXML
	private Label labelErroBirthDate;
	
	@FXML
	private Label labelErroBaseSalary;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	public void setSellerService(SellerService service) {

		this.service = service;
	}

	public void setSeller(Seller seller) {

		this.seller = seller;
	}

	public void subscribeDataChangeListener(DataChangeListeners listener) {

		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtnSaveAction(ActionEvent event) {

		if (this.seller == null) {

			throw new IllegalStateException("Sellero nulo");
		}

		if (this.service == null) {

			throw new IllegalStateException("service nulo");
		}

		try {
			
			seller = getFormData();
			service.saveOrUpdate(seller);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
			
		} catch (ValidationException e) {

			setErrorMessages(e.getErrors());

		} catch (DbException e) {

			Alerts.showAlert("Error ao cadastrar departamento", null, e.getMessage(), AlertType.ERROR);

		}

	}

	private void notifyDataChangeListeners() {

		for (DataChangeListeners listener : dataChangeListeners) {

			listener.onDataChanged();

		}

	}

	private Seller getFormData() {

		Seller dep = new Seller();

		ValidationException excepetion = new ValidationException("Valididation error");

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {

			excepetion.addError("name", "Fiel can't be empty");

		}

		dep.setId(Utils.tryParseToInt(txtId.getText()));
		dep.setName(txtName.getText());

		if (excepetion.getErrors().size() > 0) {

			throw excepetion;
		}

		return dep;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		System.out.println("cancel");
		Utils.currentStage(event).close();
	}

	private void initializeNodes() {

		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 80);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
	}

	public void updateFormData() {

		if (seller == null) {

			throw new IllegalStateException("Departamento vazio");
		}

		txtId.setText(String.valueOf(seller.getId()));
		txtName.setText(seller.getName());
		txtEmail.setText(seller.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
		
		if(seller.getBirthDate() != null) {
			
			dpBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
			
		}
		


	}

	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {

			labelErroName.setText(errors.get("name"));

		}

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeNodes();

	}

}
