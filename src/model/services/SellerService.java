package model.services;

import java.util.List;

import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	SellerDao DaoSeller = DaoFactory.createSellerDao();

	public List<Seller> findAll() {

		return DaoSeller.findAll();

	}

	public void saveOrUpdate(Seller obj) {

		if (obj.getId() == null) {

			DaoSeller.insert(obj);
			//Alerts.showAlert("Departamento", null, "Departamento inserido com sucesso", AlertType.CONFIRMATION);

		} else {

			DaoSeller.update(obj);
			//Alerts.showAlert("Departamento", null, "Departamento alterado com sucesso", AlertType.CONFIRMATION);
		}

	}
	
	public void remove(Seller dep) {
		
		DaoSeller.deleteById(dep.getId());
	}

}
