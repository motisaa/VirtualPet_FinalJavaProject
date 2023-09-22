package virtualPet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JTextArea;
public class ReadingFromStream extends Thread{





	private BufferedReader input;
	//		private JTextArea area;
	FPet chat;


	public ReadingFromStream(FPet chat) {

		this.chat = chat;
		input= chat.getInput();
	}

	public void run() {
		//el nombre solo lo leo una vez entonces está fuera del bucle infinito
		String readName="";
		try {
			readName = input.readLine();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		chat.setPetName(readName);
		
		
		try {

			while (true) {

				String receivedMessage = input.readLine();


				if (receivedMessage == null || receivedMessage.equals("*/%$")) {
					chat.disconnect();	        	        
					break; 
				} else {
					receivedMessage = "Received: " + receivedMessage;
					System.out.println(receivedMessage);
					chat.getTextArea().append("\n" + receivedMessage); 

				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}






