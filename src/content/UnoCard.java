/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package content;

/**
 *
 * @author cute_
 */
public class UnoCard {
    
  /**
   * Representing color and values using enumerations
   * Enums can help keep track of all values (both numerical and string
   * since using just int would over complicate the program)
   */  
    
    enum Color
    {
        //these are the values 
        RED, BLUE, GREEN, YELLOW, WILD;
        
        //initializing an array of our enumeration Color 
        //Color.values() retrieves all the values found in the enum
        private static Color[] colors = Color.values();
        public static Color getColor(int i)
        {
            return Color.colors[i];
        }
        
    }
    
    enum Value
    {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, 
        DRAWTWO, SKIP, REVERSE, WILD, WILDFOUR;
        
        private static Value[] values = Value.values();
        public static Value getValue(int i)
        {
            return Value.values[i];
        }
    }
    
    /**
     * after listing all possible values and colors
     * create the appropriate setters & getters 
     */
    
    private final Color color;
    private final Value value;

    public UnoCard(final Color color, final Value value)
    {
        this.value = value;
        this.color = color;
    }
    
    public Color getColor()
    {
        return this.color;
    }
    
    public Value getValue()
    {
        return this.value;
    }
    
    public String toString()
    {
        return color + " " + value;
    }
    
}
