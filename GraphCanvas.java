import java.awt.*;
import java.util.*;
import javax.swing.JComponent;

/** 
 *  Class to graphically represent and interact with a graph.
 * 
 *  @author Brooke Fitzgerald
 *  @version CSC 212, December 10, 2016 */
public class GraphCanvas extends JComponent{
	private Graph<DispNodeData,DispEdgeData> graph;
	
    /** Constructor */
    public GraphCanvas() {
        graph = new Graph<DispNodeData,DispEdgeData>();
    }
    /** Accessor for the graph */
    public Graph<DispNodeData,DispEdgeData> getGraph(){
    	return(graph);
    }
	
    /**
     *  Paints nodes and edges of the graph by their specified color and coordinates.
     *
     *  @param g The graphics object to draw with
     */
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	g.setColor(Color.white);
    	g.fillRect(0, 0, getWidth(), getHeight());
        for (Graph<DispNodeData,DispEdgeData>.Edge e:graph.getEdges()){
        	DispNodeData head = e.getHead().getData();
        	DispNodeData tail = e.getTail().getData();
        	g.setColor(e.getData().getColor());
        	g.drawLine(head.getCoord().x,head.getCoord().y, tail.getCoord().x,tail.getCoord().y);
        }
        for (Graph<DispNodeData,DispEdgeData>.Node n:graph.getNodes()){
        	DispNodeData data = n.getData();
        	int r = data.getDiameter()/2;
        	g.setColor(data.getColor());
        	g.fillOval(data.getCoord().x-r, data.getCoord().y-r, data.getDiameter(), data.getDiameter());
        	g.drawString(data.getName(), data.getCoord().x-r, data.getCoord().y-r);
        	if (n.getData().getDisplayData()){
        		//render cost on edge
        		double x = n.getData().getCoord().x;
        		double y = n.getData().getCoord().y;
        		g.setColor(Color.GRAY);
            	g.drawString(""+Math.round(n.getData().getDistance() * 100.0) / 100.0, (int) x-r, (int)y-r*3);

        	}
        }

    }

    /** Method to reset the colors of the nodes and edges in the graph. */
    public void resetGraphColors(){
    	for (Graph<DispNodeData, DispEdgeData>.Node n:graph.getNodes()){
    		n.getData().setColor(Color.black);
    		n.getData().setDisplayDist(false);

    	}
    	for (Graph<DispNodeData, DispEdgeData>.Edge e:graph.getEdges()){
    		e.getData().setColor(Color.black);
    	}
    	repaint();
    }
    
	/** Recalculates lengths of edges - used when nodes move.  */
	public void recalculateEdgeLengths(){
		Double distance;
		for ( Graph<DispNodeData, DispEdgeData>.Edge e: graph.getEdges()){
			distance = e.getHead().getData().getCoord().distance(e.getTail().getData().getCoord());
			e.getData().setWeight(distance);
		}
	}
    
    /** Prints a graphical representation of the graph */
    public void printGraph(){
    	System.out.println("Nodes: "+graph.numNodes()+", Edges: "+graph.numEdges());
    	for (Graph<DispNodeData, DispEdgeData>.Node n:graph.getNodes()){
			System.out.println("n "+n.getData().getName());
		}
		for (Graph<DispNodeData, DispEdgeData>.Edge e:graph.getEdges()){
			System.out.println("e "+e.getHead().getData().getName()+" "+e.getTail().getData().getName()+" "+Math.round(e.getData().getWeight() * 100.0) / 100.0);
		}
		System.out.println();
    }
    
    
    /**
     *  The component will look bad if it is sized smaller than this
     *  @returns The minimum dimension
     */
    public Dimension getMinimumSize() {
        return new Dimension(500,300);
    }

    /**
     *  The component will look best at this size
     *  @returns The preferred dimension
     */
    public Dimension getPreferredSize() {
        return new Dimension(500,300);
    }

    /** 
     * Animates each step of a traversal of a graph from a starting node
     * @param start Node to start traversal from */
    public void animateTraversal(ArrayList<TraversalData<DispNodeData, DispEdgeData>> traversal) {
		ListIterator<TraversalData<DispNodeData, DispEdgeData>> iter = traversal.listIterator();
		HashSet<Graph<DispNodeData, DispEdgeData>.Node> nodes = new HashSet<Graph<DispNodeData, DispEdgeData>.Node>();
		if (iter.hasNext()) nodes.add(iter.next().getNode()); // add starting node
		TraversalData<DispNodeData, DispEdgeData> next;
		while (iter.hasNext()){
			next = iter.next();
			next.getNode().getData().setColor(Color.green);
			next.getEdge().getData().setColor(Color.green);
			nodes.add(next.getNode());
			paintImmediately(0,0,getWidth(),getHeight());
			try {
			    Thread.sleep(250);                 
			} catch(InterruptedException ignore) {
			}
		}
		HashSet<Graph<DispNodeData, DispEdgeData>.Node> others =graph.otherNodes(nodes);
		for (Graph<DispNodeData, DispEdgeData>.Node n:others){
			n.getData().setColor(Color.red);
		}
	}
    
    /** 
     * Calculates and animates breadth first traversal of a graph from a starting node
     * @param start Node to start traversal from */
	public void paintBFT(Graph<DispNodeData, DispEdgeData>.Node start) {
		start.getData().setColor(Color.blue);
		paintImmediately(0,0,getWidth(),getHeight());
		ArrayList<TraversalData<DispNodeData, DispEdgeData>> bft = graph.BFT(start);
		animateTraversal(bft);
	}
	
    /** 
     * Calculates and animates breadth first traversal of a graph from a starting node
     * @param start Node to start traversal from */
	public void paintDFT(Graph<DispNodeData, DispEdgeData>.Node start) {
		start.getData().setColor(Color.blue);
		paintImmediately(0,0,getWidth(),getHeight());
		ArrayList<TraversalData<DispNodeData, DispEdgeData>> dft = graph.DFT(start);
		animateTraversal(dft);
	}
	
	/** 
	 * Method to calculate distances to nodes with Dijkstra's shortest-path algorithm
	 * @param start node from which to compute distances
	 *  */
	public Hashtable<Graph<DispNodeData, DispEdgeData>.Node,DistanceData<DispNodeData, DispEdgeData>> distances(Graph<DispNodeData, DispEdgeData>.Node start){
		Hashtable<Graph<DispNodeData, DispEdgeData>.Node,DistanceData<DispNodeData, DispEdgeData>> dists = new Hashtable<Graph<DispNodeData, DispEdgeData>.Node,DistanceData<DispNodeData, DispEdgeData>>(graph.numNodes());
		for (Graph<DispNodeData, DispEdgeData>.Node n:graph.getNodes()){
			// initialize all distance data
			dists.put(n, new DistanceData<DispNodeData, DispEdgeData>(n));
		}
		dists.get(start).setCost(0.0);
		HashSet<DistanceData<DispNodeData, DispEdgeData>> unvisited = new HashSet<DistanceData<DispNodeData, DispEdgeData>>(dists.values());
		// list to find minimum and remove visited nodes from
		DistanceData<DispNodeData, DispEdgeData> lowest;
		Double minCost;
		while(unvisited.size()>0){
			lowest =Collections.min(unvisited);
			unvisited.remove(lowest);
			minCost = lowest.getCost();
			lowest.markVisited();
			for (Graph<DispNodeData, DispEdgeData>.Node neighbor: lowest.getNode().getNeighbors()){
				Graph<DispNodeData, DispEdgeData>.Edge edge = lowest.getNode().edgeTo(neighbor);
				Double edgeCost = edge.getData().getWeight();
				Double currCost = dists.get(neighbor).getCost();
				if (!dists.get(neighbor).checkVisited()&&(minCost+edgeCost<currCost)){
					dists.get(neighbor).setSignpost(edge);
					dists.get(neighbor).setCost(minCost+edgeCost);
				}
			}
		}
		return(dists);
	}
	
	/** 
	 * Paints the shortest path between a start node and all other reachable nodes.
	 * @param start node to start algorithm at
	 *  */
	public void paintShortestDist(Graph<DispNodeData, DispEdgeData>.Node start){
		Hashtable<Graph<DispNodeData, DispEdgeData>.Node,DistanceData<DispNodeData, DispEdgeData>> dists = distances(start);
		for (DistanceData<DispNodeData, DispEdgeData> d: dists.values()){
			if (d.getSignpost()!=null){
				d.getNode().getData().setColor(Color.green);
				d.getSignpost().getData().setColor(Color.green);
				d.getNode().getData().setDistance(d.getCost());
				d.getNode().getData().setDisplayDist(true);
			} else if (d.getNode().equals(start)){
				d.getNode().getData().setColor(Color.blue);
				d.getNode().getData().setDisplayDist(false);
			} else {
				d.getNode().getData().setColor(Color.RED);
				d.getNode().getData().setDisplayDist(false);
			}
		}
		repaint();
	}

}
