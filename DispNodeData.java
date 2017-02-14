import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
/**
*  Data class containing information about how to render nodes of a graph
* 
*  @author Brooke Fitzgerald
*  @version CSC 212, December 10, 2016 */
public class DispNodeData {
	/** Coordinate of the node */
	private Point coord;
	/** Name/label of the node */
	private String name;
	/** What color the node should be */
	private Color color;
	/** diameter of the node */
	private int diameter;
	/** whether or not to display the distance */
	private boolean displayDist;
	/** distance from djistras algorithm. */
	private Double distance;

	/** 
	 *  Constructor for DispNodeData
	 *  @param coord point of node, data string label of node
	 *  */
	public DispNodeData(Point coord, String data){
		this.coord=coord;
		this.name = data;
		color = Color.black;
		diameter = 10;
		displayDist =false;

	}
	/** 	 
	 *  Constructor for DispNodeData
	 *  @param p point of node, d string label of node, c color of node, i diameter of node
	 *   */
	public DispNodeData(Point p, String d, Color c, int i){
		coord = p;
		name = d;
		color = c;
		diameter = i;
		displayDist =false;

	}

	/** Accessor for coordinate */
	public Point getCoord(){
		return(coord);
	}
	
	/** Manipulator for coordinate */
	public void setCoord(Point p) {
		coord=p;
	}
	
	/** Accessor for name */
	public String getName(){
		return(name);
	}
	
	/** Manipulator for name */
	public void setName(String d){
		name=d;
	}
	
	/** Accessor for color  */
	public Color getColor(){
		return(color);
	}
	
	/** Manipulator for color */
	public void setColor(Color c){
		color = c;
	}
	
	/** Accessor for diameter */
	public int getDiameter(){
		return(diameter);
	}
	
	/** Accessor for display data */
	public boolean getDisplayData(){
		return(displayDist);
	}
	
	/** Manipulator for display data */
	public void setDisplayDist(Boolean d){
		displayDist = d;
	}

	/** Accessor for distance */
	public double getDistance(){
		return(distance);
	}
	
	/** Manipulator for distance */
	public void setDistance(double d){
		distance = d;
	}
}