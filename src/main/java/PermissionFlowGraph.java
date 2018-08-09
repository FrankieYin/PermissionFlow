import soot.Unit;

import java.util.*;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class PermissionFlowGraph {

    private Set<FlowGraphNode> nodes;
    private Map<Long, List<Long>> edges;

    public PermissionFlowGraph() {
        nodes = new HashSet<>();
        edges = new HashMap<>();
    }

    public boolean isEntryNode(FlowGraphNode node) {
        return false;
    }

    public void addNode(FlowGraphNode graphNode) {
        nodes.add(graphNode);
    }

    public void addEdge(long from, long to) {
        if (!edges.containsKey(from)) {
            edges.put(from, new ArrayList<>());
        }
        edges.get(from).add(to);
    }

    public long findNodeByComponent(Unit v) {
        for (FlowGraphNode node : nodes) {
            if (node.containsComponent(v)) {
                return node.getId();
            }
        }
        return -1;
    }
}
