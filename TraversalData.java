import java.util.ArrayList;

/** 
 *  Data class for traversal algorithms, containing a node and the edge the node came from in the traversal.
 * 
 *  @author Brooke Fitzgerald
 *  @version CSC 212, December 10, 2016 */
public class TraversalData<N,E> {
	/** Node that you're traversing from */
	private Graph<N, E>.Node node;
	/** Edge that you follow from the node */
	private Graph<N, E>.Edge edge;
	
	/** Constructor for TraversalData */
	public TraversalData(Graph<N, E>.Node n,Graph<N, E>.Edge e){
		node = n;
		edge = e;
	}
	
	/** Accessor for node */
	public Graph<N, E>.Node getNode(){
		return(node);
	}
	
	/** Accessor for edge */
	public Graph<N, E>.Edge getEdge(){
		return(edge);
	}
}
