// This class keeps track of how many zombies are inside vs outside

public class ZombieStats{
	private int zombiesIn;
	private int zombiesOut;
	
	public ZombieStats(int in, int out){
		zombiesIn = in;
		zombiesOut = out;
	}
	
	public int getZombiesIn(){
		return zombiesIn;
	}
	
	public int getZombiesOut(){
		return zombiesOut;
	}
	
	public void setZombiesIn(int z){
		zombiesIn = z;
	}
	
	public void setZombiesOut(int z){
		zombiesOut = z;
	}
	
}