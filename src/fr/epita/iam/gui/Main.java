/**
 * 
 */
package fr.epita.iam.gui;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.datamodel.User;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.exceptions.UpdateException;
import fr.epita.iam.launcher.Global;
import fr.epita.iam.launcher.PrelaunchTests;
import fr.epita.iam.services.identity.dao.IdentityDAOManager;
import fr.epita.iam.services.users.dao.UserDAOManager;
import fr.epita.logger.Logger;

/**
 * @author Samer Masaad
 */
public class Main {
	
	private static final Logger LOGGER = new Logger(Main.class);
	public static void main(String[] args) throws SQLException, CreationException, ReadOnlyException, DuplicateException, SearchException, TransformerException, UpdateException, DeleteException {
		
		PrelaunchTests tests = new PrelaunchTests();
		if(tests.run()) {
			//program can launch
			if(Global.isReadOnly()) {
				String infoMessage = "Running in read-only mode because ";
				infoMessage += !Global.isDBWorking() ? "JDBC connection " : "XML file parsing ";
				infoMessage += "failed!";
				LOGGER.warning(infoMessage);
				JOptionPane.showMessageDialog(null, infoMessage, "Running in read-only mode", JOptionPane.INFORMATION_MESSAGE);
				
			}
			Login.main();
		} else {
			return;
		}
		
	}

}
