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
import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.User;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.NoIdentityFoundException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.services.users.dao.UserDAOManager;
import fr.epita.logger.Logger;

/**
 * @author Samer Masaad
 */
public class UserAdd {

	private static final Logger LOGGER = new Logger(UserAdd.class);
	private JFrame frmIamAdd;
	private JTextField userName;
	private JPasswordField password;
	private JTextField identityID;
	private JPasswordField repeat;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserAdd window = new UserAdd();
					window.frmIamAdd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserAdd() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIamAdd = new JFrame();
		frmIamAdd.setLocationRelativeTo(null);
		frmIamAdd.setTitle("IAM - Add User");
		frmIamAdd.setBounds(100, 100, 450, 300);
		frmIamAdd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblAddIdentity = new JLabel("Add User");

		JLabel lblDisplayName = new JLabel("Username:");

		userName = new JTextField();
		userName.setColumns(10);

		JLabel lblEmail = new JLabel("Password:");

		password = new JPasswordField();
		password.setColumns(10);

		JLabel lblUniqueId = new JLabel("Identity UID:");

		identityID = new JTextField();
		identityID.setColumns(10);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frmIamAdd.dispose();
			}
		});

		JButton btnAdd = new JButton("Add");
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!identityID.getText().isEmpty()) {
					String username = userName.getText();
					String pwd = String.valueOf(password.getPassword());
					String pwd2 = String.valueOf(repeat.getPassword());
					if (!username.isEmpty() && username.indexOf(" ") == -1 && !pwd.isEmpty() && !pwd2.isEmpty()
							&& pwd.equals(pwd2)) {
						try {
							GUIMethods.userAdd(username, pwd, identityID.getText());
							frmIamAdd.dispose();
						} catch (CreationException | ReadOnlyException | SearchException | TransformerException e1) {
							String errorMessage = "Error occured, please try again.";
							JOptionPane.showMessageDialog(null, errorMessage, "Creation failed",
									JOptionPane.ERROR_MESSAGE);
						} catch (DuplicateException e2) {
							String errorMessage = "Unique ID already exists, please choose another one.";
							JOptionPane.showMessageDialog(null, errorMessage, "Creation failed",
									JOptionPane.ERROR_MESSAGE);
						} catch (NoIdentityFoundException e3) {
							String errorMessage = "Identity ID not found, please create a corresponding identity and try again";
							JOptionPane.showMessageDialog(null, errorMessage, "Creation failed",
									JOptionPane.ERROR_MESSAGE);
							IdentityAdd.main();
						}
					} else {
						String errorMessage = "Check your inputs. (usernames cannot contain spaces)";
						JOptionPane.showMessageDialog(null, errorMessage, "Creation failed", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					String errorMessage = "Identity UID cannot be empty.";
					JOptionPane.showMessageDialog(null, errorMessage, "Creation failed", JOptionPane.ERROR_MESSAGE);
				}

			}

		});

		JLabel lblRepeatPassword = new JLabel("Repeat Password:");

		repeat = new JPasswordField();
		repeat.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(frmIamAdd.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(69).addGroup(groupLayout
								.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblUniqueId).addGap(63))
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblEmail, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
										.addGap(24))
								.addGroup(
										groupLayout.createSequentialGroup()
												.addComponent(lblDisplayName, GroupLayout.DEFAULT_SIZE, 102,
														Short.MAX_VALUE)
												.addGap(24))
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblRepeatPassword, GroupLayout.PREFERRED_SIZE, 122,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED))))
						.addGroup(groupLayout.createSequentialGroup().addGap(20)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btnCancel)
										.addComponent(lblAddIdentity))
								.addGap(110)))
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(userName, Alignment.TRAILING)
										.addComponent(password, Alignment.TRAILING).addComponent(repeat)
										.addComponent(identityID, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
								.addGap(82))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
								.addGap(29)))));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(19).addComponent(lblAddIdentity).addGap(21)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblDisplayName).addComponent(
						userName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblEmail).addComponent(
						password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(repeat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addGap(14).addComponent(lblRepeatPassword)))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblUniqueId).addComponent(
						identityID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE).addGroup(groupLayout
						.createParallelGroup(Alignment.BASELINE).addComponent(btnCancel).addComponent(btnAdd))
				.addGap(22)));
		frmIamAdd.getContentPane().setLayout(groupLayout);
	}
}
