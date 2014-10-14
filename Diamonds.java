import java.awt.*;
import java.awt.event.*;
import objectdraw.*;
import javax.swing.*;
import javax.swing.event.*;


public class Diamonds extends Card{
  private static int num = 1;
  
  public Diamonds(Location loc, DrawingCanvas canvas, 
		  WindowController w, int priority){
    
    super(Color.RED,w.getImage("diamonds.jpg"),
          w.getImage("diamonds2.jpg"), loc, canvas, num, priority);
    this.num++;
  }


 
}
