import java.util.*;

/**
 * Implements a generic general tree node where the type of the
 *  item in each node is specified by the type variable E
 *  There are many ways of designing such a tree class;
 *  this is a very simple one which does not have many features.
 */
public class BTNode <E> implements Iterable <BTNode<E>>{

    private E item;
    private List<BTNode<E>> children;

    /**
     * Constructor for objects of class BTNode
     */
    public BTNode(E item){
        this.item = item;
        this.children = new ArrayList<BTNode<E>>();
    }

    public BTNode(String next, BTNode<String> readExpr, BTNode<String> readExpr2) {
		
	}

	/**
     * Getters and Setters     */
    public E getItem(){ return item; }
    public void setItem(E item) { this.item = item; }

    public int numberOfChildren() { return children.size(); }

    public BTNode<E> getChild(int i){ return children.get(i);}

    /** Add child at end */
    public void addChild(BTNode<E> node){ children.add(node);}

    /** Add child at position*/
    public void addChild(int i, BTNode<E> node){ children.add(i,node);}

    /** Remove child at position*/
    public void removeChild(int i){  children.remove(i);}

    /** 
     * Enables foreach:
     *      for (GTNode<E> child : node){... 
     * to loop through the children of a node
     */
    public Iterator<BTNode<E>> iterator(){ return children.iterator(); }

	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	public BTNode<String> getLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	public BTNode<String> getRight() {
		// TODO Auto-generated method stub
		return null;
	}

}