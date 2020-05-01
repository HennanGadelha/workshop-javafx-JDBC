package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
	
	
	public void  setDepartmentService(DepartmentService service) {
		
		this.service = service;
	}
	
	

	public void setDepartment(Department department) {

		this.department = department;
	}

	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		
		if(this.department == null) {
			
			throw new IllegalStateException("Departmento nulo");
		}
		
		if(this.service == null) {
			
			throw new IllegalStateException("service nulo");
		}
		
		
		try {
			department = getFormData();
			service.saveOrUpdate(department);
			Utils.currentStage(event).close();
		} catch (DbException e) {
			
			Alerts.showAlert("Error ao cadastrar departamento", null, e.getMessage(), AlertType.ERROR);
			
		}
		
	}

	private Department getFormData() {
		
		Department dep = new Department();
		
		dep.setId(Utils.tryParseToInt(txtId.getText()));
		dep.setName(txtName.getText());

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

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeNodes();

	}

}
