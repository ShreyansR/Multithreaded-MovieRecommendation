import java.util.Scanner;
import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
* The Movie Server class creates a multi-threaded server
* that can accept multiple client requests at the same time.
*
* @author  Shreyans Rishi
* @version 1.0
* @since   2019-09-29
*/
public class movieServer extends JFrame
{
   /**
   * This class initializes the Connection between the server and the client
   * and a new connection will be made for every new client.
   * 
   */
	public class Connection implements Runnable
	{
		/**
		* userInput - Keeps track of the input from the client side
		* client - creates a new Socket for each client
		* writer - creates a new PrintWriter for every client 
		* reader - creates a new BufferedReader for every client
		*
		*
		*/
		String userInput;
		Socket client = new Socket();
		PrintWriter writer;
		BufferedReader reader;


		/**
   		* This class initializes the connection between the server and the client
   		* and a new connection will be made for every new client.
		*
		* @param sock - New Socket for every client initialized with a new Connection
   		*/
		public Connection(Socket sock) 
		{ 
			client = sock;
			try 
			{
				writer = new PrintWriter(client.getOutputStream(), true);                   
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} 
			catch (IOException e) 
			{
				try { 
					client.close();
				}catch (IOException ex) {
					System.out.println("Error while getting socket streams.." + ex);
				}
				return;
			}
		}
		
		/**
   		* This method is from the Runnable package
   		* and automatically runs whenever a connection is initialized.
		* 
		* The purpose of this method is to get the input from the client
		* based on a menu system.
		*
		* Menu:
		* Press 1 - For a random movie suggestion
		* Press 2 - For the list of Genres
		* Press 3 - To Exit
   		*/
		@Override
		public void run() 
		{
			try 
			{
				writer.println("Press 1 - For a random movie suggestion, Press 2 - For the list of Genres, Press 3 - To Exit");

				while ((userInput = reader.readLine()) != "3") 
				{
					server_log.append(client.getRemoteSocketAddress() + " picks option "+ userInput + "\n");

					String choose = userInput;
					switch(choose) 
					{
					
						case "1":
						{
							randomMovie();
							break; 
						}

						case "2": {
							categoryMovie();
							break; 
						}
					
						case "3":{ 
							server_log.append("Client Disconnected \n");
							client.close();
							break; 
						}
						
						default:
						{
							writer.println("Please select a valid option");
							break; 
						}
					}
				}
				client.close();
			} 
				
			catch (IOException e) {
				System.out.println("There was an IO exception...");
				System.out.println(e.getMessage());
			}
		}
		
		/**
   		* This method is for generating a random movie
   		* from a text file in the same directory called randomMoviesList.txt.
		* 
		* The purpose of this method is to create a new File object
		* to read the content of this file and save them into an array
		* which can then be used to generate a random movie name using
		* Math.random on that array.
		*
		* This randomly generated movie name is then sent back to the client.
		*
   		*/
		public void randomMovie(){
			int line = 0;
			File randomMoviesList = new File("randomMoviesList.txt");
			String movieNames[] = new String[100];

			try {
				Scanner randomMovieScanner = new Scanner(randomMoviesList);
				while(randomMovieScanner.hasNextLine()){
					movieNames[line] = randomMovieScanner.nextLine();
					line++;
				} 
				randomMovieScanner.close();
			} catch(FileNotFoundException e){
				e.printStackTrace();
			}

			int randomNumber = (int)(Math.random() * line);
			writer.println("Here's a random movie for you to watch: " + movieNames[randomNumber]);
		}

		/**
   		* This method is for generating a random movie based on a category
   		* provided by the user.
		* 
		* The purpose of this method is to create a new File object
		* to read the content of the appropriate category file and save them into an array
		* which can then be used to generate a random movie name using
		* Math.random on that array.
		*
		* A switch-case statement has been used to differentiate
		* between different categories.
		*
   		*/
		public void categoryMovie(){
			writer.println("Pick a category: Comedy, Romance, Action");
			String category = null;
			boolean loopOver = true;

			do {

				try {
					category = reader.readLine();
				}
				catch (Exception e){
					server_log.append("Could not get a movie category from user \n");
				}

				switch (category.toLowerCase()) {
					case "comedy":
					{
						int line = 0;
						File comedyMovies = new File("comedyMovies.txt");
						String comedyMovieNames[] = new String[100];
						
						try{
							Scanner comedyMovieScanner = new Scanner(comedyMovies);
							while(comedyMovieScanner.hasNextLine()){
								comedyMovieNames[line] = comedyMovieScanner.nextLine();
								line++;
							}
							comedyMovieScanner.close();
						} catch(FileNotFoundException e){
							e.printStackTrace();
						}

						int randomNumber = (int)(Math.random() * line);
						writer.println("Here's a comedy movie for you to watch: " + comedyMovieNames[randomNumber]);
						loopOver = false;
						break;
					}

					case "romance":
					{
						int line = 0;
						File romanceMovies = new File("romanceMovies.txt");
						String romanceMovieNames[] = new String[100];

						try{
							Scanner romanceMovieScanner = new Scanner(romanceMovies);
							while(romanceMovieScanner.hasNextLine()){
								romanceMovieNames[line] = romanceMovieScanner.nextLine();
								line++;
							}
							romanceMovieScanner.close();
						} catch(FileNotFoundException e){
							e.printStackTrace();
						}

						int randomNumber = (int)(Math.random() * line);
						writer.println("Here's a romantic movie for you to watch: " + romanceMovieNames[randomNumber]);
						loopOver = false;
						break;
					}

					case "action":
					{
						int line = 0;
						File actionMovies = new File("actionMovies.txt");
						String actionMovieNames[] = new String[100];

						try{
							Scanner actionMovieScanner = new Scanner(actionMovies);
							while(actionMovieScanner.hasNextLine()){
								actionMovieNames[line] = actionMovieScanner.nextLine();
								line++;
							}
							actionMovieScanner.close();
						} catch(FileNotFoundException e){
							e.printStackTrace();
						}

						int randomNumber = (int)(Math.random() * line);
						writer.println("Here's an action movie for you to watch: " + actionMovieNames[randomNumber]);
						loopOver = false;
						break;
					}

					default:
					{
						writer.println("Please enter a valid category.");
						loopOver = true;
						break;
					}
				}
			} while(loopOver == true);
		}
	}

	/**
	* This method acts like a constructor for this class
	* that initializes the GUI components of the server.
	*
	*/
	public movieServer()
	{
		initComponents();
	}

	/**
	* This method is used to create the GUI for the server
	* with different components like a Start, End, and a Clear button
	* and also creates a Text Area for the log of the server. This log
	* is also scrollable.
	*
	*/
	@SuppressWarnings("unchecked")
	private void initComponents() {

		scrollPane = new JScrollPane();
		server_log = new JTextArea();
		start_button = new JButton();
		end_button = new JButton();
		clear_button = new JButton();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Movie Recommendation - Server");
		setName("Server");
		setResizable(false);

		server_log.setColumns(20);
		server_log.setRows(5);
		scrollPane.setViewportView(server_log);

		start_button.setText("Start");
		start_button.setBackground(new Color(0, 0, 0));
		start_button.setForeground(Color.WHITE);
		start_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				startAction(evt);
			}
		});

		end_button.setText("End");
		end_button.setBackground(new Color(220,20,60));
		end_button.setForeground(Color.BLACK);
		end_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				endAction(evt);
			}
		});

		clear_button.setText("Clear");
		clear_button.setBackground(new Color(50,205,50));
		clear_button.setForeground(Color.BLACK);
		clear_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				clearAction(evt);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(scrollPane)
					.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(end_button, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(start_button, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
							.addComponent(clear_button, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
				.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
				.addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(start_button)
					.addComponent(clear_button))
				.addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(end_button)))
		);
		pack();
	}

	/**
	* This method acts like an Action method which is
	* triggered when the Start button is clicked. 
	*
	* When this method is triggered, a new Server
	* is initialized.
	*
	*/
	private void startAction(ActionEvent evt) {
		Thread starter = new Thread(new Server());
		starter.start();
		server_log.append("Server started...\n");
	}

	/**
	* This method acts like an Action method which is
	* triggered when the End button is clicked. 
	*
	* When this method is triggered, the server is
	* stopped and the GUI is closed.
	*
	*/
	private void endAction(ActionEvent evt) {
		// try {
		// 	client.close();
		// 	server_log.append("Server Stopped...");
		// } catch(IOException e){

		// }
		System.exit(1);
	}

	/**
	* This method acts like an Action method which is
	* triggered when the Clear button is clicked. 
	*
	* When this method is triggered, the server log
	* in the Text Area is cleared.
	*
	*/
	private void clearAction(ActionEvent evt) {
		server_log.setText("");
	}	

	/**
	* This is the main method which runs when the file
	* is compiled. This initializes the whole movie server class.
	*
	* @param args Unused
	*
	*/
	public static void main(String args[]) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			@Override
			public void run() {
				new movieServer().setVisible(true);
			}
		});
	}

	/**
	* This is the class where the Threads are created for each clients
	* when the Server is initialized. 
	*
	*/
	public class Server implements Runnable
	{
		@Override
		public void run() 
		{
			try 
			{
				ServerSocket serverSocket = new ServerSocket(1234);
				while(true) 
				{
				Socket client = serverSocket.accept();
				Connection cc = new Connection(client);

				Thread listener = new Thread(new Connection(client));
				listener.start();
				server_log.append("New Connection: " + client.getRemoteSocketAddress() + "\n");
				}
			} 
			catch(Exception e) 
			{
			System.out.println(e);
			}
		}
	}

	private JScrollPane scrollPane;
    private JTextArea server_log;
	private JButton clear_button;
    private JButton end_button;
    private JButton start_button;
}