package com.snva.employeelist.uiservice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.snva.employeelist.bean.Employee;
import com.snva.employeelist.service.EmployeeServiceImpl;
import com.snva.employeelist.service.IEmployeeService;
import com.snva.employeelist.service.exception.EmployeeServiceException;
import com.snva.employeelist.util.ReadUtil;
import com.snva.employeelist.database.*;


/**
 *This is an EmployeeUIServiceImpl class which implements the IEmployeeUIService
 *interface and defines all the abstract method of the interface.
 */
public class EmployeeUIServiceImpl implements IEmployeeUIService
{
	private ReadUtil m_readUtil;

	private IEmployeeService m_employeeService;

	private ConnectDB connectDB;

	/**
	 *This is the default constructor of the class which creates objects of
	 *all the instance variables of the class.
	 */
	public EmployeeUIServiceImpl()
	{
		m_readUtil= new ReadUtil();
		m_employeeService=new EmployeeServiceImpl();

		////////////////////////////////////////////////////////////////////////
		connectDB = new ConnectDB(DataBaseConfig.driver, DataBaseConfig.url, DataBaseConfig.userName, DataBaseConfig.password);
		if(connectDB.connectToDataBase())
		{
			int state = new TableOperation().creatTable(connectDB.getConnection(),"Employee", "(employeeId Int(5), firstName varchar(20), lastName varchar(20), designation varchar(20), contactNumber double, salary double, dateOfBirth date, dateOfJoining date)");
			if(state == 0)
			{
				System.out.println("======= Sync Data ======");
				ResultSet resultSet = new JdbcCRUD().getAllData(connectDB.getConnection(), "*", "Employee");
				if(resultSet != null)
				{
					try
					{
						int i = 0;
						while (resultSet.next())
						{
							Employee employee = new Employee();
							employee.setEmployeeId(resultSet.getInt("employeeId"));
							employee.setFirstName(resultSet.getString("firstName"));
							employee.setLastName (resultSet.getString("lastName"));
							employee.setDesignation(resultSet.getString("designation"));
							employee.setContactNumber(resultSet.getDouble("contactNumber"));
							employee.setSalary(resultSet.getDouble("salary"));
							employee.setDateOfBirth(resultSet.getDate("dateOfBirth"));
							employee.setDateOfJoining(resultSet.getDate("dateOfJoining"));
							m_employeeService.addNewEmployee(employee);
							i++;
						}
						System.out.println("Get " + i + " records.");
						System.out.println("[Sync Data Success]");
						System.out.println("========= END ========\n");
					}
					catch (SQLException e)
					{
						System.out.println(e);
						System.out.println("[Sync Data Failed]");
						System.out.println("========= END ========\n");
						System.exit(0);
					}
				}
			}
			else if(state == 1)
			{
				System.out.println("[success] create table 'Employee'");
			}
			else
			{
				System.out.println("[Failed] create table 'Employee'");
				System.exit(0);
			}}
		else
		{
			System.out.println("Can Not Connect to DataBase, Exit");
			System.exit(0);
		}
		////////////////////////////////////////////////////////////////////////
	}
	/**
	 *This function read an employee detail and add that employee to the list.
	 */
	public void AddNewEmployee()
	{
		Employee employee=new Employee();
		employee.setFirstName(m_readUtil.readString("Input Your First name","String cannot be empty"));
		employee.setLastName(m_readUtil.readString("Input your Last name","Sring cannot be empty"));
		employee.setDesignation(m_readUtil.readString("Input Designation","String cannot be empty"));
		employee.setContactNumber(m_readUtil.readDouble("Input Contact number","Input correct values"));
		employee.setSalary(m_readUtil.readDouble("Input Your Salary","Input Correct Values"));
		employee.setDateOfBirth(m_readUtil.readDate("Input Date of Birth (DD-MM-YYYY)","enter valid date format(DD-MM-YYYY)"));
		employee.setDateOfJoining(m_readUtil.readDate("Input Date Of Joining (DD-MM-YYYY)","enter valid date format(DD-MM-YYYY)"));
		m_employeeService.addNewEmployee(employee);

		////////////////////////////////////////////////////////////////////////
		int result = new JdbcCRUD().insertData(connectDB.getConnection(),
				"`Employee`",
				"(`employeeId`, `firstName`, `lastName`, `designation`, `contactNumber`, `salary`, `dateOfBirth`, `dateOfJoining`)",
				"("+ employee.getEmployeeId() + ", '" + employee.getFirstName() + "','" +employee.getLastName() + "','" + employee.getDesignation() + "',"+ employee.getContactNumber() + ","+employee.getSalary()+",'" +  new java.sql.Date(employee.getDateOfBirth().getTime()) +"','" +  new java.sql.Date(employee.getDateOfJoining().getTime())+"')");
		if(result == 0)
		{
			List<Employee> tmp = new ArrayList<>();
			tmp.add(employee);
			System.out.println("[DataBase Error] RollBack");
			m_employeeService.removeEmployeeByName(tmp);
		}

		////////////////////////////////////////////////////////////////////////
	}
	/**
	 * This function remove an employee if the list contains that employee.
	 * It reads the employee name from the user and search for it in the list then
	 * make a call for removing that employee.
	 * @exception EmployeeServiceException This exception is thrown when user
	 * want to see the employee detail and the list is empty.
	 */
	public void removeEmployee()
	{
		List<Employee> employeelist1;
		try
		{
			List<Employee> employeelist=m_employeeService.showAllEmployeeInformation();
			employeelist1=new ArrayList<Employee>();
			String name = m_readUtil.readString("Enter Employee name(or any part of name) : ","String cannot be empty");
			employeelist1=searchEmployeeByName(name);

			int index = m_employeeService.removeEmployeeByName(employeelist1);
			////////////////////////////////////////////////////////////////////////
			if(index >= 0)
			{
				int result = new JdbcCRUD().deleteData(connectDB.getConnection(),"Employee", "employeeId=" + employeelist1.get(index).getEmployeeId());
				if(result == 0)
				{
					m_employeeService.addNewEmployee(employeelist1.get(index));
					System.out.println("[DataBase Error] RollBack");
				}
			}
			////////////////////////////////////////////////////////////////////////
		}
		catch(EmployeeServiceException e)
		{
			System.out.println(e.getMessage());
		}
		catch(NullPointerException e)
		{
			System.out.println("Emloyee not found with this name");
		}
	}
	/**
	 * This function shows the information of all the employee containing in the list.
	 * @exception EmployeeServiceException thrown when user want to see the employee
	 * detail and the list is empty.
	 */
	public void showAllEmployee()
	{
		try
		{
			List<Employee> employeelist=m_employeeService.showAllEmployeeInformation();
			System.out.println("All Employees Information : \n ");
			printList(employeelist);
		}
		catch(EmployeeServiceException e)
		{
			System.out.println(e.getMessage());
		}
	}
	/**
	 * This function search an employee in the list by its name.
	 * @param Name. It can be first name,last name or full name of the employee
	 * @return List. Returns the list of employee having the name from which search is initiated.
	 * @exception EmployeeServiceException This exception is thrown when user
	 * want to see the employee detail and the list is empty.
	 * @exception StringIndexOutOfBoundException Thrown by String methods to
	 * indicate that an index is either negative or greater than the size of the
	 * string.
	 * @exception NullPointerException Thrown when an application
	 * attempts to use null in a case where an object is required.
	 */
	 public List<Employee> searchEmployeeByName(String name) throws NullPointerException {
		List<Employee> employeelist1 = null;
		try {
			name = name.toLowerCase();
			List<Employee> employeelist=m_employeeService.showAllEmployeeInformation();
			employeelist1 = new ArrayList<Employee>();
			Iterator<Employee> employeelistiterator= employeelist.iterator();
			while(employeelistiterator.hasNext()) {
				Employee employee=employeelistiterator.next();
				String fullName = (employee.getFirstName()+" "+employee.getLastName()).toLowerCase();
				if(fullName.contains(name))	{
					employeelist1.add(employee);
				}
			}
			printList(employeelist1);
		}catch(EmployeeServiceException e){
			System.out.println(e.getMessage());
		}
		if((employeelist1==null) || (employeelist1.size()==0))
			throw new NullPointerException();

		return employeelist1;
	}
	 /**
	  * This function sorts the complete list according to first name. It uses the
	  * another class which implements the comparator interface and
	  * its function compareTo().
	  * @exception EmployeeServiceException exception is thrown when user want to
	  * see the employee detail and the list is empty.
	  * @exception ClassCastException Thrown to indicate that the code has attempted
	  * to cast an object to a subclass of which it is not an instance.
	  * @exception UnsupportedOperationException Thrown to indicate that the requested
	  * operation is not supported.
	  */
	public void sortEmployee()
	{
		List<Employee> employeelist1=null;
		try
		 {
			List<Employee> employeelist=m_employeeService.showAllEmployeeInformation();
			employeelist1.addAll(employeelist);

			System.out.println(" Before Sorting ");
			System.out.println("---------------------");
			printList(employeelist);

			System.out.println(" After sorting ");
			System.out.println("---------------------");
			Collections.sort(employeelist1,new FirstNameCompare());
			printList(employeelist1);

		 }catch (EmployeeServiceException e){
			System.out.println(e.getMessage());
		 }catch(ClassCastException e){
			System.out.println(e.getMessage());
		 }catch(UnsupportedOperationException e){
			System.out.println(e.getMessage());
		 }
	 }

	/**
	 * This function prints the details of an Employee.
	 * @param employee Parameter of the employee bean.
	 */
	private void printInfo(Employee employee)
	{
		System.out.println("---------------------------------------");
		System.out.println("Employee ID  "+employee.getEmployeeId());
		System.out.println("First Name : "+employee.getFirstName());
		System.out.println("Last Name : "+employee.getLastName());
		System.out.println("Designation :"+employee.getDesignation());
		System.out.println("Salary : "+employee.getSalary());
		System.out.println("Date Of Birth : "+employee.getDateOfBirth());
		System.out.println("Date of Joining : "+employee.getDateOfJoining());
		System.out.println("-----------------------------------------");
		System.out.println();
	}
	/**
	 * This function prints the details of all Employee which are in the list.
	 * @param employeelist Parameter of the List(Employee bean).
	 */
	private void printList(List<Employee> employeelist)
	{
		Iterator<Employee> employeelistiterator=employeelist.iterator();
		while(employeelistiterator.hasNext())
		{
			Employee employee=employeelistiterator.next();
			System.out.println("---------------------------------------");
			System.out.println("Employee ID : "+employee.getEmployeeId());
			System.out.println("First Name : "+employee.getFirstName());
			System.out.println("Last Name : "+employee.getLastName());
			System.out.println("Designation : "+employee.getDesignation());
			System.out.println("Contact Number : "+employee.getContactNumber());
			System.out.println("Salary : "+employee.getSalary());
			System.out.println("Date Of Birth : "+employee.getDateOfBirth());
			System.out.println("Date Of Joining : "+employee.getDateOfJoining());
			System.out.println("-----------------------------------------");
			System.out.println();
		}
	}
}
