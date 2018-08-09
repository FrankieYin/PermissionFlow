import soot.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class FlowGraphNode {

    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private List<Unit> components;
    private final long id;

    public FlowGraphNode() {
        components = new ArrayList<>();
        id = NEXT_ID.getAndDecrement();
    }

    public List<Unit> getComponents() {
        return components;
    }

    public void addComponent(Unit node) {
        components.add(node);
    }

    public long getId() {
        return id;
    }

    public boolean containsComponent(Unit v) {
        return components.contains(v);
    }
}
