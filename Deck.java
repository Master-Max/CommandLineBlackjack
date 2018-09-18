//Deck for any card game
//Max Petersen 9-8-2017

import java.util.Random;

public class Deck
{
	int i, j, n;
	int min;
	int max;

	Random rand = new Random();

	Card[] c;// = new Card[53];

	public Deck(){
		c = new Card[53];

		max = 52;
		min = 1;

		//Card[] c = new Card[52];

		int cardCount = 0;
		char[] suitIDs = new char[]{'D','C','H','S'};//Diamonds, Clubs, Hearts, Spades
		c[0] = new Card();//Creates a "Joker" at c[0]
		cardCount++;

		for(i = 0; i <= 3; i++){
			for(j = 1; j <= 13; j++){
				//System.out.printf("Suit: %c | Num: %d\n", suitIDs[i], j);
				c[cardCount] = new Card(suitIDs[i], j);
				cardCount++;
			}
		}
	}

	public Card drawCard(){//Draws a random undrawn Card
		boolean gotCard = false;
		while (!gotCard){
			n = rand.nextInt((max - min) + 1) + min;
			if(!c[n].isDrawn()){
				gotCard = true;
				//return c[n];
			}
		}
		return c[n];
	}

	public void returnCard(Card rc){//Returns a card to the deck

	}

	public void disCard(Card rc){//Removes a card from play

	}
}
