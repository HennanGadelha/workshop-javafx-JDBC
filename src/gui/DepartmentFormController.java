package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {

	
	private Department department;
	
	
	
	
	
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
	
	
	public void setDepartment(Department department) {
		
		this.department = department;
	}
	
	
	@FXML
	public void onBtnSaveAction() {
		System.out.println("save");
	}
	
	@FXML
	public void onBtnCancelAction() {
		System.out.println("cancel");
	}
	
	private void initializeNodes() {
		
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		
	}
	
	public void updateFormData() {
		
		
		if(department == null) {
			
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
