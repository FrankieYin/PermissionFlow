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

    private HashMap<Unit, NodeProperty> nodes;
    private Deque<Unit> stack;
    private int sccCount, id;

    public SccSolver() {
        infoflowCFG = new InfoflowCFG();
        callGraph = Scene.v().getCallGraph();
        nodes = new HashMap<>();
        stack = new ArrayDeque<>();
        sccCount = id = 0;
    }

    public void partitionCallGraph() {
        Iterator<MethodOrMethodContext> sourceIter = callGraph.sourceMethods();
        while (sourceIter.hasNext()) {
            SootMethod method = sourceIter.next().method();
            unitDirectedGraph = infoflowCFG.getOrCreateUnitGraph(method);
            for (Unit u : unitDirectedGraph)
                if (nodes.getOrDefault(u, new NodeProperty()).id == NodeProperty.UNVISITED)
                    dfs(u);

        }
    }

    private void dfs(Unit u) {
        stack.push(u);
        id++;
        nodes.put(u, new NodeProperty(id, id, true));

        for (Unit v : getSuccs(u)) {
            if (nodes.getOrDefault(v, new NodeProperty()).id == NodeProperty.UNVISITED)
                dfs(v);
            if (nodes.get(v).onStack)
                nodes.get(u).low = Math.min(nodes.get(v).low, nodes.get(u).low);
        }

        if (nodes.get(u).id == nodes.get(u).low) {
            for (Unit node = stack.pop(); ; node = stack.pop()) {
                nodes.get(node).low = nodes.get(u).id;
                nodes.get(node).onStack = false;
                if (node == u) break;
            }
            sccCount++;
        }
    }

    private List<Unit> getSuccs(Unit u) {
        List<Unit> succs = unitDirectedGraph.getSuccsOf(u);
        if (((Stmt) u).containsInvokeExpr()) {
            for (SootMethod callee : infoflowCFG.getCalleesOfCallAt(u)) {
                succs.addAll(infoflowCFG.getOrCreateUnitGraph(callee).getHeads());
            }
        }
        return succs;
    }
}
