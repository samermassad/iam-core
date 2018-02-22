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

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DeleteException;
import fr.epita.iam.exceptions.ReadOnlyException;
import fr.epita.iam.exceptions.SearchException;
import fr.epita.iam.launcher.Global;
import fr.epita.iam.services.identity.dao.IdentityDAOManager;
import fr.epita.logger.Logger;

/**
 * @author Samer Masaad
 */
public class IdentitySearch {

	private static final Logger LOGGER = new Logger(IdentitySearch.class);
	private JFrame frmIamSearch;
	private JTextField displayName;
	private JTextField email;
	private JTextField uid;
	private JTable table;
	List<Identity> results;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IdentitySearch window = new IdentitySearch();
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
	public IdentitySearch() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIamSearch = new JFrame();
		frmIamSearch.setTitle("IAM - Search Identities");
		frmIamSearch.setBounds(300, 300, 1000, 600);
		frmIamSearch.setLocationRelativeTo(null);
		frmIamSearch.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmIamSearch.getContentPane().setLayout(new BoxLayout(frmIamSearch.getContentPane(), BoxLayout.X_AXIS));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(0.8d);
		frmIamSearch.getContentPane().add(splitPane);

		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);

		JLabel lblIdentitySearch = new JLabel("Identity Search:");

		JLabel lblDisplayName = new JLabel("Display Name:");

		displayName = new JTextField();
		displayName.setColumns(10);

		JLabel lblEmail = new JLabel("Email:");

		email = new JTextField();
		email.setColumns(10);

		JLabel lblUniqueId = new JLabel("Unique ID:");

		uid = new JTextField();
		uid.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					results = GUIMethods.identitySearch(displayName.getText(), email.getText(), uid.getText());
				} catch (SearchException e1) {
					String errorMessage = "Error occured while searching, please try again.";
					JOptionPane.showMessageDialog(null, errorMessage, "Search failed", JOptionPane.ERROR_MESSAGE);
				}
				
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setRowCount(0);
				int length = results.size();

				for (int i = 0; i < length; i++) {
					Identity current = results.get(i);
					Object[] objects;
					if(Global.isReadOnly()) {
						objects = new Object[] { i + 1,
								current.getDisplayName(),
								current.getEmail(),
								current.getUid()
						};
					} else {
						objects = new Object[] { i + 1,
								current.getDisplayName(),
								current.getEmail(),
								current.getUid(),
								"Edit Identity",
								"Delete Identity"
						};
					}
					model.addRow(objects);
							
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
							.addComponent(displayName, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
							.addComponent(lblEmail)
							.addComponent(email))
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
							.addComponent(lblUniqueId)
							.addComponent(uid))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnCancel)
							.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
							.addComponent(btnSearch)))
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
					.addComponent(displayName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblEmail)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblUniqueId)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(uid, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 309, Short.MAX_VALUE)
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

		String[] columns;
		if(Global.isReadOnly()) columns = new String[] { "ID", "Display Name", "Email", "Unique ID"};
		else columns = new String[] { "ID", "Display Name", "Email", "Unique ID", "Edit", "Delete" };
		
		DefaultTableModel model = new DefaultTableModel(columns , 0);

		// create table with data
		table = new JTable();
		table.setModel(model);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable)e.getSource();
			    int row = target.getSelectedRow();
			    int column = target.getSelectedColumn();
			    if(column == 4) {
			    	IdentityEdit.main(results.get(row));
			    } else if (column == 5) {
			    	int answer = JOptionPane.showConfirmDialog(null, "Delete identity: " + results.get(row));
			    	if(answer == 0) {
			    		try {
							GUIMethods.identityDelete(results.get(row));
							DefaultTableModel model = (DefaultTableModel) table.getModel();
							model.removeRow(row);
						} catch (Exception e1) {
							String errorMessage = "Error occured while deleting, please try again.";
							JOptionPane.showMessageDialog(null, errorMessage, "Delete failed", JOptionPane.ERROR_MESSAGE);
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
