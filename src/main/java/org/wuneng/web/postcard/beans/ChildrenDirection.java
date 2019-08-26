package org.wuneng.web.postcard.beans;

import java.util.ArrayList;
import java.util.List;

public class ChildrenDirection {
    private Direction direction;
    private List<ChildrenDirection> children;

    public ChildrenDirection(Direction direction) {
        this.direction = direction;
        this.children = new ArrayList<>();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<ChildrenDirection> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenDirection> children) {
        this.children = children;
    }

    public void addChildren(ChildrenDirection children) {
        this.children.add(children);
    }

}
