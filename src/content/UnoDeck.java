/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package content;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 * UnoDeck class that keeps track of va
 * @author cute_
 */
public class UnoDeck {
    
    private UnoCard [] cards;
    private int cardsInDeck;
            
            public UnoDeck()
            {
                //no properties attached yet, values are null
                cards = new UnoCard [108];
            }
            
            public void reset()
            {
                //array of all possible colors 
                UnoCard.Color[] colors = UnoCard.Color.values();
                cardsInDeck = 0;
                
                //do not want to include last "Wild" color
                for(int i = 0; i < colors.length - 1; i++)
                {
                    //will cycle through each color in the Color enumeration 
                    UnoCard.Color color = colors[i];
                    //1 "Zero" Card 
                    cards[cardsInDeck++] = new UnoCard(color, UnoCard.Value.getValue(0));
                    
                    //covers all cards from 1 to 9
                    for (int j = 1; j <= 9; j++)
                    {
                        cards[cardsInDeck++] = new UnoCard(color, UnoCard.Value.getValue(j));
                        cards[cardsInDeck++] = new UnoCard(color, UnoCard.Value.getValue(j));
                    }
                    
                    UnoCard.Value[] values = new UnoCard.Value[]{UnoCard.Value.DRAWTWO, 
                    UnoCard.Value.SKIP, UnoCard.Value.REVERSE};
                    
                    for(UnoCard.Value value : values)
                    {
                        cards[cardsInDeck++] = new UnoCard(color, value);
                        cards[cardsInDeck++] = new UnoCard(color, value);
                    }
                }
                
                /**WILD AND WILDFOUR are values of the color WILD
                 * default colors not applied here
                 * separate array for WILD color cards
                 */
                UnoCard.Value[] values = new UnoCard.Value[]{UnoCard.Value.WILD, 
                    UnoCard.Value.WILDFOUR};
                
                for(UnoCard.Value value : values)
                {
                    for(int i = 1; i <=4; i++)
                    {
                        cards[cardsInDeck++] = new UnoCard(UnoCard.Color.WILD, value);
                    }
                }  
            }
            
            /**
             * method to replace the deck to draw from
             * with the stockpile
             * (the cards that have been put down)
             * used when cards run out 
             */
            public void replaceDeckWith(ArrayList<UnoCard> stockpile)
            {
                /**UnoCard[cards.size()] --> creates a new Array of UnoCards
                 * with the same size as the initial deck to draw from
                 */
                this.cards = stockpile.toArray(new UnoCard[stockpile.size()]);
                this.cardsInDeck = this.cards.length;
            }
            
            /**
             * @return true if deck is found empty
             */
            public boolean isEmpty()
            {
                return cardsInDeck == 0;
            }
            
            /**
             * get a random index past the current index 
             */
            public void shuffle()
            {
                
                int n = cards.length;
                Random random = new Random();
                
                //for loop to iterate through each position in the deck
                for(int i = 0; i < cards.length; i++)
                {
                    int randomValue = i + random.nextInt(n - i);
                    //assign random value between 0 to 108 
                    UnoCard randomCard = cards[randomValue];
                    /*the card at e.g. the first index will be
                    at a  random index such as 6
                    the first card will then be 
                    */
                    cards[randomValue] = cards[i];
                    cards[i] = randomCard;
                }
                
            }
            
            //method to draw a card at current index 
            public UnoCard drawCard() throws IllegalArgumentException
            {
                if(isEmpty())
                {
                    throw new IllegalArgumentException("Deck is empty. Cannot draw cards.");
                }
                return cards[--cardsInDeck];
            }
            
            
            public ImageIcon drawCardImage() throws IllegalArgumentException
            {
                if(isEmpty())
                {
                    throw new IllegalArgumentException("Deck is empty. Cannot draw cards");
                }
                return new ImageIcon(cards[--cardsInDeck].toString() + ".png");
            }
            
            public UnoCard[] drawCard(int n)
            {
                //create an array that returns n number of cards 
                UnoCard [] returnCards = new UnoCard[n];
                
                /*
                 * exception handling for
                 * 1. negative number
                 * 2. exceeding number of cards already in deck
                 */
                
                if(n < 0)
                {
                    throw new IllegalArgumentException("");
                }
                
                if(n < cardsInDeck)
                {
                    throw new IllegalArgumentException("Cannot draw " + n +
                            " cards since there are only " + cardsInDeck +
                            " cards in the deck.");
                }
                
            
                //create a for loop that removes each card 
                for(int i = 0; i < n; i++)
                {
                    returnCards[i] = cards[--cardsInDeck];
                }
                
                return returnCards;
            }
            
            
            
            
    
}
