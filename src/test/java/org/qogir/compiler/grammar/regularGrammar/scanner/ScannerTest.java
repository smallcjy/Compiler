package org.qogir.compiler.grammar.regularGrammar.scanner;

import org.qogir.compiler.FA.State;
import org.qogir.compiler.grammar.regularGrammar.*;
import org.qogir.simulation.scanner.Scanner;

import java.io.PipedOutputStream;
import java.util.HashMap;

public class ScannerTest {
    public static void main(String[] args) {

//        String[] regexes = new String[]{"regex0 := a|ε","regex1 := c(a|b)*"}; //{"regex1 := c(a|b)*"};//{"regex0 := a|ε","regex1 := c(a|b)*"};//"regex1 := c(a|b)*","regex2 := d(f|ea*(g|h))b","c(a|b)*","a|b", "ab*", "d(f|e)","d(f|ea*(g|h))b","c(a|b)*"
        //测试数据
        //1、(a|b)*abb
        String[] regexes = new String[]{"regex1 := c(a|b)*"};
        //test defining a regular grammar
        RegularGrammar rg = new RegularGrammar(regexes);

        System.out.println(rg);
        //test building a grammar for the grammar
        Scanner scanner = new Scanner(rg);

        //test constructing the regex tree
        //System.out.println(scanner.constructRegexTrees().toString());

        //System.out.println("Show the NFA:");
        //test constructing the NFA
        //System.out.println(scanner.constructNFA().toString());

        System.out.println("Show the DFA:");
        //test constructing the DFA
        RDFA nfa = scanner.constructDFA(scanner.constructNFA());
        System.out.println(nfa.toString());
//        HashMap<State, HashMap<Integer, State>> a = nfa.getStateMappingBetweenDFAAndNFA();
//        System.out.println(a);
        System.out.println(nfa.StateMappingBetweenDFAAndNFAToString());


        System.out.println("Show the miniDFA:");
//        test minimizing the DFA
        State.STATE_ID = 0;
        System.out.println(scanner.minimizeDFA(scanner.constructDFA(scanner.constructNFA())).toString());

    }
}