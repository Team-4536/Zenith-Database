import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;




import java.util.Properties;
import java.util.logging.*;

import java.sql.*;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

	
public class Main {
	
	static String user = "root";
	static String pass = "4536";

		public static void main(String[] args) {
			new Main().run();
		}
		
		public void run(){
			NetworkTable.setClientMode();
			NetworkTable.setIPAddress("roboRIO-4536.local");
			NetworkTable table = NetworkTable.getTable("SmartDashboard");
			PowerDistributionPanel pdp = new PowerDistributionPanel();
			
			Connection conn = null;
			PreparedStatement stmt = null;
			
			try{
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", user, pass);
			System.out.println("Connected database successfully...");
			
			while (true) {
				try {
					Thread.sleep(1000);
				} catch(InterruptedException ex){
					Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
				}
				
				double mainStickX = table.getNumber("mainStick-Xaxis: ", 0.0);
				double mainStickY = table.getNumber("mainStick-Yaxis: ", 0.0);
				
				String mainStick = "INSERT INTO mainstick (DataTime, Mainstick-XAxis, Mainstick-YAxis)" + 
				" values (NOW(), ?, ?)";
				stmt = conn.prepareStatement(mainStick);
				
				stmt.setDouble(1, mainStickX);
				stmt.setDouble(2, mainStickY);
				
				stmt.execute();
				conn.commit();
				
			}
			
			
			}catch(SQLException se){
			      //Handle errors for JDBC
			      se.printStackTrace();
			}catch(Exception e){
			      //Handle errors for Class.forName
			      e.printStackTrace();
			}
			finally{
			      //finally block used to close resources
			      try{
			         if(stmt!=null)
			            conn.close();
			      }catch(SQLException se){
			      }// do nothing
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			
				
			}
		}

	}

