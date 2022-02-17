// This class represents the killer of the zombies

public class Killer implements Runnable{
	private ZombieStats stats;
	
	public Killer(ZombieStats s){
		stats = s;
	}
	
	@Override
	public void run(){
		while(true){
			// if there are zombies in the room -- kill one
			if (stats.getZombiesIn() > 0){
				stats.setZombiesIn(stats.getZombiesIn()-1);
				System.out.println("Killed zombie! Zombie count inside is  " + stats.getZombiesIn());
			}
			// if there are too many zombies -- it is too much :(
			if (stats.getZombiesIn()  >= 3){
				System.out.println("Oh no -- too many zombies -- game over!");
				return;
			}	
			// if there are no more zombies left, we are victorious
			if (stats.getZombiesOut()  == 0 && stats.getZombiesIn() == 0){
				System.out.println("Game over! We made it!");
				return;
			}
			try{
				Thread.sleep(2000);
			}
			catch(InterruptedException e){
				System.out.println("Interrupted the killer");
			}
		}	
	}
}