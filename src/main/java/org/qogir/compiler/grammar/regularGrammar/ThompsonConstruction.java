package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.State;

import java.util.ArrayDeque;
import java.util.Stack;

public class ThompsonConstruction {


    public TNFA translate(RegexTreeNode node) {

        if (node == null)
            return null;

        TNFA tnfa = new TNFA();
        //Add your implementation
        Stack<TNFA> stack = new Stack<>();


        return tnfa;
    }

    //新增方法 后序遍历
//    public String postTraversal(RegexTreeNode node){
//        Stack<RegexTreeNode> stack = new Stack<>();
////        if(node=='-'||node=='*'||node =='-'||node)
//        while(node.getFirstChild()!=null){
//            if(Character.isLetter(node.getValue())){
////            RegexTreeNode node.getFirstChild();
//                stack.push(node);
//        }

    //新增方法 后序遍历
    public String postTra(RegexTreeNode node){
        return node.postTraversal(node).toString();
    }

}
