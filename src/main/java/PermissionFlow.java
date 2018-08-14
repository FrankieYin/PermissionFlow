import soot.Scene;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.*;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class PermissionFlow {

    private boolean callGraphInitialsed;
    private static final String androidJar = "/Users/frankie/Library/Android/sdk/platforms";
    CallGraph callGraph;
    InfoflowCFG infoflowCFG;

    public PermissionFlow() {
        callGraphInitialsed = false;
    }

    public void initCallGraph(String apkName) {
        String apkFileLocation = "/Users/frankie/Downloads/SummerResearch/apks/" + apkName;
        SetupApplication analyzer = new SetupApplication(androidJar, apkFileLocation);

        analyzer.constructCallgraph();
        infoflowCFG = new InfoflowCFG();
        callGraph = Scene.v().getCallGraph();
        callGraphInitialsed = true;
    }

    public static void main(String[] args) {
        PermissionFlow analyzer = new PermissionFlow();
        analyzer.initCallGraph("app.apk");
    }
}
