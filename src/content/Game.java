/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package content;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
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
         * 
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
              
              //if none of the if conditions are met, add card to stockpile
              stockpile.add(card);   
            } 
        }
 
    
