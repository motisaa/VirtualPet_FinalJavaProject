package virtualPet;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class FSignUpSignIn extends JFrame {

	private JPanel contentPane;
	private JTextField username;
	private Connection connection;
	private String url = "jdbc:mysql://127.0.0.1:4306/virtualPet"; // test is the DB name
	private String user = "root";
	private String password = "alumnoalumno";
	private JPasswordField pass;
	private JComboBox<String> petOptions;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FSignUpSignIn frame = new FSignUpSignIn();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FSignUpSignIn() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 494, 397);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsername = new JLabel("username:");
		lblUsername.setBounds(50, 100, 90, 17);
		contentPane.add(lblUsername);

		username = new JTextField();
		username.setBounds(139, 98, 114, 21);
		contentPane.add(username);
		username.setColumns(10);

		JLabel lblPassword = new JLabel("password:");
		lblPassword.setBounds(50, 129, 60, 17);
		contentPane.add(lblPassword);

		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					String query = "insert into Users(username, Pass) values(?, ?)";
					Class.forName("com.mysql.cj.jdbc.Driver");
					connection = DriverManager.getConnection(url, user, password);

					PreparedStatement statement = connection.prepareStatement(query);
					statement.setString(1, username.getText());
					statement.setString(2, new String(pass.getPassword()));

					statement.executeUpdate();

					String petName = JOptionPane.showInputDialog(null, "Input name of pet:", "Name of your pet",
							JOptionPane.PLAIN_MESSAGE);
					if (petName != null && !petName.isEmpty()) {

						String query1 = "insert Pets(petName) values(?)";

						PreparedStatement petNameStatement = connection.prepareStatement(query1);
						petNameStatement.setString(1, petName);
						petNameStatement.executeUpdate();

						String query2 = "Select PetID FROM Pets WHERE petName = ?";

						PreparedStatement petIDStatement = connection.prepareStatement(query2);
						petIDStatement.setString(1, petName);
						ResultSet resultSet = petIDStatement.executeQuery();

						if (resultSet.next()) {
							int petID = resultSet.getInt("PetID");
							System.out.println("PetID: " + petID);
							String query3 = "Update Users set petID=? where username=?";
							PreparedStatement userPetStatement = connection.prepareStatement(query3);
							userPetStatement.setInt(1, petID);
							userPetStatement.setString(2, username.getText());
							userPetStatement.executeUpdate();
						}
					}

					JOptionPane.showMessageDialog(null, "Sign up successful!");
					username.setText("");
					pass.setText("");

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSignUp.setBounds(148, 170, 105, 27);
		contentPane.add(btnSignUp);

		petOptions = new JComboBox<String>();
		petOptions.addItem("Create new pet");
		petOptions.addItem("I have an existing pet");
		petOptions.setBounds(285, 173, 150, 21);
		contentPane.add(petOptions);

		JLabel lblSignInText = new JLabel("Have you signed up before? Please try sign in");
		lblSignInText.setBounds(59, 209, 291, 27);
		contentPane.add(lblSignInText);

		JButton btnSignIn = new JButton("Sign in");
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = "SELECT * FROM Users WHERE username = ? AND Pass = ?";

				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					connection = DriverManager.getConnection(url, user, password);

					PreparedStatement statement = connection.prepareStatement(query);
					statement.setString(1, username.getText());
					statement.setString(2, new String(pass.getPassword()));

					ResultSet resultSet = statement.executeQuery();

					if (resultSet.next()) {
						// User and password are correct
						if (petOptions.getSelectedItem().equals("Create new pet")) {
							JOptionPane.showMessageDialog(null, "Sign in successful!");
							FPet pet = new FPet();
							pet.setVisible(true);
							// Close the signup window
							dispose();
						} else {
							String query2 = "SELECT petID FROM Pets, Users WHERE username = ?";

							PreparedStatement petIDStatement = connection.prepareStatement(query2);
							petIDStatement.setString(1, username.getText());
							ResultSet resultSet2 = petIDStatement.executeQuery();

							if (resultSet2.next()) {
								FPet pet = new FPet();
								pet.setVisible(true);
								// Close the signup window
								dispose();
							} else {
								// User has no existing pet
								JOptionPane.showMessageDialog(null, "No pet found for the user", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						// User and password are incorrect
						JOptionPane.showMessageDialog(null, "Invalid username or password", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

					// Close the result set, statement, and connection
					resultSet.close();
					statement.close();
					connection.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSignIn.setBounds(148, 248, 105, 27);
		contentPane.add(btnSignIn);

		pass = new JPasswordField();
		pass.setBounds(139, 129, 114, 21);
		contentPane.add(pass);
	}
}
