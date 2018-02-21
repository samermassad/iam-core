/**
 * 
 */
package fr.epita.iam.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import fr.epita.iam.launcher.Global;

/**
 * @author Samer Masaad
 */
public class Home {

	private JFrame frmIamMain;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home window = new Home();
					window.frmIamMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIamMain = new JFrame();
		frmIamMain.setTitle("IAM - Main Menu");
		frmIamMain.setResizable(false);
		frmIamMain.setBounds(100, 100, 450, 300);
		frmIamMain.setLocationRelativeTo(null);
		frmIamMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblMainMenu = new JLabel("Main Menu");

		JLabel lblIdentities = new JLabel("Identities:");

		JLabel lblUsers = new JLabel("Users:");

		JButton btnIdentityAdd = new JButton("Add Identity");
		btnIdentityAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnIdentityAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				IdentityAdd.main();
			}
		});

		JButton btnIdentitySearch = new JButton("<html>Search / \r\nEdit / \r\nDelete<br />Identities</html>");
		btnIdentitySearch.setHorizontalAlignment(SwingConstants.CENTER);
		btnIdentitySearch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				IdentitySearch.main();
			}
		});

		JButton btnUserAdd = new JButton("Add User");
		btnUserAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UserAdd.main();
			}
		});

		JButton btnUserSearch = new JButton("<html>Search / \r\n" + "Edit / \r\n" + "Delete<br />Users</html>");
		btnUserSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UserSearch.main();
			}
		});

		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				frmIamMain.dispose();
				Login.main();
			}
		});
		GroupLayout groupLayout = new GroupLayout(frmIamMain.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(24).addComponent(lblMainMenu).addContainerGap(369,
						Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup().addGap(173)
						.addComponent(btnLogout, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(187, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup().addGap(56)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblIdentities, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(btnIdentitySearch, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnIdentityAdd, Alignment.LEADING, GroupLayout.PREFERRED_SIZE,
												146, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblUsers, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnUserSearch, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
								.addComponent(btnUserAdd, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addGap(67)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(24).addComponent(lblMainMenu).addGap(40)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addComponent(lblIdentities).addGap(18)
								.addComponent(btnIdentityAdd))
						.addGroup(groupLayout
								.createSequentialGroup().addComponent(lblUsers).addGap(18).addComponent(btnUserAdd)))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnUserSearch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(btnIdentitySearch, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 47, Short.MAX_VALUE).addComponent(btnLogout)
				.addContainerGap()));
		frmIamMain.getContentPane().setLayout(groupLayout);

		if (Global.isReadOnly()) {
			btnIdentityAdd.setEnabled(false);
			btnUserAdd.setEnabled(false);
		}

	}
}
