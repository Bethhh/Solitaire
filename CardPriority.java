import java.util.*;

/* Comparator class use for card comparison */
public class CardPriority implements Comparator{
  
  public int compare(Object o1, Object o2){
    Card c1 = (Card) o1;
    Card c2 = (Card) o2;
    return c1.getPriority() - c2.getPriority();
  }
}
