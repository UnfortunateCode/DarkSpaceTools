
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import darkspace.Galaxy;
import darkspace.Rules_Test1;




public class ReferenceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Galaxy g = new Galaxy(new Rules_Test1(), 25, 60);
		
		g.displayGalaxy();
		
		
		Graph graph = new SingleGraph("Tutorial 1");
		graph.setStrict(false);
		graph.setAutoCreate( true );
		
		
		graph.addEdge( "AB", "A", "B" );
		graph.addEdge( "BC", "B", "C" );
		graph.addEdge( "CA", "C", "A" );
		graph.addEdge( "AC", "A", "C" );


		graph.addEdge("AD", "A", "D");

	    for (Node node : graph) {
	        node.addAttribute("ui.label", node.getId());
	    }


		graph.display();


	}

}
