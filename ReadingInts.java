package virtualPet;

import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ReadingInts extends Thread {

	FPet pet;
	private DataInputStream in;
	private boolean isAlive = true;
	private int happiness;
	private int energy;
	private int hunger;
	private int hygiene;
	private int health;
	private int state;
	private int statevida=1;
	
	

	
	public ReadingInts(FPet pet) {

		this.pet = pet;
		in =pet.getIn();
		
	
	}
	

	public void enableEdition() {
		if(state == 1) {

			pet.btnSleep.setEnabled(false); 
			pet.btnWakeUp.setEnabled(true); 

			pet.btnLove.setEnabled(false);
			pet.btnFeed.setEnabled(false);
			pet.btnTakeMedicine.setEnabled(false);
			pet.btnClean.setEnabled(false);
			pet.btnPlay.setEnabled(false);
		} else if(state== 0){

			pet.btnSleep.setEnabled(true); 
			pet.btnWakeUp.setEnabled(false);

			pet.btnLove.setEnabled(true);
			pet.btnFeed.setEnabled(true);
			pet.btnTakeMedicine.setEnabled(true);
			pet.btnClean.setEnabled(true);
			pet.btnPlay.setEnabled(true);

		}
		if(statevida== 2) {

			pet.btnSleep.setEnabled(false);
			pet.btnWakeUp.setEnabled(false);
			pet.btnLove.setEnabled(false);
			pet.btnFeed.setEnabled(false);
			pet.btnTakeMedicine.setEnabled(false);
			pet.btnClean.setEnabled(false);
			pet.btnPlay.setEnabled(false);

		}
	

		
	}
	
	public void run() {
	    try {
	       
	        while (true) {
	        	
	        		//	pet.getIn().readInt();
	        	System.out.println("estoy esperando algún cambio...");
	        	        this.happiness =  in.readInt();
	        	        System.out.println("recibo esta felicidad: "+happiness);
	        	        this.energy = in.readInt();
	        	        System.out.println("recibo esta en: "+energy);
	        	        this.hunger= in.readInt();
	        	        System.out.println("recibo esta hun: "+hunger);
	        	        this.hygiene = in.readInt();
	        	        System.out.println("recibo esta hyg: "+hygiene);
	        	        this.health = in.readInt();
	        	        System.out.println("recibo esta health: "+health); 
	        	        this.state = in.readInt();
	        	        this.statevida = in.readInt();
	        	        pet.setHappinessBar(happiness);
	        	        pet.setenergyBar(energy);
	        	        pet.setHungerBar(hunger);
	        	        pet.setHygieneBar(hygiene);
	        	        pet.setHealthBar(health);
	        	        pet.pet.evolve(); //accecing  to VirtualPet class
	        	        pet.pet.isDead(); //accecing  to VirtualPet class
	        	      
	        	       enableEdition();
	        	       
	 	       
	        	    }
	        
	    } catch (IOException e) {
	       System.out.println(e.getMessage());
	    }
	}
	
}
