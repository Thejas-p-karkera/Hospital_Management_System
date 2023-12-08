package hospitalManagementSystem;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

//This class handles operations related to patients
public class Patients
{
	private Connection connection;			//created an instance variable of Connection interface;value will be assigned through main class
	
	public Patients(Connection connection)		//Parameterized constructor
	{
		this.connection = connection;
		
	}
	
    // Creates the Patients table in the database if it does not exist
	public void createTable() throws Exception
	{
		DatabaseMetaData dbMetaData = connection.getMetaData(); 						//the DatabaseMetaData object is used to get information about the database
        ResultSet tables = dbMetaData.getTables(null, null, "Patients", null);			//The getTables method checks if there is a table named "Patients"

        																				// If the ResultSet from getTables has an entry,
        if (tables.next())																//i.e., If tables.next() returns true, it means the table already exists
        {
            // If table exists
            System.out.println("\nThe Table 'Patients' already exists in the database.");
        } 
        else
        {
            // If table does not exist, create it
		Statement statement = connection.createStatement();
		
		String query = "Create table if not exists Patients("
				+"Id INT AUTO_INCREMENT PRIMARY KEY, "
				+"Name VARCHAR(50) unique NOT NULL, "
				+"Age INT NOT NULL, "
				+"Gender VARCHAR(6) NOT NULL)";
		
		statement.executeUpdate(query);
        System.out.println("Patients Table Created Successfully.");
        }
	}
	
    // Inserts a new patient into the database 
	public void addPatient() throws Exception				//throws the exception which occures while connecting to database
	{
		Scanner scanner = new Scanner(System.in);
		System.out.print("\nEnter Patient Name: ");
		String name = scanner.next();
		System.out.print("Enter Patient Age: ");
		int age = scanner.nextInt();
		System.out.print("Enter Patient Gender: ");
		String gender = scanner.next();
		
		String query = "Insert into patients(name, age, gender) values(?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setString(1, name);
		preparedStatement.setInt(2, age);
		preparedStatement.setString(3, gender);
		
		int entries = preparedStatement.executeUpdate();
		if(entries==0)
		{
			System.out.println("Failed to add Patient!!");
		}
		else
		{
			System.out.println("Patient added Succesfully!!");
		}
	}
	
    // Retrieves and displays all patient records from the database	
	public void viewPatient() throws Exception
	{
		String query = "select * from patients";
		
//		Statement statement = connection.createStatement();
//		ResultSet resultSet = statement.executeQuery(query);

		PreparedStatement preparedStatement = connection.prepareStatement(query);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.isBeforeFirst())
		{
	        System.out.println("No records found in Patients table");
	        return;
	    }
		
		System.out.println("\nPatient Details: ");
		System.out.println("==================");
		System.out.println("+------------+------------------------+---------+-----------+");
		System.out.println("|            |                        |         |           |");
		System.out.println("| Patient id | Name                   | Age     | Gender    |");
		System.out.println("|            |                        |         |           |");
		System.out.println("+------------+------------------------+---------+-----------+");
		
		while(resultSet.next())			//Repeats the loop until the data exists
		{
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			int age = resultSet.getInt("age");
			String gender = resultSet.getString("gender");
			
			System.out.printf("| %-10s | %-22s | %-7s | %-9s |\n",id, name, age, gender);
			System.out.println("+------------+------------------------+---------+-----------+");
		}
	}
	
    // Updates the name of a patient in the database
	public void updatePatient() throws Exception
	{
		Scanner scanner = new Scanner(System.in);
		System.out.print("\nEnter Patient ID to Update: ");
		int id = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter New Patient Name: ");
		String newName = scanner.next();

		
		String query = "Update patients set name = ? where id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setString(1, newName);
		preparedStatement.setInt(2, id);
		
		int update = preparedStatement.executeUpdate();
		
		if(update==0)
		{
			System.out.println("Failed to Update Patient Name!");
		}
		else
		{
			System.out.println("Patient Name is updated Successfully!");
		}
	}
	
	
	
//	This method is used to fetch the patient's record by passing patient id; if the patient exists in the database
	public boolean checkPatient(int id) throws Exception
	{
		String query = "select * from patients where id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if(resultSet.next())		//Checks whether any data exists or not
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
    // Deletes a patient record from the database
	public void deletePatient() throws Exception
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter Patient Id to Remove Records From The Database: ");
		int id = scanner.nextInt();
		
		String query = "delete from Patients where id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setInt(1, id);

		int deleted = preparedStatement.executeUpdate();
		
		if(deleted==0)
		{
			System.out.println("Failed to Remove Patient!");
		}
		else
		{
			System.out.println("Patient Record Removed Successfully!");
		}
	}

}
