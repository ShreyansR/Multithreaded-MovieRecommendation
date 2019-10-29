import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
* The Movie Client class creates a client(s) that
* can communicate with the Multi-Threaded server 
* to get random movie recommendations.
*
* @author  Shreyans Rishi
* @version 1.0
* @since   2019-09-29
*/
public class movieClient extends JFrame 
{
	/**
	* address - Stores the IP address of the target server, localhost is this case
	* port - Stores the port number for the connection, 1234 in this case
	* isConnected - Keeps track of if the client is connected to the server or not
	* sock - creates a new Socket for every client
	* reader - creates a new BufferedReader for the client to communicate with the server
	* writer - creates a new PrintWriter for the client to communicate with the server 
	*
	*/
    String address = "localhost";
    int port = 1234;
    Boolean isConnected = false;
    
    Socket sock;
    BufferedReader reader;
    PrintWriter writer;
	
	/**
	* This method Initializes a new Thread to Listen the
	* Incoming reader from the server
	* 
	*/
    public void ListenThread() 
    {
         Thread IncomingReader = new Thread(new IncomingReader());
         IncomingReader.start();
	}
	
	/**
	* This method disconnects the client from the server
	* and sets isConnected to false
	* 
	*/
	public void Disconnect() 
    {
        try 
        {
			client_log.append("Disconnected.\n");
            sock.close();
        } catch(Exception ex) {
            client_log.append("Failed to disconnect. \n");
        }
        isConnected = false;

    }
	
	/**
	* This method acts like a constructor for this class
	* that initializes the GUI components of the client.
	*
	*/
    public movieClient() 
    {
        initComponents();
    }
	
	/**
	* This method is used to process the Incoming Reader from the server
	* and keeps reading until its null. 
	*
	*/
    public class IncomingReader implements Runnable
    {
        @Override
        public void run() 
        {
            String[] data;
            String stream, log = "Log";

            try 
            {
                while ((stream = reader.readLine()) != null) 
                {
					 data = stream.split(":");
					 System.out.println(Arrays.toString(data));
                     if (data[2].equals(log)) 
                     {
						client_log.append(data[0] + ": " + data[1] + "\n");
						System.out.println(data[0] + ": " + data[1] + "\n");
						client_log.setCaretPosition(client_log.getDocument().getLength() + 20);
                     } 
                }
           }catch(Exception ex) { }
        }
    }
	
	/**
	* This method is used to create the GUI for the Client
	* with different components like a Connect, Disconnect, and a Send button
	* and also creates a Text Area for the log of the client and a Text Field
	* for the user input. This log is also scrollable.
	*
	*/
    @SuppressWarnings("unchecked")
    private void initComponents() {

        connect_button = new JButton();
        disconnect_button = new JButton();
        scrollPane = new JScrollPane();
        client_log = new JTextArea();
        client_input = new JTextField();
        send_button = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Movie Recommendation - Client");
        setName("Client"); 
        setResizable(false);

        connect_button.setText("Connect");
        connect_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                connectAction(evt);
            }
        });

        disconnect_button.setText("Disconnect");
        disconnect_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                disconnectAction(evt);
            }
        });

        client_log.setColumns(50);
        client_log.setRows(5);
        scrollPane.setViewportView(client_log);

        send_button.setText("SEND");
        send_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendAction(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(client_input, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(send_button, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                    .addComponent(scrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(connect_button)
                                .addGap(2, 2, 2)
                                .addComponent(disconnect_button)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(connect_button)
                        .addComponent(disconnect_button)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(client_input)
                    .addComponent(send_button, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
        );

        pack();
    }

	/**
	* This method is used to initialize the client connection to the server.
	* It also initializes the Output and Input stream for the client
	* to communicate with the Server.
	*
	*/
    private void connectAction(ActionEvent evt) {
        if (isConnected == false) 
        {
            try 
            {
                sock = new Socket(address, port);
                writer = new PrintWriter(sock.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                //InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                //reader = new BufferedReader(streamreader);
                client_log.append("Client Started\n");
                client_log.append("Waiting for the server to respond...\n");
                client_log.append("Press 1 - For a random movie suggestion, Press 2 - For the list of Genres, Press 3 - To Exit \n\n");
                //client_log.append(reader.readLine() + "\n");
                writer.flush(); 
                isConnected = true; 
            } 
            catch (Exception ex) 
            {
                client_log.append("Cannot Connect! Try Again. \n");
            }
            
            ListenThread();
            
        } else if (isConnected == true) 
        {
            client_log.append("You are already connected. \n");
        }
    }

	/**
	* This method is used to close the client connection to the server.
	*
	*/
    private void disconnectAction(ActionEvent evt) {
        Disconnect();
    }

	/**
	* This method is used to get the text input from the
	* Text Field and send it to the server and receive
	* a response from the server.
	*
	*/
    private void sendAction(ActionEvent evt) {
        String nothing = "";
        if ((client_input.getText()).equals(nothing)) {
            client_input.setText("");
            client_input.requestFocus();
        } else {
            try {
                writer.println(client_input.getText());
				client_log.append(reader.readLine() + "\n");
				if(client_input.getText().equals("3")){
					Disconnect();
				}
                writer.flush();
            } catch (Exception ex) {
                client_log.append("Message was not sent. \n");
            }
            client_input.setText("");
            client_input.requestFocus();
        }

        client_input.setText("");
        client_input.requestFocus();
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
            public void run() 
            {
                new movieClient().setVisible(true);
            }
        });
    
    }

	private JTextArea client_log;
	private JTextField client_input;
	private JScrollPane scrollPane;
    private JButton connect_button;
    private JButton disconnect_button;
    private JButton send_button;
    

}
