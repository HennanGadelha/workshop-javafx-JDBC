package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartamentService {

	
	
	
	
	public List<Department> findAll(){
		
		List<Department> departments = new ArrayList<>();
		
		departments.add(new Department(1, "books"));
		departments.add(new Department(3, "computer"));
		departments.add(new Department(2, "eletronics"));
		
		return departments;
		
	}
	
	
	
}
