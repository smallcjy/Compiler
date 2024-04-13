package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.State;
import java.util.ArrayDeque;


public class ThompsonConstruction {
    public TNFA translate(RegexTreeNode node) {
        if (node == null)
            return null;
        TNFA tnfa = new TNFA();
        ArrayDeque<RegexTreeNode> array = new ArrayDeque<>();
        tnfa = RTNodeToNFA(node, array);
        return tnfa;
    }

    private  TNFA arrayItemNfa(ArrayDeque<RegexTreeNode> array){
        // 根据节点的值执行相应的操作
        RegexTreeNode node = array.poll();
        switch (node.getType()) {
            case 0 -> { // node is letter
                TNFA subTnfa = new TNFA();
                subTnfa.getTransitTable().addEdge(subTnfa.getStartState(), subTnfa.getAcceptingState(), node.getValue());
                return subTnfa;
            }
            case 1 -> { // node is concatenation
                TNFA leftnode = arrayItemNfa(array);
                TNFA rightnode = arrayItemNfa(array);
                return concat(leftnode, rightnode);
            }
            case 2 -> { // node is union
                TNFA leftnode = arrayItemNfa(array);
                TNFA rightnode = arrayItemNfa(array);
                return or(leftnode, rightnode);
            }
            case 3 -> { // node is kleene closure
                TNFA cur_node = arrayItemNfa(array);
                return kneel(cur_node);
            }
        }
        return new TNFA();
    }
    private TNFA RTNodeToNFA(RegexTreeNode node, ArrayDeque<RegexTreeNode> array) {
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
        return arrayItemNfa(array);
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




