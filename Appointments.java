package hospitalManagementSystem;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Appointments
{
private Connection connection;			//created an instance of Connection interface;value will be assigned through main class
	
	public Appointments(Connection connection)		//Parameterized constructor
	{
		this.connection = connection;
		
	}
	
    // Creates the Appointments table in the database if it does not exist
	public void createTable() throws Exception
	{
		DatabaseMetaData dbMetaData = connection.getMetaData(); 							//the DatabaseMetaData object is used to get information about the database
		ResultSet tables = dbMetaData.getTables(null, null, "Appointments", null);			//The getTables method checks if there is a table named "Appointments"
		
																							// If the ResultSet from getTables has an entry,
		if(tables.next())																	//i.e., If tables.next() returns true, it means the table already exists
		{
            // If table exists
			System.out.println("The Table 'Appointments' already exists in the database.");
		}
		else
		{
            // If table does not exist, create it
		Statement statement = connection.createStatement();
		
		String query = "Create table if not exists APPOINTMENTS("
				+"Id int primary key auto_increment, "
				+"Patient_id int not null, "
				+"Doctor_id int not null, "
				+"Appointment_date date not null, "
				+"foreign key (Patient_id) references patients(id), "
				+"foreign key (doctor_id) references doctors(id))";
		
		statement.executeUpdate(query);
        System.out.println("Appointments Table Created Successfully.");
		}
	}
		
	
	public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) throws Exception
	{
	    String query = "select count(*) from appointments where Doctor_id = ? and Appointment_date = ?";
	    PreparedStatement preparedStatement = connection.prepareStatement(query);

	    preparedStatement.setInt(1, doctorId);
	    preparedStatement.setString(2, appointmentDate);

	    ResultSet resultSet = preparedStatement.executeQuery();

	    if (resultSet.next())
	    {
	        int count = resultSet.getInt(1); // Fetch count using column index

	        if (count == 0)
	        {
	            return true;
	        } 
	        else
	        {
	            return false;
	        }
	    }
	    return false;
	}
	
	
	public static boolean checkPatientAvailability(int patientId, String appointmentDate, Connection connection) throws Exception
	{
		String query = "select count(*) from appointments where Patient_id = ? and Appointment_date = ? ";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setInt(1, patientId);
		preparedStatement.setString(2, appointmentDate);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if(resultSet.next())
		{
			int count = resultSet.getInt(1);			//Checking that how many rows are available
			
			if(count == 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	
    // Books a new appointment for a patient with a doctor on a specific date
	public void bookAppointment(Patients patients, Doctors doctors, Connection connection) throws Exception
	{
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("\nEnter patient Id: ");
	    int patientId = scanner.nextInt();
	    System.out.print("Enter Doctor Id: ");
	    int doctorId = scanner.nextInt();
	    System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
	    String appointmentDate = scanner.next();

	    if (patients.checkPatient(patientId) && doctors.checkDoctor(doctorId))
	    {
	        if (checkDoctorAvailability(doctorId, appointmentDate, connection) && checkPatientAvailability(patientId, appointmentDate, connection))
	        {
	            String query = "Insert into appointments(Patient_id, Doctor_id, Appointment_date) values (?, ?, ?)";
	            PreparedStatement preparedStatement = connection.prepareStatement(query);

	            preparedStatement.setInt(1, patientId);
	            preparedStatement.setInt(2, doctorId);
	            preparedStatement.setString(3, appointmentDate);

	            int appointment = preparedStatement.executeUpdate();

	            if (appointment == 0)
	            {
	                System.out.println("Failed to Book Appointment!");
	            }
	            else
	            {
	                System.out.println("Appointment Booked Successfully!");
	            }

	        } 
	        else
	        {
	            System.out.println("Failed to Book Appointment");
	            System.out.println("Either Patient or Doctor is not Available");
	        }
	    } 
	    else
	    {
            System.out.println("Failed to Book Appointment");
	        System.out.println("Invalid Patient or Doctor ID!");
	    }
	}
	
    // Retrieves and displays all appointment records from the database
	public void viewAppointments() throws Exception {
	    String query = "SELECT a.id, a.Patient_id, p.Name, a.Doctor_id, d.Name, a.Appointment_date " +
	                   "FROM Appointments a " +
	                   "INNER JOIN Patients p ON a.Patient_id = p.Id " +
	                   "INNER JOIN Doctors d ON a.Doctor_id = d.Id";
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(query);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.isBeforeFirst())
		{
		    System.out.println("No records found in Appointments table");
		    return;
		}
		
		System.out.println("\nAppointment Details: ");
		System.out.println("=====================");
		System.out.println("+----+------------+------------------+-----------+-----------------+------------------+");
		System.out.println("|    |            |                  |           |                 |                  |");
		System.out.println("| Id | Patient_id | Patient_Name     | Doctor_id | Doctor_Name     | Appointment_date |");
		System.out.println("|    |            |                  |           |                 |                  |");
		System.out.println("+----+------------+------------------+-----------+-----------------+------------------+");
		
	    while (resultSet.next())			//Repeats the loop until the data exists
	    {
	        int appointmentId = resultSet.getInt("a.id");
	        int patientId = resultSet.getInt("a.Patient_id");
	        String patientName = resultSet.getString("p.Name");
	        int doctorId = resultSet.getInt("a.Doctor_id");
	        String doctorName = resultSet.getString("d.Name");
	        String appointmentDate = resultSet.getString("a.Appointment_date");

			System.out.printf("| %-2s | %-10s | %-16s | %-9s | %-15s | %-16s |\n",appointmentId, patientId, patientName, doctorId, doctorName, appointmentDate);
			System.out.println("+----+------------+------------------+-----------+-----------------+------------------+");

	    }

	    resultSet.close();
	    preparedStatement.close();
	}
	
    // Cancels an appointment for a patient with a doctor
	public void deleteAppointment() throws Exception
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter Patient Id to Cancel The Appointment: ");
		int PatientId = scanner.nextInt();
		System.out.print("Enter Doctor Id to Cancel The Appointment: ");
		int doctorId = scanner.nextInt();
		
		String query = "delete from appointments where Patient_id = ? and Doctor_id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.setInt(1, PatientId);
		preparedStatement.setInt(2, doctorId);

		int deleted = preparedStatement.executeUpdate();
		
		if(deleted==0)
		{
			System.out.println("Failed to Cancel Appointment!");
		}
		else
		{
			System.out.println("Appointment Cancelled Successfully!");
		}
	}

}

