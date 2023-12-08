package hospitalManagementSystem;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

//This class handles operations related to doctors
public class Doctors
{
	private Connection connection;			//created an instance of Connection interface;value will be assigned through main class
	
	public Doctors(Connection connection)		//Parameterized constructor
	{
		this.connection = connection;
		
	}
	
    // Creates the Doctors table in the database if it does not exist
	public void createTable() throws Exception
	{
		DatabaseMetaData dbMetaData = connection.getMetaData(); 						//the DatabaseMetaData object is used to get information about the database
		ResultSet tables = dbMetaData.getTables(null, null, "Doctors", null);			//The getTables method checks if there is a table named "Doctors"

																						// If the ResultSet from getTables has an entry,
		if(tables.next())																//i.e., If tables.next() returns true, it means the table already exists
		{
            // If table exists
            System.out.println("The Table 'Doctors' already exists in the database.");
        } 
        else
        {
            // If table does not exist, create it
		Statement statement = connection.createStatement();
		
		String query = "Create table if not exists DOCTORS("
				+"Id int Primary key auto_increment, "
				+"Name Varchar(50) unique not null, "
				+"Department varchar(50) not null)";
		
		statement.executeUpdate(query);
        System.out.println("Doctors Table Created Successfully.");
        }
	}
	
	
	
    // Inserts a new Doctor into the database 
	public void addDoctor() throws Exception				//throws the exception which occures while connecting to database
	{
		Scanner scanner = new Scanner(System.in);
		System.out.print("\nEnter Doctor Name: ");
		String name = scanner.nextLine();
		System.out.print("Enter Doctor's Department: ");
		String department = scanner.next();
		
		String query = "Insert into Doctors(name, department) values(?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, department);
		
		int entries = preparedStatement.executeUpdate();
		if(entries==0)
		{
			System.out.println("Failed to add Doctor!!");
		}
		else
		{
			System.out.println("Doctor added Succesfully!!");
		}
		
	}
	
	
    // Retrieves and displays all Doctor records from the database		
	public void viewDoctors() throws Exception
	{
		String query = "select * from Doctors";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.isBeforeFirst())
		{
	        System.out.println("No records found in Doctors table");
	        return;
	    }
		
		System.out.println("\nDoctors Details: ");
		System.out.println("==================");
		System.out.println("+------------+------------------------+--------------------+");
		System.out.println("|            |                        |                    |");
		System.out.println("| Doctor id  | Name                   | Department         |");
		System.out.println("|            |                        |                    |");
		System.out.println("+------------+------------------------+--------------------+");
		
		while(resultSet.next())			//Repeats the loop until the data exists
		{
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			String department = resultSet.getString("Department");
			
			System.out.printf("| %-10s | %-22s | %-18s |\n",id, name, department);
			System.out.println("+------------+------------------------+--------------------+");
		}
	}
	
    // Updates the name of a patient in the database
	public void updateDoctor() throws Exception
	{
		Scanner scanner = new Scanner(System.in);
		System.out.print("\nEnter Doctor ID to Update: ");
		int id = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter New Doctor Name: ");
		String newName = scanner.nextLine();
		
		String query = "Update Doctors set name = ? where id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setString(1, newName);
		preparedStatement.setInt(2, id);
		
		int update = preparedStatement.executeUpdate();
		
		if(update==0)
		{
			System.out.println("Failed to Update Doctor Name!");
		}
		else
		{
			System.out.println("Doctor Name is updated Successfully!");
		}
	}
	
//	This method is used to fetch the Doctor's record by passing patient id; if the patient is exists
	public boolean checkDoctor(int id) throws Exception
	{
		String query = "select * from Doctors where id = ?";
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
	public void deleteDoctor() throws Exception
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter Doctor Id to Remove Records From The Database: ");
		int id = scanner.nextInt();
		
		String query = "delete from Doctors where id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setInt(1, id);
		int deleted = preparedStatement.executeUpdate();
		
		if(deleted==0)
		{
			System.out.println("Failed to Remove Doctor!");
		}
		else
		{
			System.out.println("Doctor Record Removed Successfully!");
		}
	}

}

