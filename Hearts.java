import java.awt.*;
import java.awt.event.*;
import objectdraw.*;
import javax.swing.*;
import javax.swing.event.*;


public class Hearts extends Card{
  private static int num = 1;
  
  public Hearts(Location loc, DrawingCanvas canvas, 
		WindowController w, int priority){
    
    super(Color.RED,w.getImage("hearts.jpg"),
          w.getImage("hearts2.jpg"), loc, canvas, num, priority);
    this.num++;
  }


 
}
