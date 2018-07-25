import org.xmlpull.v1.XmlPullParserException;
import soot.*;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.solver.cfg.InfoflowCFG;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;

import java.io.IOException;
import java.util.Iterator;

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
//                System.out.println(ug.getHeads());
//                System.out.println(ug.size());
//                System.out.println();
                for (Unit u : ug) {
                    System.out.println(u);
//                    List<ValueBox> boxes = u.getUseBoxes();
//                    System.out.println("The values used: ");
//                    for (ValueBox box : boxes) {
//                        Value value = box.getValue();
//                        System.out.print(value + ", ");
//                        System.out.println("value type: " + value.getType());
//                    }
                }
                firstime = false;
            }

            /**
             * Edge from specialinvoke $r1.<com.example.frankie.todoist.feature.MainActivity: void <init>()>()
             *     in <dummyMainClass: void dummyMainMethod(java.lang.String[])>
             *     to <com.example.frankie.todoist.feature.MainActivity: void <init>()>
             * Edge from specialinvoke $r0.<android.support.v7.app.AppCompatActivity: void <init>()>()
             *     in <com.example.frankie.todoist.feature.MainActivity: void <init>()>
             *         to <android.support.v7.app.AppCompatActivity: void <init>()>
             * Edge from $i0 = staticinvoke <android.support.v4.content.ContextCompat: int checkSelfPermission(android.content.Context,java.lang.String)>($r0, "android.permission.WRITE_CALENDAR")
             * in <com.example.frankie.todoist.feature.MainActivity: void onCreate(android.os.Bundle)>
             *     to <android.support.v4.content.ContextCompat: void <clinit>()>
             * Edge from $r2 = newarray (java.lang.String)[1]
             * in <com.example.frankie.todoist.feature.MainActivity: void onCreate(android.os.Bundle)>
             *     to <java.lang.String: void <clinit>()>
             * Edge from virtualinvoke $r1.<com.example.frankie.todoist.feature.MainActivity: void onCreate(android.os.Bundle)>(null)
             * in <dummyMainClass: void dummyMainMethod(java.lang.String[])>
             *     to <com.example.frankie.todoist.feature.MainActivity: void onCreate(android.os.Bundle)>
             * Edge from $r2 = newarray (java.lang.String)[1]
             * in <dummyMainClass: void dummyMainMethod(java.lang.String[])>
             *     to <java.lang.String: void <clinit>()>
             * Edge from specialinvoke $r0.<android.support.v7.app.AppCompatActivity: void onCreate(android.os.Bundle)>($r1)
             * in <com.example.frankie.todoist.feature.MainActivity: void onCreate(android.os.Bundle)>
             *     to <android.support.v7.app.AppCompatActivity: void onCreate(android.os.Bundle)>
             * Edge from virtualinvoke $r1.<com.example.frankie.todoist.feature.MainActivity: void onRequestPermissionsResult(int,java.lang.String[],int[])>(0, $r2, $r3)
             * in <dummyMainClass: void dummyMainMethod(java.lang.String[])>
             *     to <com.example.frankie.todoist.feature.MainActivity: void onRequestPermissionsResult(int,java.lang.String[],int[])>
             * Edge from <java.lang.String: java.util.Comparator CASE_INSENSITIVE_ORDER> = null
             * in <java.lang.String: void <clinit>()>
             *     to <java.lang.String: void <clinit>()>
             * Edge from staticinvoke <android.support.v4.app.ActivityCompat: void requestPermissions(android.app.Activity,java.lang.String[],int)>($r0, $r2, 1002)
             * in <com.example.frankie.todoist.feature.MainActivity: void onCreate(android.os.Bundle)>
             *     to <android.support.v4.app.ActivityCompat: void requestPermissions(android.app.Activity,java.lang.String[],int)>
             *
             * Edge from $i0 = staticinvoke <android.support.v4.content.ContextCompat: int checkSelfPermission(android.content.Context,java.lang.String)>($r0, "android.permission.WRITE_CALENDAR")
             * in <com.example.frankie.todoist.feature.MainActivity: void onCreate(android.os.Bundle)>
             *     to <android.support.v4.content.ContextCompat: int checkSelfPermission(android.content.Context,java.lang.String)>
             *
             * Edge from virtualinvoke $r0.<com.example.frankie.todoist.feature.MainActivity: void setContentView(int)>($i0)
             * in <com.example.frankie.todoist.feature.MainActivity: void onCreate(android.os.Bundle)>
             *     to <android.support.v7.app.AppCompatActivity: void setContentView(int)>
             */

//            System.out.println("Edge from " + uSrc + " in " + smSrc + " to " + smDest);
        }
    }
}
