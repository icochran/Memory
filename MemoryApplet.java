// MemoryApplet.java
// Memory Game
//
// CS 201 Final Project
// Issy Cochran, Megan Mahoney

import java.awt.*; // abstract window toolkit
import java.awt.event.*;  // event handling
import java.applet.*;     // Applet classes
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//This sets up the applet for the game
public class MemoryApplet extends Applet implements ActionListener {
	private static final long serialVersionUID = 1L;

	protected Image hotdog; protected Image watch; protected Image cat; protected Image dog;
	protected Image dolphin; protected Image elephant; protected Image fish; protected Image frog;
	protected Image giraffe; protected Image pig; protected Image racoon; protected Image snail;
	protected Image sun; protected Image scooter; protected Image blankImage;
	static final Color dred = new Color(160, 0, 100);
	static final Color lblue = new Color(180, 190, 255);
	
	protected Image[] list; protected List<Image> nameCards;
	protected int rows = 4; protected int cols = 7;;
	protected Card[][] board = new Card[rows][cols];
	protected int length = 150; 
	protected Label score1; protected Label score2; protected int count1 = 0; protected int count2 = 0;
	protected Label title;
	protected Board2 b;
	protected int level; // level 0 = easy, level 1 = hard
	protected boolean hard = false;
	protected boolean easy = false;
	
	public void init() {
		// this initializes all of the images 
		hotdog = getImage(getDocumentBase(), "hotdog.jpg");
		watch = getImage(getDocumentBase(), "watch.jpg");
		cat = getImage(getDocumentBase(), "cat.jpg");
		dog = getImage(getDocumentBase(), "dog.jpg");
		dolphin = getImage(getDocumentBase(), "dolphin.jpg");
		elephant = getImage(getDocumentBase(), "elephant.jpg");
		fish = getImage(getDocumentBase(), "fish.jpg");
		frog = getImage(getDocumentBase(), "frog.jpg");
		giraffe = getImage(getDocumentBase(), "giraffe.jpg");
		pig = getImage(getDocumentBase(), "pig.jpg");
		racoon = getImage(getDocumentBase(), "racoon.jpg");
		snail = getImage(getDocumentBase(), "snail.jpg");
		sun = getImage(getDocumentBase(), "sun.jpg");
		scooter = getImage(getDocumentBase(), "scooter.png");
		blankImage = getImage(getDocumentBase(), "blank.jpeg");
		
		setSize(1200,800);
		
		// create a Board2 object which handles the game
		b = new Board2(this);
		b.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		
		this.setFont(new Font("TimesRoman", Font.BOLD, 40));
		this.setLayout(new BorderLayout());
		this.setBackground(Color.PINK);
		
		// This sets up the bottom part of the game board which shows the scores and reset button
		Panel p1 = new Panel(new GridLayout(1, 3));
		score1 = makeLabel("Player 1: " + count1, lblue); 
		score2 = makeLabel("Player 2: " + count2, lblue);
		p1.add(score1);
		p1.add(makeButton(Color.PINK, "Reset"));
		p1.add(score2);
		
		// this sets up the top display of the board
		// which includes the level of difficulty buttons and which player's turn it is
		Panel p2 = new Panel(new GridLayout(1, 3));
		title = makeLabel("Memory Game", dred);
		Panel leftP2 = new Panel(new FlowLayout());
		leftP2.add(makeButton(lblue, "Easy"));
		leftP2.add(makeButton(lblue, "Hard"));
		leftP2.setBackground(Color.white);
		p2.add(leftP2);
		p2.add(title);
		p2.add(makeLabel("", Color.white));
		
		this.add("North", p2); 
		this.add("Center", b);
		this.add("South", p1);
	}
	
	// This method returns a label with a white background and the given text and color 
	public Label makeLabel(String name, Color color) {
		Label label = new Label(name, Label.CENTER);
		label.setForeground(color);
		label.setBackground(Color.white);
		return label;
	}
	
	// This method returns a button with the given text and color, white background, and adds 
	// an actionListener 
	public Button makeButton(Color color, String name) {
		Button button = new Button(name);
		button.setForeground(color);
		button.addActionListener(this);
		button.setBackground(Color.white);
		return button;
	}
	
	// This method shuffles the instance images with the pair #s based off of the level # and 
	// puts those images into the board variable
	public void shuffle(int level) {
		// hard level has 14 pairs
		if (level == 1) {
			Image[] list = {snail, snail, scooter, scooter, cat, cat, dog, dog, fish, fish, frog, frog,
					giraffe, giraffe, pig, pig, racoon, racoon, sun, sun, dolphin, dolphin, elephant, 
					elephant, hotdog, hotdog, watch, watch};
			nameCards = Arrays.asList(list);
			cols = 7;
		}
		// easy level has 10 pairs
		else {
			Image[] list = {snail, snail, scooter, scooter, cat, cat, dog, dog, fish, fish, frog, frog,
					giraffe, giraffe, pig, pig, racoon, racoon, sun, sun};
			nameCards = Arrays.asList(list);
			cols = 5;
		}
		// this code shuffles the list then turns it back into an array 
		Collections.shuffle(nameCards);
		list = (Image[]) nameCards.toArray();
		// put the array into a 2d array called board
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Card card = new Card(list[j + i*cols], false);
				board[i][j] = card;
			}
		}
	}
	
	// this handles clicking buttons
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Button) {
            String label = ((Button)e.getSource()).getLabel();
            if (label.equals("Reset")) {
            	b.reset(); // reset the game
            }
            else if (label.equals("Hard")) {
            	if (!easy && !hard) {
            		level = 1;
            		shuffle(level); // set the board to be hard
            		hard = true;
            		b.howToPlay = false;
            		b.repaint(); // start the game 
            	}
            }
            else if (label.equals("Easy")) {
            	if (!hard && !easy) {
            		level = 0;
            		shuffle(level); // set the board to be easy
            		easy = true;
            		b.howToPlay = false;
            		b.repaint(); // start the game 
            	}
            }
		}
	}
	
	// These two methods are used to delay flipping the cards to blank 
	// and was inspired by the Squash2.java code by Daniel Scharstein
	public void start() {
        b.start();
    }
    public void stop() {
        b.stop();
    }
}

// This class handle board activity and display
class Board2 extends Canvas implements MouseListener, Runnable {
	
	private static final long serialVersionUID = 1L;
	
	protected int clickCount = 0; // keeps track of how many clicks have been done per guess
	protected int x = 50; // tell the board where to start drawing the grid so it has a border
	protected int y = 50; // tell the board where to start drawing the grid so it has a border
	protected MemoryApplet parent; // gives access to the Labels in MemoryApplet class
	protected Card previousCardClicked; // the first card clicked 
	protected int whichPlayer = 1; // 1 when player 1, 0 when player 2
	protected Thread t;
	protected long startTime;
	protected Card currentCard;
	protected boolean howToPlay;
	protected boolean winnerOne;
	protected boolean winnerTwo;
	protected boolean tie;
	protected boolean secondCardClicked;
	
	public Board2(MemoryApplet a) {
		parent = a;
		addMouseListener(this);
		howToPlay = true;
		winnerOne = false;
		winnerTwo = false;
		tie = false;
		secondCardClicked = false;
	}
	
	public void paint(Graphics g) {
		if (howToPlay) {
			g.drawString("Welcome to Memory!", 495, 80);
			g.drawString("Here is how to play:", 505, 110);
			g.drawString("1. Find a friend to play with, Player 1 will go first.", 325, 140);
			g.drawString("2. Every time you wish to play the game, select which mode by clicking", 325, 160);
			g.drawString("    the Easy or Hard button at the top and the game will begin. If you", 325, 180);
			g.drawString("    would like to switch modes after selecting a level you must press", 325, 200);
			g.drawString("    the reset button first.", 325, 220);
			g.drawString("3. Each turn, click on two cards that you think are the same to win", 325, 240);
			g.drawString("    a point. If the two chosen cards are not a match, they will briefly", 325, 260);
			g.drawString("    display their image, then be flipped back over and it will be the", 325, 280);
			g.drawString("    next player’s turn. If the two cards do match, they will remain flipped", 325, 300);
			g.drawString("    over, showing their images and the player who found the match will get", 325, 320);
			g.drawString("    another turn to guess two cards.", 325, 340);
			g.drawString("4. Keep playing until all the pairs are found.", 325, 360);
			g.drawString("5. If you wish to restart the game at any point, click the reset button.", 325, 380);
			g.drawString("Click easy or hard to begin!", 475, 405);
		}
		else if (winnerOne) {
			endMessage(g, "Player 1 Wins Memory!", 370); // prints a end message
		}
		else if (winnerTwo) {
			endMessage(g, "Player 2 Wins Memory!", 370); // prints a end message
		}
		else if (tie) {
			endMessage(g, "Its a tie!", 505); // prints a end message
		} 
		// this means the game is currently being played
		else {
			// Check which turn it is and display whose turn it is on a label
			if (whichPlayer == 1) {
    			parent.title.setText("Player 1's Turn");
    		}
    		else {
    			parent.title.setText("Player 2's Turn");	
    		}
			
			int length = parent.length;
			// if it is easy mode shift the board to the right by 200 pixels (50 more than the hard level)
			if (parent.level == 0) {
				x = 200;
			}
			// go through the board object and draw each card based off of its boolean frontOrBack
			for (int i = 0; i < parent.rows; i++) {
				for (int j = 0; j < parent.cols; j++) {
					if (parent.board[i][j].isFlipped()) {
						g.drawImage(parent.board[i][j].getImage(), x + length*j, y +length*i, this);
					}
					else {
						g.drawImage(parent.blankImage, x + length*j, y + length*i, this);
					}
					g.drawRect(x + length*j, y +  length*i, length, length);
				}
			}
		}
	} 
	
	// This function prints out a message when the game ends
	public void endMessage(Graphics g, String message, int x) {
		g.setFont(new Font("TimesRoman", Font.BOLD, 40));
		g.drawString(message, x, 180);
		g.drawString("Congratulations. To play again click the reset button.", 130, 300);
		g.drawString("If you want to exit the game, close the applet.", 215, 340);
		g.drawString("Thanks for playing", 410, 380);
	}
	
	// This function handles clicking 
	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();
		// if they have not clicked twice and the game is playing call whenMousePressed()
		if (!howToPlay && !secondCardClicked) {
			if (parent.level == 1) {
				// checks if user clicks in bounds for the hard level
				if (50 <= p.x && p.x <= 1100 && 50 <= p.y && p.y <= 750) {
					whenMousePressed(e);
				}
			}
			// checks if the user clicked in bounds for the easy level
			else if (200 <= p.x && p.x <= 950 && 50 <= p.y && p.y <= 750) {
				whenMousePressed(e);
			}
		}
	} 
	
	public void whenMousePressed(MouseEvent e) {
		Point p = e.getPoint();
		// find location of which card they clicked on 
		int i = (p.y - y) / parent.length;
		int j = (p.x - x) / parent.length;
		// set a current card to this location on the board
		currentCard = parent.board[i][j];
		// if the user made their first guess
		if (clickCount == 0) {
			if (!parent.board[i][j].frontOrBack) { // if it has not been flipped yet
				parent.board[i][j].frontOrBack = !parent.board[i][j].frontOrBack; // flip it
				clickCount++; // add to clickCount
				repaint(); // repaint the board
				previousCardClicked = parent.board[i][j]; // keep track of first card clicked
			}
		}
		// if they made their second guess
		else { 
			if (!parent.board[i][j].frontOrBack) { // if it has not been flipped yet
				parent.board[i][j].frontOrBack = !parent.board[i][j].frontOrBack; // flip it
				repaint(); // repaint the board
				if (parent.board[i][j].getImage().equals(previousCardClicked.getImage())) { // if match
					clickCount = 0; // reset the number of guesses
					if (whichPlayer == 1) { // if player 1 got a match
						parent.count1++; // increase their score and update the label
						parent.score1.setText("Player 1: " + parent.count1); 
					}
					else { // otherwise do the same but for player 2
						parent.count2++;
						parent.score2.setText("Player 2: " + parent.count2);
					} 
				}
				else { // if not a match
					startTime = System.currentTimeMillis(); // start a timer
					clickCount++; // increase the clickCount
					secondCardClicked = true;
				}
			}
		}
	}
	
	// This function is always running and controls the action of flipping incorrectly guessed cards 
	public void run() {
		// This code was inspired by the Squash2.java code by Daniel Scharstein 
		Thread currentThread = Thread.currentThread();
        while (currentThread == t) {
        	// if they were not a match and enough time has passed
        	if (clickCount == 2 && System.currentTimeMillis() - startTime > 800) {
        		// flip both cards over
            	previousCardClicked.frontOrBack = !previousCardClicked.frontOrBack;
        		currentCard.frontOrBack = !currentCard.frontOrBack;
        		// change the player's turn
        		if (whichPlayer == 1) {
        			whichPlayer = 0;
        		}
        		else {	
        			whichPlayer = 1;
        		}
        		clickCount = 0; // restart the number of guesses
        		secondCardClicked = false;
        		repaint(); // repaint the board
        	}
        	// check if someone won for each level and call findWinner()
        	if (parent.count1 + parent.count2 == 14 && parent.level == 1) {
        		findWinner();
        	} 
        	if (parent.count1 + parent.count2 == 10 && parent.level == 0) {
        		findWinner();
        	}
        	// this code below was inspired by the Squash2.java code by Daniel Scharstein
        	try {
                Thread.sleep(10);
            } 
        	catch (InterruptedException e) {};
        }
	}
	
	// This function determines which player won the game or if it was a tie
	public void findWinner() {
		if (winnerOne || winnerTwo || tie) {
			return;
		}
		else if (parent.count1 > parent.count2) 
    		winnerOne = true;
    	else if (parent.count1 < parent.count2) 
    		winnerTwo = true;
    	else 
    		tie = true;
    	repaint(); // updates the board to display winner message
	}
    
	// Handles when the reset button is pressed by setting all of the instance variables to their 
	// initial state and call repaint()
    public void reset() {
    	howToPlay = true;
    	clickCount = 0;
    	x = 50;
    	winnerOne = false;
		winnerTwo = false;
		tie = false;
		secondCardClicked = false;
		parent.hard = false;
		parent.easy = false;
		whichPlayer = 1;
		parent.count1 = 0;
		parent.count2 = 0;
		parent.score1.setText("Player 1: " + parent.count1);
		parent.score2.setText("Player 2: " + parent.count2);
		parent.title.setText("Memory Game");
		repaint(); 
    }
    
    // These two methods are used to delay flipping the cards to blank 
 	// and were inspired by the Squash2.java code by Daniel Scharstein
    public void start() {
        t = new Thread(this);
        t.start();
    }
    public void stop() {
        t = null;
    }
    
    // need these because we implement a KeyListener
	public void keyTyped(KeyEvent e) {} 
	public void keyReleased(KeyEvent e) {}
	// need these also because we implement a MouseListener
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}

// This class of cards requires an Image and a boolean determining if it has been flipped
class Card {
	protected Image imageName;
	protected boolean frontOrBack;

	public Card(Image image, boolean frontOrBack) {
		this.imageName = image;
		this.frontOrBack = frontOrBack;
	}
	
	public Image getImage() {
		return imageName;
	}
	
	public boolean isFlipped() {
		return frontOrBack;
	}
}


