import com.sun.javafx.binding.StringFormatter;
import permissionData.Permission;
import permissionData.PscoutParser;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class FlowGraphNode {

    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private List<Unit> components;
    private Set<Permission> permissions;

    private final long id;

    public FlowGraphNode() {
        components = new ArrayList<>();
        permissions = new HashSet<>();
        id = NEXT_ID.getAndIncrement();
    }

    public List<Unit> getComponents() {
        return components;
    }

    public void addComponent(Unit node) {
        components.add(node);
        if (((Stmt) node).containsInvokeExpr()) {
            InvokeExpr expr = ((Stmt) node).getInvokeExpr();
            String signature = expr.getMethod().getSignature();
            Set<Permission> newPermissions = PscoutParser.getPermissionMapping(signature);
            permissions.addAll(newPermissions);
        }
    }

    public long getId() {
        return id;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean containsComponent(Unit v) {
        return components.contains(v);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Node %d has %d components\n", id, components.size()));
        builder.append("With Permissions: ").append(permissions).append("\n");
        for (Unit u : components) {
            builder.append(String.format("%s\n", u));
        }
        return builder.toString();
    }

    public void merge(FlowGraphNode node) {
        components.addAll(node.getComponents());
        permissions.addAll(node.getPermissions());
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }
}
