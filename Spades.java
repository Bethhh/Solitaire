import java.awt.*;
import java.awt.event.*;
import objectdraw.*;
import javax.swing.*;
import javax.swing.event.*;


public class Spades extends Card{
  private static int num = 1;
  
  public Spades(Location loc, DrawingCanvas canvas,
		WindowController w, int priority){
    
    super(Color.BLACK,w.getImage("spades.jpg"),
          w.getImage("spades2.jpg"), loc, canvas, num, priority);
    this.num++;
  }


 
}
