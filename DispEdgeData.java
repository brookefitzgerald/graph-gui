import java.awt.Color;

/** 
 *  Data class containing information about how to render edges of a graph
 * 
 *  @author Brooke Fitzgerald
 *  @version CSC 212, December 10, 2016 */
public class DispEdgeData {
	/** Color to color the edge */
	private Color color;
	/** weight (length) of the edge */
	private Double weight;

	/** Constructor for DispEdgeData
	 *  @param d weight of edge
	 *  */
	public DispEdgeData(Double d){
		weight = d;
		color = Color.black;
	}
	
	/** Constructor for DispEdgeData
	 *  @param d weight of edge, c color of edge
	 *  */
	public DispEdgeData(Double d, Color c){
		weight = d;
		color = c;
	}	
	
	/** Accessor for weight */
	public Double getWeight(){
		return(weight);
	}
	
	/** Maninpulator for weight */
	public void setWeight(Double w){
		weight=w;
	}
	
	/** Accessor for color */
	public Color getColor(){
		return(color);
	}
	
	/** Manipulator for color */
	public void setColor(Color c){
		color = c;
	}
}
