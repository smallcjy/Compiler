package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.FiniteAutomaton;
import org.qogir.compiler.FA.State;

/**
 * A nondeterministic finite automaton (NFA) is a 5-tuple (S,∑,F,s0,sf). Here,
 *   S - a finite set of state. i.e The vertex set of transition graph of NFA
 *   ∑ - an Alphabet, not including ε
 *   F - S X ∑ U {ε} -> subset of S
 *   s0 - a start state
 *   sf - an accepting state.
 * Here, following McNaughton-Yamada-Thompson algorithm, NFA has the following properties:
 *   each NFA has only one accepting state;
 *   The accepting state has no outgoing transitions, and the start state has no incoming
 *   transitions.
 *   Each state other than the accepting state has either one outgoing transition on a symbol
 *   in alphabet or two outgoing transitions, both on ε(epsilon).
 */

public class TNFA extends FiniteAutomaton {

    private State acceptingState = new State();

    public TNFA(){
        super();
        acceptingState.setType(State.ACCEPT);
        this.transitTable.addVertex(acceptingState);
    }

    public TNFA(State acceptingState){
        super();
        this.acceptingState = acceptingState;
        this.acceptingState.setType(State.ACCEPT);
        this.transitTable.addVertex(this.acceptingState);
    }

    public State getAcceptingState() {
        return acceptingState;
    }

    public void setAcceptingState(State acceptingState) {
        this.acceptingState = acceptingState;
    }

    //NFA连接操作
    public void link(TNFA another_nfa){
        this.transitTable.merge(another_nfa.transitTable);
        //添加连接边
        this.transitTable.addEdge(this.getAcceptingState(), another_nfa.getStartState(), 'ε');
        //更新接受状态
        this.acceptingState = another_nfa.getAcceptingState();
    }

    //NFA | 操作
    public void or(TNFA another_nfa){
        //设置新的开始状态
        State new_state =new State();
        //添加新状态的转换条件
        this.transitTable.addVertex(new_state);
        this.transitTable.addEdge(new_state, this.getStartState(), 'ε');
        this.transitTable.addEdge(new_state, another_nfa.getStartState(), 'ε');
        //set start state
        this.startState = new_state;
        //merge
        this.transitTable.merge(another_nfa.transitTable);
        //设置新结束状态
        State new_accept_state = new State();
        this.transitTable.addVertex(new_accept_state);
        this.transitTable.addEdge(this.getAcceptingState(), new_accept_state, 'ε');
        this.transitTable.addEdge(another_nfa.getAcceptingState(), new_accept_state, 'ε');
        this.acceptingState = new_accept_state;
    }

    //闭包操作
    public void kneel(){
        //set the new state we need
        State start_state = new State();
        State accept_state = new State();
        //add start_state in table
        this.transitTable.addVertex(start_state);
        this.transitTable.addEdge(start_state, this.getStartState(), 'ε');
        //add accept_state in table
        this.transitTable.addVertex(accept_state);
        this.transitTable.addEdge(this.getAcceptingState(), accept_state, 'ε');

        this.transitTable.addEdge(this.getAcceptingState(), this.getStartState(), 'ε');

        //set new start stare and accept state
        this.startState = start_state;
        this.acceptingState = accept_state;
        //add the edge we need next
        this.transitTable.addEdge(this.getStartState(), this.getAcceptingState(), 'ε');
    }
}

















