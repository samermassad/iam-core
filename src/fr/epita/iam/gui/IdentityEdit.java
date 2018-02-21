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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.UpdateException;
import fr.epita.iam.services.identity.dao.IdentityDAOManager;

/**
 * @author Samer Masaad
 */
public class IdentityEdit {

	private JFrame frmIamEdit;
	private static JTextField uid;
	private static JTextField email;
	private static JTextField displayName;
	private static Identity from;

	/**
	 * Launch the application.
	 */
	public static void main(Identity identity) {
		from = identity;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IdentityEdit window = new IdentityEdit();
					window.frmIamEdit.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				displayName.setText(identity.getDisplayName());
				email.setText(identity.getEmail());
				uid.setText(identity.getUid());
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IdentityEdit() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIamEdit = new JFrame();
		frmIamEdit.setResizable(false);
		frmIamEdit.setTitle("IAM - Edit Identity");
		frmIamEdit.setBounds(100, 100, 450, 300);
		frmIamEdit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel lblEditIdentity = new JLabel("Edit Identity:");
		
		uid = new JTextField();
		uid.setEditable(false);
		uid.setColumns(10);
		
		JLabel label = new JLabel("Unique ID:");
		
		email = new JTextField();
		email.setColumns(10);
		
		JLabel label_1 = new JLabel("Email:");
		
		displayName = new JTextField();
		displayName.setColumns(10);
		
		JLabel label_2 = new JLabel("Display Name:");
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmIamEdit.dispose();
			}
		});
		
		JButton btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					GUIMethods.identityEdit(from, displayName.getText(), email.getText(), uid.getText());
					frmIamEdit.dispose();
				} catch (ReadOnlyException | UpdateException | TransformerException e1) {
					String errorMessage = "Error occured. Please try again.";
					JOptionPane.showMessageDialog(null, errorMessage, "Update failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(frmIamEdit.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
							.addComponent(btnSaveChanges)
							.addGap(22))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblEditIdentity)
							.addContainerGap(362, Short.MAX_VALUE))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(113)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(displayName, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
						.addComponent(email)
						.addComponent(label_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(label_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(uid, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
					.addContainerGap(138, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addComponent(lblEditIdentity)
					.addGap(17)
					.addComponent(label_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(displayName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(label_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(uid, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnSaveChanges))
					.addGap(19))
		);
		frmIamEdit.getContentPane().setLayout(groupLayout);
	}

}
