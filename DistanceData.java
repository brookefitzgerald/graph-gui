
/** 
 *  Data class for distance algorithms, containing a node, the edge the node came from in the traversal, 
 *  cost, and whether or not the node has been visited or not.
 * 
 *  @author Brooke Fitzgerald
 *  @version CSC 212, December 10, 2016 */
public class DistanceData<N,E> implements Comparable<DistanceData<N,E>>{
	/** Edge to travel along the shortest path */
	private Graph<N, E>.Edge signpost;
	/** Node that you're looking at */
	private Graph<N, E>.Node node;
	/** Cost between this node and the target node */
	private Double cost;
	/** test whether the node has been visited or not */
	private Boolean visited;
	
	/** Constructor for distance data */
	public DistanceData(Graph<N, E>.Node n){
		node = n;
		cost = Double.POSITIVE_INFINITY;
		signpost = null;
		visited = false;
	}
	
	/** Accessor for node */
	public Graph<N, E>.Node getNode(){
		return(node);
	}
	
	/** Accessor for cost */
	public Double getCost(){
		return(cost);
	}
	
	/** Manipulator for cost */
	public void setCost(Double c){
		cost=c;
	}
	
	/** Accessor for visited */
	public boolean checkVisited(){
		return(visited);
	}
	
	/** Manipulator for visited */
	public void markVisited(){
		visited=true;
	}
	
	/** Manipulator for signpost */
	public void setSignpost(Graph<N, E>.Edge s){
		signpost=s;
	}
	
	/** Accessor for Signpost */
	public Graph<N, E>.Edge getSignpost(){
		return(signpost);
	}
	
	/** Compares DistanceData by their cost */
	@Override
	public int compareTo(DistanceData<N,E> other) {
		int to_return = 0;
		double d;
		d=(getCost()-other.getCost());
		if (d>0){
			to_return = (int) Math.ceil(d);
		}else if (d<0){
			to_return = (int) Math.floor(d);
		} 
		return to_return;
	}
}
