import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Player {
	private ArrayList<Card> cards = new ArrayList<Card>();
	private Location location;
	private int roll;
	private Room room;
	private Map<Room, Integer> roomDistances = new HashMap<Room, Integer>();

	public Player (ArrayList<Card> hand, Location location){
		this.cards = hand;
		this.location = location;
	}


	public void rollDice(){
		Random rand = new Random();
		roll = rand.nextInt(6);
		System.out.println(roll);
	}

	/**
	 * To calculate the new location of the player, the update location method will need to take the location of the room it is going to,
	 * the distance to the room, the dice roll and the current location of the player.
	 * 
	 * if location.x < current location.x, move left until it isn't anymore.
	 * if location.x > current location.x, move right until it isn't anymore.
	 * if location.y < location.y, move up until it isn't anymore.
	 * if location.y > location.y, move down until it isn't anymore.
	 * 
	 * @param Target Room - Room
	 * @return New Location - Location
	 */
	public void updateLocation(Room r) throws IllegalStateException{
		int stepsRemaining = roll;
		int stepsTaken = 0;
		int distance = roomDistances.get(r);
		Location roomLocation = r.getLocation();
		while(stepsRemaining > 0 && stepsTaken < distance){			
			if(roomLocation.getX() < location.getX()){
				this.location.setX(location.getX()-1);
				stepsRemaining += -1;
				stepsTaken += 1;
			}
			else if(roomLocation.getX() > location.getX()){
				this.location.setX(location.getX()+1);
				stepsRemaining += -1;
				stepsTaken += 1;
			}
			else if(roomLocation.getY() < location.getY()){
				this.location.setY(location.getY()-1);
				stepsRemaining += -1;
				stepsTaken += 1;
			}
			else if(roomLocation.getY() > location.getY()){
				this.location.setY(location.getY()+1);
				stepsRemaining += -1;
				stepsTaken += 1;
			}
			else{
				throw new IllegalStateException("Cannot move to desired location or already there");
			}
		}
	}

	/**
	 * calculates the distances from the current location to all of the rooms.
	 * @param r
	 */
	public void calculateDistances(){	//Still need to call this method somewhere
		for(Room r : roomDistances.keySet()){
			Location roomLocation = r.getLocation();
			int distance = Math.abs(roomLocation.getX() - location.getX()) + Math.abs(roomLocation.getY() - location.getY());
			roomDistances.put(r, distance);
		}
	}
	
	public Location getLocation(){
		return location;
	}

	public ArrayList<Card> getCards(){
		return cards;
	}
	
	public void setCards(ArrayList<Card> c){
		cards = c;
	}

	public int getRoll(){
		return roll;
	}

	public Map<Room, Integer> getRoomDist(){
		return roomDistances;
	}
}