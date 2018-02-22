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
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.CreationException;
import fr.epita.iam.exceptions.DuplicateException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.services.identity.dao.IdentityDAOManager;
import fr.epita.logger.Logger;

/**
 * @author Samer Masaad
 */
public class IdentityAdd {

	private static final Logger LOGGER = new Logger(IdentityAdd.class);
	private JFrame frmIamAdd;
	private JTextField displayName;
	private JTextField email;
	private JTextField uid;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IdentityAdd window = new IdentityAdd();
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
	public IdentityAdd() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIamAdd = new JFrame();
		frmIamAdd.setLocationRelativeTo(null);
		frmIamAdd.setTitle("IAM - Add Identity");
		frmIamAdd.setBounds(100, 100, 450, 300);
		frmIamAdd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblAddIdentity = new JLabel("Add Identity");

		JLabel lblDisplayName = new JLabel("Display Name:");

		displayName = new JTextField();
		displayName.setColumns(10);

		JLabel lblEmail = new JLabel("Email:");

		email = new JTextField();
		email.setColumns(10);

		JLabel lblUniqueId = new JLabel("Unique ID:");

		uid = new JTextField();
		uid.setColumns(10);

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
				try {
					if(!uid.getText().isEmpty() && uid.getText().indexOf(" ") == -1) {
						GUIMethods.add(displayName.getText(), email.getText(), uid.getText());
						frmIamAdd.dispose();
					} else {
						String errorMessage = "Unique ID cannot be empty or contain spaces.";
						JOptionPane.showMessageDialog(null, errorMessage, "Creation failed", JOptionPane.ERROR_MESSAGE);
					}
				} catch (CreationException | ReadOnlyException | SearchException | TransformerException e1) {
					String errorMessage = "Error occured, please try again.";
					JOptionPane.showMessageDialog(null, errorMessage, "Creation failed", JOptionPane.ERROR_MESSAGE);
				} catch (DuplicateException e2) {
					String errorMessage = "Unique ID already exists, please choose another one.";
					JOptionPane.showMessageDialog(null, errorMessage, "Creation failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(frmIamAdd.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
						.createSequentialGroup().addGap(69)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDisplayName, GroupLayout.PREFERRED_SIZE, 92,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniqueId, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(uid, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
								.addComponent(displayName, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE).addComponent(
										email, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup().addGap(20)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btnCancel)
										.addComponent(lblAddIdentity))))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE).addGap(22)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(19).addComponent(lblAddIdentity).addGap(52)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblDisplayName).addComponent(
						displayName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmail))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblUniqueId).addComponent(
						uid, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 35, Short.MAX_VALUE).addGroup(groupLayout
						.createParallelGroup(Alignment.BASELINE).addComponent(btnCancel).addComponent(btnAdd))
				.addGap(22)));
		frmIamAdd.getContentPane().setLayout(groupLayout);
	}

}
