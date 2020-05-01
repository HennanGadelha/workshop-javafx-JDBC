package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department department;

	private DepartmentService service;

	private List<DataChangeListeners> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErroName;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	public void setDepartmentService(DepartmentService service) {

		this.service = service;
	}

	public void setDepartment(Department department) {

		this.department = department;
	}

	public void subscribeDataChangeListener(DataChangeListeners listener) {

		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtnSaveAction(ActionEvent event) {

		if (this.department == null) {

			throw new IllegalStateException("Departmento nulo");
		}

		if (this.service == null) {

			throw new IllegalStateException("service nulo");
		}

		try {
			
			department = getFormData();
			service.saveOrUpdate(department);
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

	private Department getFormData() {

		Department dep = new Department();

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
		Constraints.setTextFieldMaxLength(txtName, 30);

	}

	public void updateFormData() {

		if (department == null) {

			throw new IllegalStateException("Departamento vazio");
		}

		txtId.setText(String.valueOf(department.getId()));
		txtName.setText(department.getName());

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
