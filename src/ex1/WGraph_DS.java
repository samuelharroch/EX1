package ex1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class WGraph_DS implements weighted_graph ,java.io.Serializable {
    private HashMap<Integer,node_info> myNodes;
    private int numEdges;
    private int MC;

    public WGraph_DS() {
        this.myNodes = new HashMap<Integer, node_info>();
    }
    public WGraph_DS(weighted_graph g){
        this.myNodes = new HashMap<Integer, node_info>();
        for (node_info n: g.getV()) {
            addNode(n.getKey());
        }
        for (node_info n: g.getV()) {
            for (node_info nei: g.getV(n.getKey())) {
                connect(n.getKey(), nei.getKey(), g.getEdge(n.getKey(), nei.getKey()));
            }
        }

    }

    @Override
    public node_info getNode(int key) {
        return myNodes.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (node1==node2 && myNodes.containsKey(node1)) return true;
        if (myNodes.containsKey(node1)&& myNodes.containsKey(node2)){
            return ((NodeInfo)(myNodes.get(node1))).nodeNei.containsKey(node2);
        }
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {

        if (hasEdge(node1,node2))
            if (node1==node2) return 0;
            else return ((NodeInfo)(myNodes.get(node1))).weightEdge.get(node2);
        return -1;
    }

    @Override
    public void addNode(int key) {
        if(!myNodes.containsKey(key)){
            myNodes.put(key,new NodeInfo(key));
            MC++;
        }
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if(!myNodes.containsKey(node1) ||  !myNodes.containsKey(node2) || node1==node2)
            return;
        if(hasEdge(node1,node2)){
            ((NodeInfo)(myNodes.get(node1))).weightEdge.put(node2,Math.abs(w));
            ((NodeInfo)(myNodes.get(node2))).weightEdge.put(node1,Math.abs(w));
        } else {
            ((NodeInfo)(myNodes.get(node1))).nodeNei.put(node2,myNodes.get(node2));
            ((NodeInfo)(myNodes.get(node1))).weightEdge.put(node2,Math.abs(w));
            ((NodeInfo)(myNodes.get(node2))).nodeNei.put(node1,myNodes.get(node1));
            ((NodeInfo)(myNodes.get(node2))).weightEdge.put(node1,Math.abs(w));
            numEdges++;
        }
        MC++;
    }

    @Override
    public Collection<node_info> getV() {

        return myNodes.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        if(myNodes.containsKey(node_id))
            return ((NodeInfo)(myNodes.get(node_id))).nodeNei.values();
        return null;
    }

    @Override
    public node_info removeNode(int key) {
        if (myNodes.containsKey(key)){
            getV(key).forEach(node_info -> {
                ((NodeInfo)(node_info)).nodeNei.remove(key);
                ((NodeInfo)(node_info)).weightEdge.remove(key);
                numEdges--;
            });
            MC++;
            return myNodes.remove(key);

        }
        return null;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1,node2) && node1!=node2){
            ((NodeInfo)(myNodes.get(node1))).nodeNei.remove(node2);
            ((NodeInfo)(myNodes.get(node1))).weightEdge.remove(node2);
            ((NodeInfo)(myNodes.get(node2))).nodeNei.remove(node1);
            ((NodeInfo)(myNodes.get(node2))).weightEdge.remove(node1);
            numEdges--;
            MC++;
        }
    }

    @Override
    public int nodeSize() {
        return myNodes.size();
    }

    @Override
    public int edgeSize() {
        return numEdges;
    }

    @Override
    public int getMC() {
        return MC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return numEdges == wGraph_ds.numEdges &&
                this.nodeSize()==wGraph_ds.nodeSize() &&
                Objects.equals(myNodes, wGraph_ds.myNodes);
    }


    private class NodeInfo implements node_info ,java.io.Serializable{
        private int key;
        private HashMap<Integer,node_info> nodeNei; // the neighbors, <neighbor's key, the neighbor(node)>
        private HashMap<Integer,Double> weightEdge; // the weights of the edges started in this node
                                                    // <neighbor's key, the weight of the edge>
        private String info;
        private double tag;
        private int index;


        public NodeInfo(int key) {
            this.key = key;
            nodeNei= new HashMap<Integer, node_info>();
            weightEdge= new HashMap<Integer, Double>();
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            this.info=s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag= t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return key == nodeInfo.key &&
                    Objects.equals(weightEdge, nodeInfo.weightEdge) ;
        }


    }
}
