import java.util.*;

/**
 * Implements a generic general tree node where the type of the
 *  item in each node is specified by the type variable E
 *  There are many ways of designing such a tree class;
 *  this is a very simple one which does not have many features.
 */
public class GTNode <E> implements Iterable <GTNode<E>>{

    private E item;
    private List<GTNode<E>> children;

    /**
     * Constructor for objects of class BTNode
     */
    public GTNode(E item){
        this.item = item;
        this.children = new ArrayList<GTNode<E>>();
    }

    /**
     * Getters and Setters     */
    public E getItem(){ return item; }
    public void setItem(E item) { this.item = item; }

    public int numberOfChildren() { return children.size(); }

    public GTNode<E> getChild(int i){ return children.get(i);}

    /** Add child at end */
    public void addChild(GTNode<E> node){ children.add(node);}

    /** Add child at position*/
    public void addChild(int i, GTNode<E> node){ children.add(i,node);}

    /** Remove child at position*/
    public void removeChild(int i){  children.remove(i);}

    /** 
     * Enables foreach:
     *      for (GTNode<E> child : node){... 
     * to loop through the children of a node
     */
    public Iterator<GTNode<E>> iterator(){ return children.iterator(); }

}