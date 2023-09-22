package virtualPet;

import javax.swing.JOptionPane;

public class VirtualPet {
	private String name;
	private String[] lifeStage= {"Baby", "Child", "teenager","Young","Senior"};
	private int currentStageIndex=0;
	private int hunger;
	private int happiness;
	private int energy;
	private int health;
	private boolean isSleeping;
	private boolean isAlive;
	private boolean healthy;
	private int hygiene;
	private String image;
	private boolean isPlaying=false;
	private boolean isLoved=false;
	
	
	// for future updates/version
	protected String owner1;
	protected String owner2;
	private int petID;



	public VirtualPet(String name) {
		this.name = name;
		this.hunger = 50;
		this.happiness = 50;
		this.energy = 100;
		this.isSleeping = false;
		this.healthy = true;
		this.hygiene = 50;
		this.health = 70;
		this.isAlive = true;

		image="./img/cute-gatitobaby.gif";



	}

	public String getName() {
		return name;
	}


	public int getHunger() {
		return hunger;
	}

	public int getHappiness() {
		return happiness;
	}

	public int getEnergy() {
		return energy;
	}


	public String getImage() {
		return image;
	}

	public int getHygiene() {

		return hygiene;
	}

	public String getCurrentLifeStage() {
		return lifeStage[currentStageIndex];
	}
	public int getCurrentStageIndex() {
		return currentStageIndex;
	}

	public int getHealth() {
		return health;
	}


	public boolean isSleeping() {
		return isSleeping;
	}

	public boolean isHealthy() {
		return healthy;
	}

	public boolean isClean() {
		if(hygiene <50) {
			return false;
		}else {

			return true;
		}
	}
	public boolean isAlive() {
		return isAlive;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public void feed() {
		hunger -= 10;
		happiness += 5;
		health += 2;
		if (hunger < 0) {
			hunger = 0;
		}
		if (happiness > 100) {
			happiness = 100;
		}
		if (health > 100) {
			health = 100;
		}
	}

	public void giveLove() {

		isLoved=true;
		happiness += 30;
		energy +=20;
		health += 5;

		if (happiness > 100) {
			happiness = 100;
		}
		if(energy > 100) {
			energy=100;
		}
		if (health > 100) {
			health = 100;
		}
	}

	public void sleep() {
		if (!isSleeping) {
			isSleeping = true;
			energy = 100;
		}
	}



	public void takeMedicine() {
		
		health +=30;
		happiness -= 30;

		if (health > 100) {
			health = 100;
		}

		if (happiness < 0) {
			happiness = 0;
		}
		
	}

	public void clean() {

		hygiene = 100;

	}

	public void isDead() {
		if (hunger >= 100 && health <= 0) {
			isAlive = false;
			JOptionPane.showMessageDialog(null, "Your pet died", "Died", JOptionPane.ERROR_MESSAGE);
		}
	}


	public void evolve() {

		if (happiness == 100 && energy == 100 && hygiene == 100 && health == 100) {
			if (currentStageIndex < lifeStage.length - 1) {
				currentStageIndex++;
				String message = "Your pet has evolved to " + lifeStage[currentStageIndex] + "!";
				JOptionPane.showMessageDialog(null, message, "Evolution", JOptionPane.INFORMATION_MESSAGE);


			} else {
				JOptionPane.showMessageDialog(null, "Your pet has reached its maximum life stage.", "Evolution", JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	private void checkHealth() {
		if (health <= 35) {

			JOptionPane.showMessageDialog(null, "Your pet needs medical care", "Medicine", JOptionPane.INFORMATION_MESSAGE);

		}
	}



	public void play() {

		isPlaying=true;

		happiness += 20;
		energy -= 10;
		hunger += 10;
		hygiene -= 20;
		health -= 10; 
		checkHealth();

		if (happiness > 100) {
			happiness = 100;
		}
		if (energy < 0) {
			energy = 0;
		}
		if (hunger > 100) {
			hunger = 100;
		}
		if (hygiene < 0) {
			hygiene = 0;
		}
		if (health < 0) {
			health = 0;
		}
	}

	public void wakeUp() {
		isSleeping =  false;
		hunger = 0;

	}




	public void setHunger(int hunger) {
		this.hunger = hunger;
	}

	public void setHappiness(int happiness) {
		this.happiness = happiness;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public void setHealthy(boolean healthy) {
		this.healthy = healthy;
	}
	public void setHealth(int health) {
		this.health = health;
	}

	public void setHygiene(int hygiene) {
		this.hygiene = hygiene;
	}
	
	// for other version

	public String getOwner1() {
		return owner1;
	}

	public void setOwner1(String owner1) {
		this.owner1 = owner1;
	}

	public String getOwner2() {
		return owner2;
	}

	public void setOwner2(String owner2) {
		this.owner2 = owner2;
	}

	public int getPetID() {
		return petID;
	}

	public void setPetID(int petID) {
		this.petID = petID;
	}


	


}