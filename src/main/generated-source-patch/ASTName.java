/* Generated By:JJTree: Do not edit this line. ASTName.java */

package vavi.tools.fixed;

public class ASTName extends SimpleNode {
    public ASTName(int id) {
        super(id);
    }

    public ASTName(JavaParser p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    private String name;

    public void setName(String name) {
//System.err.println("ASTName: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
