package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.State;
import org.qogir.compiler.util.tree.DefaultTreeNode;

import java.util.ArrayDeque;
import java.util.Stack;

public class ThompsonConstruction {


//    public TNFA translate(RegexTreeNode node) {
//
//        if (node == null)
//            return null;
//
//        TNFA tnfa = new TNFA();
//        //Add your implementation
//        Stack<TNFA> stack = new Stack<>();
//        while(node.getFirstChild()!=null){
//            switch (node.getValue()) {
//                case (0): {      //node is letter
//                    TNFA subTnfa = new TNFA();
//                    subTnfa.setStartState(new State());
//                    subTnfa.setAcceptingState(new State());
//                    subTnfa.getTransitTable().addEdge(subTnfa.getStartState(), subTnfa.getAcceptingState(), node.getValue());
//                    stack.push(subTnfa);
//                    break;
//                }
//                case (1): {     //node is concatenation
//                    TNFA rightTnfa = stack.pop();
//                    TNFA leftTnfa = stack.pop();
//                    leftTnfa.concat(rightTnfa);
//                    stack.push(leftTnfa);
//                    break;
//
//                }
//                case (2): {     //node is union
//                    TNFA rightTnfa = stack.pop();
//                    TNFA leftTnfa = stack.pop();
//                    leftTnfa.or(rightTnfa);
//                    stack.push(leftTnfa);
//                    break;
//                }
//                case (3): {     //node is kleene closure
//                    TNFA subTnfa = stack.pop();
//                    subTnfa.kneel();
//                    stack.push(subTnfa);
//                    break;
//                }
//                case (4): {     //node is leftParenthesis
//                    break;
//                }
//                case (5): {     //node is rightParenthesis
//                    break;
//                }
//            }
//            node = (RegexTreeNode)node.getFirstChild();
//        }
//        return tnfa;
//    }
//
//    //新增方法 后序遍历
//    public String postTra(RegexTreeNode node) {
//        return node.postTraversal(node).toString();
//    }

    public TNFA translate(RegexTreeNode node) {
        if (node == null)
            return null;

        TNFA tnfa = new TNFA();
        Stack<TNFA> stack = new Stack<>();

        postOrderTraversal(node, stack);

        // 当遍历完成后，栈顶就是整个正则表达式对应的NFA
        if (!stack.isEmpty()) {
            tnfa = stack.pop();
        }

        return tnfa;
    }

    private void postOrderTraversal(RegexTreeNode node, Stack<TNFA> stack) {
        if (node == null)
            return;

        // 先处理子节点
        postOrderTraversal((RegexTreeNode) node.getFirstChild(), stack);
        // 然后处理兄弟节点
        postOrderTraversal((RegexTreeNode) node.getNextSibling(), stack);

        // 根据节点的值执行相应的操作
        switch (node.getValue()) {
            case 0 -> { // node is letter
                TNFA subTnfa = new TNFA();
                subTnfa.setStartState(new State());
                subTnfa.setAcceptingState(new State());
                subTnfa.getTransitTable().addEdge(subTnfa.getStartState(), subTnfa.getAcceptingState(), node.getValue());
                stack.push(subTnfa);
            }
            case 1 -> { // node is concatenation
                TNFA rightTnfa = stack.pop();
                TNFA leftTnfa = stack.pop();
                leftTnfa.concat(rightTnfa);
                stack.push(leftTnfa);
            }
            case 2 -> { // node is union
                TNFA rightTnfa = stack.pop();
                TNFA leftTnfa = stack.pop();
                leftTnfa.or(rightTnfa);
                stack.push(leftTnfa);
            }
            case 3 -> { // node is kleene closure
                TNFA subTnfa = stack.pop();
                subTnfa.kneel();
                stack.push(subTnfa);
            }
            case 4 -> { // node is leftParenthesis
            }
            case 5 -> { // node is rightParenthesis
            }
        }
    }





}

