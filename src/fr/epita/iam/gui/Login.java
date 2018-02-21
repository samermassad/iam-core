/**
 * 
 */
package fr.epita.iam.gui;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import fr.epita.iam.datamodel.User;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.services.users.dao.UserDAOManager;
import fr.epita.logger.Logger;

/**
 * @author Samer Masaad
 */
public class Login {

	private static final Logger LOGGER = new Logger(Login.class);
	protected static final int MAXTRIES = 3;
	private JFrame frmIamLogin;
	private JTextField username;
	private JPasswordField password;
	private int loginTries = 0;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmIamLogin.setVisible(true);
				} catch (Exception e) {
					LOGGER.error("Failed to load login window",e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIamLogin = new JFrame();
		frmIamLogin.setTitle("IAM - Login");
		frmIamLogin.setResizable(false);
		frmIamLogin.setLocationRelativeTo(null);
		frmIamLogin.setBounds(100, 100, 363, 216);
		frmIamLogin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel lblUsername = new JLabel("Username:");
		
		username = new JTextField();
		username.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		
		password = new JPasswordField(10);
		
		JButton button = new JButton("Login");
		frmIamLogin.getRootPane().setDefaultButton(button);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String usrname = username.getText();
				String pwd = String.valueOf(password.getPassword());
				if(GUIMethods.login(usrname, pwd)) {
					frmIamLogin.dispose();
					Home.main();
				} else {
					loginTries++;
					if(loginTries >= MAXTRIES) {
						String errorMessage = "Maximum number of failed tries reached! Exiting..";
						JOptionPane.showMessageDialog(null, errorMessage, "Login failed", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					} else {
						String errorMessage = "Wrong username or password!";
						JOptionPane.showMessageDialog(null, errorMessage, "Login failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
		});
		
		JLabel lblWelcomeToIam = new JLabel("Welcome to IAM Program");
		GroupLayout groupLayout = new GroupLayout(frmIamLogin.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(267, Short.MAX_VALUE)
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblWelcomeToIam)
					.addContainerGap(226, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(22)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUsername)
						.addComponent(username, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
						.addComponent(password, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(187, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblWelcomeToIam)
					.addGap(18)
					.addComponent(lblUsername)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblPassword)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
					.addComponent(button)
					.addGap(16))
		);
		frmIamLogin.getContentPane().setLayout(groupLayout);
	}
}
