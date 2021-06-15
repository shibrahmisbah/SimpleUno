/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package content;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Game class that handles all interactions between UnoCard and UnoDeck class
 * @author cute_
 */
public class Game {
    
    //keep track of player
    private int currentPlayer;
    //storing Player IDs
    private String[] playerIds;
    
    //create an object of the UnoDeck
    private UnoDeck deck;
    //arraylist of an arraylist of all hands (each hand has 1 arraylist of cards)
    private ArrayList<ArrayList<UnoCard>> playerHand;
    private ArrayList<UnoCard> stockpile;
    
    private UnoCard.Color validColor;
    private UnoCard.Value validValue;
    
    //counterclockwise / clockwise for reverse card 
    boolean gameDirection;
    
    public Game(String[] pids)
    {
        //initializing private data fields
        currentPlayer = 0;
        playerIds = pids;
        deck = new UnoDeck();
        deck.shuffle(); 
        stockpile = new ArrayList<UnoCard>();
        gameDirection = false;
        playerHand = new ArrayList<ArrayList<UnoCard>>();
        
        //fill up each hand with UnoCards using a for loop
        for(int i = 0; i < pids.length; i++)
        {
            //creates one hand of UnoCards per player and stores it in playerHand
            ArrayList<UnoCard> hand = new ArrayList<UnoCard>(Arrays.asList(deck.drawCard(7)));
            playerHand.add(hand);
        }
    }
        /**
         * a method that adds valid cards to the stockpile at the start of game
         * @param game 
         */
        public void start(Game game)
        {
            UnoCard card = deck.drawCard();
            validColor = card.getColor();
            validValue = card.getValue();
            
            if(card.getValue() == UnoCard.Value.WILD)
            {
              start(game);  
            }
            
            if(card.getValue() == UnoCard.Value.WILDFOUR || card.getValue() == UnoCard.Value.DRAWTWO)
            {
              start(game);
            }
            
            if(card.getValue() == UnoCard.Value.SKIP)
            {
              //print out message for player 
              JLabel message = new JLabel(playerIds[currentPlayer] + " was skipped");
              message.setFont(new Font("Arial", Font.BOLD, 48));
              JOptionPane.showMessageDialog(null, message);
              
              //decide which player to go to after skipping
              if(gameDirection == false)
              {
                  currentPlayer = (currentPlayer + 1) % playerIds.length;
              }
              else if(gameDirection == true)
              {
                currentPlayer = (currentPlayer - 1) % playerIds.length;  
                
                if(currentPlayer == -1)
                {
                    currentPlayer = playerIds.length - 1;
                }
              }
            }
              
            if(card.getValue() == UnoCard.Value.REVERSE)
              {
                 JLabel message = new JLabel(playerIds[currentPlayer] + 
                         " The game direction has changed.");
                 message.setFont(new Font("Arial", Font.BOLD, 48));
                 JOptionPane.showMessageDialog(null, message);
                 //flip the game direction
                 gameDirection ^= true;
                 currentPlayer = playerIds.length - 1;
              }
              
              //if any of the conditions are met, add card to stockpile
              stockpile.add(card);   
            } 
        
        public UnoCard getTopCard()
        {
            return new UnoCard(validColor, validValue);
        }
        
        public ImageIcon getTopCardImage()
        {
            return new ImageIcon(validColor + "-" + validValue + ".png");
        }
        
        //end game if any player has an empty hand
        public boolean isGameOver()
        {
            for(String player : this.playerIds)
            {
                if(hasEmptyHand(player)){
                    return true;
                }
            } 
            return false;
        }
        
        public String getCurrentPlayer()
        {
            return playerIds[this.currentPlayer];
        }
        
        public String getPreviousPlayer(int i)
        {
            int index = currentPlayer - i;
            
            //if index is -1 (was currently 0) then index = last player in array 
            if(index == -1){
                index = playerIds.length - 1;
            }
            
            return playerIds[index];
        }
        
        public String[] getPlayers()
        {
            return playerIds;
        }
        
        public ArrayList<UnoCard> getPlayerHand(String pid)
        {
            int index = Arrays.asList(playerHand).indexOf(pid);
            return playerHand.get(index);
        }
        
        //get size of specified player 
        public int getPlayerHandSize(String pid)
        {
            return getPlayerHand(pid).size();
        }
        
        /**
         * choice variable is the index of the particular card we want to get
         * returns an ArrayList of UnoCards that gets the particular index
         * @param pid
         * @param choice
         * @return 
         */
        public UnoCard getPlayerCard(String pid, int choice)
        {
            ArrayList<UnoCard> hand = getPlayerHand(pid);
            return hand.get(choice);
        }
               
         public boolean hasEmptyHand(String pid)
        {
            return getPlayerHand(pid).isEmpty();
        }
         
        public boolean validCardPlay(UnoCard card)
        {
            return card.getColor() == validColor || card.getValue() == validValue;
        }
        
        public void checkPlayerTurn(String pid) throws InvalidPlayerTurnException
        {
            if(this.playerIds[this.currentPlayer] != pid)
            {
                throw new InvalidPlayerTurnException("it is not " + pid + 
                        "'s turn", pid);
            }
            
        }
        
        public void submitDraws(String pid) throws InvalidPlayerTurnException
        {
            checkPlayerTurn(pid);
            
            if(deck.isEmpty())
            {
                deck.replaceDeckWith(stockpile);
                deck.shuffle();
            }
            
            getPlayerHand(pid).add(deck.drawCard());
            
                if(gameDirection == false)
                {
                    currentPlayer = (currentPlayer + 1) % playerIds.length;
                }
                else if(gameDirection == true)
                {
                    currentPlayer = (currentPlayer - 1) % playerIds.length;
                    if(currentPlayer == -1)
                    {
                        currentPlayer = playerIds.length - 1;
                    }
                }
            
                
            }
        
        public void setCardColor(UnoCard.Color color)
        {
            validColor = color;
        }
        
        public void submitPlayerCard(String pid, UnoCard card, UnoCard.Color declaredColor)
        throws InvalidPlayerTurnException, InvalidColorSubmissionException,InvalidValueSubmissionException
        {
            //check for valid player turn and create an arraylist for current PlayerHand
            checkPlayerTurn(pid);
            ArrayList<UnoCard> pHand = getPlayerHand(pid);
            
            UnoCard.Color actualColor = card.getColor();
            UnoCard.Color expectedColor = validColor;
            
            UnoCard.Value actualValue = card.getValue();
            UnoCard.Value expectedValue = validValue;
            
            
            if(!validCardPlay(card))
            {
                if(card.getColor() == UnoCard.Color.WILD)
                {
                    validColor = card.getColor();
                    validValue = card.getValue();
                }
                
                if(card.getColor() != validColor)
                {
                    JLabel messageC = new JLabel("Invalid player move, expected color:"
                            + expectedColor + " but got color " + actualColor);
                    messageC.setFont(new Font("Arial", Font.BOLD, 38));
                    JOptionPane.showMessageDialog(null, messageC);
                    throw new InvalidColorSubmissionException(messageC, actualColor, expectedColor);
                }
                
                if(card.getValue() != validValue)
                {
                    JLabel messageV = new JLabel("Invalid player move, expected color:"
                            + expectedValue + " but got color " + actualValue);
                    messageV.setFont(new Font("Arial", Font.BOLD, 38));
                    JOptionPane.showMessageDialog(null, messageV);
                    throw new InvalidValueSubmissionException(messageV, actualValue, expectedValue);
                }
            }//end of if block
            
            pHand.remove(card);
            
            //declaring winner
            if(hasEmptyHand(playerIds[currentPlayer]))
            {
                JLabel message = new JLabel(playerIds[currentPlayer]
                                            + " won the game. Thank you for playing!");
                    message.setFont(new Font("Arial", Font.BOLD, 38));
                    JOptionPane.showMessageDialog(null, message);
                    System.exit(0);
            }
            
            validColor = card.getColor();
            validValue = card.getValue();
            stockpile.add(card);
            
            if(gameDirection == false)
                {
                    currentPlayer = (currentPlayer + 1) % playerIds.length;
                }
                else if(gameDirection == true)
                {
                    currentPlayer = (currentPlayer - 1) % playerIds.length;
                    if(currentPlayer == -1)
                    {
                        currentPlayer = playerIds.length - 1;
                    }
                }
            
           if(card.getColor() == UnoCard.Color.WILD)
           {
                validColor = declaredColor;
           }
           
           if(card.getValue() == UnoCard.Value.DRAWTWO)
           {
               pid = playerIds[currentPlayer];
               getPlayerHand(pid).add(deck.drawCard());
               getPlayerHand(pid).add(deck.drawCard());
               JLabel message = new JLabel(pid + " drew 2 cards!");
           }
            
           if(card.getValue() == UnoCard.Value.WILDFOUR)
           {
               pid = playerIds[currentPlayer];
               getPlayerHand(pid).add(deck.drawCard());
               getPlayerHand(pid).add(deck.drawCard());
               getPlayerHand(pid).add(deck.drawCard());
               getPlayerHand(pid).add(deck.drawCard());
               JLabel message = new JLabel(pid + " drew 4 cards!");
           }
           
            if(card.getValue() == UnoCard.Value.SKIP)
           {
               JLabel message = new JLabel(playerIds[currentPlayer] + " was skipped!");
               message.setFont(new Font("Arial", Font.BOLD, 38));
               JOptionPane.showMessageDialog(null, message);
               if(gameDirection == false)
               {
                   currentPlayer = currentPlayer + 1 % playerIds.length;
               }
               else if(gameDirection == true)
               {
                    currentPlayer = currentPlayer - 1 % playerIds.length;
                    if(gameDirection == -1)
                    {
                        currentPlayer = playerIds.length - 1;
                    }
               }
           }

           if(card.getValue() == UnoCard.Value.REVERSE)
           {
             JLabel message = new JLabel(pid + " reversed the direction!");
             message.setFont(new Font("Arial", Font.BOLD, 38));
             JOptionPane.showMessageDialog(null, message);
             
             gameDirection ^= true;
             if()
           }
           
           
            
           
            
        }
        
        }//end of game class

        /**
         * InvalidPlayerTurnException
         * a constructor that takes a message and player ID
         * set PlayerId to pid
         * create an accessor method for PlayerId
         * @author cute_
         */


        class InvalidPlayerTurnException extends Exception
        {
            String PlayerId;
            
            public InvalidPlayerTurnException(String message, String pid)
            {
                super(message);
                PlayerId = pid;
            }
            
            public String getPid()
            {
                return PlayerId;
            }
        //end of IPTE class
        }

        class InvalidColorSubmissionException extends Exception
        {
            private UnoCard.Color expected;
            private UnoCard.Color actual;
            
            public InvalidColorSubmissionException(JLabel message, UnoCard.Color actual, UnoCard.Color expected)
            {
                this.actual = actual;
                this.expected = expected;
            }  
            
        }//end of InvalidColor class

        class InvalidValueSubmissionException extends Exception
        {
            private UnoCard.Value expected;
            private UnoCard.Value actual;
            
            public InvalidValueSubmissionException(JLabel message,
                    UnoCard.Value actual, UnoCard.Value expected)
            {
                this.actual = actual;
                this.expected = expected;
            }
    
        }//end of InvalidValue class
 
    
