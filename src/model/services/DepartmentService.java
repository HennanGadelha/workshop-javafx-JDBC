package model.services;

import java.util.List;

import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	DepartmentDao DaoDepartment = DaoFactory.createDepartmentDao();

	public List<Department> findAll() {

		return DaoDepartment.findAll();

	}

	public void saveOrUpdate(Department obj) {

		if (obj.getId() == null) {

			DaoDepartment.insert(obj);
			//Alerts.showAlert("Departamento", null, "Departamento inserido com sucesso", AlertType.CONFIRMATION);

		} else {

			DaoDepartment.update(obj);
			//Alerts.showAlert("Departamento", null, "Departamento alterado com sucesso", AlertType.CONFIRMATION);
		}

	}

}
