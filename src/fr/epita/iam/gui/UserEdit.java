/**
 * 
 */
package fr.epita.iam.gui;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.exceptions.UpdateException;
import fr.epita.iam.services.users.dao.UserDAOManager;

/**
 * @author Samer Masaad
 */
public class UserEdit {

	private JFrame frmIamEdit;
	private static JTextField identityID;
	private static JPasswordField oldPassword;
	private static JTextField userName;
	private static User from;
	private JPasswordField newPassword;
	private JPasswordField repeatPassword;

	/**
	 * Launch the application.
	 */
	public static void main(User user) {
		// User user = new User("samermassad", null, 106);
		from = user;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserEdit window = new UserEdit();
					window.frmIamEdit.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				userName.setText(user.getUserName());
				identityID.setText(String.valueOf(user.getIdentityID()));
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserEdit() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIamEdit = new JFrame();
		frmIamEdit.setResizable(false);
		frmIamEdit.setTitle("IAM - Edit User");
		frmIamEdit.setBounds(100, 100, 450, 300);
		frmIamEdit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblEditIdentity = new JLabel("Edit User:");

		identityID = new JTextField();
		identityID.setEditable(false);
		identityID.setColumns(10);

		JLabel label = new JLabel("Identity UID:");

		oldPassword = new JPasswordField();
		oldPassword.setColumns(10);

		JLabel lblOldPassword = new JLabel("Password:");

		userName = new JTextField();
		userName.setColumns(10);

		JLabel label_2 = new JLabel("Username:");

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmIamEdit.dispose();
			}
		});

		JButton btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String oldPwd = null;
				String newPwd = null;
				String repeatPwd = null;
				boolean success = true;

				oldPwd = String.valueOf(oldPassword.getPassword());
				int iID = Integer.parseInt(identityID.getText());

				if (!oldPwd.isEmpty()) {
					try {
						success = GUIMethods.checkPassword(iID, oldPwd);
						if (!success) {
							String errorMessage = "Password is wrong.";
							JOptionPane.showMessageDialog(null, errorMessage, "Update failed",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (SearchException e2) {
						String errorMessage = "Something wrong happened.";
						JOptionPane.showMessageDialog(null, errorMessage, "Update failed",
								JOptionPane.ERROR_MESSAGE);
					}
					int num = numOfFilledPasswords();

					if (num == 2) {
						newPwd = String.valueOf(newPassword.getPassword());
						repeatPwd = String.valueOf(repeatPassword.getPassword());
						if(!newPwd.equals(repeatPwd)) {
							success = false;
							String errorMessage = "Passwords are not identical.";
							JOptionPane.showMessageDialog(null, errorMessage, "Update failed", JOptionPane.ERROR_MESSAGE);
						}
					} else if (num != 0) {
						success = false;
						String errorMessage = "Please fill both password fields.";
						JOptionPane.showMessageDialog(null, errorMessage, "Update failed", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					success = false;
					String errorMessage = "Please provide the password to edit user.";
					JOptionPane.showMessageDialog(null, errorMessage, "Update failed", JOptionPane.ERROR_MESSAGE);
				}
				if (success) {
					try {
						GUIMethods.userUpdate(from, userName.getText(), newPwd, iID);
						frmIamEdit.dispose();
					} catch (ReadOnlyException | TransformerException | SearchException e1) {
						String errorMessage = "Error occured!";
						JOptionPane.showMessageDialog(null, errorMessage, "Update failed", JOptionPane.ERROR_MESSAGE);
					} catch (UpdateException e2) {
						String errorMessage = "Error occured, possible username duplicate!";
						JOptionPane.showMessageDialog(null, errorMessage, "Update failed", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});

		JLabel lblNewPassword = new JLabel("New Password: (Optional)");

		newPassword = new JPasswordField();
		newPassword.setColumns(10);

		JLabel lblRepeatPassword = new JLabel("Repeat Password:");

		repeatPassword = new JPasswordField();
		repeatPassword.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(frmIamEdit.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(19)
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
								.addComponent(btnSaveChanges).addGap(22))
						.addGroup(groupLayout.createSequentialGroup().addComponent(lblEditIdentity).addContainerGap(378,
								Short.MAX_VALUE))))
				.addGroup(groupLayout.createSequentialGroup().addGap(47).addGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(label_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(userName, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(identityID, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))
						.addGap(36)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblRepeatPassword, GroupLayout.PREFERRED_SIZE, 133,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(repeatPassword, GroupLayout.PREFERRED_SIZE, 133,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(lblNewPassword, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(oldPassword)
										.addComponent(lblOldPassword, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
								.addComponent(newPassword, GroupLayout.PREFERRED_SIZE, 133,
										GroupLayout.PREFERRED_SIZE))));
		groupLayout
				.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
						groupLayout.createSequentialGroup().addGap(19).addComponent(lblEditIdentity).addGap(18)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup().addComponent(label_2)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(userName,
														GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(groupLayout.createSequentialGroup().addComponent(lblOldPassword)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(oldPassword, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup().addComponent(label)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(identityID,
														GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(groupLayout.createSequentialGroup().addComponent(lblNewPassword)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(newPassword, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblRepeatPassword).addGap(6)
								.addComponent(repeatPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnCancel)
										.addComponent(btnSaveChanges))
								.addGap(19)));
		frmIamEdit.getContentPane().setLayout(groupLayout);
	}

	/**
	 * @return
	 */
	protected int numOfFilledPasswords() {
		int num = 0;
		if (newPassword.getPassword().length != 0)
			num++;
		if (repeatPassword.getPassword().length != 0)
			num++;
		return num;
	}

}
