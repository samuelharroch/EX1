package ex1;

import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WGraph_DSTest {

    @Test
    public void getEdge(){
        weighted_graph g= new WGraph_DS();
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.connect(1,2,5.0);
        g.connect(1,3,6.0);
        assertTrue(g.hasEdge(1,2));
        assertTrue(g.hasEdge(1,3));
        assertFalse(g.hasEdge(1,4));
        assertTrue(g.hasEdge(1,1)); //edge of node to himself
        assertFalse(g.hasEdge(2,3));

        assertEquals(g.getEdge(1,2),5.0);
        assertEquals(g.getEdge(1,3),6.0);
        assertEquals(g.getEdge(1,1),0.0);
        assertEquals(g.getEdge(1,4),-1);
    }

    @Test
    public void remove() {
        weighted_graph g = new WGraph_DS();
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.connect(1, 2, 5.0);
        g.connect(1, 3, 6.0);
        g.connect(5, 4, 7.0);// no existing node 5
        g.connect(1, 4, 8.0);

        assertEquals(g.edgeSize(),3);
        g.removeNode(1); // all the edges should be removed
        assertEquals(g.edgeSize(),0);// check it
        assertEquals(g.nodeSize(),3);

    }

    @Test
    public void getV() {
        weighted_graph g = new WGraph_DS();
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.connect(1, 2, 5.0);
        g.connect(1, 3, 6.0);
        g.connect(1, 4, 8.0);

        node_info[] nei1={g.getNode(2),g.getNode(3),g.getNode(4)};// the expected neighbors of node 1

        assertArrayEquals( g.getV(1).toArray(),nei1);
    }
    @Test
    public void isConnected(){
        weighted_graph g = new WGraph_DS();
        WGraph_Algo ga= new WGraph_Algo();
        ga.init(g);
        assertTrue(ga.isConnected()); // empty graph should be connected

        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.connect(1, 2, 5.0);
        g.connect(1, 3, 6.0);

        assertFalse(ga.isConnected());

        g.connect(1, 4, 8.0);
        assertTrue(ga.isConnected());
    }
    @Test
    public void copy() {
        weighted_graph g = new WGraph_DS();
        WGraph_Algo ga = new WGraph_Algo();
        ga.init(g);

        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.connect(1, 2, 5.0);
        g.connect(1, 3, 6.0);

        weighted_graph gCopy= ga.copy();
        assertEquals(g.nodeSize(),gCopy.nodeSize());
        assertEquals(g.edgeSize(),gCopy.edgeSize());

        assertEquals(ga.getGraph(),gCopy);
        g.addNode(5);
        assertNotEquals(g,gCopy);
        gCopy.addNode(5);
        assertEquals(ga.getGraph(),gCopy);
    }
    @Test
    public void shortestPath() {
        weighted_graph g = new WGraph_DS();
        WGraph_Algo ga = new WGraph_Algo();
        ga.init(g);

        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);
        g.connect(1, 2, 1.0);
        g.connect(1, 3, 2.0);
        g.connect(2,4,3);
        g.connect(3,4,4.0);
        g.connect(4,5,0);
        assertEquals(4.0, ga.shortestPathDist(1,5));
        assertEquals(0, ga.shortestPathDist(1,1));
        g.addNode(6);
        assertEquals(-1, ga.shortestPathDist(1,6));
        assertNull(ga.shortestPath(1,6));

        node_info[] path={g.getNode(1),g.getNode(2),g.getNode(4),g.getNode(5)};

        assertArrayEquals( ga.shortestPath(1,5).toArray(),path);
    }

    @Test
    public void save_load(){
        weighted_graph g = new WGraph_DS();
        WGraph_Algo ga = new WGraph_Algo();
        ga.init(g);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);
        g.connect(1, 2, 1.0);
        g.connect(1, 3, 2.0);
        g.connect(2,4,3);
        g.connect(3,4,4.0);
        g.connect(4,5,0);

        assertTrue(ga.save("mySHfile"));
        assertTrue(ga.load("mySHfile"));
        assertEquals(g,ga.getGraph());
    }
    }