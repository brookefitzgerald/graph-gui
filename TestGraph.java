import java.awt.Point;
import java.util.*;
import java.awt.Point;

public class TestGraph {

	public static void main(String[] args) {
		Graph<String,Double> graph = new Graph<String,Double>();
		//// Building a Graph
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addNode("D");

		graph.addEdge(3.0, graph.getNode(0), graph.getNode(1));
		graph.addEdge(4.0, graph.getNode(1), graph.getNode(3));
		graph.addEdge(2.0, graph.getNode(0), graph.getNode(2));
		
		System.out.println(((Graph<String,Double>.Node) graph.getNode(0)));
		System.out.println(((Graph<String,Double>.Node) graph.getNode(1)));
		//// Node Testing
		System.out.println("Node Testing");
		Graph<String,Double>.Node node = graph.getNode(0);
		node.setData("AA");
		System.out.println(node.getData());                   // AA
		node.setData("A");
		for (Graph<String,Double>.Node n:node.getNeighbors()){
			System.out.print(n.getData()+" ");                // B C
		}
		System.out.println();
		Graph<String,Double>.Node neighbor = graph.getNode(1);
		System.out.println(node.edgeTo(neighbor).getData());            // 3.0
		System.out.println(node.isNeighbor(neighbor));        // true
		for (Graph<String,Double>.Edge e:node.getEdges()){
			System.out.print(e.getData()+" ");				  // 3.0 2.0
		}	
		System.out.println();
		System.out.println();
		
		//// Edge Testing
		System.out.println("Edge Testing");
		Graph<String,Double>.Edge edge = graph.getEdge(0);
		System.out.println(edge.getOpposite(node).getData()); // B
		System.out.println(edge.getHead().getData());         //A
		System.out.println(edge.getTail().getData());         //B
		edge.setData(0.2);
		System.out.println(edge.getData());                   //.2
		graph.addEdge(5.0, graph.getNode(0), graph.getNode(1));
		System.out.println(edge.equals(graph.getEdge(0)));    //true
		System.out.println();
		
		//// Graph Testing
		System.out.println("Graph Testing");
		graph.print(); 
		graph.check(); // won't return anything, throws error if graph is inconsistent
		
		graph.BFT(graph.getNode(0));  // A B C D
		graph.DFT(graph.getNode(0));  // A B D C

		HashSet<Graph<String,Double>.Edge> edges = new HashSet<Graph<String,Double>.Edge>();
		edges.add(graph.getEdge(0));
		edges.add(graph.getEdge(1));
		HashSet<Graph<String,Double>.Node> endpoints = graph.endpoints(edges);
		for (Graph<String,Double>.Node endpoint: endpoints){
			System.out.print(endpoint.getData()+" ");          // A B D (order may be different)
		}
		System.out.println("");

		HashSet<Graph<String,Double>.Node> other = graph.otherNodes(endpoints);
		for (Graph<String,Double>.Node o: other){
			System.out.print(o.getData()+" ");                 // C
		}
		System.out.println("");
		
		
		
		System.out.println("Edges: "+graph.numEdges()+", Nodes: "+graph.numNodes());  // 4,4
		System.out.println();
		graph.removeEdge(graph.getEdge(0));
		graph.removeEdge(graph.getNode(1),graph.getNode(2));
		graph.print();
		System.out.println("Edges: "+graph.numEdges()+", Nodes: "+graph.numNodes());  // 4,2
		
		graph.removeNode(graph.getNode(1)); //removes node and all of its edges
		graph.print();
		System.out.println("Edges: "+graph.numEdges()+", Nodes: "+graph.numNodes());  // 1,3
		graph.check();
		
		System.out.println(graph.getEdgeRef(graph.getNode(0),graph.getNode(1)).getData());  // 2.0
		
	}

}
