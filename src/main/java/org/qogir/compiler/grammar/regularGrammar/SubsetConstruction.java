package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.State;
import org.qogir.compiler.util.graph.LabeledDirectedGraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The subset construction Algorithm for converting an NFA to a DFA.
 * The subset construction Algorithm takes an NFA N as input and output a DFA D accepting the same language as N.
 * The main mission is to eliminate ε-transitions and multi-transitions in NFA and construct a transition table for D.
 * The algorithm can be referred to {@see }
 */
public class SubsetConstruction {

    /**
     * Eliminate all ε-transitions reachable from a single state in NFA through the epsilon closure operation.
     * @param s a single state of NFA
     * @param tb the transition table of NFA
     * @return a set of state reachable from the state s on ε-transition
     * @author smallc
     */
    private HashMap<Integer, State> epsilonClosures(State s, LabeledDirectedGraph<State> tb){
        if (!tb.vertexSet().contains(s)) { //if vertex s not in the transition table
                return null;
        }

        HashMap<Integer,State> nfaStates = new HashMap<>();
        nfaStates.put(s.getId(), s);
        //Add your implementation
        ArrayDeque<State> array = new ArrayDeque<>();
        array.addFirst(s);
        while(!array.isEmpty()){
            State cur = array.poll();
            tb.edgeSet().forEach(item -> {
                if(item.getSource()==cur && item.getLabel() == 'ε'){
                    nfaStates.put(tb.getEdgeTarget(item).getId(), tb.getEdgeTarget(item));
                    array.addFirst(tb.getEdgeTarget(item));
                }
            });
        }

        return nfaStates;
    }

    /**
     * Eliminate all ε-transitions reachable from a  state set in NFA through the epsilon closure operation
     * @param ss a state set of NFA
     * @param tb the transition table of NFA
     * @return a set of state reachable from the state set on ε-transition
     * @author xuyang
     */

    public HashMap<Integer, State> epsilonClosure(HashMap<Integer, State> ss, LabeledDirectedGraph<State> tb){
        HashMap<Integer,State> nfaStates = new HashMap<>();
        for(State s : ss.values()){
            nfaStates.putAll(epsilonClosures(s,tb));
        }
        return nfaStates;
    }

    /**
     *
     * @param s
     * @param ch
     * @param tb
     * @return
     */
    private HashMap<Integer,State> moves(State s, Character ch, LabeledDirectedGraph<State> tb){
        HashMap<Integer,State> nfaStates = new HashMap<>();

        //Add your implementation
        tb.edgeSet().forEach(item -> {
            if(item.getSource() == s && item.getLabel() == ch){
                nfaStates.put(tb.getEdgeTarget(item).getId(), tb.getEdgeTarget(item));
            }
        });
        return nfaStates;
    }

    public HashMap<Integer,State> move(HashMap<Integer, State> ss, Character ch, LabeledDirectedGraph<State> tb){
        HashMap<Integer,State> nfaStates = new HashMap<>();
        for(State s : ss.values()){
            nfaStates.putAll(moves(s,ch,tb));
        }
        return nfaStates;
    }

    public HashMap<Integer,State> epsilonClosureWithMove(HashMap<Integer, State> sSet, Character ch, LabeledDirectedGraph<State> tb){
        HashMap<Integer,State> states = new HashMap<>();
        states.putAll(epsilonClosure(move(sSet, ch, tb),tb));
        return states;
    }

    private boolean equal(HashMap<Integer, State> s1, HashMap<Integer, State> s2) {
        if (s1.size() != s2.size()) {
            return false;
        }
        for (Integer key : s1.keySet()) {
            if(s1.get(key) != s2.get(key)) return false;
        }
        return true;
    }

    private  boolean contain(HashMap<State, HashMap<Integer, State>> s, HashMap<Integer, State> value){
        for (State i : s.keySet()) {
            if(equal(s.get(i), value)) {
                return true;
            }
        }
        return false;
    }

    public RDFA subSetConstruct(TNFA tnfa){
        RDFA result = new RDFA();
        result.getTransitTable().addVertex(result.getStartState());
        // Add your implementation
        //获得tnfa初始状态的ε闭包
        HashMap<Integer, State> sSet = new HashMap<>();
        sSet.put(tnfa.getStartState().getId(),  tnfa.getStartState());
        HashMap<Integer, State> sDfaNfa = epsilonClosure(sSet, tnfa.getTransitTable());
        //创建一个NFA的步骤：设置初始状态，设置转换关系(加起点、加边、加终点)，设置dfaToNfa
        result.setStateMappingBetweenDFAAndNFA(result.getStartState(), sDfaNfa);
        //创建遍历队列
        ArrayList<State> dstate = new ArrayList<State>();
        dstate.add(result.getStartState());
        AtomicInteger cur_index = new AtomicInteger(0);
        while (cur_index.get() < dstate.size()) {
            State scur = dstate.get(cur_index.get());
            //找到当前状态对应的闭包
            HashMap<Integer, State> cur = result.getStateMappingBetweenDFAAndNFA().get(scur);
            //对于每一个转换字符构建move闭包
            tnfa.getAlphabet().forEach(ch -> {
                HashMap<Integer, State> item = epsilonClosureWithMove(cur, ch, tnfa.getTransitTable());
                //判断这个闭包是否在现存的Dfa状态集内，如果没有创建新状态并加入到列表中，如果有则直接设置转换关系
                if(contain(result.getStateMappingBetweenDFAAndNFA(), item)) {
                    //找到闭包对应的状态
                    result.getStateMappingBetweenDFAAndNFA().keySet().forEach(targe->{
                        if(equal(result.getStateMappingBetweenDFAAndNFA().get(targe), item)) {
                            result.getTransitTable().addEdge(scur, targe, ch);
                        }
                    });
                } else {
                    //Todo: 图的遍历出不来，标记已经经过的节点
                    State newState = new State();
                    result.getTransitTable().addVertex(newState);
                    result.getTransitTable().addEdge(scur,newState,ch);
                    result.getStateMappingBetweenDFAAndNFA().put(newState, item);
                    dstate.add(newState);
                }
            });
            cur_index.addAndGet(1);
        }

        return result;
    }
}
