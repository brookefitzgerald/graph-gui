import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 *  Implements a GUI for creating and exploring a graph.
 *
 *  @author  Brooke Fitzgerald
 *  @version CSC 212, December 10, 2016  */
public class GraphGui {
	/** The graph to be displayed */
	private GraphCanvas canvas;

	/** Label for the input mode instructions */
	private JLabel instr;

	/** The input mode */
	private InputMode mode = InputMode.ADD_NODES;

	/** Remembers point where first mousedown event in drag sequence occurs */
	private Point origPoint;

	/** Remembers Node where first mousedown event in drag sequence occurs */
	private Graph<DispNodeData, DispEdgeData>.Node nodeUnderMouse;

	/** Node that Dikijstra's algorithm starts with */
	private Graph<DispNodeData, DispEdgeData>.Node start;

	/** Remembers label to attach to node. */
	private String label;

	/** Label Textbox*/
	private JTextField textbox;

	/** Node to assign label to */
	private Graph<DispNodeData, DispEdgeData>.Node nodeToLabel;

	/**
	 *  Schedules a job for the event-dispatching thread
	 *  creating and showing this application's GUI.
	 */
	public static void main(String[] args) {
		final GraphGui GUI = new GraphGui();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI.createAndShowGUI();
			}
		});
	}

	/** Sets up the GUI window */
	public void createAndShowGUI() {
		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("Point GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add components
		createComponents(frame);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/** Puts content in the GUI window */
	public void createComponents(JFrame frame) {
		// graph display
		Container pane = frame.getContentPane();
		pane.setLayout(new FlowLayout());
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		canvas = new GraphCanvas();
		PointMouseListener pml = new PointMouseListener();
		canvas.addMouseListener(pml);
		canvas.addMouseMotionListener(pml);
		panel1.add(canvas);
		instr = new JLabel("Click to add new nodes; drag to other nodes to add edges.");
		panel1.add(instr,BorderLayout.NORTH);
		pane.add(panel1);

		// controls
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(9,1));
		JButton addPointButton = new JButton("Add Nodes/Edges");
		panel2.add(addPointButton);
		addPointButton.addActionListener(new AddPointListener());

		JButton rmvPointButton = new JButton("Remove Nodes/Edges");
		panel2.add(rmvPointButton);
		rmvPointButton.addActionListener(new RmvPointListener());

		JButton moveNodeButton = new JButton("Move Nodes");
		panel2.add(moveNodeButton);
		moveNodeButton.addActionListener(new MoveNodeListener());

		JButton printGraphButton = new JButton("Print Graph");
		panel2.add(printGraphButton);
		printGraphButton.addActionListener(new PrintGraphListener());

		JButton bftButton = new JButton("Breadth First Traversal");
		panel2.add(bftButton);
		bftButton.addActionListener(new BFTListener());

		JButton dftButton = new JButton("Depth First Traversal");
		panel2.add(dftButton);
		dftButton.addActionListener(new DFTListener());

		JButton shortPathButton = new JButton("Find Shortest Path");
		panel2.add(shortPathButton);
		shortPathButton.addActionListener(new ShortPathListener());

		//new button add labels, text box appears, must select node, type in label and press enter.
		JButton addLabelButton = new JButton("Change Node Labels");
		panel2.add(addLabelButton);
		addLabelButton.addActionListener(new LabelButtonListener());

		JTextField addLabel = new JTextField(20);
		textbox = addLabel;
		panel2.add(addLabel);
		addLabel.addActionListener(new LabelTextListener());
		textbox.setVisible(false);
		//new button, file chooser

		pane.add(panel2);
	}

	/** 
	 * Returns a point found within the drawing radius of the given location, 
	 * or null if none
	 *
	 *  @param x  the x coordinate of the location
	 *  @param y  the y coordinate of the location
	 *  @return  a point from the canvas if there is one covering this location, 
	 *  or a null reference if not
	 */
	public Graph<DispNodeData, DispEdgeData>.Node findNearbyNode(int x, int y) {
		// Loop over all points in the canvas and see if any of them
		// overlap with the location specified.  You may wish to use
		// the .distance() method of class Point.
		boolean found = false;
		Graph<DispNodeData, DispEdgeData>.Node p=null;
		ListIterator<Graph<DispNodeData, DispEdgeData>.Node> iter = (canvas.getGraph().getNodes()).listIterator();
		while (iter.hasNext()&&!found){
			p = iter.next();
			if (p.getData().getCoord().distance(x, y)<15){
				found = true;
			} else {
				p=null;
			}
		}
		return(p);
	}

	/** Constants for recording the input mode */
	enum InputMode {
		ADD_NODES, RMV_NODES, MOVE_NODES, BFT, DFT, SHORT, ADD_LABEL
	}

	/** Listener for AddPoint button */
	private class AddPointListener implements ActionListener {
		/** Event handler for AddPoint button */
		public void actionPerformed(ActionEvent e) {
			textbox.setVisible(false);
			nodeToLabel = null;
			canvas.resetGraphColors();
			mode = InputMode.ADD_NODES;
			instr.setText("Click to add new nodes; drag to other node to add edge between nodes.");
		}
	}

	/** Listener for RmvPoint button */
	private class RmvPointListener implements ActionListener {
		/** Event handler for RmvPoint button */
		public void actionPerformed(ActionEvent e) {
			textbox.setVisible(false);
			nodeToLabel = null;
			canvas.resetGraphColors();
			mode= InputMode.RMV_NODES;
			instr.setText("Click to remove nodes; drag node to node with edge to remove edge between them");
		}
	}

	/** Listener for AddPoint button */
	private class MoveNodeListener implements ActionListener {
		/** Event handler for AddPoint button */
		public void actionPerformed(ActionEvent e) {
			textbox.setVisible(false);
			nodeToLabel = null;
			canvas.resetGraphColors();
			mode = InputMode.MOVE_NODES;
			instr.setText("Click and drag nodes to move.");
		}
	}
	/** Listener for PrintGraph button */
	private class PrintGraphListener implements ActionListener {
		/** Event handler for PrintGraph button */
		public void actionPerformed(ActionEvent e) {
			canvas.resetGraphColors();
			canvas.printGraph();
		}
	}

	/** Listener for breadth first traversal button */
	private class BFTListener implements ActionListener {
		/** Event handler for bft button */
		public void actionPerformed(ActionEvent e) {
			textbox.setVisible(false);
			nodeToLabel = null;
			canvas.resetGraphColors();
			mode = InputMode.BFT;
			instr.setText("Click starting node for breadth first traversal.");
		}
	}
	/** Listener for depth first traversal button */
	private class DFTListener implements ActionListener {
		/** Event handler for dft button */
		public void actionPerformed(ActionEvent e) {
			textbox.setVisible(false);
			nodeToLabel = null;
			canvas.resetGraphColors();
			mode = InputMode.DFT;
			instr.setText("Click starting node for depth first traversal.");
		}
	}

	/** Listener for shortest path button */
	private class ShortPathListener implements ActionListener {
		/** Event handler for ShortPath button */
		public void actionPerformed(ActionEvent e) {
			textbox.setVisible(false);
			nodeToLabel = null;
			canvas.resetGraphColors();
			mode = InputMode.SHORT;
			instr.setText("Click starting node to find the shortest path to all other nodes.");
		}
	}

	/** Listener for label text field */
	private class LabelTextListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			label = textbox.getText(); 
			if (nodeToLabel!=null){
				nodeToLabel.getData().setName(label);
				nodeToLabel.getData().setColor(Color.black);
			} else {
				System.out.println("Select a node first");
			}
			canvas.repaint();
			textbox.setText("");
			textbox.setEditable(false);
			nodeToLabel = null;
		}
	}

	/** Listener for add label text button */
	private class LabelButtonListener implements ActionListener {
		/** Event handler for label text button */
		public void actionPerformed(ActionEvent e) {
			canvas.resetGraphColors();
			mode = InputMode.ADD_LABEL;
			textbox.setVisible(true);
			textbox.setEditable(false);
			instr.setText("Click desired node, type desired label for node in text box, then press enter.");
		}
	}
	/** Mouse listener for PointCanvas element */
	private class PointMouseListener extends MouseAdapter
	implements MouseMotionListener {

		/** Responds to click event depending on mode */
		public void mouseClicked(MouseEvent e) {
			Graph<DispNodeData, DispEdgeData>.Node p = findNearbyNode(e.getX(),e.getY());
			switch (mode) {
			case ADD_NODES:
				// If the click is not on top of an existing point, create a new one and add it to the canvas.
				// Otherwise, emit a beep, as shown below:
				if (p==null){
					canvas.getGraph().addNode(new DispNodeData(e.getPoint(),""+(canvas.getGraph().numNodes()+1)));
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			case RMV_NODES:
				// If the click is on top of an existing point, remove it from the canvas's list of points.
				// Otherwise, emit a beep.
				if (p!=null){
					canvas.getGraph().removeNode(p);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			case BFT:
				if (p!=null){
					canvas.resetGraphColors();
					canvas.paintBFT(p);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			case DFT:
				if (p!=null){
					canvas.resetGraphColors();
					canvas.paintDFT(p);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			case SHORT:
				if (p!=null){
					canvas.resetGraphColors();
					canvas.paintShortestDist(p);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			case ADD_LABEL:
				if (p!=null){
					nodeToLabel = p;
					textbox.setEditable(true);
					p.getData().setColor(Color.blue);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			}
			canvas.repaint();
		}

		/** Records point under mousedown event in anticipation of possible drag */
		public void mousePressed(MouseEvent e) {
			// FILL IN:  Record point under mouse, if any
			if (mode.equals(InputMode.ADD_NODES)||mode.equals(InputMode.RMV_NODES)||mode.equals(InputMode.MOVE_NODES)){
				Graph<DispNodeData, DispEdgeData>.Node node = findNearbyNode(e.getX(),e.getY());
				if (node!=null){
					origPoint = new Point(node.getData().getCoord());
					nodeUnderMouse  = node;
				}
			}
		}

		/** Responds to mouseup event */
		public void mouseReleased(MouseEvent e) {
			if ((mode.equals(InputMode.ADD_NODES)||mode.equals(InputMode.RMV_NODES)||mode.equals(InputMode.MOVE_NODES)) && 
					nodeUnderMouse!=null && 
					nodeUnderMouse.getData().getCoord()!=origPoint){
				if (!mode.equals(InputMode.MOVE_NODES)){
					nodeUnderMouse.getData().setCoord(origPoint);
					Graph<DispNodeData, DispEdgeData>.Node node = findNearbyNode(e.getX(),e.getY());
					if (node!=null){
						if (mode.equals(InputMode.ADD_NODES)){
							DispEdgeData data = new DispEdgeData(origPoint.distance(node.getData().getCoord()));
							canvas.getGraph().addEdge(data, nodeUnderMouse, node);
						} else{
							canvas.getGraph().removeEdge(nodeUnderMouse, node);
						}
					}
				} else {
					canvas.recalculateEdgeLengths();
				}
			}
			nodeUnderMouse = null;
			origPoint = null;
			canvas.repaint();
		}

		/** Responds to mouse drag event */
		public void mouseDragged(MouseEvent e) {
			// If mode allows point motion, and there is a point under the mouse, 
			// then change its coordinates to the current mouse coordinates & update display
			if ((mode.equals(InputMode.ADD_NODES)||mode.equals(InputMode.RMV_NODES)||mode.equals(InputMode.MOVE_NODES))&&nodeUnderMouse!=null){
				nodeUnderMouse.getData().setCoord(e.getPoint());
				canvas.repaint();
			}
		}

		// Empty but necessary to comply with MouseMotionListener interface.
		public void mouseMoved(MouseEvent e) {}
	}
}