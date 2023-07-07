package com.snva.employeelist.service;

import java.util.List;

import com.snva.employeelist.bean.Employee;
import com.snva.employeelist.service.exception.EmployeeServiceException;
/**
 * IEmployeeService is an interface which defines various abstract functions
 * for the service layer.
 */
public interface IEmployeeService
{
	public void addNewEmployee(Employee employee);
	public int removeEmployeeByName(List<Employee> employeelist); // change return value
	public List<Employee> showAllEmployeeInformation() throws EmployeeServiceException;
	
}
