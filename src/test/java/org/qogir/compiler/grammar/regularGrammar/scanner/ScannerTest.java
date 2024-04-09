package org.qogir.compiler.grammar.regularGrammar.scanner;

import org.qogir.compiler.grammar.regularGrammar.*;
import org.qogir.simulation.scanner.Scanner;

import java.io.PipedOutputStream;

public class ScannerTest {
    public static void main(String[] args) {

//        String[] regexes = new String[]{"regex0 := a|ε","regex1 := c(a|b)*"}; //{"regex1 := c(a|b)*"};//{"regex0 := a|ε","regex1 := c(a|b)*"};//"regex1 := c(a|b)*","regex2 := d(f|ea*(g|h))b","c(a|b)*","a|b", "ab*", "d(f|e)","d(f|ea*(g|h))b","c(a|b)*"
//
//        //test defining a regular grammar
//        RegularGrammar rg = new RegularGrammar(regexes);
//
//        System.out.println(rg);
//        //test building a grammar for the grammar
//        Scanner scanner = new Scanner(rg);
//
//        //test constructing the regex tree
//        System.out.println(scanner.constructRegexTrees().toString());
//
//        //test constructing the post traversal result
//        Regex regex1 = new Regex("regex2", "c(a|b)*", 1);
//        ParseRegex pr = new ParseRegex(regex1);
//        RegexTree rt = pr.parse();
//        RegexTreeNode rtn = rt.getRoot();
//        String str = rtn.postTraversal(rtn).toString();
//        System.out.println(str);

        //test constructing the NFA
        String[] regexes2 = new String[]{"regex0 := a|b","regex1 := cd"};
        RegularGrammar rg2 = new RegularGrammar(regexes2);
        System.out.println(rg2);
        Scanner scanner2 = new Scanner(rg2);
        System.out.println(scanner2.constructRegexTrees().toString());
        System.out.println(scanner2.constructNFA().toString());

//        System.out.println("Show the NFA:");
////        test constructing the NFA
//        System.out.println(scanner.constructNFA().toString());

        //System.out.println("Show the DFA:");
        //test constructing the DFA

        //System.out.println(scanner.constructDFA(scanner.constructNFA()).toString());
        //System.out.println("Show the miniDFA:");
        //test minimizing the DFA
        //State.STATE_ID = 0;
        //System.out.println(scanner.minimizeDFA(scanner.constructDFA(scanner.constructNFA())).toString());

    }
}