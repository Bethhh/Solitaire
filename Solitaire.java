import java.awt.*;
import java.awt.event.*;
import objectdraw.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;



public class Solitaire extends WindowController 
	               implements MouseMotionListener,
                                  MouseListener {
  private static int BORDER = 20;
  private static int LINE = 15;
  private static int SPACE = 10;
  private static int WIDTH = 100;
  private static int HEIGHT = 150;

  private static int RandomIntGenerator r;

  private static PriorityQueue<Card> pq;
  private static Queue<Card> otherCard;

  private static Card[] topArray;
  private static Card[] cArray;

  public static Card grabbed;

  private static Card first;
  private static Card second;
  private static Card third;

  private static boolean reset = false;

  private static Location initial = new Location(BORDER, BORDER );

  private static Location other1 = new Location( initial.getX() + WIDTH + SPACE,
                                                 initial.getY() );
  private static Location other2 = new Location( initial.getX() + WIDTH + SPACE 
                                                + LINE * 2, initial.getY() );
  private static Location other3 = new Location( initial.getX() + WIDTH + SPACE
                                                + LINE * 4, initial.getY() );

  public void begin(){
    reset = false;
    r = new RandomIntGenerator(1, 100);
    pq = new PriorityQueue<Card>(52, new CardPriority());
   

    Card c;
    cArray = new Card[13];//7 11
    topArray = new Card[12];
    cArray[12] = new Card(initial, canvas, 14 );// the empty card for othercard
    cArray[12].pile = 13;
    cArray[12].setTop(true);

    for( int i = 1; i <= 13; i++ ){
      pq.offer(new Clubs(initial, canvas,this, r.nextValue()));
      pq.offer(new Hearts(initial, canvas,this, r.nextValue()));
      pq.offer(new Diamonds(initial,canvas,this, r.nextValue()));
      pq.offer(new Spades(initial, canvas, this, r.nextValue()));
    }

    int x = BORDER;
    

    for( int k = 1; k <= 7; k++ ){
      ////c = pq.poll();
      ////c.pile = k;
      ////cArray[k-1] = c;
      int y = BORDER+BORDER+HEIGHT-LINE;

      cArray[k-1] = new Card(new Location(initial.getX()+WIDTH*(k-1)+SPACE*(k-1), y+LINE), canvas, 14 );
           ////c.moveTo(new Location(x, y));
      cArray[k-1].pile = k;
      Card n = cArray[k-1];

      //System.out.println("    " + k );
      for( int i = 1; i <= k; i++ ){
      	y = y + LINE;
      	c = pq.poll();
      	c.pile = k;
        c.moveTo(new Location(x, y));
        c.sendToFront();
        n.next = c;
      	c.previous = n;
      	n = c;
	      //y = y + LINE;
	      //System.out.println(i +" "+k);

      }
      n.show();
      n.setTop(true);
      topArray[k-1] = n;
      x = x + WIDTH + SPACE;
    }

    for( int i = 7; i < 11; i++ ) {
      cArray[i] = new Card(new Location(initial.getX()+WIDTH*(i-4)+SPACE*(i-4), BORDER), canvas, 0 );//3 4 5 6
      topArray[i] = cArray[i];
      cArray[i].pile = i + 1;
      cArray[i].setTop(true);
    }

    otherCard = new LinkedList<Card>();
    while(pq.size() != 0){
      otherCard.add(pq.poll());
    }

    otherCard.add(cArray[12]);

    canvas.addMouseListener( this );
    canvas.addMouseMotionListener( this );

  }


  public void mouseDragged(MouseEvent e){}
  public void mouseMoved(MouseEvent e){}

  public void mouseReleased(MouseEvent e){
    boolean move = false;
    for( int i = 0; i < Solitaire.topArray.length; i++ ){
      //System.out.println("tA[i].pile" +i);
      if(grabbed != null && topArray[i] != null && 
         i!=grabbed.pile-1 &&
         topArray[i].contains(new Location(e.getX(), e.getY()))){
        if( topArray[i].canConnect(grabbed) && topArray[i].pile < 8 ){//pile else if
          //System.out.println("AAA");
	        //System.out.println("cccgrabbed.pile=" + grabbed.pile + "tA[i].pile" + topArray[i].pile);
          Card b = topArray[i];
          Solitaire.move(grabbed, topArray[i]);
          Location newLoc = new Location(b.loc.getX(), b.loc.getY()+LINE);

          if( b.getEmpty() ){
            newLoc = new Location(b.loc.getX(), b.loc.getY());
          }      
          grabbed.moveTo(newLoc);
          grabbed.sendToFront();//
          Location newLoc2 = new Location(newLoc);
          while(grabbed.next!=null){
            newLoc2.translate(0, LINE);
            grabbed.next.moveTo(newLoc2);
            grabbed.next.sendToFront();//
            grabbed = grabbed.next;
            newLoc2 = new Location(newLoc2);
          }
          move = true;
          grabbed = null;
          //move this card to topArray[i]
        }//end if canconnect
        else if(topArray[i].pile < 12 && topArray[i].pile > 7 
                && grabbed.getTop() && topArray[i].isNext(grabbed) ){//test not top
          //System.out.println("AAA");
	        //System.out.println("cccgrabbed.pile=" + grabbed.pile + "tA[i].pile" + topArray[i].pile);
          Card b = topArray[i];
          Solitaire.move(grabbed, topArray[i]);
          Location newLoc = new Location(b.loc.getX(), b.loc.getY());
     
          grabbed.moveTo(newLoc);
          grabbed.sendToFront();//
          
          move = true;
          grabbed = null;

        }
      }//else if(topArray[i]=null//move 13 only
    }
    if(!move && grabbed != null){
  
      grabbed.moveTo(grabbed.getOrig());
      grabbed.sendToFront();//

      while(grabbed.next!=null){
     
        grabbed.next.moveTo(grabbed.next.getOrig());
        grabbed.next.sendToFront();//
     
	      grabbed = grabbed.next;
      }
    }
  }
  public void mousePressed(MouseEvent e){}

  public void mouseClicked(MouseEvent e){
  //no card case
    Location point = new Location( e.getX(), e.getY() );
    if( cArray[12].contains( point ) )
    {
      System.out.println( "contains" );//contains?
      if( first != null && first.pile == 12 ){
        otherCard.add(first);
        first.setTop(false);
      }
      if( second != null && second.pile == 12 ){
        otherCard.add(second);
        second.moveTo(other1);
        second.setTop(false);
      }
      if( third != null && third.pile == 12 ){
        otherCard.add(third);
        third.moveTo(other1);
        third.setTop(false);
      }
      if( topArray[11] != null ){
        topArray[11].setTop(false);
      }

      if( reset){
        otherCard.add(otherCard.poll());
        Card a = cArray[11];
        //System.out.println(a.num);
        while(a != null ){
          a.previous = null;
          a.moveTo(initial);
          a.hide();
          a.sendToFront();
          System.out.println(a.num);
          Card b = a.next;
          a.next = null;
          a = b;

        }
       
        cArray[11] = null;
        topArray[11] = null;
        first = null;
        second = null;
        third = null;
        reset = false;

        //move back card
      }else{
        System.out.println("resetflase");
        if( otherCard.peek() == cArray[12] ){
          first = null;
          second = null;
          third = null;
        }else{
          first = otherCard.poll();
          first.moveTo(other1);
          first.sendToFront();
          first.show();
          if( otherCard.peek() == cArray[12] ){
            second = null;
            third = null;
            first.setTop(true);
          }else{
            second = otherCard.poll();
            second.moveTo(other2);
            second.sendToFront();
            second.show();
            if( otherCard.peek() == cArray[12] ){
              third = null;
              second.setTop(true);
            }else{
              third = otherCard.poll();
              third.moveTo(other3);
              third.sendToFront();
              third.show();
              third.setTop(true);

            }

          }
        }
      
        if( first == null || second == null || third == null || otherCard.peek() == cArray[12] ){
          System.out.println("inempty");
          cArray[12].sendToFrontEmpty();
          reset = true;
        }

        if( topArray[11] == null && first != null ) {//test null
          cArray[11] = first;
          cArray[11].previous = null;
          topArray[11] = cArray[11];
        }else if( first != null ){
          topArray[11].next = first;
          first.previous = topArray[11];
          topArray[11] = first;
        }
        if( first != null ) {
          first.next = second;
          first.pile = 12;
          if( second != null ) {
            second.previous = first;
            second.next = third;
            second.pile = 12;
            if( third != null ) {
              third.previous = second;
              third.next = null;
              third.pile = 12;
              topArray[11] = third;
            }else{
              topArray[11] = second;
            }
          }else{
            topArray[11] = first;
          }
        }
      }//end if reset

     
    }//end if contains
  }


  public void mouseExited(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}

  public static void move(Card a, Card b){
    //System.out.println("a.pile=" + a.pile + "b.pile=" + b.pile);
    topArray[a.pile-1] = a.previous;
    if(a.previous != null && !(a.previous.getEmpty()) ){
      a.previous.setTop(true);
      a.previous.show();
      a.previous.next = null;
    }else if(a.previous != null &&  a.previous.getEmpty() ){//empty
      a.previous.setTop(true);

      a.previous.next = null;
    }
   // System.out.println("a.pile="+a.pile);

    //System.out.println("b.pile="+b.pile);
    topArray[b.pile-1].next = a;//keep going
    while(a.next!=null){
      a.previous = topArray[b.pile-1];
      topArray[b.pile-1] = a;
      a.pile = b.pile;
      a = a.next;
    }
    a.previous = topArray[b.pile-1];
    a.next = null;
    b.setTop(false);
    //a.setTop(true);
    topArray[b.pile-1] = a;//a.next.next...
    a.pile = b.pile;
    
  }

}
