import java.util.*;
/**
 *  Implements a graph data structure with nested classes Node and Edge.
 *
 *  @author  Brooke Fitzgerald
 *  @version CSC 212, 7 December 2016
 */
public class Graph<N,E> {
	/** nodes of the graph */
	private ArrayList<Node> nodes;
	/** edges of the graph */
	private ArrayList<Edge> edges;
	
	/** constructor for a graph*/
	public Graph(){
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}
	
	public ArrayList<Node> getNodes(){
		return(nodes);
	}
	public ArrayList<Edge> getEdges(){
		return(edges);
	}
	
	/** 
	 * Method to add an edge
	 * @param data data to associate with edge, param head head node of edge, tail tail node of edge 
	 */
	public void addEdge(E data, Node head,Node tail){
		if (head!=tail){
			Edge edge = new Edge(data,head,tail);
			edges.add(edge);
			head.addEdge(edge);
			tail.addEdge(edge);
		}
	}
	
	/**
	 * Method to add a node
	 * @param data data to associate with node.
	 * */
	public void addNode(N data){
		nodes.add(new Node(data));
	}
	
	/**
	 * Method to add a node
	 * @param node node to add to graph.
	 * */
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	/**
	 * Method to get an edge
	 * @param index index of edge in graph master list.
	 * */
	public Edge getEdge(int index){
		return(edges.get(index));
	}
	
	/**
	 * Method to get an edge by its head and tail
	 * @param head head node of edge, tail tail node of edge.
	 * */
	public Edge getEdgeRef(Node head,Node tail){
		Edge edge =null;
		boolean found = false;
		ListIterator<Edge> iter = edges.listIterator();
		while (iter.hasNext()&&!found){
			edge = iter.next();
			if ((edge.getHead()==head)&&(edge.getTail()==tail)){
				found=true;
			}else if ((edge.getHead()==tail)&&(edge.getTail()==head)){
				found = true;
			}
		}
		return(edge);
	}
	
	/**
	 * Method to get a node
	 * @param index index of node in node list of graph.
	 * */
	public Node getNode(int index){
		return(nodes.get(index));
	}
	
	/**
	 * Method to remove an edge from a graph
	 * @param edge edge to be removed.
	 * */
	public void removeEdge(Edge edge){
		edge.getHead().removeEdge(edge);
		edge.getTail().removeEdge(edge);
		edges.remove(edge);
	}

	/**
	 * Method to remove an edge
	 * @param head head node of edge, tail tail node of edge.
	 * */
	public void removeEdge(Node head,Node tail){
		boolean flag = false;
		Edge edge;
		ListIterator<Edge> iter = edges.listIterator();
		while (iter.hasNext()&&!flag){
			edge = iter.next();
			if (edge.getHead().equals(head)&&edge.getTail().equals(tail)){
				head.removeEdge(edge);
				tail.removeEdge(edge);
				iter.remove();
				flag = true;
			}
		}
	}
	
	/**
	 * Method to remove an edge
	 * @param node node to be removed
	 * */
	public void removeNode(Node node){
		if (node.getEdges().size()!=0){
			for (Edge edge: node.getEdges()){
				edge.getOpposite(node).removeEdge(edge);
				edges.remove(edge);
			}
		}
		nodes.remove(node);
	}
	
	/** Get the number of edges*/
	public int numEdges(){
		return(edges.size());
	}
	/** Get the number of nodes*/
	public int numNodes(){
		return(nodes.size());
	}
	
	/** Method to check a graph for consistency, throws errors if inconsistent. */
	public void check(){
		// checks the consistency of the graph
		for (Node node:nodes){
			if (node.getEdges()!=null){
				for (Edge edge:node.getEdges()){
					// make sure the edges of every node are in the edges list
					if (!edges.contains(edge)){
						System.err.println("Node "+node.getData()+" has an edge that is missing from the edge list.");
					}
					// every edge listed links to that node as its head or tail
					if (!edge.getHead().equals(node)&&!edge.getTail().equals(node)){
						System.err.println("Node "+node.getData()+" has an edge that doesn't link back to the node.");
					}
				}
			}
		}
		for (Edge edge:edges){
			// make sure the head and tail of every edge is in the nodes list
			if (!(nodes.contains(edge.getHead())&&nodes.contains(edge.getTail()))){
				System.err.println("Edge "+edge.getData()+" has a head or tail that is missing from the node list.");
			}
			// head and tail of every edge link back to that edge
			if (!edge.getHead().getEdges().contains(edge)||!edge.getTail().getEdges().contains(edge)){
				System.err.println("Edge "+edge.getData()+" has a head or tail that doesn't link back to the edge.");
			}
		}
	}
	
	/** Method to do depth first traversal of a graph */
	public ArrayList<TraversalData<N,E>> DFT(Node start){
		HashSet<Node> seen = new HashSet<Node>(nodes.size());
		ArrayList<TraversalData<N,E>> traversal = new ArrayList<TraversalData<N,E>>(numNodes());
		DFT_recurse(null,start,seen,traversal);
		return(traversal);
	}
	/** Recursive method for depth first traversal */
	private void DFT_recurse(Node head,Node tail,HashSet<Node> seen,ArrayList<TraversalData<N,E>> traversal){
		if (!seen.contains(tail)){
			traversal.add(new TraversalData<N,E>(tail,getEdgeRef(head,tail)));
			seen.add(tail);
			for (Node neighbor: tail.getNeighbors()){
				DFT_recurse(tail,neighbor,seen,traversal);
			}
		}
	}
	
	/** Method to do breadth first traversal of a graph */
	public ArrayList<TraversalData<N,E>> BFT(Node start){
		Node node;
		Edge edge;
		TraversalData<N,E> elt;
		HashSet<Node> seen = new HashSet<Node>(nodes.size());
		ArrayList<TraversalData<N,E>> traversal = new ArrayList<TraversalData<N,E>>(numNodes());
		ArrayList<TraversalData<N,E>> queue = new ArrayList<TraversalData<N,E>>(numNodes());
		queue.add(new TraversalData<N,E>(start,null));
		seen.add(start);
		while (!queue.isEmpty()){
			elt = queue.remove(0);
			node=elt.getNode();
			edge=elt.getEdge();
			traversal.add(new TraversalData<N,E>(node,edge));
			for (Node neighbor:node.getNeighbors()){
				 if (!seen.contains(neighbor)){
					 queue.add(new TraversalData<N,E>(neighbor,getEdgeRef(neighbor,node)));
					 seen.add(neighbor);
				 }
			}
		}
		return(traversal);
	}

	/** Method to print a representation of the graph */
	public void print(){
		System.out.println();
		System.out.println("Nodes of the graph:");
		for (Node n:nodes){
			System.out.println(n.getData());
		}
		System.out.println("Edges of the graph:");
		for (Edge e:edges){
			System.out.println(e.getData()+": "+e.getHead().getData()+"-"+e.getTail().getData());
		}
		System.out.println();

	}
	
	
	
	/**
	 *  Method to get the endpoints from a collection of edges.
	 *  @param edges HashSet of edges to get endpoints from.
	 *  */
	public HashSet<Node> endpoints(HashSet<Edge> edges){
		HashSet<Node> to_return= new HashSet<Node>(); 
		for (Edge e:edges){
			to_return.add(e.getHead());
			to_return.add(e.getTail());
		}
		return(to_return);
	}
	
	/**
	 *  Method to get the nodes in a  graph that aren't in a particular group of nodes.
	 *  @param group HashSet of edges to test for belonging.
	 *  */
	public HashSet<Node> otherNodes(HashSet<Node> group){
		HashSet<Node> to_return= new HashSet<Node>();
		for (Node n:nodes){
			if (!group.contains(n)){
				to_return.add(n);
			}
		}
		return(to_return);
	}
	/** 
	 * Class that implements nodes of a graph.
	 * */
	public class Node {
		
		/** data of the node */
		private N data;
		
		/** edges of the node */
		private ArrayList<Edge> edges;
		
		/** 
		 * Constructor for a node 
		 * @param d data of node
		 * */
		Node(N d){
			data = d;
			edges = new ArrayList<Edge>();
		}

		/** 
		 * Constructor for a node
		 * @param d data of node, e edge of node.
		 *  */
		Node(N d, Edge e){
			data = d;
			edges = new ArrayList<Edge>();
			edges.add(e);
		}

		/** Constructor for a node
		 * @param d data of a node, e edges of a node */
		Node(N d, ArrayList<Edge> e){
			data = d;
			edges = e;
		}

		/** Accessor for data */
		public N getData(){
			return(this.data);
		}

		/** 
		 * Manipulator for data 
		 * @param d data of node
		 * */
		public void setData(N d){
			this.data = d;
		}

		/** Accessor for edges */	
		public ArrayList<Edge> getEdges(){
			return(edges);
		}

		/** 
		 * Method to add a single edge to a node
		 * @param e edge to add
		 *  */
		private void addEdge(Edge e){
			edges.add(e);
		}

		/** 
		 * Method to return the edge to a specified node, or null if there is none
		 * @param neighbor node to check for edge between
		 *  */
		public Edge edgeTo(Node neighbor){
			Edge to_return = null;
			boolean found = false;
			ListIterator<Edge> iter = edges.listIterator();
			while (iter.hasNext()&&!found){
				to_return = iter.next();
				if (to_return.getHead().equals(neighbor)||to_return.getTail().equals(neighbor)){
					found = true;
				} else {
					to_return = null;
				}
			}
			return(to_return);
		}

		/** 
		 * Method to return true if nodes are connected
		 * @param neighbor node to test for direct proximity
		 *  */
		public boolean isNeighbor(Node neighbor){
			boolean to_return = false;
			ListIterator<Edge> iter = edges.listIterator();
			while (iter.hasNext()&&!to_return){
				if ((iter.next()).getHead().equals(neighbor)||(iter.previous()).getTail().equals(neighbor)){
					to_return = true;
				}
				iter.next();
			}
			return(to_return);
		}

		/** Method to return a list of neighbors of a node */
		public ArrayList<Node> getNeighbors(){
			ArrayList<Node> to_return = new ArrayList<Node>();
			Node node;
			for (Edge e:edges){
				node = (e.getHead()==this)? e.getTail():e.getHead();
				to_return.add(node);
			}
			return(to_return);
		}

		/** Meth0d to remove an edge from a node */
		private void removeEdge(Edge edge){
			edges.remove(edge);
		}
		
	}
		
	
	/** 
	 * Class that implements the edges of a graph
	 * */
	public class Edge {

		/** Head node of an edge */
		private Node head;

		/** Tail node of an edge */
		private Node tail;

		/** Data contained in edge */
		private E data;

		/** 
		 * Constructor for an edge
		 * @param e data of edge, h head node of edge, t tail node of edge
		 *  */
		Edge(E d, Node h,Node t){
			data = d;
			head = h;
			tail = t;
		}

		/** Accessor for data */
		public E getData(){
			return(this.data);
		}

		/** Manipulator for data */
		public void setData(E d){
			this.data = d;
		}

		/** Accessor for head of edge. */
		public Node getHead(){
			return(this.head);
		}

		/** Accessor for tail of edge */
		public Node getTail(){
			return(this.tail);
		}

		/** 
		 * Method to get the node on the opposite side of an edge
		 * @param node Node to get the opposite of
		 * */
		public Node getOpposite(Node node){
			Node to_return = null;
			if (head==node){
				to_return = tail;
			} else if (tail==node){
				to_return = head;
			} else {
				System.err.println("Tried to get opposite of a node that doesn't exist.");
			}
			return(to_return);
		}

		/** 
		 * Method that makes sure that two edges are equal if they have the same endpoints.
		 * @param o object to test for equality.
		 *  */
		@Override
		public boolean equals(Object o){
			boolean to_return = super.equals(o);
			if (o.getClass()==this.getClass()){
				@SuppressWarnings("unchecked")
				Edge e = (Edge) o;
				if ((e.getTail()==this.getTail())&&(e.getHead()==this.getHead())){
					to_return = true;
				} else if ((e.getHead()==this.getTail())&&(e.getTail()==this.getHead())){
					to_return = true;
				} else {
					to_return = false;
				}
			}
			return(to_return);
		}

		/** Method that overrides the hashing of an edge, only uses endpoints to hash. */
	    @Override
	    public int hashCode() {
	        return Objects.hash(head, tail);
	    }
		
	}
}


