package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller seller;

	private SellerService service;

	private DepartmentService departmentService;

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
	private ComboBox<Department> comboBoxDepartment;

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

	private ObservableList<Department> departments;

	public void setServices(SellerService service, DepartmentService departmentService) {

		this.service = service;
		this.departmentService = departmentService;

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

			throw new IllegalStateException("Seller nulo");
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

		Seller sel = new Seller();

		ValidationException excepetion = new ValidationException("Valididation error");

		sel.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {

			excepetion.addError("name", "Fiel can't be empty");

		}

		sel.setName(txtName.getText());

		// validando email
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {

			excepetion.addError("email", "Fiel can't be empty");

		}

		sel.setEmail(txtEmail.getText());
		// fim validacao email

		
		if(dpBirthDate.getValue() == null) {
			
			excepetion.addError("birthDate", "Fiel can't be empty");
			
		}else {
			
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			sel.setBirthDate(Date.from(instant));
			
		}
		


		// validando base salary

		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {

			excepetion.addError("baseSalary", "Fiel can't be empty");

		}

		sel.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		sel.setDepartment(comboBoxDepartment.getValue());

		if (excepetion.getErrors().size() > 0) {

			throw excepetion;
		}

		return sel;
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
		initializeComboBoxDepartment();

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

		if (seller.getBirthDate() != null) {

			dpBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));

		}

		if (seller.getDepartment() == null) {

			comboBoxDepartment.getSelectionModel();

		} else {

			comboBoxDepartment.setValue(seller.getDepartment());

		}

	}

	public void loadAssociatedObjects() {

		if (departmentService == null) {

			throw new IllegalStateException("Department was null");
		}

		List<Department> deps = departmentService.findAll();
		departments = FXCollections.observableArrayList(deps);
		comboBoxDepartment.setItems(departments);

	}

	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();


		labelErroName.setText(fields.contains("name") ? errors.get("name") : "");
		
		labelErroEmail.setText(fields.contains("email") ? errors.get("email") : "");
		
		labelErroBirthDate.setText(fields.contains("birthDate") ? errors.get("birthDate") : "");
		
		labelErroBaseSalary.setText(fields.contains("baseSalary") ? errors.get("baseSalary") : "");

	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeNodes();

	}

}
