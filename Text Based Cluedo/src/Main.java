import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {
	private static boolean finished = false;
	
	public static void createGame(Scanner scan){
		System.out.println("Welcome to Cluedo!");
		int numP = 0;
		
		//ask for how many players are playing
		System.out.println("How many players?");
		numP = scan.nextInt();
		
		Board b = new Board(numP);
		
		int playerNum = 0;
		
		playGame(scan, b, playerNum);
	}
	
	/**
	 * Loop through all the players while the game hasn't been won.
	 * If a player gets eliminated, break the loop then remove 
	 * the player and start the loop again from where it left off.
	 * @param players
	 * @param scan
	 * @param b
	 * @param playerNum - the player before the current
	 */
	public static void playGame(Scanner scan, Board b, int playerNum){
		playerNum = (playerNum % b.getPlayers().size()) + 1;
		Player currentPlayer = b.getPlayers().get(playerNum-1);
		Player eliminatedPlayer = null;
		while (finished == false) {
			System.out.println("Current player is " + playerNum + " - " + currentPlayer.getName());
			System.out.println("Number of player:" + b.getPlayers().size());
			System.out.println("Size of hand is: " + currentPlayer.getCards().size());
			Room r = null;
			//calculate distances	
			currentPlayer.calculateDistances(b);
			System.out.println("Calculated distances");
			Map<Room, Integer> rDist = currentPlayer.getRoomDist();
			int i = 0;
			//look at room distances map and print out each entry
			ArrayList<Room> rooms = new ArrayList<Room>();
			System.out.println("rDist size = " + rDist.size());
			for(Entry<Room, Integer> e: rDist.entrySet()){
				Room cr = e.getKey();
				int dist = e.getValue();
				rooms.add(cr);
				System.out.println(i + " " + cr.getName() + " " + dist);
				i++;
			}
				
			//roll dice
			int diceNum = currentPlayer.rollDice();
			System.out.println("You rolled a " + diceNum);
			
			//give option for movement (dont have to)
			System.out.println("Where do you want to move to?");
			int numChoice = scan.nextInt();
			
			//update location
			r = rooms.get(numChoice);
			currentPlayer.updateLocation(r);
			scan.useDelimiter(System.getProperty("line.separator"));
			//make a guess option (suggestion, accusation OR nothing)
			System.out.println("What would you like to do?");
			System.out.println("1 - Suggestion");
			System.out.println("2 - Accusation");
			System.out.println("3 - Nothing");
			int option = scan.nextInt();
		    if(option == 1 || option == 2) { 
		    	
		    	System.out.println("Please type the 3 cards that you are guessing on a new line");
		    	System.out.println("In this order: Room, Weapon, Character");
		    	//get cards from typed input on new line
		    	String roomName = scan.next();
		    	int index = b.getRoomNames().indexOf(roomName);
		    	Room guessRoom = b.getRooms().get(index);
		    	
		    	int indexW = b.getWeaponNames().indexOf(scan.next());
		    	Weapon guessWeapon = b.getWeapons().get(indexW);
		    	
		    	String characterN = scan.next();
		    	int indexC = b.getCharacterNames().indexOf(characterN);
		    	Character guessCharacter = b.getCharacters().get(indexC);
		    	
		    	ArrayList<Card> guessHand = new ArrayList<Card>();
		    	guessHand.add(guessWeapon);
		    	guessHand.add(guessRoom);
		    	guessHand.add(guessCharacter);
		    	
		    	//create a guess hand
		    	boolean opt = false;
		    	if(option == 1){
		    		opt = true;
		    	}
		    	
				Guess guess = new Guess(opt, guessHand, currentPlayer);
				
				if(guess.getEliminatedPlayer()!=null){
					eliminatedPlayer = guess.getEliminatedPlayer();
					break;
				}
				else if (guess.hasWon()) { 
					finished = true;
					System.out.println("Congratulations " + currentPlayer.getName() + " you have won!");
					return;
				}
		    	
		    }
		    playerNum = (playerNum % b.getPlayers().size()) + 1;
		    currentPlayer = b.getPlayers().get(playerNum-1);
		}
		
		playerNum = (playerNum % Board.players.size()) - 1;
		Board.players.remove(eliminatedPlayer);
		while(b.getPlayers().size() > 1){
			playGame(scan, b, playerNum);
		}
	}
		
	public static void main(String [ ] args){
		Scanner scan = new Scanner(System.in);
		createGame(scan);
	}

}