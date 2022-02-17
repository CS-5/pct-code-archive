//This class represents the friend opening the door to let the zombie in

import java.util.concurrent.Semaphore;

public class Door implements Runnable{
	private ZombieStats stats;
	private int friendNum;
	private Semaphore lock;
	
	public Door(int num, ZombieStats s, Semaphore l){
		stats = s;
		friendNum = num;
		lock = l;
	}
	
	@Override
	public void run(){
		// if there are zombies outside
		while(stats.getZombiesOut()  > 0){
			try{
				lock.acquire();

				// if the killer is only battling 1 zombie -- let one more in -- he/she can handle it
				if (stats.getZombiesIn() < 2){
					stats.setZombiesIn(stats.getZombiesIn()+1);
					System.out.println("Friend " + friendNum + " let in a zombie");
					stats.setZombiesOut(stats.getZombiesOut()-1);
				}
				lock.release();

				// wait a random time before looping 
				Thread.sleep((int)Math.random()*1000);
			}
			catch(InterruptedException e){
				System.out.println("Interrupted friend " + friendNum);
			}

		}
		System.out.println("Friend " + friendNum+ " says there are no more zombies outside.");
	}

}