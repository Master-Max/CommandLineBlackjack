import java.util.Scanner;

public class Blackjack{

	//Arrays containing Unicode Playing Cards 0-Ace, 10-Jack, 11-Queen, 12-King
	String[] diamond = {"\uD83C\uDCC1","\uD83C\uDCC2","\uD83C\uDCC3","\uD83C\uDCC4","\uD83C\uDCC5","\uD83C\uDCC6","\uD83C\uDCC7","\uD83C\uDCC8","\uD83C\uDCC9","\uD83C\uDCCA","\uD83C\uDCCB","\uD83C\uDCCD","\uD83C\uDCCE"};
	String[] club = {"\uD83C\uDCD1","\uD83C\uDCD2","\uD83C\uDCD3","\uD83C\uDCD4","\uD83C\uDCD5","\uD83C\uDCD6","\uD83C\uDCD7","\uD83C\uDCD8","\uD83C\uDCD9","\uD83C\uDCDA","\uD83C\uDCDB","\uD83C\uDCDD","\uD83C\uDCDE"};
	String[] heart = {"\uD83C\uDCB1","\uD83C\uDCB2","\uD83C\uDCB3","\uD83C\uDCB4","\uD83C\uDCB5","\uD83C\uDCB6","\uD83C\uDCB7","\uD83C\uDCB8","\uD83C\uDCB9","\uD83C\uDCBA","\uD83C\uDCBB","\uD83C\uDCBD","\uD83C\uDCBE"};
	String[] spade = {"\uD83C\uDCA1","\uD83C\uDCA2","\uD83C\uDCA3","\uD83C\uDCA4","\uD83C\uDCA5","\uD83C\uDCA6","\uD83C\uDCA7","\uD83C\uDCA8","\uD83C\uDCA9","\uD83C\uDCAA","\uD83C\uDCAB","\uD83C\uDCAD","\uD83C\uDCAE"};

	String cardBack = "\uD83C\uDCA0";//Back of a Playing Card Unicode


	String hBS = "\u2500";//[h]orizontal [B]ar [S]ingle
	String vBS = "\u2502";//[v]ertical [B]ar [S]ingle
	String rCUS = "\u2518";//[r]ight [C]orner [U]p [S]ingle
	String lCDS = "\u2510";//[l]eft [C]orner [D]own [S]ingle

	String hBD = "\u2550";//[h]orizontal [B]ar [D]ouble
	String vBD = "\u2551";//[v]ertical [B]ar [D]ouble
	String rCUD = "\u255D";//[r]ight [C]orner [U]p [D}ouble
	String lCDD = "\u2557";//[l]eft [C]orner [D]own [D}ouble
	String rCDD = "\u2554";//[r]ight [C]orner [D]own [D}ouble

	String pHandVis;//Strings that get printed to represent player's and dealer's hands
	String dHandVis;

	int dTotal, pTotal;

	Card[] dealerHand;
	int dhCount = 0;//Number of cards in the Dealer's hand

	Card[] playerHand;// = new Card[12];
	int phCount = 0;//Number of cards in the Player's Hand

	Scanner key = new Scanner(System.in);
	Deck deck;

	boolean revealing = false;

	boolean finRound = false;

	boolean playerTurn = true;

	public static void main(String[] args){
		Blackjack b = new Blackjack();
		b.startGame();
	}

	public void startGame(){
		revealing = false;
		dhCount = 0;
		phCount = 0;
		dealerHand = new Card[12];
		playerHand = new Card[12];
		dHandVis = "";
		pHandVis = "";
		dTotal = 0;
		pTotal = 0;
		deck = new Deck();
		System.out.println("Made The Deck");

		//System.out.printf("%s\n",cardBack);
		this.firstDeal();
		this.makeTable();

		while(!finRound){
			try{
				//h-hit, s-stay, b-bust
				while(playerTurn){
					playerChoice(key.nextLine().toLowerCase().charAt(0));
					makeTable();
					checkBust(1);
				}
				if(!finRound && getTotal(0) <
				 getTotal(1) && getTotal(0) != 21){
					hit(0);
					makeTable();
					checkBust(0);
				}else{
					finRound = true;
				}
			}catch(Exception e){e.printStackTrace();}
		}
		revealing = true;
		makeTable();
		winner();
		finRound = true;
		//System.out.printf("\nP: %s [%s|%s]\n",pHandVis,playerHand[dhCount-1].getSuit(),playerHand[dhCount-1].getNum());
		//System.out.printf("D: %s [%s|%s]\n\n",dHandVis,dealerHand[dhCount-1].getSuit(),dealerHand[dhCount-1].getNum());
	}

	public void winner(){
		if(getTotal(1)>getTotal(0) && getTotal(1)<=21){
			System.out.println("\nYou Win!");
		}else if(getTotal(0)>21 && getTotal(1)<=21){
			System.out.println("\nYou Win!");
		}else if(getTotal(1)==getTotal(0)){
			System.out.println("\nYou Tied");
		}else{
			System.out.println("\nThe House Wins");
		}
	}

	public void firstDeal(){
		dealerHand[dhCount] = deck.drawCard();
		dhCount++;
		dealerHand[dhCount] = deck.drawCard();
		dhCount++;//After First Deal dhCount is at 2

		playerHand[phCount] = deck.drawCard();
		phCount++;
		playerHand[phCount] = deck.drawCard();
		phCount++;

		this.firstHandsVis();
	}

	public void playerChoice(char ch){
		switch(ch){
			case 'h':
				hit(1);
				break;
			case 's':
				stay();
				break;
		}
	}

	public void hit(int p){//D-0 | P-1
		if(p == 0){
			dealerHand[dhCount] = deck.drawCard();
			dhCount++;
			addCardVis(0);
		}else if(p == 1){
			playerHand[phCount] = deck.drawCard();
			phCount++;
			addCardVis(1);
		}
	}

	public void stay(){
		playerTurn = !playerTurn;
	}

	public void checkBust(int p){
		int t;
		if(p == 1 && getTotal(1)>21){
			System.out.println("Busted");
			playerTurn = false;
			finRound = true;
		}else if(p == 0 && getTotal(0)>21){
			System.out.println("Busted");
		}
	}

	public int getTotal(int p){//Sets dTotal and pTotal 0-D | 1-P | 2-SecD
		int t = 0;
		int tmp;
		int aces = 0;
		if(p == 0){//Dealer
			for(int i = 0; i <= dhCount-1; i++){
				tmp = dealerHand[i].getNum();
				if(tmp>=11){
					t += 10;
				}else if(tmp == 1){
					t += 11;
					aces ++;
				}else{
					t += tmp;
				}
			}
			while((t>21) && (aces != 0)){
				t -= 10;
			}
		}else if(p == 1){//Player
			for(int i = 0; i <= phCount-1; i++){
				tmp = playerHand[i].getNum();
				if(tmp>=11){
					t += 10;
				}else if(tmp == 1){
					t += 11;
					aces ++;
				}else{
					t += tmp;
				}
			}
			while((t>21) && (aces != 0)){
				t -= 10;
			}
		}else if(p == 2){//Secret Dealer
			for(int i = 1; i <= dhCount-1; i++){
				tmp = dealerHand[i].getNum();
				if(tmp>=11){
					t += 10;
				}else if(tmp == 1){
					t += 11;
					aces ++;
				}else{
					t += tmp;
				}
			}
			while((t>21) && (aces != 0)){
				t -= 10;
			}
		}
		return t;
	}

	public void firstHandsVis(){//Creates the first vis hands
		try{
			//Dealer's Hand
			dHandVis += cardBack + " ";
			this.addCardVis(0);
			//Player's Hand
			//pHandVis = "Player ";
			if(playerHand[0].getSuit() == 'D'){
				pHandVis +=diamond[playerHand[0].getNum()-1];
			}else if(playerHand[0].getSuit() == 'C'){
				pHandVis +=club[playerHand[0].getNum()-1];
			}else if(playerHand[0].getSuit() == 'H'){
				pHandVis +=heart[playerHand[0].getNum()-1];
			}else if(playerHand[0].getSuit() == 'S'){
				pHandVis +=spade[playerHand[0].getNum()-1];
			}
			pHandVis +=" ";
			this.addCardVis(1);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("\n\nfirstHandsVis");
			e.printStackTrace();
			System.out.printf("P: %s [%s|%s]\n",pHandVis,playerHand[dhCount-1].getSuit(),playerHand[dhCount-1].getNum());
			System.out.printf("D: %s [%s|%s]\n\n",dHandVis,dealerHand[dhCount-1].getSuit(),dealerHand[dhCount-1].getNum());
			//this.firstHandsVis();
		}
	}

	public void addCardVis(int p){//Adds Card U Chars to strings: Dealer=0, Player=1
		try{
			if(p == 0){//Dealer's Hand
				if(dealerHand[dhCount-1].getSuit() == 'D'){
					dHandVis +=diamond[dealerHand[dhCount-1].getNum()-1];
				}else if(dealerHand[dhCount-1].getSuit() == 'C'){
					dHandVis +=club[dealerHand[dhCount-1].getNum()-1];
				}else if(dealerHand[dhCount-1].getSuit() == 'H'){
					dHandVis +=heart[dealerHand[dhCount-1].getNum()-1];
				}else if(dealerHand[dhCount-1].getSuit() == 'S'){
					dHandVis +=spade[dealerHand[dhCount-1].getNum()-1];
				}
				dHandVis += " ";
			}
			else if(p == 1){//Player's Hand
				if(playerHand[phCount-1].getSuit() == 'D'){
					pHandVis += diamond[playerHand[phCount-1].getNum()-1];
				}else if(playerHand[phCount-1].getSuit() == 'C'){
					pHandVis += club[playerHand[phCount-1].getNum()-1];
				}else if(playerHand[phCount-1].getSuit() == 'H'){
					pHandVis += heart[playerHand[phCount-1].getNum()-1];
				}else if(playerHand[phCount-1].getSuit() == 'S'){
					pHandVis += spade[playerHand[phCount-1].getNum()-1];
				}
				pHandVis +=" ";
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("\n\naddCardVis");
			e.printStackTrace();
			System.out.printf("P: %s [%s|%s]\n",pHandVis,playerHand[dhCount-1].getSuit(),playerHand[dhCount-1].getNum());
			System.out.printf("D: %s [%s|%s]\n\n",dHandVis,dealerHand[dhCount-1].getSuit(),dealerHand[dhCount-1].getNum());
			//this.addCardVis(p);
		}
	}

	public void makeTable(){
		int dt,pt;
		if(revealing){
			dt = getTotal(0);
			dHandVis = "";
			int n = dhCount;
			dhCount = 1;
			for(int i=0;i<n;i++){
				addCardVis(0);
				dhCount++;
			}
			dhCount = n;
		}else{
			dt = getTotal(2);
		}
		pt = getTotal(1);
		System.out.print("\033[H\033[2J");
		System.out.flush();
		System.out.printf("\n\nDealer%s T: %d\n",vBS,dt);
		System.out.printf("%s%s%s%s%s%s%s\n",hBS,hBS,hBS,hBS,hBS,hBS,rCUS);
		//for(int i = 0; i<=dhCount; i++){System.out.printf("%s%s",hBD,hBD);}
		System.out.print("\n");
		System.out.printf("  %s%s\n",dHandVis,vBD);
		for(int i = 0; i<=dhCount; i++){System.out.printf("%s%s",hBD,hBD);}
		System.out.printf("%s\n",rCUD);

		System.out.println("\n  Hit [OR] Stay  \n");

		System.out.print(rCDD);
		for(int i = 0; i<=phCount; i++){System.out.printf("%s%s",hBD,hBD);}
		System.out.printf("%s\n",lCDD);
		System.out.printf("%s%s  %s\n",vBD,pHandVis,vBD);
		System.out.print("\n");
		System.out.printf("%s%s%s%s%s%s%s\n",hBS,hBS,hBS,hBS,hBS,hBS,lCDS);
		System.out.printf("Player%s T: %d\n>>",vBS,pt);
	}

	public void makeTestTable(){

		System.out.printf("Dealer%s\n",vBS);
		System.out.printf("%s%s%s%s%s%s%s\n",hBS,hBS,hBS,hBS,hBS,hBS,rCUD);


		for(int i = 0; i <= 12; i++){System.out.printf("%s ",diamond[i]);}
		System.out.print("\n");

		for(int i = 0; i <= 12; i++){System.out.printf("%s%s",hBD,hBD);}
		System.out.print("\n");

		for(int i = 0; i <= 12; i++){System.out.printf("%s ",club[i]);}
		System.out.print("\n");

		for(int i = 0; i <= 12; i++){System.out.printf("%s%s",hBD,hBD);}
		System.out.print("\n");

		for(int i = 0; i <= 12; i++){System.out.printf("%s ",heart[i]);}
		System.out.print("\n");

		for(int i = 0; i <= 12; i++){System.out.printf("%s%s",hBD,hBD);}
		System.out.print("\n");

		for(int i = 0; i <= 12; i++){System.out.printf("%s ",spade[i]);}
		System.out.print("\n");

		for(int i = 0; i <= 12; i++){System.out.printf("%s%s",hBD,hBD);}
		System.out.print("\n");
	}
}
