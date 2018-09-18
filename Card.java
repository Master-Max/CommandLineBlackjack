public class Card{
	private int num;//Programs should interpret (1)=Ace, (11)=Jack, (12)=Queen, and (13)= King
	private char suit;//Programs should call with [D]iamonds, [C]lubs, [H]earts, and [S]pades
	private char color;//Set is suit is properly set: Either [r]ed or [b]lack
	private boolean drawn = false;

	public Card(){
		num = 69;
		suit = 'J';
		color = 'j';
	}
	
	public Card(char s, int n){
		num = n;
		suit = s;
		if((suit == 'D') || (suit == 'H')){
			color = 'r';
		}else if((suit == 'C') || (suit == 'S')){
			color = 'b';
		}else{
			System.out.printf("Error %c is not a valid suit type\n", suit);
		}
	}
	
	public int getNum(){
		return num;
	}
	
	public char getSuit(){
		return suit;
	}
	
	public char getColor(){
		return color;
	}
	
	public boolean isDrawn(){
		return drawn;
	}

	public void drawn(){
		drawn = !drawn;
	}
}
