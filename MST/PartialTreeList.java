package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.MinHeap;
import structures.PartialTree;
import structures.Vertex;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
    public static PartialTreeList initialize(Graph graph) {

    	

		/* COMPLETE THIS METHOD */

        PartialTreeList partialtreelist = new PartialTreeList();



        

        for (int i=0; i < graph.vertices.length; i++) {



            Vertex aVertex = graph.vertices[i];



            PartialTree partialTree = new PartialTree(aVertex);



            Vertex.Neighbor aNeighborInLoop = aVertex.neighbors;                    

            while (aNeighborInLoop != null) {

                Arc arc = new Arc(aVertex, aNeighborInLoop.vertex, aNeighborInLoop.weight);



                partialTree.getArcs().insert(arc);



                aNeighborInLoop = aNeighborInLoop.next;

            }



            partialtreelist.append(partialTree);

        }

                return partialtreelist;

		

	}
	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
public static ArrayList<Arc> execute(PartialTreeList ptlist) {

		

		/* COMPLETE THIS METHOD */



        ArrayList<Arc> arcsArrayList = new ArrayList<>();

        while (ptlist.size() > 1) {



            PartialTree tree = ptlist.remove();



            MinHeap<Arc> minHeap = tree.getArcs();



            Arc minArc = minHeap.deleteMin();

            while (minArc != null) {                                        

                

                Vertex v1 = minArc.getv1();

                Vertex v2 = minArc.getv2();



                PartialTree otherPartialTree;

               

                otherPartialTree = ptlist.removeTreeContaining(v1);         

                if (otherPartialTree == null) {

                    

                    otherPartialTree = ptlist.removeTreeContaining(v2);     

                   

                }



                

                if (otherPartialTree != null) {                             

                    tree.merge(otherPartialTree);

                    arcsArrayList.add(minArc);                               

                    

                    ptlist.append(tree);                            

                    

                    break;                                                  

                }

                else {

                    // v1 and v2 of the arc are not in any other PartialTree.

                    // They both are in the current PartialTree. Ignore and continue with the next arc from the MinHeap

                }



                

                minArc = minHeap.deleteMin();

            }

        }

        

        return arcsArrayList;	

	}
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 

	/* COMPLETE THIS METHOD */

throws NoSuchElementException {

PartialTree partialtreeremove = null;





if (rear == null) {

    throw new NoSuchElementException("Empty Tree List.");

}





Node hold = rear;





do {

    

    PartialTree tree = hold.tree;

    boolean isVertexInTree = checkVertexInPartialTree(tree, vertex);

    if (isVertexInTree) {

        

        partialtreeremove = tree;

       

        removeNodeFromList(hold);

       

        break;

    }

  

    hold = hold.next;



   

} while (hold != rear);



if (partialtreeremove == null) {

    

    return null;

}

else {

    return partialtreeremove;

}

}



private boolean checkVertexInPartialTree (PartialTree partialTree, Vertex vertex) {

Vertex parentTree = vertex;

while (parentTree.parent != parentTree) {         

    parentTree = parentTree.parent;

}

return parentTree == partialTree.getRoot();

}



private void removeNodeFromList (Node node) {

Node nodeBefore;               

nodeBefore = node;

while (!(nodeBefore.next == node)) {

    nodeBefore = nodeBefore.next;

}



Node nodeAfter = node.next;



if (nodeAfter == node && nodeBefore == node) {

    rear = null;

    size--;

}

else if (nodeAfter == nodeBefore) {

    if (node == rear) {                        

        rear = rear.next;

    }

    

    (node.next).next = node.next;             

    size--;

}



else {

    if (node == rear) {

        

        rear = nodeBefore;

    }

   

    nodeBefore.next = nodeAfter;

    size--;

}



node.next = null;

}
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}

