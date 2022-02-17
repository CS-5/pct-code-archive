// Carson Seese - CIT344 Zombie Invasion Assignment - 10/24/19

import java.util.concurrent.Semaphore;

/**
 * Warehouse.java
 *
 * This class represents the factory where the 
 * doors will open and the zombies will enter.
 * Threads are created to represent the people. 
 */


public class Warehouse
{
	public static final int NUM_OF_FRIENDS = 4;
	public static final int NUM_OF_ZOMBIES_OUTSIDE = 15;
	public static final int NUM_OF_ZOMBIES_INSIDE = 0;

	public static void main(String args[])
	{
		ZombieStats stats = new ZombieStats(NUM_OF_ZOMBIES_INSIDE,NUM_OF_ZOMBIES_OUTSIDE); 
	
		//your friends will let the zombies in at each door
		Thread[] friends = new Thread[NUM_OF_FRIENDS];
		Semaphore s = new Semaphore(1);
		for (int i = 0; i < NUM_OF_FRIENDS; i++) {
			friends[i] = new Thread(new Door(i, stats, s));
			friends[i].start();
		}

		//you'll kill the zombies
		Thread you  = new Thread(new Killer(stats));
		you.start();
   }
}