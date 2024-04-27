package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.State;
import org.qogir.compiler.util.graph.LabelEdge;
import org.qogir.compiler.util.graph.LabeledDirectedGraph;

import java.util.*;

/**
 * This class implements the state minimization algorithm for reducing the number of states in a deterministic finite automaton (DFA).
 * The algorithm takes a DFA as input and produces an equivalent DFA with a minimal number of states.
 */
public class StateMinimization {
    /**
     * Computes the epsilon closures for a given state in an NFA.
     *
     * @param s  the state for which to compute epsilon closures
     * @param tb the transition table of the NFA
     * @return a set of states reachable from the state s via epsilon transitions
     */
    private HashMap<Integer, State> epsilonClosures(State s, LabeledDirectedGraph<State> tb) {
        if (!tb.vertexSet().contains(s)) { //if vertex s not in the transition table
            return null;
        }
        HashMap<Integer, State> nfaStates = new HashMap<>();
        nfaStates.put(s.getId(), s);
        //Add your implementation
        ArrayDeque<State> array = new ArrayDeque<>();
        array.addFirst(s);
        while (!array.isEmpty()) {
            State cur = array.poll();
            tb.edgeSet().forEach(item -> {
                if (item.getSource() == cur && item.getLabel() == 'ε') {
                    nfaStates.put(tb.getEdgeTarget(item).getId(), tb.getEdgeTarget(item));
                    array.addFirst(tb.getEdgeTarget(item));
                }
            });
        }
        return nfaStates;
    }

    /**
     * Computes the epsilon closures for a given set of states in an NFA.
     *
     * @param ss the set of states for which to compute epsilon closures
     * @param tb the transition table of the NFA
     * @return a set of states reachable from the states in ss via epsilon transitions
     */
    public HashMap<Integer, State> epsilonClosure(HashMap<Integer, State> ss, LabeledDirectedGraph<State> tb) {
        HashMap<Integer, State> nfaStates = new HashMap<>();
        for (State s : ss.values()) {
            nfaStates.putAll(epsilonClosures(s, tb));
        }
        return nfaStates;
    }

    /**
     * Computes the moves for a given state and character in an NFA.
     *
     * @param s  the source state
     * @param ch the input character
     * @param tb the transition table of the NFA
     * @return a set of states reachable from the source state s via the input character ch
     */
    private HashMap<Integer, State> moves(State s, Character ch, LabeledDirectedGraph<State> tb) {
        HashMap<Integer, State> nfaStates = new HashMap<>();
        tb.edgeSet().forEach(item -> {
            if (item.getSource() == s && item.getLabel() == ch) {
                nfaStates.put(tb.getEdgeTarget(item).getId(), tb.getEdgeTarget(item));
            }
        });
        return nfaStates;
    }

    /**
     * Computes the moves for a given set of states and character in an NFA.
     *
     * @param ss the set of source states
     * @param ch the input character
     * @param tb the transition table of the NFA
     * @return a set of states reachable from the source states ss via the input character ch
     */
    public HashMap<Integer, State> move(HashMap<Integer, State> ss, Character ch, LabeledDirectedGraph<State> tb) {
        HashMap<Integer, State> nfaStates = new HashMap<>();
        for (State s : ss.values()) {
            nfaStates.putAll(moves(s, ch, tb));
        }
        return nfaStates;
    }
    /**
     * Computes the epsilon closure with move for a given set of states and character in an NFA.
     *
     * @param sSet the set of source states
     * @param ch    the input character
     * @param tb   the transition table of the NFA
     * @return a set of states reachable from the source states sSet via epsilon closures and the input character ch
     */
    public HashMap<Integer, State> epsilonClosureWithMove(HashMap<Integer, State> sSet, Character ch, LabeledDirectedGraph<State> tb) {
        HashMap<Integer, State> states = new HashMap<>();
        states.putAll(epsilonClosure(move(sSet, ch, tb), tb));
        return states;
    }

    //-------------------------------以上为复用子集构造法中的方法，以下为新增的实现方法-----------------------------------

    /**
     * Checks if s1 is a subset of s2.
     *
     * @param s1 the first set of states
     * @param s2 the second set of states
     * @return true if s1 is a subset of s2, false otherwise
     */
    private boolean isSubset(HashMap<Integer, State> s1, HashMap<Integer, State> s2) {
        if (s1.isEmpty()) return true;
        if (s1.size() > s2.size()) return false;
        for (Integer key : s1.keySet()) {
            if (s1.get(key) != s2.get(key)) return false;
        }
        return true;
    }

    /**
     * Gets the group to which a state belongs in the specified collection of groups.
     *
     * @param s      the state for which to find the group
     * @param groups the collection of groups to search
     * @return the group to which the state belongs, or null if not found
     */
    private HashMap<Integer, State> getGroup(State s, LinkedHashSet<HashMap<Integer, State>> groups) {
        for (HashMap<Integer, State> group : groups) {
            if (group.containsValue(s)) return group;
        }
        return null;
    }

    /**
     * Classifies a collection of states based on their target states for a given input character.
     *
     * @param s         the collection of states to classify
     * @param ch        the input character
     * @param dfa       the DFA
     * @param allGroups all the groups of states
     * @return a new set of groups after classification
     */
    private LinkedHashSet<HashMap<Integer, State>> classifyAccordingToTarget(HashMap<Integer, State> s, char ch, RDFA dfa, LinkedHashSet<HashMap<Integer, State>> allGroups) {
        HashMap<HashMap<Integer, State>, HashMap<Integer, State>> classifyGroup = new HashMap<>();    //新分类的组, 外层hashmap的State表示targetState
        Integer[] keys = s.keySet().toArray(new Integer[0]);
        for (int i = 0; i < keys.length; i++) {
            State targetState = getTargetState((State) s.get(keys[i]), ch, dfa);
            HashMap<Integer, State> belongGroup = getGroup(targetState, allGroups);
            int size = classifyGroup.keySet().size();
            for (HashMap<Integer, State> group : classifyGroup.keySet()) {
                if (belongGroup == group) {  //已有现成的分组
                    classifyGroup.get(group).put(keys[i], s.get(keys[i]));
                } else {
                    size--;
                }
            }
            if (size == 0) {   //说明不是已有的分组
                HashMap<Integer, State> newgroup = new HashMap<>();
                newgroup.put(keys[i], s.get(keys[i]));
                classifyGroup.put(belongGroup, newgroup);
            }
        }
        LinkedHashSet<HashMap<Integer, State>> classifyGroup2 = new LinkedHashSet<>();
        classifyGroup2.addAll(classifyGroup.values());
        for (HashMap<Integer, State> group : allGroups) {
            if (!group.equals(s)) {
                classifyGroup2.add(group);
            }
        }
        return classifyGroup2;
    }

    /**
     * Copies a state and its outgoing edges from one DFA to another.
     *
     * @param dfa1      the destination DFA
     * @param dfa2      the source DFA
     * @param allGroups all the groups of states
     * @param state     the state to copy
     */
    private void copyDFA(RDFA dfa1, RDFA dfa2, LinkedHashSet<HashMap<Integer, State>> allGroups, State state) {
        dfa1.getTransitTable().addVertex(state);
        for (char ch : dfa2.getAlphabet()) {
            LabelEdge l = getOutEdge(dfa2, state, ch);
            if (l != null) {
                for (HashMap<Integer, State> group : allGroups) {
                    if (group.containsValue(l.getTarget())) {
                        Integer[] keys = group.keySet().toArray(new Integer[0]);
                        l.setTarget(group.get(keys[0]));
                    }
                }
                if (!dfa1.getTransitTable().containsEdge(l)) {
                    dfa1.getTransitTable().addEdge(l);
                }
            }
        }
    }

     /**
     * Checks if the specified set of states contains a start state.
     *
     * @param states the set of states to check
     * @return true if the set contains a start state, false otherwise
     */
    private boolean isContainStartState(HashMap<Integer, State> states) {
        for (State state : states.values()) {
            if (state.getType() == State.START) return true;
        }
        return false;
    }

    /**
     * Merges multiple equivalent states in a DFA into a single state and copies them to a new DFA.
     *
     * @param dfa          the original DFA
     * @param minimizeDFA  the minimized DFA
     * @param allGroups    all the groups of states
     * @param mGroup       the group of states to merge
     * @return the merged state
     */
    private State mergeAndCopyDFA(RDFA dfa, RDFA minimizeDFA, LinkedHashSet<HashMap<Integer, State>> allGroups, HashMap<Integer, State> mGroup) {
        Integer[] keys = mGroup.keySet().toArray(new Integer[0]);
        State mergeState = mGroup.get(keys[0]);
        minimizeDFA.getTransitTable().addVertex(mergeState);
        for (State s : mGroup.values()) {
            for (char ch : dfa.getAlphabet()) {
                LabelEdge l = getOutEdge(dfa, s, ch);
                if (l != null) {
                    l.setSource(mergeState); //组内成员重定位，将源转移给mergeState
                    if (mGroup.containsValue(l.getTarget())) {
                        l.setTarget(mergeState);   //target为组内成员，将targetState转移给mergeState
                    } else {   //target为组外成员，查找对应的组的mergeState
                        for (HashMap<Integer, State> group : allGroups) {
                            if (group.containsValue(l.getTarget())) {
                                Integer[] keys2 = group.keySet().toArray(new Integer[0]);
                                l.setTarget(group.get(keys2[0]));
                            }
                        }
                    }
                    if (!minimizeDFA.getTransitTable().containsEdge(l)) {
                        minimizeDFA.getTransitTable().addEdge(l);
                    }
                }

            }
        }
        return mergeState;
    }
    /**
     * Merges a group of equivalent states into a single state in the minimized DFA.
     * The first state in the group is chosen as the merged state.
     *
     * @param allGroups   all the groups of states to merge
     * @param dfa         the original DFA
     * @param minimizeDFA the minimized DFA
     */
    private void mergeStates(LinkedHashSet<HashMap<Integer, State>> allGroups, RDFA dfa, RDFA minimizeDFA) {
        for (HashMap<Integer, State> s : allGroups) {
            State mergeState = new State();
            if (s.size() == 1) {
                Integer[] keys = s.keySet().toArray(new Integer[0]);
                mergeState = s.get(keys[0]);
                copyDFA(minimizeDFA, dfa, allGroups, mergeState);
            } else if (s.size() > 1) {
                mergeState = mergeAndCopyDFA(dfa, minimizeDFA, allGroups, s);
            }
            if (isContainStartState(s)) {
                minimizeDFA.setStartState(mergeState);
            }
        }
    }

    /**
     * Retrieves the outgoing edge of a state in the DFA for the specified input character.
     *
     * @param dfa   the DFA
     * @param state the source state
     * @param c     the input character
     * @return the outgoing edge corresponding to the state and character, or null if not found
     */
    private LabelEdge getOutEdge(RDFA dfa, State state, char c) {
        LabeledDirectedGraph<State> dfaGraph = dfa.getTransitTable();
        for (State s : dfaGraph.vertexSet()) {
            if (state.equals(s)) {  // 找到对应的state
                for (LabelEdge edge : dfaGraph.edgeSet()) {
                    if (edge.getSource() == state && edge.getLabel() == c) {  //找到对应的边
                        return edge;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets the target state reached from a source state for the specified input character.
     *
     * @param sourceState the source state
     * @param c           the input character
     * @param dfa         the DFA
     * @return the target state reached from the source state for the specified character, or null if not found
     */
    private State getTargetState(State sourceState, char c, RDFA dfa) {
        LabelEdge l =getOutEdge(dfa, sourceState, c);
        if(l!=null)
            return dfa.getTransitTable().getEdgeTarget(l);
        return null;
    }

    /**
     * Initializes the equivalence state groups, dividing the states of the DFA into two groups: accepting states and non-accepting states.
     *
     * @param dfa the DFA
     * @return the initial set of equivalence state groups
     */
    private LinkedHashSet<HashMap<Integer, State>> initialGroups(RDFA dfa) {
        LinkedHashSet<HashMap<Integer, State>> initialGroups = new LinkedHashSet<HashMap<Integer, State>>();   //已经划分好的所有分组
        HashMap<Integer, State> group1 = new HashMap<>();
        HashMap<Integer, State> group2 = new HashMap<>();
        for (State state : dfa.getTransitTable().vertexSet()) {
            if (state.getType() == State.ACCEPT) {
                group1.put(state.getId(), state);
            } else {
                group2.put(state.getId(), state);
            }
        }
        initialGroups.add(group1);
        initialGroups.add(group2);
        return initialGroups;
    }

    /**
     * Checks if a set of states in a state collection are all equivalent for a given input character.
     *
     * @param dfa       the DFA
     * @param allGroups all the groups of states
     * @param g         the set of states to check
     * @param ch        the input character
     * @return true if the states in the set are all equivalent, false otherwise
     */
    private boolean stateThroughCharIsEqual(RDFA dfa, LinkedHashSet<HashMap<Integer, State>> allGroups, HashMap<Integer, State> g, char ch) {
        // 获取经过每个char ch可以转移的状态集合闭包
        Integer[] keys = g.keySet().toArray(new Integer[0]);
        HashMap<Integer, State> groupClosure = epsilonClosureWithMove(g, ch, dfa.getTransitTable());  //集合的闭包
        //判断这个闭包是否为已划分的状态集的子集
        int num = allGroups.size();
        for (HashMap<Integer, State> group : allGroups) {
            if (!isSubset(groupClosure, group)) {
                num--;
            }
        }
        return num != 0;  //表达式为真则说明闭包不在已划分的状态集内，即该状态集里的状态并非全部等价
    }

    /**
     * Minimizes the given DFA by reducing the number of states.
     *
     * @param dfa the DFA to be minimized
     * @return a minimized DFA equivalent to the input DFA
     */
    public RDFA minimize(RDFA dfa) {
        RDFA minimizeDFA = new RDFA();
        //初始化分组
        LinkedHashSet<HashMap<Integer, State>> allGroups = initialGroups(dfa);   //已经划分好的所有分组
        LinkedHashSet<HashMap<Integer, State>> temp = allGroups;  //缓存已划分的分组，用于后续判断有无修改
        // 以现有的两组分组开始，划分其他分组
        do {
            temp = allGroups;
            for (char ch : dfa.getAlphabet()) {
                // 获取经过每个char ch可以转移的状态集合闭包
                for (HashMap<Integer, State> g : allGroups) {
                    if (!stateThroughCharIsEqual(dfa, allGroups, g, ch))
                        allGroups = classifyAccordingToTarget(g, ch, dfa, allGroups);   //新分类的组
                }
            }
        } while (!temp.equals(allGroups));  //更新已划分的集合,若有更新则循环
        mergeStates(allGroups, dfa, minimizeDFA);
        return minimizeDFA;
    }
}
