import java.awt.*;
import java.awt.event.*;
import objectdraw.*;
import javax.swing.*;
import javax.swing.event.*;

public class Card implements MouseMotionListener,
                             MouseListener{

  protected Color color;
  //protected int num;
  public int num;
  public int pile;

  protected int priority;

  protected boolean hidden = true;
  protected boolean cardGrabbed = false;
  protected boolean top = false;
  protected boolean empty = false;

  public Card next;
  public Card previous;

  // The image
  protected Image img;
  protected Location imgloc;
  protected VisibleImage cardImg;
  protected static int SPACE_H = 25;//space from the top

  // The small image
  protected Image img_s;
  protected Location img_s_loc;
  protected VisibleImage cardImg_s;

 
  // Text
  protected Location textloc;
  protected static int TEXT_S = 3;//space from left
  protected Text number;

  protected DrawingCanvas canvas;

  // The rec Card
  protected Location loc;
  protected Location orig;
  protected static int WIDTH = 100;
  protected static int HEIGHT = 150;
  protected static int LINE = 15; 
  protected FilledRect rec;
  protected FilledRect back;
  protected FramedRect border;
  protected FramedOval o;//change to pic later

  // The current location of the mouse
  private Location point;
  // The last location of the mouse
  private Location lastPoint;
  public Card( Location loc, DrawingCanvas canvas, int num ){
    this.color = Color.WHITE;
    this.canvas = canvas;
    this.loc = loc;
    this.num = num;
    this.rec = new FilledRect(loc, WIDTH, HEIGHT, canvas);
    rec.setColor(Color.WHITE);
    this.border = new FramedRect(loc, WIDTH, HEIGHT, canvas);
    this.o = new FramedOval(loc, WIDTH, HEIGHT, canvas);
    this.empty = true;
    this.top = false;
    this.hidden = true;
  }


  public Card( Color color, Image img, Image img_s, Location loc,
               DrawingCanvas canvas, int num, int priority ) {

    this.color = color;
    this.canvas = canvas;
    this.num = num;
    this.priority = priority;

    this.img = img;
    this.img_s = img_s;

    this.loc = loc;
    this.orig = loc;

    this.imgloc = new Location( loc.getX(), loc.getY() + SPACE_H );
    this.img_s_loc = new Location( loc.getX() + 15, loc.getY() + 4 );
    this.textloc = new Location( loc.getX()+TEXT_S, loc.getY() );
    
    this.rec = new FilledRect(loc, WIDTH, HEIGHT, canvas);
    rec.setColor(Color.WHITE);
    
    this.cardImg = new VisibleImage( this.img, imgloc, canvas);
    this.cardImg_s = new VisibleImage( this.img_s, img_s_loc, canvas);
    this.number = new Text(num, textloc, canvas); 

    this.back = new FilledRect(loc, WIDTH, HEIGHT, canvas);
    this.back.show();
    this.border = new FramedRect(loc, WIDTH, HEIGHT, canvas);
   
    if(num == 11){
      this.number.setText("J");
    }else if(num == 12 ){
      this.number.setText("Q");
    }else if(num == 13 ){
      this.number.setText("K");
    }else if(num == 1 ){
      this.number.setText("A");
    }
    this.back.setColor(Color.PINK);
    number.setColor(color);
    number.setBold();
    canvas.addMouseListener( this );
    canvas.addMouseMotionListener( this );
    
  }
  public void moveTo(Location point){

    this.orig = point;
    this.moveTo2(point);
  }

  //no orig location move
  //move for drag
  public void moveTo2(Location point){
    this.loc = point;
    this.imgloc = new Location(point.getX(), point.getY() + SPACE_H);
    this.img_s_loc = new Location(point.getX()+15, point.getY()+4);
    this.textloc = new Location(point.getX()+TEXT_S, point.getY());

    this.rec.moveTo(point);
    this.cardImg_s.moveTo(img_s_loc);
    this.cardImg.moveTo(imgloc);
    this.number.moveTo(textloc);
    this.back.moveTo(point);
    this.border.moveTo(point);
  }
  public int getPriority(){
    return this.priority;
  }

  public void show(){
    this.back.hide();
    this.hidden = false;
  }
  public void hide(){
    this.back.show();
    this.hidden = true;
  }
  public boolean getHidden(){
    return this.hidden;
  }
  public boolean getEmpty(){
    return this.empty;
  }
  public void sendToFront(){
    this.rec.sendToFront();
    this.cardImg.sendToFront();
    this.cardImg_s.sendToFront();
    this.number.sendToFront();
    this.back.sendToFront();
    this.border.sendToFront();
  }
  public void sendToFrontEmpty(){
    this.rec.sendToFront();
    this.o.sendToFront();
    this.border.sendToFront();
  }

  public void mouseDragged(MouseEvent e){
    Location point = new Location(e.getX(), e.getY());
    Location newLoc = new Location(loc);
    newLoc.translate(point.getX() - lastPoint.getX(),
	                   point.getY() - lastPoint.getY());
    //System.out.println("Drag"+cardGrabbed);
    if(cardGrabbed && !(this.getEmpty())){
      System.out.println("Drag"+this.num);
      if( newLoc.getX() <= canvas.getWidth()
          && newLoc.getX() >= 0
          && newLoc.getY() <= canvas.getHeight()
          && newLoc.getY() >= 0 ){
       	//System.out.println("Draggggg");
        this.sendToFront();
       	//System.out.println("Drdddddddaggggg");
        this.moveTo2(newLoc);
        Card b = this;
        Location newLoc2 = new Location(newLoc);
        //System.out.println("b.num"+b.num + " "+ newLoc2.getY());
        while(b.next!=null){  
	        //System.out.println("bbbbbb");	  
          newLoc2.translate(0,LINE);
          b.next.moveTo2(newLoc2);
	        //System.out.println("bbbbbb");
          b.next.sendToFront();
	        //System.out.println("bbbbbb");
          b = b.next;
          newLoc2 = new Location(newLoc2);
          //System.out.println("b.num"+b.num + " "+ newLoc2.getY());
        }
        lastPoint = point;
      }
      Solitaire.grabbed = this;
    }
  }
  public void mouseMoved(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseReleased(MouseEvent e){
   /* boolean move = true;
    if(move){
      for( int i = 0; i < Solitaire.topArray.length; i++ ){
        if(Solitaire.topArray[i].contains(new Location(e.getX(), e.getY()))){
          if(Solitaire.topArray[i].canConnect(this)){
	    System.out.println("this.pile=" + this.pile + "tA[i].pile" + Solitaire.topArray[i].pile);
	    Card b = Solitaire.topArray[i];
            Solitaire.move(this, Solitaire.topArray[i]);
	    Location newLoc = new Location(b.loc.getX(), b.loc.getY()+LINE);
	    this.moveTo(newLoc);
	    move = false;
	  //move this card to topArray[i]
          }
        }
      }
    }else{

      this.moveTo(orig);
    }*/
  }
  public void mousePressed(MouseEvent e){
    // Gets the last location
    lastPoint = new Location( e.getX(), e.getY() );
    
    // Check if the ball is grabbed
    cardGrabbed = this.contains( lastPoint );
    // if(cardGrabbed)
    //System.out.println(this.pile);
  }
  public void mouseClicked(MouseEvent e){
  }
  public void mouseExited(MouseEvent e){
  }
  public boolean getGrabbed(){
    return this.cardGrabbed;
  }
  public void setGrabbed(boolean b){
    this.cardGrabbed = b;
  }
  public boolean contains(Location point) {
    if( (this.top && !(this.hidden)) || ( this.top &&this.getEmpty())){
      System.out.println(this.num + "       " + this.top + "    " +this.rec.contains(point));
      return this.rec.contains(point);
    }else if( !(this.hidden) && this.pile != 12){
      //System.out.println(this.top + "    " +this.hidden+" m");
      
      double x = point.getX();
      double y = point.getY();
      //System.out.println("this.loc="+this.loc.getY()+"this.num="+this.num);
      if( x >= this.loc.getX() && x <= this.loc.getX() + WIDTH 
	        && y >= this.loc.getY() && y < this.loc.getY() + LINE){
      	//System.out.println("this.num="+this.num);
        return true;
      }
    }
    return false;
  }
  public void setTop( boolean b){
    this.top = b;
  }
  public boolean getTop(){
    return this.top;
  }
  public Location getOrig(){
    return this.orig;
  }
  public void setOrig(Location orig){
    this.orig = orig;
  }
  public boolean isNext(Card b){
    if( (this.num + 1) == b.num ){
      if(this.getClass() == b.getClass() )
        return true;
      else if( b instanceof Card && this.getEmpty() )
        return true;
    }
    return false;
  }
  public boolean canConnect(Card b){
    if(this.color != b.color && (this.num - 1) == b.num )
      return true;
    return false;
  }

}
