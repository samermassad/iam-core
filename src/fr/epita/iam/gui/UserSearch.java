/**
 * 
 */
package fr.epita.iam.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.xml.transform.TransformerException;

import fr.epita.iam.datamodel.User;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.services.users.dao.UserDAOManager;
import fr.epita.logger.Logger;

/**
 * @author Samer Masaad
 */
public class UserSearch {

	private static final Logger LOGGER = new Logger(UserSearch.class);
	private JFrame frmIamSearch;
	private JTextField userName;
	private JTextField uid;
	private JTable table;
	List<User> results = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserSearch window = new UserSearch();
					window.frmIamSearch.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserSearch() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIamSearch = new JFrame();
		frmIamSearch.setTitle("IAM - Search Users");
		frmIamSearch.setBounds(300, 300, 1000, 600);
		frmIamSearch.setLocationRelativeTo(null);
		frmIamSearch.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmIamSearch.getContentPane().setLayout(new BoxLayout(frmIamSearch.getContentPane(), BoxLayout.X_AXIS));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(0.8d);
		frmIamSearch.getContentPane().add(splitPane);

		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);

		JLabel lblIdentitySearch = new JLabel("User Search:");

		JLabel lblDisplayName = new JLabel("Username:");

		userName = new JTextField();
		userName.setColumns(10);

		JLabel lblUniqueId = new JLabel("Identity UID:");

		uid = new JTextField();
		uid.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					results = GUIMethods.userSearch(userName.getText(), uid.getText());
				} catch (SearchException e) {
					String errorMessage = "Error occured while searching, please try again.";
					JOptionPane.showMessageDialog(null, errorMessage, "Search failed", JOptionPane.ERROR_MESSAGE);
				}

				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0);
				int length = results.size();

				for (int i = 0; i < length; i++) {
					User current = results.get(i);
					model.addRow(
							new Object[] { i + 1,
									current.getUserName(),
									current.getUid(),
									"Edit User",
									"Delete User"
							});
				}

				table.setModel(model);

			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmIamSearch.dispose();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
							.addComponent(lblIdentitySearch)
							.addComponent(lblDisplayName)
							.addComponent(userName, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnCancel)
							.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
							.addComponent(btnSearch))
						.addComponent(lblUniqueId)
						.addComponent(uid, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblIdentitySearch)
					.addGap(37)
					.addComponent(lblDisplayName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(userName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblUniqueId)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(uid, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 367, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnSearch))
					.addGap(16))
		);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JLabel lblResults = new JLabel("Results:");
		panel_1.add(lblResults, BorderLayout.NORTH);

		DefaultTableModel model = new DefaultTableModel(
				new String[] { "ID", "Username", "Identity UID", "Edit", "Delete" }, 0);

		// create table with data
		table = new JTable();
		table.setModel(model);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable)e.getSource();
			    int row = target.getSelectedRow();
			    int column = target.getSelectedColumn();
			    if(column == 3) {
			    	UserEdit.main(results.get(row));
			    } else if (column == 4) {
			    	int answer = JOptionPane.showConfirmDialog(null, "Delete user: " + results.get(row));
			    	if(answer == 0) {
			    		UserDAOManager dao = new UserDAOManager();
						try {
							dao.delete(results.get(row));
							DefaultTableModel model = (DefaultTableModel) table.getModel();
							model.removeRow(row);
						} catch (ReadOnlyException | TransformerException | DeleteException e1) {
							String errorMessage = "Error occured while deleting, please try again.";
							JOptionPane.showMessageDialog(null, errorMessage, "Delete failed", JOptionPane.ERROR_MESSAGE);
							LOGGER.error("error occured while searching", e1);
						}
			    	}
			    } 
			}
		});
		// add the table to the frame
		JScrollPane scrollPane = new JScrollPane(table);
		
		panel_1.add(scrollPane, BorderLayout.CENTER);

	}
}
