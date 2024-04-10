package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.State;
import org.qogir.compiler.util.tree.DefaultTreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
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


//    public TNFA translate(RegexTreeNode node) {
//        if (node == null)
//            return null;
//
//        TNFA tnfa = new TNFA();
//        Stack<TNFA> stack = postOrderTraversal(node);
//
//
//        // 当遍历完成后，栈顶就是整个正则表达式对应的NFA
//        if (!stack.isEmpty()) {
//            tnfa = stack.pop();
//        }
//
//        return tnfa;
//    }
//
//    private Stack<TNFA> postOrderTraversal(RegexTreeNode node) {
//        if (node == null) {
//            Stack<TNFA> stack = new Stack<>();
//            return stack;
//        }
////        // 先处理子节点
////        postOrderTraversal((RegexTreeNode) node.getFirstChild(), stack);
////        // 然后处理兄弟节点
////        postOrderTraversal((RegexTreeNode) node.getNextSibling(), stack);
//        RegexTreeNode current = node;
//        Stack<RegexTreeNode> s1 = new Stack();
//        Stack<RegexTreeNode> s2 = new Stack();
//        while (current != null || !s1.isEmpty()) {
//            while (current != null) {
//                s1.push(current);
//                s2.push(current);
//                current = (RegexTreeNode) current.getNextSibling();
//            }
//            if (!s1.isEmpty()) {
//                current = s1.pop();
//                current = (RegexTreeNode) current.getFirstChild();
//            }
//        }
//        Stack<TNFA> stack = new Stack<>();
//        while (!s2.isEmpty()) {
//            RegexTreeNode new_node = s2.pop();
//            // 根据节点的值执行相应的操作
//
//            switch (new_node.getType()) {
//                case 0 -> { // node is letter
//                    TNFA subTnfa = new TNFA();
//                    subTnfa.getTransitTable().addEdge(subTnfa.getStartState(), subTnfa.getAcceptingState(), node.getValue());
//                    stack.push(subTnfa);
//
//                }
//                case 1 -> { // node is concatenation
//                    TNFA rightTnfa = stack.pop();
//                    TNFA leftTnfa = stack.pop();
//                    leftTnfa.concat(rightTnfa);
//                    stack.push(leftTnfa);
//                }
//                case 2 -> { // node is union
//                    TNFA rightTnfa = stack.pop();
//                    TNFA leftTnfa = stack.pop();
//                    leftTnfa.or(rightTnfa);
//                    stack.push(leftTnfa);
//                }
//                case 3 -> { // node is kleene closure
//                    TNFA subTnfa = stack.pop();
//                    subTnfa.kneel();
//                    stack.push(subTnfa);
//                }
//                case 4 -> { // node is leftParenthesis
//                }
//                case 5 -> { // node is rightParenthesis
//                }
//            }
//            System.out.println(stack.size());
//        }
//        return stack;
//    }


    public TNFA translate(RegexTreeNode node) {
        if (node == null)
            return null;

        TNFA tnfa = new TNFA();
        ArrayDeque<RegexTreeNode> array = new ArrayDeque<>();
        tnfa = postOrderTraversal(node, array);


        return tnfa;
    }

    private  TNFA array_item_nfa(ArrayDeque<RegexTreeNode> array){
                // 根据节点的值执行相应的操作
        RegexTreeNode node = array.poll();
        switch (node.getType()) {
            case 0 -> { // node is letter
                TNFA subTnfa = new TNFA();
                subTnfa.getTransitTable().addEdge(subTnfa.getStartState(), subTnfa.getAcceptingState(), node.getValue());
                return subTnfa;
            }
            case 1 -> { // node is concatenation
                TNFA leftnode = array_item_nfa(array);
                TNFA rightnode = array_item_nfa(array);
                return concat(leftnode, rightnode);
            }
            case 2 -> { // node is union
                TNFA leftnode = array_item_nfa(array);
                TNFA rightnode = array_item_nfa(array);
                return or(leftnode, rightnode);
            }
            case 3 -> { // node is kleene closure
                TNFA cur_node = array_item_nfa(array);
                return kneel(cur_node);
            }
        }
        return new TNFA();
    }
    private TNFA postOrderTraversal(RegexTreeNode node, ArrayDeque<RegexTreeNode> array) {
        if (node == null) {
            return null;
        }
        ArrayDeque<RegexTreeNode> list = new ArrayDeque<>();
        list.push(node);
        while (!list.isEmpty()) {
            RegexTreeNode first_node = list.getFirst();
            if (first_node.getFirstChild() != null) {
                list.addLast((RegexTreeNode) first_node.getFirstChild());
            }
            if (first_node.getNextSibling() != null) {
                list.addLast((RegexTreeNode) first_node.getNextSibling());
            }
            array.addLast(list.poll());
        }
        return array_item_nfa(array);

//        Stack<TNFA> s = new Stack<>();
//        while(!stack.isEmpty()){
//            RegexTreeNode cur_node = stack.pop();
//            // 根据节点的值执行相应的操作
//
//            switch (cur_node.getType()) {
//                case 0 -> { // node is letter
//                    TNFA subTnfa = new TNFA();
//                    subTnfa.getTransitTable().addEdge(subTnfa.getStartState(), subTnfa.getAcceptingState(), cur_node.getValue());
//                    s.push(subTnfa);
//
//                }
//                case 1 -> { // node is concatenation
//                    TNFA rightTnfa = s.pop();
//                    TNFA leftTnfa = s.pop();
//                    leftTnfa.concat(rightTnfa);
//                    s.push(leftTnfa);
//                }
//                case 2 -> { // node is union
//                    TNFA rightTnfa = s.pop();
//                    TNFA leftTnfa = s.pop();
//                    leftTnfa.or(rightTnfa);
//                    s.push(leftTnfa);
//                }
//                case 3 -> { // node is kleene closure
//                    TNFA subTnfa = s.pop();
//                    subTnfa.kneel();
//                    s.push(subTnfa);
//                }
//
//            }
//        }
//        return s.pop();
//        // 先处理子节点
//        postOrderTraversal((RegexTreeNode) node.getFirstChild(), stack);
//        // 然后处理兄弟节点
//        postOrderTraversal((RegexTreeNode) node.getNextSibling(), stack);



    }

    //NFA连接操作
    public TNFA concat(TNFA nfa, TNFA another_nfa){
        TNFA result = new TNFA();
        nfa.getAcceptingState().setType(1);
        another_nfa.getStartState().setType(1);
        nfa.getTransitTable().merge(another_nfa.getTransitTable());
        //添加连接边
        nfa.getTransitTable().addEdge(nfa.getAcceptingState(), another_nfa.getStartState(), 'ε');
        //更新接受状态
        nfa.setAcceptingState(another_nfa.getAcceptingState());
        result = nfa;
        return result;
    }

    //NFA | 操作
    public TNFA or(TNFA nfa, TNFA another_nfa){
        TNFA result = new TNFA();
        nfa.getStartState().setType(1);
        nfa.getAcceptingState().setType(1);
        another_nfa.getStartState().setType(1);
        another_nfa.getAcceptingState().setType(1);
        //设置新的开始状态

        State new_state =new State();
        new_state.setType(0);
        //添加新状态的转换条件
        nfa.getTransitTable().addVertex(new_state);
        nfa.getTransitTable().addEdge(new_state, nfa.getStartState(), 'ε');
        nfa.getTransitTable().addEdge(new_state, another_nfa.getStartState(), 'ε');
        //set start state
        nfa.setStartState(new_state);
        //merge
        nfa.getTransitTable().merge(another_nfa.getTransitTable());
        //设置新结束状态
        State new_accept_state = new State();
        new_accept_state.setType(2);
        nfa.getTransitTable().addVertex(new_accept_state);
        nfa.getTransitTable().addEdge(nfa.getAcceptingState(), new_accept_state, 'ε');
        nfa.getTransitTable().addEdge(another_nfa.getAcceptingState(), new_accept_state, 'ε');
        nfa.setAcceptingState(new_accept_state);
        result = nfa;
        return result;
    }

    //闭包操作
    public TNFA kneel(TNFA nfa){
        TNFA result = new TNFA();
        nfa.getStartState().setType(1);
        nfa.getAcceptingState().setType(1);
        //set the new state we need
        State start_state = new State();
        State accept_state = new State();
        start_state.setType(0);
        accept_state.setType(2);
        //add start_state in table
        nfa.getTransitTable().addVertex(start_state);
        nfa.getTransitTable().addEdge(start_state, nfa.getStartState(), 'ε');
        //add accept_state in table
        nfa.getTransitTable().addVertex(accept_state);
        nfa.getTransitTable().addEdge(nfa.getAcceptingState(), accept_state, 'ε');

        nfa.getTransitTable().addEdge(nfa.getAcceptingState(), nfa.getStartState(), 'ε');

        //set new start stare and accept state
        nfa.setStartState(start_state);
        nfa.setAcceptingState(accept_state);
        //add the edge we need next
        nfa.getTransitTable().addEdge(nfa.getStartState(), nfa.getAcceptingState(), 'ε');
        result = nfa;
        return result;
    }
}


//        // 根据节点的值执行相应的操作
//        switch (node.getValue()) {
//            case 0 -> { // node is letter
//                TNFA subTnfa = new TNFA();
//                subTnfa.getTransitTable().addEdge(subTnfa.getStartState(), subTnfa.getAcceptingState(), node.getValue());
//                stack.push(subTnfa);
//            }
//            case 1 -> { // node is concatenation
//                TNFA rightTnfa = stack.pop();
//                TNFA leftTnfa = stack.pop();
//                leftTnfa.concat(rightTnfa);
//                stack.push(leftTnfa);
//            }
//            case 2 -> { // node is union
//                TNFA rightTnfa = stack.pop();
//                TNFA leftTnfa = stack.pop();
//                leftTnfa.or(rightTnfa);
//                stack.push(leftTnfa);
//            }
//            case 3 -> { // node is kleene closure
//                TNFA subTnfa = stack.pop();
//                subTnfa.kneel();
//                stack.push(subTnfa);
//            }
//            case 4 -> { // node is leftParenthesis
//            }
//            case 5 -> { // node is rightParenthesis
//            }
//        }




