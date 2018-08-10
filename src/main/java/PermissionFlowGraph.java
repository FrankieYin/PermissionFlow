import soot.Unit;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;

import java.util.*;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class PermissionFlowGraph {

    private Map<Long, FlowGraphNode> nodes;
    private Map<Long, List<Long>> edges;

    public PermissionFlowGraph() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }

    public boolean isEntryNode(FlowGraphNode node) {
        return false;
    }

    public void addNode(FlowGraphNode graphNode) {
        nodes.put(graphNode.getId(), graphNode);
    }

    public void addEdge(long from, long to) {
        if (!edges.containsKey(from)) {
            edges.put(from, new ArrayList<>());
        }
        edges.get(from).add(to);
    }

    public long findNodeByComponent(Unit v) {
        for (FlowGraphNode node : nodes.values()) {
            if (node.containsComponent(v)) {
                return node.getId();
            }
        }
        return -1;
    }

    public int size() {
        return nodes.size();
    }

    public Map<Long, FlowGraphNode> getNodes() {
        return nodes;
    }

    public Map<Long, List<Long>> getEdges() {
        return edges;
    }

    public void printGraph(InfoflowCFG infoflowCFG) {
        System.out.printf("There are %d nodes in the permission flow graph\n", nodes.size());
        for (FlowGraphNode node : nodes.values()) {
            System.out.println(node);
        }

        for (long from : edges.keySet()) {
            System.out.printf("Edge from node %d to node(s) %s\n", from, edges.get(from));
        }
    }
}
