package yrs.yvhn;

import java.io.IOException;
import java.lang.*;
import TeacherLiba.*;

public class Main {

    public static void main(String[] args) {

        MyLangExt testLang = new MyLangExt("Examples/Pascal/PASCAL_GR.TXT", 1);

        //Виведення символів
        testLang.printTerminals();
        testLang.printNonterminals();

        //Пошук несуттєвих символів
        testLang.createNonProdRools();
        testLang.createNonDosNeterminals();

        //Робота з епсілом-нетерміналами
        int[] epsilon = testLang.createEpsilonNonterminals();
        testLang.setEpsilonNonterminals(epsilon);

        //FirstK
        LlkContext[] firstContext = testLang.firstK();
        testLang.setFirstK(firstContext);

        //FollowK
        LlkContext[] followContext = testLang.followK();
        testLang.setFollowK(followContext);

        testLang.firstFollowK();

        testLang.strongLlkCondition();

        int[] uprTable = testLang.createUprTable();
        testLang.setUprTable(uprTable);

        testLang.printGramma();

        try {
            LabClass labClass = new LabClass(testLang, "pascex.txt");
            if (labClass.ParseRec())
                System.out.println("Програма синтаксично правильна");
            else
                System.out.println("Програмо не синтаксично правильна");

        } catch (Exception exception) {
            exception.getStackTrace();
        }
    }

}

