import org.xmlpull.v1.XmlPullParserException;
import permissionData.PscoutParser;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkDefinition;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;

import java.io.IOException;
import java.util.*;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class PermissionFlow {

    private boolean callGraphInitialsed;
    private static final String androidJar = "/Users/frankie/Library/Android/sdk/platforms";
    CallGraph callGraph;
    InfoflowCFG infoflowCFG;
    public List<SootMethod> problemMethods;

    public PermissionFlow() {
        callGraphInitialsed = false;
    }

    public void initCallGraph(String apkName) {
        String apkFileLocation = "/Users/frankie/Downloads/SummerResearch/apks/" + apkName;
        SetupApplication analyzer = new SetupApplication(androidJar, apkFileLocation);

        analyzer.constructCallgraph();
        infoflowCFG = new InfoflowCFG();
        callGraph = Scene.v().getCallGraph();
        problemMethods = new ArrayList<>();
        callGraphInitialsed = true;
    }

    public void analyzeCallGraph() {

        // Get all the entry points
        boolean m1 = true;
        boolean m2 = true;
        SootMethod method1 = null;
        SootMethod method2 = null;
        for (Edge edge : callGraph) {
            SootMethod src = edge.src();
            SootMethod tgt = edge.tgt();
//            System.out.println("Calling from " + src + " to " + tgt);
            if (src.getName().compareTo("method1") == 0 && m1) {
//                analyzeNode(src);
                method1 = src;
                m1 = false;
            }

            if (src.getName().compareTo("method2") == 0 && m2) {
//                analyzeNode(src);
                method2 = src;
                m2 = false;
            }
        }

        Iterator<Unit> ug1 = infoflowCFG.getOrCreateUnitGraph(method1).iterator();
        Iterator<Unit> ug2 = infoflowCFG.getOrCreateUnitGraph(method2).iterator();
//        while (ug1.hasNext() && ug2.hasNext()) {
//            Unit u = ug1.next();
//            Unit v = ug2.next();
//            System.out.print(u + " == " + v + ": ");
//            System.out.println(u == v);
//        }


//        for (Unit u : ug) {
//            if (((Stmt) u).containsInvokeExpr()) {
//                InvokeExpr expr = ((Stmt) u).getInvokeExpr();
//                String signature = expr.getMethod().getSignature();
//                List<String> permissions = PscoutParser.getPermissionMapping(signature);
//            }
//        }
    }

    public void analyzeNode(SootMethod node) {
        /**
         * Assume there's only one head for now
         * Starting from the head, we traverse the whole method
         * Examine an instruction:
         * 1. associate the instruction with the permissions that it's holding
         * 2. check whether this instruction is a call statement:
         *      if yes, check whether it's an api call in which case update the permission
         *      if it's a user call, get the SootMethod of the callee and apply analyzeNode() recursively
         * 3. this function should update permissions in place but keep a snapshot of permission set when the set is
         * changed
         */
        DirectedGraph<Unit> unitDirectedGraph = null;
        try {
            unitDirectedGraph = infoflowCFG.getOrCreateUnitGraph(node);
        } catch (RuntimeException e) {
            problemMethods.add(node);
        }

        if (unitDirectedGraph == null) return;

        System.out.println(node);
        for (Unit u : unitDirectedGraph) {
            System.out.println(u);
            if (((Stmt) u).containsInvokeExpr()) {
                System.out.println("has callee");
                Collection<SootMethod> callees = infoflowCFG.getCalleesOfCallAt(u);
                System.out.println(callees.isEmpty());
                for (SootMethod callee : callees) {
                    System.out.println(callee);
                }
            }
        }
        System.out.println();

        // traverse the heads until the method has been exhausted
    }

    /**
     * Use Tarjan's strongly connected components algorithm to partition the call graph
     * into a condensed directed acyclic graph, each node of which is a {@link FlowGraphNode}
     * with permission information annotated.
     *
     * @return
     */
    public DirectedGraph<FlowGraphNode> partitionCallGraph() {
        return null;
    }

    public static void main(String[] args) {
        PermissionFlow analyzer = new PermissionFlow();
        analyzer.initCallGraph("app.apk");
        analyzer.analyzeCallGraph();
//        System.out.println(analyzer.problemMethods);
    }
}
