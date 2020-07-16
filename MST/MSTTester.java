package app;

import java.io.IOException;
import java.util.ArrayList;

import structures.Arc;
import structures.Graph;
import structures.Vertex;

public class MSTTester {
	public static void main(String[] args) throws IOException {

			// TODO Auto-generated method stub
//			Graph g = new Graph("graph1.txt");
//			PartialTreeList ptl = PartialTreeList.initialize(g);
//			ArrayList<Arc> mst = PartialTreeList.execute(ptl);
//			System.out.println(mst);
		Graph graph = null;
        try {
            graph = new Graph("graph2.txt");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //
        PartialTreeList partialTreeList = PartialTreeList.initialize(graph);
        //
        ArrayList<Arc> arcArrayList = PartialTreeList.execute(partialTreeList);
        //
        for (int i = 0; i < arcArrayList.size(); i++) {
            Arc anArcArrayList = arcArrayList.get(i);
            System.out.println(anArcArrayList);
        }
	}
}

