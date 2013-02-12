package jacob.program.utils;

import java.io.File;
import java.util.List;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class DatabaseCreator {
	
	private static final String
	TABLE_NAME = "teams",
	CREATE_TABLE = "CREATE " + TABLE_NAME + " teams(matchNumber INT, teamNumber INT, color INT)";
	
	private static final String[] outputFiles = {
		"tab1.sqlite",
		"tab2.sqlite",
		"tab3.sqlite",
		"tab4.sqlite",
		"tab5.sqlite",
		"tab6.sqlite"
	};
	
	String outputDirectory;
	
	List<Input> data;
	
	public DatabaseCreator(String outputDirectory, List<Input> data) {
		this.outputDirectory = outputDirectory;
		this.data = data;
	}
	
	public void execute() throws SqlJetException {
		// Clear them if they exist
		clearAllFiles();
		// Create files
		SqlJetDb[] db = new SqlJetDb[outputFiles.length];
		int i = 0;
		for (String file : outputFiles) {
			System.out.println("Got here 1");
			File tmp = new File(outputDirectory + "/" + file);
			db[i] = SqlJetDb.open(tmp, true);
			db[i].runTransaction(new ISqlJetTransaction() {
	            public Object run(SqlJetDb db) throws SqlJetException {
	                db.getOptions().setUserVersion(1);
	                return true;
	            }
	        }, SqlJetTransactionMode.WRITE);
			i++;
			System.out.println("Created Database " + i);
		}
		
		for (int j = 0; j < data.size(); j++) {
			ISqlJetTable table = db[j%outputFiles.length].getTable(TABLE_NAME);
			Input d = data.get(i);
			table.insert(Integer.getInteger(d.getMatchNumber()), Integer.getInteger(d.getTeamNumber()), Integer.getInteger(d.getColor()));
			System.out.println("Inserted Data " + j);
		}
		
		for (SqlJetDb tmpDb : db) {
			tmpDb.commit();
		}
	}
	
	private void clearAllFiles() {
		for (String file : outputFiles) {
			File tmp = new File(outputDirectory + "/" + file);
			tmp.delete();
		}
	}
	
}
