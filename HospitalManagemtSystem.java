package hospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

//This is the main class that interacts with the user
public class HospitalManagemtSystem
{
	public static void main(String[] args) throws Exception
	{
		Scanner scanner = new Scanner(System.in);
		
		String url = "jdbc:mysql://localhost:3306/hospital";
		String userName = "root";
		String passWord = "root123";
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(url, userName, passWord);
		
		Patients patients = new Patients(connection);
        Doctors doctors = new Doctors(connection);
        Appointments appointments = new Appointments(connection);

        // Create tables if they do not exist
        patients.createTable();
        doctors.createTable();
        appointments.createTable();
        System.out.println();
		
		while(true)
		{
			System.out.println("===========================");
			System.out.println("HOSPITAL MANAGEMENT SYSTEM");
			System.out.println("===========================");
			System.out.println("1. Add Patient");
			System.out.println("2. Add Doctor");
			System.out.println("3. View Patients");
			System.out.println("4. View Doctors");
			System.out.println("5. Update Name");
			System.out.println("6. Book Appointment");
			System.out.println("7. View Appointment");
			System.out.println("8. Cancel Appointment");
			System.out.println("9. Delete Record");
			System.out.println("10. Exit");
			System.out.print("\nEnter your Choice :");

			int choice = scanner.nextInt();
			
			switch(choice)
			{
				case 1:						//Add Patient
					patients.addPatient();
					System.out.println();
					break;
					
				case 2:						//Add Doctor
					doctors.addDoctor();
					System.out.println();
					break;
					
				case 3:						//View Patients
					patients.viewPatient();
					System.out.println();
					break;
					
				case 4:						//View Doctors
					doctors.viewDoctors();
					System.out.println();
					break;
					
				case 5:						//Update Patient/Doctor Name
					updateName(patients, doctors);
					System.out.println();
					break;
					
				case 6:						//Book Appointment
					appointments.bookAppointment(patients, doctors, connection);
					System.out.println();
					break;
					
				case 7:						//View Appointments
					appointments.viewAppointments();
					System.out.println();
					break;
					
				case 8:						//Cancel Appointments
					appointments.deleteAppointment();
					System.out.println();
					break;
					
				case 9:						//Delete Patient/Doctor Records
					deleteRecords(patients, doctors);
					System.out.println();
					break;
					
				case 10:						//Exit
					return;
					
				default:
					System.out.println("Invalid Choice... Enter a valid Choice!");
					break;
				
			}
			
		}
		
	}
	
    // Updates the name of a patient or doctor based on user input
	public static void updateName(Patients patients, Doctors doctors) throws Exception
	{
		while(true)
		{
			Scanner scanner = new Scanner(System.in);
			
			System.out.println("\nSelect What You Have to Update");
			System.out.println("=================================");
			System.out.println("1. Patient's Name");
			System.out.println("2. Doctor's Name");
			System.out.println("3. Exit");
			System.out.print("\nEnter Your Choice:");
			
			int choice = scanner.nextInt();
			
			switch(choice)
			{
			case 1:
				patients.updatePatient();
				System.out.println();
				break;
			
			case 2:
				doctors.updateDoctor();
				System.out.println();
				break;
				
			case 3:
				return;
				
			default:
				System.out.println("Invalid Choice... Enter a valid Choice!");
				break;
			}
		}
	}
	
    // Deletes a patient or doctor record based on user input
	public static void deleteRecords(Patients patients, Doctors doctors) throws Exception
	{
		while(true)
		{
			Scanner scanner = new Scanner(System.in);
			
			System.out.println("\nSelect What You Have to Delete");
			System.out.println("=================================");
			System.out.println("1. Patient's Record");
			System.out.println("2. Doctor's Record");
			System.out.println("3. Exit");
			System.out.print("\nEnter Your Choice:");
			
			int choice = scanner.nextInt();
			
			switch(choice)
			{
			case 1:
				patients.deletePatient();
				System.out.println();
				break;
			
			case 2:
				doctors.deleteDoctor();
				System.out.println();
				break;
				
			case 3:
				return;
				
			default:
				System.out.println("Invalid Choice... Enter a valid Choice!");
				break;
			}
		}
	}
	
}
