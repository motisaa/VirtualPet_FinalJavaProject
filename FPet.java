package virtualPet;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;


import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JProgressBar;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class FPet extends JFrame {

	//pet and other instances
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField textField;
	private JTextField petName;
	protected String name;
	protected VirtualPet pet;
	//progress bar instances
	private JProgressBar hungerBar;
	private JProgressBar happinessBar;
	private JProgressBar energyBar;
	private JProgressBar hygieneBar;
	private JProgressBar healthBar;
	// bottom instances
	protected JButton btnServer;
	protected JButton btnClient;
	protected JButton btnDisconnect;
	protected JButton btnSend;
	protected JButton btnSleep;
	protected JButton btnLove;
	protected JButton btnFeed;
	protected JButton btnTakeMedicine;
	protected JButton btnClean;
	protected JButton btnPlay;
	protected JButton btnWakeUp;

	//socket instances
	private static final int PORT = 5005;
	private static final int PORT2 = 5006;
	private static final String SERVER = "10.2.1.205";
	private ServerSocket server;
	private Socket client;
	private Socket client2;

	private BufferedReader input;
	private PrintStream output;
	private DataInputStream in;
	private DataOutputStream out;

	private ReadingFromStream b;
	private ReadingInts c;

	private boolean isSocketOpen= true;
	//state instances
	private static final int WITH=0; // with connection, state awake
	private static final int WITHOUT=1; // without connection, state sleeping
	private static final int MUERTO=2;
	private int state = WITHOUT;
	private int statevida=0;


	//DB instances
	private Connection connection;
	private String url = "jdbc:mysql://127.0.0.1:4306/virtualPet";
	private String user = "root";
	private String password = "alumnoalumno";





	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FPet frame = new FPet();
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
	public FPet() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 968, 731);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(253, 245, 230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);


		pet = new VirtualPet(null);


		petName = new JTextField();
		petName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sentName = petName.getText();
				pet.setName(petName.getText());

				if (petName != null) {
					try {
						// Establish a connection to the database
						connection = DriverManager.getConnection(url, user, password);

						// Prepare the SQL query
						String query = "INSERT INTO Pets (petName) VALUE(?)";  
						PreparedStatement statement = connection.prepareStatement(query);
						statement.setString(1, pet.getName()); 
						statement.executeUpdate();

					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}

				if(!sentName.isEmpty()) {
					output.println(sentName);
					petName.setText(petName.getText()); 
				}
			}
		});
		petName.setBounds(125, 17, 114, 21);
		contentPane.add(petName);
		petName.setColumns(10);





		JLabel l = new JLabel("");
		l.setBounds(29, 55, 436, 389);
		l.setIcon(new ImageIcon("./img/cute-gatitobaby.gif")); // /home/motsai/eclipse-workspace/VirtualPet/img/
		//l.setIcon(new ImageIcon(pet.updateImage(pet.getImage())));

		contentPane.add(l);

		btnLove = new JButton("");
		btnLove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pet.giveLove();
				updateProgressBars();
				pet.evolve();
				//update database
				updateDB();
				// Send the changes 
				sendchanges();

			}
		});
		btnLove.setIcon(new ImageIcon("./img/like_1_12.png"));
		btnLove.setBounds(29, 499, 85, 81);
		contentPane.add(btnLove);

		btnFeed = new JButton("");
		btnFeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pet.feed();
				updateProgressBars();
				pet.isDead();
				pet.evolve();
				//update database
				updateDB();
				// Send the changes 
				sendchanges();
			}
		});
		btnFeed.setIcon(new ImageIcon("./img/animal_12.png"));
		btnFeed.setBounds(149, 499, 77, 81);
		contentPane.add(btnFeed);

		btnTakeMedicine = new JButton("");
		btnTakeMedicine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pet.takeMedicine();
				updateProgressBars();
				pet.isDead();
				pet.evolve();
				enableEdition();
				//update database
				updateDB();
				//Send the changes 
				sendchanges();

			}
		});
		btnTakeMedicine.setIcon(new ImageIcon("./img/takemedicine.png"));
		btnTakeMedicine.setBounds(266, 499, 77, 81);
		contentPane.add(btnTakeMedicine);

		btnClean = new JButton("");
		btnClean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pet.clean();
				updateProgressBars();
				pet.isDead();
				pet.evolve();
				enableEdition();
				//update database
				updateDB();
				// Send the changes 
				sendchanges();  
			}
		});
		btnClean.setIcon(new ImageIcon("./img/shower.png"));
		btnClean.setBounds(380, 499, 85, 81);
		contentPane.add(btnClean);

		btnPlay = new JButton("");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pet.play();
				updateProgressBars();
				pet.isDead();
				pet.evolve();

				if(!pet.isAlive()) {
					statevida= MUERTO;
					enableEdition();
					sendchanges();
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						connection = DriverManager.getConnection(url, user, password);

						String getIDQuery = "Select PetID FROM Pets WHERE petName = ?";

						PreparedStatement petIDStatement = connection.prepareStatement(getIDQuery);
						petIDStatement.setString(1, pet.getName());
						ResultSet resultSet = petIDStatement.executeQuery();

						if (resultSet.next()) {
							int petID = resultSet.getInt("PetID");
							System.out.println("PetID: " + petID);
							String lifeQuery = "Update Pets set Alive=? where petID=? and petName=?" ;
							PreparedStatement updatelife = connection.prepareStatement(lifeQuery);
							updatelife.setString(1, "no");
							updatelife.setInt(2, petID);
							updatelife.setString(3, pet.getName());
							updatelife.executeUpdate();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}

				}
				//update database
				updateDB();
				// Send the changes 
				sendchanges();
				enableEdition();
			}
		});
		btnPlay.setIcon(new ImageIcon("./img/wool-ball_12.png"));
		btnPlay.setBounds(505, 499, 85, 81);
		contentPane.add(btnPlay);

		btnSleep = new JButton("");
		btnSleep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pet.sleep();
				updateProgressBars();
				pet.isDead();
				pet.evolve();
				state = WITHOUT; //STATE = SLEEPING
				enableEdition();
				//update database
				updateDB();
				sendchanges();


				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					connection = DriverManager.getConnection(url, user, password);

					String getIDQuery = "Select PetID FROM Pets WHERE petName = ?";

					PreparedStatement petIDStatement = connection.prepareStatement(getIDQuery);
					petIDStatement.setString(1, pet.getName());
					ResultSet resultSet = petIDStatement.executeQuery();

					if (resultSet.next()) {
						int petID = resultSet.getInt("PetID");
						System.out.println("PetID: " + petID);
						String lifeQuery = "Update Pets set Awake=? where petID=? and petName=?" ;
						PreparedStatement updatelife = connection.prepareStatement(lifeQuery);
						updatelife.setString(1, "no");
						updatelife.setInt(2, petID);
						updatelife.setString(3, pet.getName());
						updatelife.executeUpdate();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}





			}
		});
		btnSleep.setIcon(new ImageIcon("./img/kitty_12.png"));
		btnSleep.setBounds(637, 499, 85, 81);
		contentPane.add(btnSleep);


		btnWakeUp = new JButton("");
		btnWakeUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pet.wakeUp();
				updateProgressBars();
				pet.isDead() ;
				pet.evolve();
				state = WITH; //state awake
				enableEdition();
				//update database
				updateDB();
				// Send changes 
				sendchanges();

				//update state to awake
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					connection = DriverManager.getConnection(url, user, password);

					String getIDQuery = "Select PetID FROM Pets WHERE petName = ?";

					PreparedStatement petIDStatement = connection.prepareStatement(getIDQuery);
					petIDStatement.setString(1, pet.getName());
					ResultSet resultSet = petIDStatement.executeQuery();

					if (resultSet.next()) {
						int petID = resultSet.getInt("PetID");
						System.out.println("PetID: " + petID);
						String lifeQuery = "Update Pets set Awake=? where petID=? and petName=?" ;
						PreparedStatement updatelife = connection.prepareStatement(lifeQuery);
						updatelife.setString(1, "yes");
						updatelife.setInt(2, petID);
						updatelife.setString(3, pet.getName());
						updatelife.executeUpdate();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}
		});
		btnWakeUp.setIcon(new ImageIcon("./img/kitty_1_12.png"));
		btnWakeUp.setBounds(776, 499, 85, 81);
		contentPane.add(btnWakeUp);


		btnServer = new JButton("Connect to Server");
		btnServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try (
						ServerSocket server = new ServerSocket(PORT); 
						ServerSocket server2 = new ServerSocket(PORT2)) {
					client = server.accept();
					client2 = server2.accept();

					//setSoLinger closes the socket giving 10mS to receive the remaining data
					//	client.setSoLinger (true, 10);
					//an input reader to read from the socket
					input = new BufferedReader(new InputStreamReader(client2.getInputStream()));
					//to print data out
					output = new PrintStream(client2.getOutputStream());

					//send and recieve int types
					in = new DataInputStream(client.getInputStream());
					out = new DataOutputStream(client.getOutputStream());


	
					runReading();
					runread();

				} catch (IOException ex) {
					ex.printStackTrace();
				}


			}
		});
		btnServer.setBounds(505, 36, 153, 27);
		contentPane.add(btnServer);

		btnClient = new JButton("Connect to client");
		btnClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					client = new Socket(SERVER, PORT);

					client2 = new Socket(SERVER, PORT2);

					input = new BufferedReader(new InputStreamReader(client2.getInputStream()));
					output = new PrintStream(client2.getOutputStream());
				
					in = new DataInputStream(client.getInputStream());
					out = new DataOutputStream(client.getOutputStream());

					runReading();
					runread();
					updateProgressBars();
				}catch (IOException ex) {
					System.err.println("Client -> " + ex.getMessage());
				}
			}
		});
		btnClient.setBounds(670, 36, 153, 27);
		contentPane.add(btnClient);



		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(502, 90, 402, 288);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setColumnHeaderView(textArea);

		btnSend = 
				
				new JButton("");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		btnSend.setIcon(new ImageIcon("./img/send_1_5.png"));
		btnSend.setBounds(850, 400, 57, 47);
		contentPane.add(btnSend);


		happinessBar = new JProgressBar();
		happinessBar.setForeground(Color.BLUE);
		happinessBar.setBounds(96, 620, 99, 15);
		contentPane.add(happinessBar);
		happinessBar.setValue(pet.getHappiness());
		happinessBar.setStringPainted(true);

		JLabel lblHappiness = new JLabel("Happiness:");
		lblHappiness.setBounds(27, 619, 87, 17);
		contentPane.add(lblHappiness);

		hungerBar = new JProgressBar();
		hungerBar.setForeground(Color.BLUE);
		hungerBar.setBounds(266, 621, 99, 14);
		contentPane.add(hungerBar);
		hungerBar.setValue(pet.getHunger());
		hungerBar.setStringPainted(true);

		JLabel lblHunger = new JLabel("Hunger:");
		lblHunger.setBounds(213, 620, 60, 15);
		contentPane.add(lblHunger);

		energyBar = new JProgressBar();
		energyBar.setForeground(Color.BLUE);
		energyBar.setBounds(438, 621, 105, 14);
		contentPane.add(energyBar);
		energyBar.setValue(pet.getEnergy());
		energyBar.setStringPainted(true);


		JLabel lblEnergy = new JLabel("Energy:");
		lblEnergy.setBounds(380, 619, 60, 17);
		contentPane.add(lblEnergy);

		hygieneBar = new JProgressBar();
		hygieneBar.setForeground(Color.BLUE);
		hygieneBar.setBounds(626, 621, 99, 14);
		contentPane.add(hygieneBar);
		hygieneBar.setValue(pet.getHygiene());
		hygieneBar.setStringPainted(true);

		JLabel lblHygiene = new JLabel("Hygiene:");
		lblHygiene.setBounds(561, 619, 60, 17);
		contentPane.add(lblHygiene);

		JLabel lblHealth = new JLabel("Health:");
		lblHealth.setBounds(739, 619, 60, 17);
		contentPane.add(lblHealth);

		healthBar = new JProgressBar();
		healthBar.setForeground(Color.BLUE);
		healthBar.setBounds(789, 620, 105, 14);
		contentPane.add(healthBar);
		healthBar.setValue(pet.getHealth());
		healthBar.setStringPainted(true);



		btnDisconnect = new JButton("disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disconnect();
			}
		});
		btnDisconnect.setBounds(831, 36, 105, 27);
		contentPane.add(btnDisconnect);

		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		textField.setBounds(501, 399, 334, 47);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblPetName = new JLabel("Pet Name:");
		lblPetName.setBounds(40, 12, 99, 31);
		contentPane.add(lblPetName);




	}
	
	//methods
	private void updateProgressBars() {
		happinessBar.setValue(pet.getHappiness());
		hungerBar.setValue(pet.getHunger());
		energyBar.setValue(pet.getEnergy());
		hygieneBar.setValue(pet.getHygiene());
		healthBar.setValue(pet.getHealth());
	}

	public void enableEdition() {
		if(state == WITHOUT) {
			btnServer.setEnabled(true);
			btnClient.setEnabled(true);
			btnSend.setEnabled(false);
			btnDisconnect.setEnabled(false);

			btnSleep.setEnabled(false); 
			btnWakeUp.setEnabled(true); 
			btnLove.setEnabled(false);
			btnFeed.setEnabled(false);
			btnTakeMedicine.setEnabled(false);
			btnClean.setEnabled(false);
			btnPlay.setEnabled(false);
		} else if(state== WITH){
			btnServer.setEnabled(false);
			btnClient.setEnabled(false);
			btnSend.setEnabled(true);
			btnDisconnect.setEnabled(true);

			btnSleep.setEnabled(true); 
			btnWakeUp.setEnabled(false);
			btnLove.setEnabled(true);
			btnFeed.setEnabled(true);
			btnTakeMedicine.setEnabled(true);
			btnClean.setEnabled(true);
			btnPlay.setEnabled(true);

		}
		if(statevida== MUERTO) {

			btnSleep.setEnabled(false);
			btnWakeUp.setEnabled(false);
			btnLove.setEnabled(false);
			btnFeed.setEnabled(false);
			btnTakeMedicine.setEnabled(false);
			btnClean.setEnabled(false);
			btnPlay.setEnabled(false);

		}



	}



	private void sendMessage() {
		String sentmessage = textField.getText();
		if(!sentmessage.isEmpty()) {
			output.println(sentmessage);
			textArea.setText( textArea.getText() +"\nMe: " + sentmessage+ "\n"); 
			textField.setText("");
		}
	}

	public void disconnect() {
		try {
			if (client != null && client.isConnected()) {
				client.close();
				isSocketOpen = false;

			}
			if (server != null && !server.isClosed()) {
				server.close();
				isSocketOpen = false;

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {

			state = WITHOUT;
			enableEdition();
		}
	}
	public void runReading() {
		b = new ReadingFromStream(this);
		b.start();
		state = WITH;
		enableEdition();
	}

	public void runread() {
		c = new ReadingInts(this);
		c.start();
		state = WITH;
		enableEdition();
	}
	public BufferedReader getInput() {
		return input;
	}
	public JTextArea getTextArea() {
		return textArea;
	}

	public DataInputStream getIn() {
		return in;
	}

	public int getCurrentindex() {
		return pet.getCurrentStageIndex();
	}
	public void setHappinessBar(int value) {
		happinessBar.setValue(value);
	}

	public void setHungerBar(int value) {
		hungerBar.setValue(value);
	}

	public void setenergyBar(int value) {
		energyBar.setValue(value);
	}
	public void setHygieneBar(int value) {
		hygieneBar.setValue(value);
	}


	public void setHealthBar(int value) {
		healthBar.setValue(value);
	}


	public void setPetName(String name){
		petName.setText(name);
		pet.setName(name);
	}
	public void updateDB() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);

			if (pet.getName() != null) {
				String getIDQuery = "Select PetID FROM Pets WHERE petName = ?";

				PreparedStatement petIDStatement = connection.prepareStatement(getIDQuery);
				petIDStatement.setString(1, pet.getName());
				ResultSet resultSet = petIDStatement.executeQuery();

				//If the ResultSet has a row (if the query obtained some result), the value of PetID is retrieved using getInt("PetID").
				if (resultSet.next()) {
					int petID = resultSet.getInt("PetID");
					pet.setPetID(petID);
					System.out.println("PetID: " + petID);
					String query3 = "Update Pets set Happiness=?,Energy=?,Hunger=?,Hygiene=?,Health=?,lifeStage =? where petID=?" ;
					PreparedStatement updateStatement = connection.prepareStatement(query3);
					updateStatement.setInt(1, pet.getHappiness());
					updateStatement.setInt(2, pet.getEnergy());
					updateStatement.setInt(3, pet.getHunger());
					updateStatement.setInt(4, pet.getHygiene());
					updateStatement.setInt(5, pet.getHealth());
					updateStatement.setString(6, pet.getCurrentLifeStage());
					updateStatement.setInt(7, petID);
					updateStatement.executeUpdate();
				}
			}else {
				String petN = JOptionPane.showInputDialog(null, "Input name of pet:", "Name of your pet",
						JOptionPane.PLAIN_MESSAGE);
				if (petN != null && !petN.isEmpty()) {
					pet.setName(petN);
					petName.setText(petN);
					output.println(petN);
					String query1 = "insert Pets(petName) values(?)";

					PreparedStatement petNameStatement = connection.prepareStatement(query1);
					petNameStatement.setString(1, petN);
					petNameStatement.executeUpdate();

					String getPetIDQ = "Select PetID FROM Pets WHERE petName = ?";

					PreparedStatement petIDStatement = connection.prepareStatement(getPetIDQ);
					petIDStatement.setString(1, petN);
					ResultSet resultSet = petIDStatement.executeQuery();

					if (resultSet.next()) {
						int petID = resultSet.getInt("PetID");
						
						pet.setPetID(petID);
						
						System.out.println("PetID: " + petID);
						String update = "Update Pets set Happiness=?,Energy=?,Hunger=?,Hygiene=?,Health=?,lifeStage=? where petID=?" ;
						PreparedStatement updateStatement = connection.prepareStatement(update);
						updateStatement.setInt(1, pet.getHappiness());
						updateStatement.setInt(2, pet.getEnergy());
						updateStatement.setInt(3, pet.getHunger());
						updateStatement.setInt(4, pet.getHygiene());
						updateStatement.setInt(5, pet.getHealth());
						updateStatement.setString(6, pet.getCurrentLifeStage());
						updateStatement.setInt(7, petID);
						updateStatement.executeUpdate();


					}
				} else {
					JOptionPane.showMessageDialog(null, "Enter pet name", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		} catch (Exception e2) {
			System.out.println("Failed to update happiness,energy,hunger,hygienea and health of pet: " + e2.getMessage());
		}
	}


	public void sendchanges() {
		try {
			out.writeInt(pet.getHappiness());
			out.writeInt(pet.getEnergy());
			out.writeInt(pet.getHunger());
			out.writeInt(pet.getHygiene());
			out.writeInt(pet.getHealth());
			out.writeInt(state);
			out.writeInt(statevida);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}  
	}

}
