package jacob.program.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseCreator {

	private static final String 
	DRIVER = "org.sqlite.JDBC",
	JDBC = "jdbc:sqlite:";

	private static final int
	TIMEOUT = 30;

	private static final String
	TABLE_NAME = "teams",
	CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (matchNumber numeric, teamNumber numeric, color text)";

	private static final String[] outputFiles = {
		"tablet1.sqlite",
		"tablet2.sqlite",
		"tablet3.sqlite",
		"tablet4.sqlite",
		"tablet5.sqlite",
		"tablet6.sqlite"
	};

	String outputDirectory;

	List<Input> data;

	public DatabaseCreator(String outputDirectory, List<Input> data) throws ClassNotFoundException {
		// Register SQLite Driver
		Class.forName(DRIVER);

		this.outputDirectory = outputDirectory;
		this.data = data;
	}

	public boolean execute() {
		clearAllFiles();

		for (int i = 0; i < outputFiles.length; i++) {
			String dbURL = JDBC + outputDirectory + "/" + outputFiles[i];
			System.out.println(dbURL);

			Connection conn = null;
			try {
				conn = DriverManager.getConnection(dbURL);
			} catch (SQLException e) {
				System.out.println("Could not open database : " + e.getStackTrace());
				try {conn.close();} catch(Exception ignore){}
				return false;
			} 

			if (conn != null) {
				Statement statement = null;
				try {
					statement = conn.createStatement();
					statement.setQueryTimeout(TIMEOUT);
				} catch (SQLException e) {
					System.out.println("Could not create Statement : " + e.getStackTrace());
					return false;
				}
				
				if (statement != null) {
					// Create table
					try {
						statement.executeUpdate(CREATE_TABLE);
					} catch (SQLException e1) {
						System.out.println(CREATE_TABLE);
						System.out.println("Could not create table : " + e1.getStackTrace());
						try {statement.close();} catch(Exception ignore){}
						return false;
					}
					
					// Insert data
					for (int j = i; j < data.size(); j+= outputFiles.length) {
						Input info = data.get(j);
						String query = "INSERT INTO " + TABLE_NAME + " VALUES('" + 
								info.getMatchNumber() + "', '" +
								info.getTeamNumber() + "', '" +
								info.getColor() + "')";
						try {
							statement.executeUpdate(query);
						} catch (SQLException e) {
							System.out.println("Insert of team " + info.getTeamNumber() + " from match " + info.getMatchNumber() + " failed." +
									e.getStackTrace());
							try {statement.close();} catch(Exception ignore){}
							return false;
						}
					}
				}
				try {statement.close();} catch(Exception ignore){}
			}
			
			try {conn.close();} catch(Exception ignore){}
		}

		return true;
	}

	public void clearAllFiles() {
		for (String file : outputFiles) {
			File tmp = new File(outputDirectory + "/" + file);
			tmp.delete();
		}
	}

}
