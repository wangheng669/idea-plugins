import java.util.ArrayList;
import java.util.List;

public class NodeTree {
    private String name;
    private List<NodeTree> children;
    private double line;

    public NodeTree(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NodeTree> getChildren() {
        return children;
    }

    public void setChildren(List<NodeTree> children) {
        this.children = children;
    }

    public double getLine() {
        return line;
    }

    public void setLine(double line) {
        this.line = line;
    }
}
