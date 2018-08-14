import soot.Unit;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;

import java.util.*;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class PermissionFlowGraph {

    private Map<Long, FlowGraphNode> nodes;
    private Map<Long, List<Long>> outgoingEdges;
    private Map<Long, List<Long>> incomingEdges;
    private Set<FlowGraphNode> entries;

    private boolean condensed;
    private PermissionFlowGraph condensedGraph;

    public PermissionFlowGraph() {
        nodes = new HashMap<>();
        outgoingEdges = new HashMap<>();
        incomingEdges = new HashMap<>();
        entries = new HashSet<>();
        condensed = false;
    }

    public void addNode(FlowGraphNode graphNode) {
        nodes.put(graphNode.getId(), graphNode);
        entries.add(graphNode);
    }

    public void addEdge(long from, long to) {
        if (!outgoingEdges.containsKey(from)) {
            outgoingEdges.put(from, new ArrayList<>());
        }
        outgoingEdges.get(from).add(to);

        if (!incomingEdges.containsKey(to)) {
            incomingEdges.put(to, new ArrayList<>());
        }
        incomingEdges.get(to).add(from);

        entries.remove(nodes.get(to));
    }

    public long findNodeByComponent(Unit v) {
        for (FlowGraphNode node : nodes.values()) {
            if (node.containsComponent(v)) {
                return node.getId();
            }
        }
        return -1;
    }

    public PermissionFlowGraph condenseGraph() {
        if (condensed) {
            return this;
        }

        condensedGraph = new PermissionFlowGraph();

        for (FlowGraphNode node : entries) {
            dfs(new FlowGraphNode(), node.getId(), new ArrayList<>());
        }

        condensed = true;
        return condensedGraph;
    }

    private Long dfs(FlowGraphNode container, Long nodeId, List<Long> visited) {
        // check if nodeId has been visited
        if (visited.contains(nodeId)) {
            return container.getId();
        }

        visited.add(nodeId);

        FlowGraphNode node = nodes.get(nodeId);
        if (outgoingEdges.getOrDefault(nodeId, new ArrayList<>()).size() <= 1
                && incomingEdges.getOrDefault(nodeId, new ArrayList<>()).size() <= 1
                && node.getPermissions().equals(container.getPermissions())) {

            container.merge(node);
            List<Long> children = outgoingEdges.getOrDefault(nodeId, new ArrayList<>());
            if (children.size() == 0) {
                return container.getId();
            }
            return dfs(container, children.get(0), visited);
        }

        condensedGraph.addNode(node);
        for (Long childNode : outgoingEdges.getOrDefault(nodeId, new ArrayList<>())) {
            condensedGraph.addEdge(nodeId, dfs(new FlowGraphNode(), childNode, visited));
        }

        if (!container.isEmpty()) {
            condensedGraph.addNode(container);
            condensedGraph.addEdge(container.getId(), nodeId);
            return container.getId();
        }
        return node.getId();
    }

    public int size() {
        return nodes.size();
    }

    public Set<FlowGraphNode> getEntries() {
        return entries;
    }

    public Map<Long, FlowGraphNode> getNodes() {
        return nodes;
    }

    public Map<Long, List<Long>> getOutgoingEdges() {
        return outgoingEdges;
    }

    public void printGraph(InfoflowCFG infoflowCFG) {
        System.out.printf("There are %d nodes in the permission flow graph\n", nodes.size());
        for (FlowGraphNode node : nodes.values()) {
            System.out.println(node);
        }

        for (long from : outgoingEdges.keySet()) {
            System.out.printf("Edge from node %d to node(s) %s\n", from, outgoingEdges.get(from));
        }
    }
}
