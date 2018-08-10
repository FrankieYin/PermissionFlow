import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.toolkits.graph.DirectedGraph;

import java.util.*;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class SccSolver {

    private class NodeProperty {

        static final int UNVISITED = -1;
        int id, low;
        boolean onStack;

        NodeProperty(int id, int low, boolean onStack) {
            this.id = id;
            this.low = low;
            this.onStack = onStack;
        }

        NodeProperty() {
            id = low = UNVISITED;
            onStack = false;
        }
    }

    private CallGraph callGraph;
    private InfoflowCFG infoflowCFG;
    private DirectedGraph<Unit> unitDirectedGraph;
    private SootMethod currentMethod;

    private Map<Unit, NodeProperty> nodes;
    private Deque<Unit> stack;
    private int id;

    private PermissionFlowGraph graph;
    private Map<Unit, List<Long>> sccEdge;

    public SccSolver() {
        infoflowCFG = new InfoflowCFG();
        callGraph = Scene.v().getCallGraph();
        nodes = new HashMap<>();
        stack = new ArrayDeque<>();
        id = 0;
        graph = new PermissionFlowGraph();
        sccEdge = new HashMap<>();
    }

    public PermissionFlowGraph getSCCGraph() {
        Iterator<MethodOrMethodContext> sourceIter = callGraph.sourceMethods();
        List<SootMethod> entrys = new ArrayList<>();
        while (sourceIter.hasNext()) {
            SootMethod method = sourceIter.next().method();
            if (callGraph.isEntryMethod(method)) {
                entrys.add(method);
            }
        }

        for (SootMethod method : entrys) {
            currentMethod = method;
            unitDirectedGraph = infoflowCFG.getOrCreateUnitGraph(currentMethod);
            for (Unit u : unitDirectedGraph)
                if (nodes.getOrDefault(u, new NodeProperty()).id == NodeProperty.UNVISITED)
                    dfs(u);
        }

        return graph;
    }

    private void dfs(Unit u) {
        stack.push(u);
        id++;
        nodes.put(u, new NodeProperty(id, id, true));

        for (Unit v : getSuccs(u)) {
            if (nodes.getOrDefault(v, new NodeProperty()).id == NodeProperty.UNVISITED)
                dfs(v);
            if (nodes.get(v).onStack) {
                nodes.get(u).low = Math.min(nodes.get(v).low, nodes.get(u).low);
            } else {
                long nodeId = graph.findNodeByComponent(v);
                if (!sccEdge.containsKey(u)) {
                    sccEdge.put(u, new ArrayList<>());
                }
                if (!sccEdge.get(u).contains(nodeId)) {
                    sccEdge.get(u).add(nodeId);
                }
            }
        }

        if (nodes.get(u).id == nodes.get(u).low) {
            // new scc component, construct a node to contain it
            FlowGraphNode graphNode = new FlowGraphNode();
            for (Unit node = stack.pop(); ; node = stack.pop()) {
                nodes.get(node).low = nodes.get(u).id;
                nodes.get(node).onStack = false;
                graphNode.addComponent(node);
                for (long to : sccEdge.getOrDefault(node, new ArrayList<>())) {
                    graph.addEdge(graphNode.getId(), to);
                }
                if (node == u) break;
            }
            graph.addNode(graphNode);
        }
    }

    private List<Unit> getSuccs(Unit u) {
        SootMethod method = infoflowCFG.getMethodOf(u);
        if (method != currentMethod) {
            currentMethod = method;
            unitDirectedGraph = infoflowCFG.getOrCreateUnitGraph(currentMethod);
        }
        List<Unit> succs = unitDirectedGraph.getSuccsOf(u);
        if (((Stmt) u).containsInvokeExpr()) {
            for (SootMethod callee : infoflowCFG.getCalleesOfCallAt(u)) {
                succs.addAll(infoflowCFG.getOrCreateUnitGraph(callee).getHeads());
            }
        }
        return succs;
    }

    public static void main(String[] args) {
        PermissionFlow analyzer = new PermissionFlow();
        analyzer.initCallGraph("app.apk");
        SccSolver solver = new SccSolver();
        PermissionFlowGraph graph = solver.getSCCGraph();
        graph.printGraph(solver.infoflowCFG);
    }
}
