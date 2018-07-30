package icfg;

import org.xmlpull.v1.XmlPullParserException;
import pscoutData.PscoutParser;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class CFG {

    public static void main(String[] args) throws IOException, XmlPullParserException {
        // Initialize Soot
        SetupApplication analyzer = new SetupApplication("/Users/frankie/Library/Android/sdk/platforms",
                "/Users/frankie/Downloads/SummerResearch/apks/RequestPermission.apk");
        analyzer.constructCallgraph();

        InfoflowCFG icfg = new InfoflowCFG();

        // Iterate over the callgraph
        boolean firstime = true;

        for (Iterator<Edge> edgeIt = Scene.v().getCallGraph().iterator(); edgeIt.hasNext(); ) {
            Edge edge = edgeIt.next();

            SootMethod smSrc = edge.src();
            Unit uSrc = edge.srcStmt();
            SootMethod smDest = edge.tgt();

            if (smSrc.getName().compareTo("onCreate") == 0 && firstime) {
                System.out.println(smSrc);
                DirectedGraph<Unit> ug = icfg.getOrCreateUnitGraph(smSrc);
                for (Unit u : ug) {
                    if (((Stmt) u).containsInvokeExpr()) {
                        InvokeExpr expr = ((Stmt) u).getInvokeExpr();
                        String signature = expr.getMethod().getSignature();
                        List<String> permissions = PscoutParser.getPermissionMapping(signature);
                    }
                }
                firstime = false;
            }

//            System.out.println("Edge from " + uSrc + " in " + smSrc + " to " + smDest);
        }
    }
}
