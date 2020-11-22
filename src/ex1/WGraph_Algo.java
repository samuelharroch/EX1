package ex1;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms  {

    private weighted_graph myGraph;

    public WGraph_Algo() {
        myGraph= new WGraph_DS();
    }

    @Override
    public void init(weighted_graph g) {
        myGraph=g;
    }

    @Override
    public weighted_graph getGraph() {
        return myGraph;
    }

    @Override
    public weighted_graph copy() {
        return new WGraph_DS(myGraph);
    }

    // BFS algorithm
    private node_info[] MyPath(int scr){
        // using the tag parameters of each node as index
        int tag=0;
        for (node_info n: myGraph.getV()) {
            n.setTag(tag++);

        }
        // array of boolean representing the nodes we visit using the tag
        boolean [] visited= new boolean[myGraph.nodeSize()];
        visited[(int)(myGraph.getNode(scr).getTag())]=true; // we visit in scr node

        // array of nodes using the index as the tag of visited node and
        // prev[i]= The node we pass through to visit the node associate with the tag using the index
        node_info [] prev =new node_info[myGraph.nodeSize()];

        // queue of visited nodes
        Queue<node_info> q = new LinkedList<node_info>();
        q.add(myGraph.getNode(scr)); //push the scr node

        while (!q.isEmpty()){
            node_info node= q.poll(); // poll the first node in the queue, we don't want to visit him, again
            Collection<node_info> neighbours = myGraph.getV(node.getKey()); // the neighbors of the polled node
            for (node_info n:neighbours) {
                if (!visited[(int) n.getTag()]) // if we didn't visit this neighbor
                {
                    q.add(n);
                    visited[(int) n.getTag()]=true;
                    prev[(int) n.getTag()]=node; //keep the current node as the "source" of the neighbor
                }
            }
        }
        return prev;
    }

    @Override
    // still using BFS algorithm because he is faster than dijkstra
    public boolean isConnected() {
        if (myGraph.nodeSize()==0) return true; // assume that the empty graph is connected
        //create the visited nodes array using the first node in the graph collection of nodes
        node_info [] prev = MyPath(myGraph.getV().iterator().next().getKey());

        for (int i=1; i< prev.length;i++) // start from 1 cause the tag scr index (0 in this case)is always null
        {
            if (prev[i]== null) return false; // if one node is unvisited
        }
        return true;
    }
    private node_info[] dijkstra(int src){
        int index;
        double minValue, newDist;
        Pair curr;

        // using the tag parameters of each node
        int tag=0;
        for (node_info n: myGraph.getV()) {
            n.setTag(tag++);

        }
        // array of boolean representing the nodes we visit using the tag/index
        boolean [] visited = new boolean[myGraph.nodeSize()];
        // array of nodes using the index as the tag of visited node and
        // prev[i]= The node we pass through to visit the node associate with the tag using the index
        node_info[] prev= new node_info[myGraph.nodeSize()];
        // array of distance between the src node to the other node connected to him (not directly)
        double[] dist = new double[myGraph.nodeSize()];
        Arrays.fill(dist, Double.MAX_VALUE); // fill the array to infinity
        dist[(int) myGraph.getNode(src).getTag()]=0;

        // PriorityQueue using pair of (node , and the distance from the src to him)
        PriorityQueue < Pair > pq = new PriorityQueue();
        pq.add(new Pair(myGraph.getNode(src),0));

        while (pq.size()!=0){
            curr= pq.poll();// poll the first node in the queue, we don't want to visit him, again
            index= (int) curr.n.getTag(); // keep the index/tag of the polled node
            minValue= curr.weight; // keep  the distance from the src to the polled node
            visited[index]=true;
            if(dist[index]< minValue) continue; // if the current distance is better than the proposing one
            for (node_info nei: myGraph.getV(curr.n.getKey())) //loop over the neighbors of the polled node
            {
                if(visited[(int)nei.getTag()]) continue;
                newDist= dist[index] + myGraph.getEdge(curr.n.getKey(), nei.getKey());
                if(newDist< dist[(int) nei.getTag()]) //if the new dist from the src to the polled node is better - update
                {
                    prev[(int) nei.getTag()]= curr.n ;
                    dist[(int) nei.getTag()]= newDist;
                    pq.add(new Pair(nei,newDist));
                }
            }
        }
        return prev; // returning the path
    }
    @Override
    public double shortestPathDist(int src, int dest) {
        // if one node doesn't exist
        if (myGraph.getNode(src)==null || myGraph.getNode(dest)==null) return -1;
        // path node to himself
        if (src==dest)return 0;

        node_info[] path = dijkstra(src);
        if(path[(int)myGraph.getNode(dest).getTag()]== null) return -1;

        Stack<node_info> s= new Stack<>(); // to reconstruct the path
        s.push(myGraph.getNode(dest)); // push the dest node

        // such of backtracking from the dest to the scr
        for (int i = (int) myGraph.getNode(dest).getTag(); path[i]!=null; i= (int) path[i].getTag())
            s.push(path[i]);

        double dist = 0;
        while (s.size()>1){
            dist+= myGraph.getEdge(s.pop().getKey(),s.peek().getKey());
        }
        return dist;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        // if one node doesn't exist
        if (myGraph.getNode(src)==null || myGraph.getNode(dest)==null) return null;

        node_info[] path = dijkstra(src);
        if(path[(int)myGraph.getNode(dest).getTag()]== null) return null;

        Stack<node_info> s= new Stack<>(); // to reconstruct the path
        s.push(myGraph.getNode(dest)); // push the dest node

        // such of backtracking from the dest to the scr
        for (int i = (int) myGraph.getNode(dest).getTag(); path[i]!=null; i= (int) path[i].getTag())
            s.push(path[i]);
        // reverse the path
        List<node_info> l= new ArrayList<node_info>();
        while (!s.isEmpty())
            l.add(s.pop());

        return l;
    }

    @Override
    public boolean save(String file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(this.myGraph);

            fileOutputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            weighted_graph deserializedGraph = (weighted_graph) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();
            init(deserializedGraph);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // inner class of pair (node , and the distance from the src to him) for the priority queue in dijkstra algorithm
    static class Pair implements Comparable {

        node_info n;
        double weight;
        public Pair(node_info n, double weight) {
            this.n = n;
            this.weight = weight;
        }

        @Override
        public int compareTo(Object o) {
            Pair pr = (Pair)o;

            if(weight > pr.weight)
                return 1;
            else
                return -1;

        }

    }

}
