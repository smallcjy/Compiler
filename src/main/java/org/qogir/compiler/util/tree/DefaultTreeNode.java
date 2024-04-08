package org.qogir.compiler.util.tree;

import org.qogir.compiler.grammar.regularGrammar.RegexTreeNode;

import javax.swing.tree.TreeNode;
import java.io.Serial;
import java.io.Serializable;

public class DefaultTreeNode implements Serializable {
    @Serial
    private static final long serialVersionUID = -7535431420416610980L;

    private DefaultTreeNode firstChild;
    private DefaultTreeNode nextSibling;

    public DefaultTreeNode() {
        this.firstChild = null;
        this.nextSibling = null;
    }

    public DefaultTreeNode(DefaultTreeNode firstChild, DefaultTreeNode nextSibling) {
        this.firstChild = firstChild;
        this.nextSibling = nextSibling;
    }

    public void setFirstChild(DefaultTreeNode firstChild) {
        this.firstChild = firstChild;
    }

    public DefaultTreeNode getFirstChild() {
        return firstChild;
    }

    public void setNextSibling(DefaultTreeNode nextSibling) {
        this.nextSibling = nextSibling;
    }

    public DefaultTreeNode getNextSibling() {
        return nextSibling;
    }


    // 成员变量作为后序遍历得到的字符串
    private StringBuilder sb = new StringBuilder();

    public StringBuilder postTraversal(DefaultTreeNode node) {
        if (node != null) {
            if (node.getFirstChild() != null) {
                postTraversal(node.getFirstChild());
            }
            if (node.getNextSibling() != null) {
                postTraversal(node.getNextSibling());
            }
            sb.append(node);
        }
        return sb;
    }
}

//        postTraversal(node.getFirstChild());
//        postTraversal(node.getNextSibling());
//        sb.append(node);
//        return sb;



