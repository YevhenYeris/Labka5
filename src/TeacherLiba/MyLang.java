package TeacherLiba;
//
// Decompiled by Procyon v0.5.36
//



import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyLang
{
    private int axioma;
    private boolean create;
    private int LLK;
    protected LinkedList<Node> language;
    private LinkedList<TableNode> lexemaTable;
    protected int[] terminals;
    protected int[] nonterminals;
    private int[] epsilonNerminals;
    private LlkContext[] termLanguarge;
    protected LlkContext[] firstK;
    protected LlkContext[] followK;
    private LinkedList<LlkContext>[] LocalkContext;
    private int[] uprTable;

    public MyLang(final String fileLang, final int llk1) {
        this.LLK = llk1;
        this.create = false;
        this.language = new LinkedList<Node>();
        this.lexemaTable = new LinkedList<TableNode>();
        this.readGrammat(fileLang);
        final Iterator i$ = this.language.iterator();
        if (i$.hasNext()) {
            final Node tmp = (Node) i$.next();
            final int[] ii = tmp.getRoole();
            this.axioma = ii[0];
        }
        this.terminals = this.createTerminals();
        this.nonterminals = this.createNonterminals();
        this.termLanguarge = this.createTerminalLang();
    }

    public boolean isCreate() {
        return this.create;
    }

    public void setLocalkContext(final LinkedList<LlkContext>[] localK) {
        this.LocalkContext = localK;
    }

    public LinkedList<LlkContext>[] getLocalkContext() {
        return this.LocalkContext;
    }

    public void printUprTable() {
        final int[] trm = this.getTerminals();
        final int[] nontrm = this.getNonTerminals();
        for (int ii = 0, iiLen = nontrm.length; ii < iiLen; ++ii) {
            System.out.println(this.getLexemaText(nontrm[ii]));
            for (int jj = 0, jjLen = trm.length; jj < jjLen + 1; ++jj) {
                final int index = (trm.length + 1) * ii + jj;
                if (this.uprTable[index] > 0) {
                    if (jj == jjLen) {
                        System.out.println("\t\u041f\u0440\u0430\u0432\u0438\u043b\u043e " + this.uprTable[index] + "\tEps");
                    }
                    else {
                        System.out.println("\t\u041f\u0440\u0430\u0432\u0438\u043b\u043e " + this.uprTable[index] + "\t" + this.getLexemaText(trm[jj]));
                    }
                }
            }
        }
    }

    public void lefrRecusionTrace() {
        LinkedList<Node> lang = this.getLanguarge();
        int[] term = this.getTerminals();
        int[] epsilon = this.getEpsilonNonterminals();
        for (Node tmp : lang) {
            if (tmp.getRoole().length == 1) {
                tmp.setTeg(1);
                continue;
            }
            if (tmp.getRoole()[1] > 0) {
                tmp.setTeg(1);
                continue;
            }
            tmp.setTeg(0);
        }
        block1: for (Node tmp : lang) {
            if (tmp.getTeg() == 1) continue;
            int[] role = tmp.getRoole();
            for (int ii = 1; ii < role.length && role[11] <= 0; ++ii) {
                int ii1;
                //  TmpList tree = new TmpList(null, tmp, ii);
                //tree.searchLeftRecursion(this);
                for (ii1 = 0; ii1 < epsilon.length && epsilon[ii1] != role[ii]; ++ii1) {
                }
                if (ii1 == epsilon.length) continue block1;
            }
        }
    }

    public LinkedList<LlkContext>[] createLocalK() {
        LinkedList<LlkContext>[] localK = null;
        final LlkContext[] termLang = this.getLlkTrmContext();
        final LlkContext[] first = this.getFirstK();
        final int[] term = this.getTerminals();
        final int[] nonterm = this.getNonTerminals();
        localK = (LinkedList<LlkContext>[])new LinkedList[nonterm.length];
        final LlkContext[] tmpLlk = new LlkContext[40];
        final int[] mult = new int[40];
        final int[] maxmult = new int[40];
        for (int ii = 0; ii < nonterm.length; ++ii) {
            localK[ii] = null;
        }
        int ii;
        for (ii = 0; ii < nonterm.length && nonterm[ii] != this.axioma; ++ii) {}
        LlkContext rezult = new LlkContext();
        rezult.addWord(new int[0]);
        (localK[ii] = new LinkedList<LlkContext>()).add(rezult);
        int iter = 0;
        boolean count;
        do {
            ++iter;
            System.out.println("\u041f\u043e\u0431\u0443\u0434\u043e\u0432\u0430 \u043c\u043d\u043e\u0436\u0438\u043d\u0438 LocalK: \u0456\u0442\u0435\u0440\u0430\u0446\u0456\u044f " + iter);
            count = false;
            for (ii = 0; ii < nonterm.length; ++ii) {
                int nomrole = 0;
                for (final Node tmp : this.language) {
                    ++nomrole;
                    int ii2;
                    for (ii2 = 0; ii2 < nonterm.length && nonterm[ii2] != tmp.getRoole()[0]; ++ii2) {}
                    if (localK[ii2] == null) {
                        continue;
                    }
                    final int indexLeft = ii2;
                    int[] role;
                    int multcount;
                    int j;
                    int j2;
                    Iterator i$2;
                    LlkContext tmp2;
                    int realCalc;
                    int minLength;
                    int[] llkWord;
                    for (role = tmp.getRoole(), ii2 = 1; ii2 < role.length && role[ii2] != nonterm[ii]; ++ii2) {
                        if (ii2 != role.length) {
                            multcount = 0;
                            for (j = ii2 + 1; j < role.length; ++j) {
                                if (role[j] >= 0) {
                                    for (j2 = 0; j2 < term.length && term[j2] != role[j]; ++j2) {}
                                    tmpLlk[multcount++] = termLang[j2];
                                }
                                else {
                                    for (j2 = 0; j2 < nonterm.length && nonterm[j2] != role[j]; ++j2) {}
                                    tmpLlk[multcount++] = first[j2];
                                }
                            }
                            System.out.println("\u041f\u0440\u0430\u0432\u0438\u043b\u043e " + nomrole + "\u043f\u043e\u0437\u0438\u0446\u0456\u044f " + ii2);
                            i$2 = localK[indexLeft].iterator();
                            while (i$2.hasNext()) {
                                tmp2 = (LlkContext) i$2.next();
                                tmpLlk[multcount] = tmp2;
                                for (j2 = 0; j2 < multcount + 1; ++j2) {
                                    mult[j2] = 0;
                                    maxmult[j2] = tmpLlk[j2].calcWords();
                                }
                                realCalc = 0;
                                for (j2 = 0; j2 < multcount + 1; ++j2) {
                                    if (j2 == 0) {
                                        realCalc = tmpLlk[j2].minLengthWord();
                                    }
                                    else {
                                        minLength = tmpLlk[j2].minLengthWord();
                                        if (realCalc >= this.getLlkConst()) {
                                            break;
                                        }
                                        realCalc += minLength;
                                    }
                                }
                                realCalc = j2;
                                rezult = new LlkContext();
                                do {
                                    llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                                    rezult.addWord(llkWord);
                                } while (this.newCalcIndex(mult, maxmult, realCalc));
                                if (localK[ii] == null) {
                                    (localK[ii] = new LinkedList<LlkContext>()).add(rezult);
                                    count = true;
                                }
                                else {
                                    if (this.belongToLlkContext(localK[ii], rezult)) {
                                        continue;
                                    }
                                    localK[ii].add(rezult);
                                    count = true;
                                }
                            }
                        }
                    }
                }
            }
        } while (count);
        return localK;
    }

    public void printLocalk() {
        final LinkedList<LlkContext>[] localK = this.getLocalkContext();
        final int[] term = this.getTerminals();
        final int[] nonterm = this.getNonTerminals();
        for (int ii = 0; ii < nonterm.length; ++ii) {
            System.out.println("\u041a\u043e\u043d\u0442\u0435\u043a\u0441\u0442 Local \u0434\u043b\u044f \u043d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b\u0430 " + this.getLexemaText(nonterm[ii]));
            for (final LlkContext tmp : localK[ii]) {
                final int count = tmp.calcWords();
                System.out.println("{ ");
                for (int ii2 = 0; ii2 < tmp.calcWords(); ++ii2) {
                    final int[] word = tmp.getWord(ii2);
                    if (word.length == 0) {
                        System.out.print("\u0415-\u0441\u043b\u043e\u0432\u043e");
                    }
                    else {
                        for (int ii3 = 0; ii3 < word.length; ++ii3) {
                            System.out.print(this.getLexemaText(word[ii3]) + " ");
                        }
                    }
                    System.out.println();
                }
                System.out.println("} ");
            }
        }
    }

    public boolean llkCondition() {
        boolean upr = true;
        int count = 0;
        final LinkedList<LlkContext>[] localK = this.getLocalkContext();
        final LlkContext[] firstTerm = this.getLlkTrmContext();
        final LlkContext[] firstNonterm = this.getFirstK();
        final int[] term = this.getTerminals();
        final int[] nonterm = this.getNonTerminals();
        final LlkContext[] tmpLlk = new LlkContext[40];
        final int[] mult = new int[40];
        final int[] maxmult = new int[40];
        for (final Node tmp : this.getLanguarge()) {
            ++count;
            final int[] role = tmp.getRoole();
            int count2 = 0;
            for (final Node tmp2 : this.getLanguarge()) {
                ++count2;
                if (tmp == tmp2) {
                    break;
                }
                final int[] role2 = tmp2.getRoole();
                if (role[0] != role2[0]) {
                    continue;
                }
                int ii;
                for (ii = 0; ii < nonterm.length && role[0] != nonterm[ii]; ++ii) {}
                for (final LlkContext loctmp : localK[ii]) {
                    int counMult = 0;
                    for (int j1 = 1; j1 < role.length; ++j1) {
                        if (role[j1] > 0) {
                            int j2;
                            for (j2 = 0; j2 < term.length && term[j2] != role[j1]; ++j2) {}
                            tmpLlk[counMult++] = firstTerm[j2];
                        }
                        else {
                            int j2;
                            for (j2 = 0; j2 < nonterm.length && nonterm[j2] != role[j1]; ++j2) {}
                            tmpLlk[counMult++] = firstNonterm[j2];
                        }
                    }
                    tmpLlk[counMult++] = loctmp;
                    for (int j1 = 0; j1 < counMult; ++j1) {
                        mult[j1] = 0;
                        maxmult[j1] = tmpLlk[j1].calcWords();
                    }
                    int realCalc = 0;
                    int j1;
                    for (j1 = 0; j1 < counMult; ++j1) {
                        if (j1 == 0) {
                            realCalc = tmpLlk[j1].minLengthWord();
                        }
                        else {
                            final int minLength = tmpLlk[j1].minLengthWord();
                            if (realCalc >= this.getLlkConst()) {
                                break;
                            }
                            realCalc += minLength;
                        }
                    }
                    realCalc = j1;
                    final LlkContext result = new LlkContext();
                    do {
                        final int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                        result.addWord(llkWord);
                    } while (this.newCalcIndex(mult, maxmult, realCalc));
                    counMult = 0;
                    for (j1 = 1; j1 < role2.length; ++j1) {
                        if (role2[j1] > 0) {
                            int j2;
                            for (j2 = 0; j2 < term.length && term[j2] != role2[j1]; ++j2) {}
                            tmpLlk[counMult++] = firstTerm[j2];
                        }
                        else {
                            int j2;
                            for (j2 = 0; j2 < nonterm.length && nonterm[j2] != role2[j1]; ++j2) {}
                            tmpLlk[counMult++] = firstNonterm[j2];
                        }
                    }
                    tmpLlk[counMult++] = loctmp;
                    for (j1 = 0; j1 < counMult; ++j1) {
                        mult[j1] = 0;
                        maxmult[j1] = tmpLlk[j1].calcWords();
                    }
                    realCalc = 0;
                    for (j1 = 0; j1 < counMult; ++j1) {
                        if (j1 == 0) {
                            realCalc = tmpLlk[j1].minLengthWord();
                        }
                        else {
                            final int minLength = tmpLlk[j1].minLengthWord();
                            if (realCalc >= this.getLlkConst()) {
                                break;
                            }
                            realCalc += minLength;
                        }
                    }
                    realCalc = j1;
                    final LlkContext result2 = new LlkContext();
                    do {
                        final int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                        result2.addWord(llkWord);
                    } while (this.newCalcIndex(mult, maxmult, realCalc));
                    for (j1 = 0; j1 < result.calcWords(); ++j1) {
                        if (result2.wordInContext(result.getWord(j1))) {
                            System.out.println("\u041f\u0430\u0440\u0430 \u043f\u0440\u0430\u0432\u0438\u043b (" + count + "," + count2 + ") \u043d\u0435 \u0437\u0430\u0434\u043e\u0432\u044b\u043b\u044c\u043d\u044f\u044e\u0442\u044c LL(" + this.getLlkConst() + ")- \u0443\u043c\u043e\u0432\u0456");
                            upr = false;
                            break;
                        }
                    }
                }
            }
        }
        if (upr) {
            System.out.println("\u0413\u0440\u0430\u043c\u0430\u0442\u0438\u043a\u0430 \u0437\u0430\u0434\u043e\u0432\u043e\u043b\u044c\u043d\u044f\u044e\u0454 LL(" + this.getLlkConst() + ")- \u0443\u043c\u043e\u0432\u0456");
        }
        return upr;
    }

    private boolean belongToLlkContext(final LinkedList<LlkContext> list, final LlkContext llk) {
        final int llkCount = llk.calcWords();
        for (final LlkContext tmp : list) {
            if (llkCount != tmp.calcWords()) {
                continue;
            }
            final int tmpCount = tmp.calcWords();
            int ii;
            for (ii = 0; ii < llkCount && tmp.wordInContext(llk.getWord(ii)); ++ii) {}
            if (ii == llkCount) {
                return true;
            }
        }
        return false;
    }

    public int indexNonterminal(final int ssItem) {
        for (int ii = 0; ii < this.nonterminals.length; ++ii) {
            if (ssItem == this.nonterminals[ii]) {
                return ii;
            }
        }
        return 0;
    }

    public int indexTerminal(final int ssItem) {
        for (int ii = 0; ii < this.terminals.length; ++ii) {
            if (ssItem == this.terminals[ii]) {
                return ii;
            }
        }
        return 0;
    }

    public int getLexemaCode(final byte[] lexema, final int lexemaLen, final int lexemaClass) {
        for (final TableNode tmp : this.lexemaTable) {
            final String ss = tmp.getLexemaText();
            if (ss.length() != lexemaLen) {
                continue;
            }
            int ii;
            for (ii = 0; ii < ss.length() && ss.charAt(ii) == (char)lexema[ii]; ++ii) {}
            if (ii == ss.length()) {
                return tmp.getLexemaCode();
            }
        }
        return -1;
    }

    public void printTerminals() {
        System.out.println("\u0421\u041f\u0418\u0421\u041e\u041a \u0422\u0415\u0420\u041c\u0406\u041d\u0410\u041b\u0406\u0412:");
        if (this.terminals == null) {
            return;
        }
        for (int ii = 0; ii < this.terminals.length; ++ii) {
            System.out.println("" + (ii + 1) + "  " + this.terminals[ii] + "\t " + this.getLexemaText(this.terminals[ii]));
        }
    }

    public void printNonterminals() {
        System.out.println("\u0421\u041f\u0418\u0421\u041e\u041a \u041d\u0415\u0422\u0415\u0420\u041c\u0406\u041d\u0410\u041b\u0406\u0412:");
        for (int ii = 0; ii < this.nonterminals.length; ++ii) {
            System.out.println("" + (ii + 1) + "  " + this.nonterminals[ii] + "\t " + this.getLexemaText(this.nonterminals[ii]));
        }
    }

    public int[] getUprTable() {
        return this.uprTable;
    }

    public void setUprTable(final int[] upr) {
        this.uprTable = upr;
    }

    public LlkContext[] getFirstK() {
        return this.firstK;
    }

    public void setFirstK(final LlkContext[] first) {
        this.firstK = first;
    }

    public LlkContext[] getFollowK() {
        return this.followK;
    }

    public void setFollowK(final LlkContext[] follow) {
        this.followK = follow;
    }

    public void prpintRoole(final Node nod) {
        final int[] role = nod.getRoole();
        for (int ii = 0; ii < role.length; ++ii) {
            if (ii == 0) {
                System.out.print(this.getLexemaText(role[ii]) + " -> ");
            }
            else {
                System.out.print(" " + this.getLexemaText(role[ii]));
            }
        }
        System.out.println();
    }

    public boolean createNonProdRools() {
        if (this.getNonTerminals().length == 0) {
            return true;
        }
        final int[] prodtmp = new int[this.getNonTerminals().length];
        int count = 0;
        for (final Node tmp : this.language) {
            tmp.setTeg(0);
        }
        int upr;
        do {
            upr = 0;
            for (final Node tmp : this.language) {
                final int[] rool1 = tmp.getRoole();
                if (tmp.getTeg() == 1) {
                    continue;
                }
                int ii;
                for (ii = 1; ii < rool1.length; ++ii) {
                    if (rool1[ii] <= 0) {
                        int ii2;
                        for (ii2 = 0; ii2 < count && prodtmp[ii2] != rool1[ii]; ++ii2) {}
                        if (ii2 == count) {
                            break;
                        }
                    }
                }
                if (ii != rool1.length) {
                    continue;
                }
                int ii2;
                for (ii2 = 0; ii2 < count && prodtmp[ii2] != rool1[0]; ++ii2) {}
                if (ii2 == count) {
                    prodtmp[count++] = rool1[0];
                }
                tmp.setTeg(1);
                upr = 1;
            }
        } while (upr == 1);
        if (count == prodtmp.length) {
            System.out.print("\u0412 \u0433\u0440\u0430\u043c\u0430\u0442\u0438\u0446\u0456 \u043d\u0435\u043f\u0440\u043e\u0434\u0443\u043a\u0442\u0438\u0432\u043d\u0456 \u043f\u0440\u0430\u0432\u0438\u043b\u0430 \u0432\u0456\u0434\u0441\u0443\u0442\u043d\u0456\n");
            return true;
        }
        System.out.print("\u041d\u0435\u043f\u0440\u043e\u0434\u0443\u043a\u0442\u0438\u0432\u043d\u0456 \u043f\u0440\u0430\u0432\u0438\u043b\u0430: \n");
        for (final Node tmp : this.language) {
            if (tmp.getTeg() == 1) {
                continue;
            }
            final int[] rool1 = tmp.getRoole();
            for (int ii = 0; ii < rool1.length; ++ii) {
                if (ii == 1) {
                    System.out.print(" ::= ");
                }
                System.out.print(this.getLexemaText(rool1[ii]) + " ");
                if (rool1.length == 1) {
                    System.out.print(" ::= ");
                }
            }
            System.out.println("");
        }
        return false;
    }

    public void Variant1Yevvtushenko(){
        final int[] nonDosNeterminals = getNonDosNeterminals();
        //get all unreachable terminals
        int count1 = 0;
        //counter for moving with iterator from example of MyLang - this.language
        for (final Node tmp : this.language) {
            boolean isReachable = true;
            //flag for rule if it starts from unreachable. False if yes, true otherwise
            final int[] roole = tmp.getRoole();
            //array of codes of lexems in rule. Like in rule #xxxx# goes into #yyyy#....#zzzzz#. roole[0] is #xxxx#, roole[1]...roole[length-1] are #yyyy#....#zzzzz#
            for (int i =0; i <  nonDosNeterminals.length; i++){
                if (roole[0] == nonDosNeterminals[i])
                    // roole[0] is first non-terminal in the rule
                    // so we check if it is in list of unereachable, that's ok
                    isReachable = false;
            }
            if (isReachable){
                // else skip all that rule
                continue;
            }
            ++count1;
            // print all that unreachable rules
            System.out.print("" + count1);
            for (int ii = 0; ii < roole.length; ++ii) {
                //iterating over that single node (over each lexem in rule)
                if (ii == 1) {
                    // if it's second lexem, then print the ::= symbol, which means "->" in TP subject
                    System.out.print(" ::= ");
                }
                for (int i =0; i <  nonDosNeterminals.length; i++){
                    if (roole[ii] == nonDosNeterminals[i])
                        //print this if we're starting from non-reachable non-termina;
                        System.out.print("(non-reachable)");
                }
                System.out.print(this.getLexemaText(roole[ii]) + " ");
                //get lexema from code and print it. Lexema is like #yyyy#, or #zzzz# or some terminal symbol
                if (roole.length == 1) {
                    System.out.print(" ::= ");
                }
            }
            System.out.println("");
        }
        // print all non-reachable non-terminals
        for (int i = 0; i < nonDosNeterminals.length; ++i) {
            System.out.println("non-reachable non-terminal: " + this.getLexemaText(nonDosNeterminals[i]) + "\n ");
        }
        return;
    }

    public boolean createNonDosNeterminals() {
        final int[] nonTerminals = this.getNonTerminals();
        final int[] dosnerminals = new int[nonTerminals.length];
        int count = 0;
        final int iter = 0;
        if (nonTerminals == null) {
            return true;
        }
        Iterator i$ = this.language.iterator();
        if (i$.hasNext()) {
            final Node tmp = (Node) i$.next();
            dosnerminals[0] = tmp.getRoole()[0];
            count = 1;
        }
        int upr;
        do {
            upr = 0;
            i$ = this.language.iterator();
            while (i$.hasNext()) {
                final Node tmp = (Node) i$.next();
                int[] rool1;
                int ii;
                for (rool1 = tmp.getRoole(), ii = 0; ii < count && dosnerminals[ii] != rool1[0]; ++ii) {}
                if (ii == count) {
                    continue;
                }
                for (ii = 1; ii < rool1.length; ++ii) {
                    if (rool1[ii] < 0) {
                        int ii2;
                        for (ii2 = 0; ii2 < count && dosnerminals[ii2] != rool1[ii]; ++ii2) {}
                        if (ii2 == count) {
                            dosnerminals[count] = rool1[ii];
                            upr = 1;
                            ++count;
                        }
                    }
                }
            }
        } while (upr == 1);
        upr = nonTerminals.length - count;
        if (upr == 0) {
            System.out.println("\u0412 \u0433\u0440\u0430\u043c\u0430\u0442\u0438\u0446\u0456 \u043d\u0435\u0434\u043e\u0441\u044f\u0436\u043d\u0438\u0445 \u043d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b\u0456\u0432 \u043d\u0435\u043c\u0430\u0454");
            return true;
        }
        final int[] nonDosNeterminals = new int[upr];
        upr = 0;
        for (int ii = 0; ii < nonTerminals.length; ++ii) {
            int ii2;
            for (ii2 = 0; ii2 < count && nonTerminals[ii] != dosnerminals[ii2]; ++ii2) {}
            if (ii2 == count) {
                nonDosNeterminals[upr++] = nonTerminals[ii];
            }
        }
        for (int ii = 0; ii < nonDosNeterminals.length; ++ii) {
            System.out.println("\u041d\u0435\u0434\u043e\u0441\u044f\u0436\u043d\u0438\u0439 \u043d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b: " + this.getLexemaText(nonDosNeterminals[ii]) + "\n ");
        }
        return false;
    }

    public int[] getNonDosNeterminals() {
        final int[] nonTerminals = this.getNonTerminals();
        final int[] dosnerminals = new int[nonTerminals.length];
        int count = 0;
        final int iter = 0;
        if (nonTerminals == null) {
            return null;
        }
        Iterator i$ = this.language.iterator();
        if (i$.hasNext()) {
            final Node tmp = (Node) i$.next();
            dosnerminals[0] = tmp.getRoole()[0];
            count = 1;
        }
        int upr;
        do {
            upr = 0;
            i$ = this.language.iterator();
            while (i$.hasNext()) {
                final Node tmp = (Node) i$.next();
                int[] rool1;
                int i;
                for (rool1 = tmp.getRoole(), i = 0; i < count && dosnerminals[i] != rool1[0]; ++i) {}
                if (i == count) {
                    continue;
                }
                for (i = 1; i < rool1.length; ++i) {
                    if (rool1[i] < 0) {
                        int j;
                        for (j = 0; j < count && dosnerminals[j] != rool1[i]; ++j) {}
                        if (j == count) {
                            dosnerminals[count] = rool1[i];
                            upr = 1;
                            ++count;
                        }
                    }
                }
            }
        } while (upr == 1);
        upr = nonTerminals.length - count;
        if (upr == 0) {
            System.out.println("No unreachable terminals");
            return null;
        }
        final int[] nonDosNeterminals = new int[upr];
        upr = 0;
        for (int i = 0; i < nonTerminals.length; ++i) {
            int j;
            for (j = 0; j < count && nonTerminals[i] != dosnerminals[j]; ++j) {}
            if (j == count) {
                nonDosNeterminals[upr++] = nonTerminals[i];
            }
        }
        return nonDosNeterminals;
    }

    public boolean leftRecursNonnerminal() {
        final int[] controlSet = new int[this.getNonTerminals().length];
        final int[] nontrm = this.getNonTerminals();
        final int[] eps = this.getEpsilonNonterminals();
        int upr = 0;
        int upr2 = 0;
        for (int ii = 0; ii < nontrm.length; ++ii) {
            int count = 0;
            int count2 = 1;
            upr = 0;
            controlSet[count] = nontrm[ii];
            System.out.print("\u0422\u0435\u0441\u0442\u0443\u0454\u043c\u043e: " + this.getLexemaText(controlSet[0]) + "\n");
            do {
                for (final Node tmp : this.language) {
                    final int[] rool1 = tmp.getRoole();
                    if (rool1[0] != controlSet[count]) {
                        continue;
                    }
                    int ii2;
                    for (ii2 = 1; ii2 < rool1.length; ++ii2) {
                        if (rool1[ii2] > 0) {
                            break;
                        }
                        if (rool1[ii2] == controlSet[0]) {
                            break;
                        }
                        int ii3;
                        for (ii3 = 0; ii3 < count2 && rool1[ii2] != controlSet[ii3]; ++ii3) {}
                        if (ii3 == count2) {
                            controlSet[count2++] = rool1[ii2];
                        }
                        if (eps == null) {
                            break;
                        }
                        for (ii3 = 0; ii3 < eps.length && rool1[ii2] != eps[ii3]; ++ii3) {}
                        if (ii3 == eps.length) {
                            break;
                        }
                    }
                    if (ii2 == rool1.length) {
                        continue;
                    }
                    if (rool1[ii2] == controlSet[0]) {
                        System.out.print("\u041d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b: " + this.getLexemaText(controlSet[0]) + " \u043b\u0456\u0432\u043e\u0440\u0435\u043a\u0443\u0440\u0441\u0438\u0432\u043d\u0438\u0439 \n");
                        upr = 1;
                        upr2 = 1;
                        break;
                    }
                }
                if (upr == 1) {
                    break;
                }
            } while (++count < count2);
        }
        if (upr2 == 0) {
            System.out.print("\u0412 \u0433\u0440\u0430\u043c\u0430\u0442\u0438\u0446\u0456 \u0432\u0456\u0434\u0441\u0443\u0442\u043d\u0456 \u043b\u0456\u0432\u043e\u0440\u0435\u043a\u0443\u0440\u0441\u0438\u0432\u043d\u0456 \u043d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b\u0438 \n");
            return false;
        }
        return true;
    }

    public boolean rightRecursNonnerminal() {
        final int[] controlSet = new int[this.getNonTerminals().length];
        final int[] nontrm = this.getNonTerminals();
        final int[] eps = this.getEpsilonNonterminals();
        int upr = 0;
        int upr2 = 0;
        for (int ii = 0; ii < nontrm.length; ++ii) {
            int count = 0;
            int count2 = 1;
            upr = 0;
            controlSet[count] = nontrm[ii];
            do {
                for (final Node tmp : this.language) {
                    final int[] rool1 = tmp.getRoole();
                    if (rool1[0] != controlSet[count]) {
                        continue;
                    }
                    int ii2;
                    for (ii2 = rool1.length - 1; ii2 > 0; --ii2) {
                        if (rool1[ii2] > 0) {
                            break;
                        }
                        if (rool1[ii2] == controlSet[0]) {
                            break;
                        }
                        int ii3;
                        for (ii3 = 0; ii3 < count2 && rool1[ii2] != controlSet[ii3]; ++ii3) {}
                        if (ii3 == count2) {
                            controlSet[count2++] = rool1[ii2];
                        }
                        if (eps == null) {
                            break;
                        }
                        for (ii3 = 0; ii3 < eps.length && rool1[ii2] != eps[ii3]; ++ii3) {}
                        if (ii3 == eps.length) {
                            break;
                        }
                    }
                    if (ii2 == 0) {
                        continue;
                    }
                    if (rool1[ii2] == controlSet[0]) {
                        System.out.print("\u041d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b: " + this.getLexemaText(controlSet[0]) + " \u043f\u0440\u0430\u0432\u043e\u0440\u0435\u043a\u0443\u0440\u0441\u0438\u0432\u043d\u0438\u0439 \n");
                        upr = 1;
                        upr2 = 1;
                        break;
                    }
                }
                if (upr == 1) {
                    break;
                }
            } while (++count < count2);
        }
        if (upr2 == 0) {
            System.out.print("\u0412 \u0433\u0440\u0430\u043c\u0430\u0442\u0438\u0446\u0456 \u0432\u0456\u0434\u0441\u0443\u0442\u043d\u0456 \u043f\u0440\u0430\u0432\u043e\u0440\u0435\u043a\u0443\u0440\u0441\u0438\u0432\u043d\u0456 \u043d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b\u0438 \n");
            return false;
        }
        return true;
    }

    public LlkContext[] firstK() {
        int[] llkWord = new int[10];
        boolean upr = false;
        final int[] term = this.getTerminals();
        final int[] nonterm = this.getNonTerminals();
        final LlkContext[] llkTrmContext = this.getLlkTrmContext();
        final LlkContext[] rezult = new LlkContext[nonterm.length];
        final LlkContext[] tmpLlk = new LlkContext[40];
        final int[] mult = new int[40];
        final int[] maxmult = new int[40];
        int iter = 0;
        System.out.println("LLK-\u043a\u043e\u043d\u0442\u0435\u043a\u0441\u0442 \u0440\u0456\u0432\u043d\u0439 " + this.getLlkConst());
        for (int ii = 0; ii < rezult.length; ++ii) {
            rezult[ii] = new LlkContext();
        }
        do {
            upr = false;
            final int prav = 0;
            System.out.println("\u0406\u0442\u0435\u0440\u0430\u0446\u0456\u044f \u043f\u043e\u0448\u0443\u043a\u0443 \u043c\u043d\u043e\u0436\u0438\u043d\u0438 FirstK " + ++iter);
            for (final Node tmp : this.language) {
                int ii;
                int[] tmprole;
                for (tmprole = tmp.getRoole(), ii = 0; ii < nonterm.length && tmprole[0] != nonterm[ii]; ++ii) {}
                if (tmprole.length == 1) {
                    if (!rezult[ii].addWord(new int[0])) {
                        continue;
                    }
                    upr = true;
                }
                else {
                    int ii2;
                    for (ii2 = 1; ii2 < tmprole.length; ++ii2) {
                        if (tmprole[ii2] > 0) {
                            int ii3;
                            for (ii3 = 0; ii3 < term.length && term[ii3] != tmprole[ii2]; ++ii3) {}
                            tmpLlk[ii2 - 1] = llkTrmContext[ii3];
                        }
                        else {
                            int ii3;
                            for (ii3 = 0; ii3 < nonterm.length && nonterm[ii3] != tmprole[ii2]; ++ii3) {}
                            if (rezult[ii3].calcWords() == 0) {
                                break;
                            }
                            tmpLlk[ii2 - 1] = rezult[ii3];
                        }
                    }
                    if (ii2 != tmprole.length) {
                        continue;
                    }
                    int multCount;
                    int ii3;
                    for (multCount = tmprole.length - 1, ii3 = 0; ii3 < multCount; ++ii3) {
                        mult[ii3] = 0;
                        maxmult[ii3] = tmpLlk[ii3].calcWords();
                    }
                    int realCalc = 0;
                    for (ii3 = 0; ii3 < multCount; ++ii3) {
                        if (ii3 == 0) {
                            realCalc = tmpLlk[ii3].minLengthWord();
                        }
                        else {
                            final int minLength = tmpLlk[ii3].minLengthWord();
                            if (realCalc >= this.getLlkConst()) {
                                break;
                            }
                            realCalc += minLength;
                        }
                    }
                    realCalc = ii3;
                    do {
                        llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                        if (rezult[ii].addWord(llkWord)) {
                            upr = true;
                        }
                    } while (this.newCalcIndex(mult, maxmult, realCalc));
                }
            }
        } while (upr);
        System.out.println("\u041a\u0456\u043d\u0435\u0446\u044c \u043f\u043e\u0448\u0443\u043a\u0443 \u043c\u043d\u043e\u0436\u0438\u043d FIRSTk");
        return rezult;
    }

    public LlkContext[] followK() {
        final LinkedList<Node> lan = this.getLanguarge();
        final LlkContext[] firstK = this.getFirstK();
        final int[] term = this.getTerminals();
        final int[] nonterm = this.getNonTerminals();
        final LlkContext[] llkTrmContext = this.getLlkTrmContext();
        final LlkContext[] rezult = new LlkContext[nonterm.length];
        final LlkContext[] tmpLlk = new LlkContext[40];
        final int[] mult = new int[40];
        final int[] maxmult = new int[40];
        int iter = 0;
        for (int ii = 0; ii < rezult.length; ++ii) {
            rezult[ii] = new LlkContext();
        }
        int ii;
        int aaxioma;
        for (aaxioma = this.getAxioma(), ii = 0; ii < nonterm.length && nonterm[ii] != aaxioma; ++ii) {}
        rezult[ii].addWord(new int[0]);
        boolean upr;
        do {
            upr = false;
            final int prav = 0;
            System.out.println("\u0406\u0442\u0435\u0440\u0430\u0446\u0456\u044f \u043f\u043e\u0431\u0443\u0434\u043e\u0432\u043e \u043c\u043d\u043e\u0436\u0438\u043d\u0438 FollowK " + ++iter);
            for (final Node tmp : lan) {
                int[] tmprole;
                for (tmprole = tmp.getRoole(), ii = 0; ii < nonterm.length && tmprole[0] != nonterm[ii]; ++ii) {}
                if (ii == nonterm.length) {
                    System.out.println("\u041d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u0430 \u0441\u0438\u0442\u0443\u0430\u0446\u0456\u044f \u0434\u043b\u044f " + this.getLexemaText(tmprole[0]));
                    return null;
                }
                if (rezult[ii].calcWords() == 0) {
                    continue;
                }
                final int leftItem = ii;
                for (int jj = 1; jj < tmprole.length; ++jj) {
                    if (tmprole[jj] <= 0) {
                        int multCount = 0;
                        for (int ii2 = jj + 1; ii2 < tmprole.length; ++ii2) {
                            if (tmprole[ii2] > 0) {
                                int ii3;
                                for (ii3 = 0; ii3 < term.length && term[ii3] != tmprole[ii2]; ++ii3) {}
                                tmpLlk[multCount++] = llkTrmContext[ii3];
                            }
                            else {
                                int ii3;
                                for (ii3 = 0; ii3 < nonterm.length && nonterm[ii3] != tmprole[ii2]; ++ii3) {}
                                tmpLlk[multCount++] = firstK[ii3];
                            }
                        }
                        tmpLlk[multCount++] = rezult[leftItem];
                        for (int ii3 = 0; ii3 < multCount; ++ii3) {
                            mult[ii3] = 0;
                            maxmult[ii3] = tmpLlk[ii3].calcWords();
                        }
                        int realCalc = 0;
                        int ii3;
                        for (ii3 = 0; ii3 < multCount; ++ii3) {
                            if (ii3 == 0) {
                                realCalc = tmpLlk[ii3].minLengthWord();
                            }
                            else {
                                final int minLength = tmpLlk[ii3].minLengthWord();
                                if (realCalc >= this.getLlkConst()) {
                                    break;
                                }
                                realCalc += minLength;
                            }
                        }
                        realCalc = ii3;
                        for (ii3 = 0; ii3 < nonterm.length; ++ii3) {
                            if (nonterm[ii3] == tmprole[jj]) {
                                break;
                            }
                        }
                        do {
                            final int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                            if (rezult[ii3].addWord(llkWord)) {
                                upr = true;
                            }
                        } while (this.newCalcIndex(mult, maxmult, realCalc));
                    }
                }
            }
        } while (upr);
        System.out.println("\u041a\u0456\u043d\u0435\u0446\u044c \u043f\u043e\u0448\u0443\u043a\u0443 \u043c\u043d\u043e\u0436\u0438\u043d FollowK");
        return rezult;
    }

    public void firstKforRoole() {
        final int[] term = this.getTerminals();
        final int[] nonterm = this.getNonTerminals();
        final LlkContext[] llkTrmContext = this.getLlkTrmContext();
        final LlkContext[] tmpLlk = new LlkContext[40];
        final int[] mult = new int[40];
        final int[] maxmult = new int[40];
        int roleNumb = 0;
        for (final Node tmp : this.getLanguarge()) {
            ++roleNumb;
            System.out.println("\u041f\u043e\u0431\u0443\u0434\u043e\u0432\u0430 \u043c\u043d\u043e\u0436\u0438\u043d FirstK(w) \u0434\u043b\u044f \u043f\u0440\u0430\u0432\u043e\u0457 \u0447\u0430\u0441\u0442\u0438\u043d\u0438 \u043f\u0440\u0430\u0432\u0438\u043b\u0430 " + roleNumb);
            final int[] tmprole = tmp.getRoole();
            int multCount = 0;
            for (int ii = 1; ii < tmprole.length; ++ii) {
                if (tmprole[ii] > 0) {
                    int ii2;
                    for (ii2 = 0; ii2 < term.length && term[ii2] != tmprole[ii]; ++ii2) {}
                    tmpLlk[multCount] = llkTrmContext[ii2];
                }
                else {
                    int ii2;
                    for (ii2 = 0; ii2 < nonterm.length && nonterm[ii2] != tmprole[ii]; ++ii2) {}
                    tmpLlk[multCount] = this.firstK[ii2];
                }
                ++multCount;
            }
            LlkContext rezult;
            if (multCount == 0) {
                rezult = new LlkContext();
                rezult.addWord(new int[0]);
            }
            else {
                for (int ii2 = 0; ii2 < multCount; ++ii2) {
                    mult[ii2] = 0;
                    maxmult[ii2] = tmpLlk[ii2].calcWords();
                }
                int realCalc = 0;
                int ii2;
                for (ii2 = 0; ii2 < multCount; ++ii2) {
                    if (ii2 == 0) {
                        realCalc = tmpLlk[ii2].minLengthWord();
                    }
                    else {
                        final int minLength = tmpLlk[ii2].minLengthWord();
                        if (realCalc >= this.getLlkConst()) {
                            break;
                        }
                        realCalc += minLength;
                    }
                }
                realCalc = ii2;
                rezult = new LlkContext();
                do {
                    final int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                    rezult.addWord(llkWord);
                } while (this.newCalcIndex(mult, maxmult, realCalc));
            }
            tmp.addFirstKforRoole(rezult);
        }
        System.out.println("\u041c\u043d\u043e\u0436\u0438\u043d\u0438 FirstK(w)+ FollowK(A): \u0410->w \u043f\u043e\u0431\u0443\u0434\u043e\u0432\u0430\u043d\u0456 ");
    }

    public void firstFollowK() {
        final int[] term = this.getTerminals();
        final int[] nonterm = this.getNonTerminals();
        final LlkContext[] llkTrmContext = this.getLlkTrmContext();
        final LlkContext[] tmpLlk = new LlkContext[40];
        final int[] mult = new int[40];
        final int[] maxmult = new int[40];
        int roleNumb = 0;
        for (final Node tmp : this.getLanguarge()) {
            ++roleNumb;
            System.out.println("\u041f\u043e\u0431\u0443\u0434\u043e\u0432\u0430 \u043c\u043d\u043e\u0436\u0438\u043d FirstK(w)+ FollowK(A) \u0434\u043b\u044f \u043f\u0440\u0430\u0432\u0438\u043b\u0430 " + roleNumb);
            final int[] tmprole = tmp.getRoole();
            int multCount = 0;
            for (int ii = 1; ii < tmprole.length; ++ii) {
                if (tmprole[ii] > 0) {
                    int ii2;
                    for (ii2 = 0; ii2 < term.length && term[ii2] != tmprole[ii]; ++ii2) {}
                    tmpLlk[multCount] = llkTrmContext[ii2];
                }
                else {
                    int ii2;
                    for (ii2 = 0; ii2 < nonterm.length && nonterm[ii2] != tmprole[ii]; ++ii2) {}
                    tmpLlk[multCount] = this.firstK[ii2];
                }
                ++multCount;
            }
            int ii2;
            for (ii2 = 0; ii2 < nonterm.length && nonterm[ii2] != tmprole[0]; ++ii2) {}
            tmpLlk[multCount++] = this.followK[ii2];
            for (ii2 = 0; ii2 < multCount; ++ii2) {
                mult[ii2] = 0;
                maxmult[ii2] = tmpLlk[ii2].calcWords();
            }
            int realCalc = 0;
            for (ii2 = 0; ii2 < multCount; ++ii2) {
                if (ii2 == 0) {
                    realCalc = tmpLlk[ii2].minLengthWord();
                }
                else {
                    final int minLength = tmpLlk[ii2].minLengthWord();
                    if (realCalc >= this.getLlkConst()) {
                        break;
                    }
                    realCalc += minLength;
                }
            }
            realCalc = ii2;
            final LlkContext rezult = new LlkContext();
            do {
                final int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                rezult.addWord(llkWord);
            } while (this.newCalcIndex(mult, maxmult, realCalc));
            tmp.addFirstFollowK(rezult);
        }
        System.out.println("\u041c\u043d\u043e\u0436\u0438\u043d\u0438 FirstK(w)+ FollowK(A): \u0410->w \u043f\u043e\u0431\u0443\u0434\u043e\u0432\u0430\u043d\u0456 ");
    }

    public void printFirstkContext() {
        final int[] nonterm = this.getNonTerminals();
        final LlkContext[] firstContext = this.getFirstK();
        if (firstContext == null) {
            return;
        }
        for (int j = 0; j < nonterm.length; ++j) {
            System.out.println("FirstK-\u043a\u043e\u043d\u0442\u0435\u043a\u0441\u0442 \u0434\u043b\u044f \u043d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b\u0430: " + this.getLexemaText(nonterm[j]));
            final LlkContext tmp = firstContext[j];
            for (int ii = 0; ii < tmp.calcWords(); ++ii) {
                final int[] word = tmp.getWord(ii);
                if (word.length == 0) {
                    System.out.print("\u0415-\u0441\u043b\u043e\u0432\u043e");
                }
                else {
                    for (int ii2 = 0; ii2 < word.length; ++ii2) {
                        System.out.print(this.getLexemaText(word[ii2]) + " ");
                    }
                }
                System.out.println();
            }
        }
    }

    public void printFollowkContext() {
        final int[] nonterm = this.getNonTerminals();
        final LlkContext[] followContext = this.getFollowK();
        for (int j = 0; j < nonterm.length; ++j) {
            System.out.println("FollowK-\u043a\u043e\u043d\u0442\u0435\u043a\u0441\u0442 \u0434\u043b\u044f \u043d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b\u0430: " + this.getLexemaText(nonterm[j]));
            final LlkContext tmp = followContext[j];
            for (int ii = 0; ii < tmp.calcWords(); ++ii) {
                final int[] word = tmp.getWord(ii);
                if (word.length == 0) {
                    System.out.print("\u0415-\u0441\u043b\u043e\u0432\u043e");
                }
                else {
                    for (int ii2 = 0; ii2 < word.length; ++ii2) {
                        System.out.print(this.getLexemaText(word[ii2]) + " ");
                    }
                }
                System.out.println();
            }
        }
    }

    public void printFirstFollowK() {
        int count = 0;
        for (final Node tmp : this.getLanguarge()) {
            ++count;
            System.out.print("\u041f\u0440\u0430\u0432\u0438\u043b\u043e \u2116" + count + " ");
            this.prpintRoole(tmp);
            final LlkContext loc = tmp.getFirstFollowK();
            for (int ii1 = 0; ii1 < loc.calcWords(); ++ii1) {
                final int[] word = loc.getWord(ii1);
                if (word.length == 0) {
                    System.out.print("\u0415-\u0441\u043b\u043e\u0432\u043e");
                }
                else {
                    for (int ii2 = 0; ii2 < word.length; ++ii2) {
                        System.out.print(this.getLexemaText(word[ii2]) + " ");
                    }
                }
                System.out.println();
            }
        }
    }

    public void printFirstFollowForRoole() {
        int count = 0;
        final byte[] readline = new byte[80];
        int rooleNumber = 0;
        while (true) {
            System.out.println("\u0412\u0432\u0435\u0434\u0456\u0442\u044c \u043d\u043e\u043c\u0435\u0440 \u043f\u0440\u0430\u0432\u0438\u043b\u0430 \u0430\u0431\u043e end");
            try {
                final int textLen = System.in.read(readline);
                final String StrTmp = new String(readline, 0, textLen, "ISO-8859-1");
                if (StrTmp.trim().equals("end")) {
                    return;
                }
                rooleNumber = new Integer(StrTmp.trim());
            }
            catch (Exception ee) {
                System.out.println("\u041d\u0435\u0432\u0456\u0440\u043d\u0438\u0439 \u043a\u043e\u0434 \u0434\u0456\u0457, \u043f\u043e\u0432\u0442\u043e\u0440\u0456\u0442\u044c: ");
            }
            count = 0;
            for (final Node tmp : this.getLanguarge()) {
                if (++count != rooleNumber) {
                    continue;
                }
                this.prpintRoole(tmp);
                final LlkContext loc = tmp.getFirstFollowK();
                for (int ii1 = 0; ii1 < loc.calcWords(); ++ii1) {
                    final int[] word = loc.getWord(ii1);
                    if (word.length == 0) {
                        System.out.print("\u0415-\u0441\u043b\u043e\u0432\u043e");
                    }
                    else {
                        for (int ii2 = 0; ii2 < word.length; ++ii2) {
                            System.out.print(this.getLexemaText(word[ii2]) + " ");
                        }
                    }
                    System.out.println();
                }
                break;
            }
        }
    }

    public boolean strongLlkCondition() {
        boolean upr = true;
        int count = 0;
        for (final Node tmp : this.getLanguarge()) {
            ++count;
            final int[] role = tmp.getRoole();
            final LlkContext cont = tmp.getFirstFollowK();
            int count2 = 0;
            for (final Node tmp2 : this.getLanguarge()) {
                ++count2;
                if (tmp == tmp2) {
                    break;
                }
                final int[] role2 = tmp2.getRoole();
                if (role[0] != role2[0]) {
                    continue;
                }
                final LlkContext cont2 = tmp2.getFirstFollowK();
                for (int ii = 0; ii < cont.calcWords(); ++ii) {
                    if (cont2.wordInContext(cont.getWord(ii))) {
                        System.out.println("\u041f\u0430\u0440\u0430 \u043f\u0440\u0430\u0432\u0438\u043b (" + count + "," + count2 + ") \u043d\u0435 \u0437\u0430\u0434\u043e\u0432\u043e\u043b\u044c\u043d\u044f\u044e\u0442\u044c \u0441\u0438\u043b\u044c\u043d\u0456\u0439 LL(" + this.getLlkConst() + ")- \u0443\u043c\u043e\u0432\u0456");
                        upr = false;
                        break;
                    }
                }
            }
        }
        if (upr) {
            System.out.println("\u0413\u0440\u0430\u043c\u0430\u0442\u0438\u043a\u0430 \u0437\u0430\u0434\u043e\u0432\u0456\u043b\u044c\u043d\u044f\u044e\u0454 \u0441\u0438\u043b\u044c\u043d\u0456\u0439 LL(" + this.getLlkConst() + ")- \u0443\u043c\u043e\u0432\u0456");
        }
        return upr;
    }

    public int[] createUprTable() {
        final int[] term = this.getTerminals();
        final int[] nonterm = this.getNonTerminals();
        if (this.LLK != 1) {
            System.out.println("\u0421\u043f\u0440\u043e\u0431\u0430 \u043f\u043e\u0431\u0443\u0434\u0443\u0432\u0430\u0442\u0438 \u0442\u0430\u0431\u043b\u0438\u0446\u044e \u0443\u043f\u0440\u0430\u0432\u043b\u0456\u043d\u043d\u044f \u0434\u043b\u044f k=" + this.LLK);
            return null;
        }
        final int[] upr = new int[(term.length + 1) * nonterm.length];
        for (int ii = 0; ii < upr.length; ++ii) {
            upr[ii] = 0;
        }
        int rowline = 0;
        for (final Node tmp : this.getLanguarge()) {
            ++rowline;
            int[] role;
            int ii2;
            for (role = tmp.getRoole(), ii2 = 0; ii2 < nonterm.length && role[0] != nonterm[ii2]; ++ii2) {}
            final int rowindex = ii2;
            final LlkContext cont = tmp.getFirstFollowK();
            for (int ii = 0; ii < cont.calcWords(); ++ii) {
                final int[] word = cont.getWord(ii);
                int colindex = 0;
                if (word.length == 0) {
                    colindex = term.length;
                }
                else {
                    for (ii2 = 0; ii2 < term.length; ++ii2) {
                        if (word[0] == term[ii2]) {
                            colindex = ii2;
                            break;
                        }
                    }
                }
                if (upr[rowindex * (term.length + 1) + colindex] != 0) {
                    System.out.println("\u041f\u0440\u0438 \u043f\u043e\u0431\u0443\u0434\u043e\u0432\u0456 \u0442\u0430\u0431\u043b\u0438\u0446\u0456 \u0443\u043f\u0440\u0430\u0432\u043b\u0456\u043d\u043d\u044f \u0434\u043b\u044f \u043d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b\u0430 " + this.getLexemaText(nonterm[rowindex]) + " \u043f\u043e\u0440\u0443\u0448\u0448\u0435\u043d\u043e LL(1)-\u0432\u043b\u0430\u0441\u0442\u0438\u0432\u0456\u0441\u0442\u044c");
                    return null;
                }
                upr[rowindex * (term.length + 1) + colindex] = rowline;
            }
        }
        System.out.println("\u0422\u0430\u0431\u043b\u0438\u0446\u044f \u0443\u043f\u0440\u0430\u0432\u043b\u0456\u043d\u043d\u044f LL(1)-\u0430\u043d\u0430\u043b\u0456\u0437\u0443 \u043f\u043e\u0431\u0443\u0434\u043e\u0432\u0430\u043d\u0430 ");
        return upr;
    }

    public int[] createUprTableWithCollision() {
        final int[] term = this.getTerminals();
        final int[] nonterm = this.getNonTerminals();
        if (this.LLK != 1) {
            System.out.println("\u0421\u043f\u0440\u043e\u0431\u0430 \u043f\u043e\u0431\u0443\u0434\u0443\u0432\u0430\u0442\u0438 \u0442\u0430\u0431\u043b\u0438\u0446\u044e \u0443\u043f\u0440\u0430\u0432\u043b\u0456\u043d\u043d\u044f \u0434\u043b\u044f k=" + this.LLK);
            return null;
        }
        final int[] upr = new int[(term.length + 1) * nonterm.length];
        for (int ii = 0; ii < upr.length; ++ii) {
            upr[ii] = 0;
        }
        int rowline = 0;
        for (final Node tmp : this.getLanguarge()) {
            ++rowline;
            final int[] role = tmp.getRoole();
            final int rowindex = this.indexNonterminal(role[0]);
            final LlkContext cont = tmp.getFirstFollowK();
            for (int count = cont.calcWords(), ii = 0; ii < count; ++ii) {
                final int[] word = cont.getWord(ii);
                int colindex = 0;
                if (word.length == 0) {
                    colindex = term.length;
                }
                else {
                    colindex = this.indexTerminal(word[0]);
                }
                final int index = rowindex * (term.length + 1) + colindex;
                if (upr[index] != 0) {
                    upr[index] = -1;
                }
                else {
                    upr[index] = rowline;
                }
            }
        }
        for (int rowindex = 0; rowindex < nonterm.length; ++rowindex) {
            int colindex;
            for (colindex = 0; colindex < term.length + 1 && upr[rowindex * (term.length + 1) + colindex] >= 0; ++colindex) {}
            if (colindex != term.length + 1) {
                System.out.println("\u0414\u043b\u044f \u043d\u0435\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b\u0430 " + this.getLexemaText(nonterm[rowindex]) + " \u043f\u043e\u0440\u0443\u0448\u0435\u043d\u043e LL(1)-\u0432\u043b\u0430\u0441\u0442\u0438\u0432\u0456\u0441\u0442\u044c ");
            }
        }
        return upr;
    }

    private boolean newCalcIndex(final int[] mult, final int[] maxmult, final int realCalc) {
        for (int ii = realCalc - 1; ii >= 0; --ii) {
            if (mult[ii] + 1 != maxmult[ii]) {
                final int n = ii;
                ++mult[n];
                return true;
            }
            mult[ii] = 0;
        }
        return false;
    }

    private int[] newWord(final int LLK, final LlkContext[] tmp, final int[] mult, final int realCalc) {
        final int[] word = new int[LLK];
        int llkTmp = 0;
        for (int ii = 0; ii < realCalc; ++ii) {
            if (tmp[ii] == null) {
                System.out.println("\u041d\u0435 \u043e\u0442\u0440\u0438\u043c\u0430\u043b\u0438 \u043c\u043e\u0432\u0438 \u0437 \u0456\u043d\u0434\u0435\u0441\u043e\u043c " + ii);
            }
            final int[] wordtmp = tmp[ii].getWord(mult[ii]);
            if (wordtmp == null) {
                System.out.println("\u041d\u0435 \u043e\u0442\u0440\u0438\u043c\u0430\u043b\u0438 \u0435\u043b\u0435\u043c\u0435\u043d\u0442\u0430 \u043f\u043e \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0456 " + ii + " \u0437 \u0456\u043d\u0434\u0435\u0441\u043e\u043c " + mult[ii]);
            }
            for (int ii2 = 0; ii2 < wordtmp.length && llkTmp != LLK; word[llkTmp++] = wordtmp[ii2], ++ii2) {}
            if (llkTmp == LLK) {
                break;
            }
        }
        final int[] word2 = new int[llkTmp];
        for (int ii = 0; ii < llkTmp; ++ii) {
            word2[ii] = word[ii];
        }
        return word2;
    }

    public int getLlkConst() {
        return this.LLK;
    }

    private int getLexemaCode(final String lexema, final int lexemaClass) {
        for (final TableNode tmp : this.lexemaTable) {
            if (tmp.getLexemaText().equals(lexema) && (tmp.getLexemaCode() & 0xFF000000) == lexemaClass) {
                return tmp.getLexemaCode();
            }
        }
        return 0;
    }

    public LlkContext[] getLlkTrmContext() {
        return this.termLanguarge;
    }

    private LlkContext[] createTerminalLang() {
        final LlkContext[] cont = new LlkContext[this.terminals.length];
        for (int ii = 0; ii < this.terminals.length; ++ii) {
            final int[] trmWord = { this.terminals[ii] };
            (cont[ii] = new LlkContext()).addWord(trmWord);
        }
        return cont;
    }

    public String getLexemaText(final int code) {
        for (final TableNode tmp : this.lexemaTable) {
            if (tmp.getLexemaCode() == code) {
                return tmp.getLexemaText();
            }
        }
        return null;
    }

    public int[] getTerminals() {
        return this.terminals;
    }

    public int[] getNonTerminals() {
        return this.nonterminals;
    }

    public int[] getEpsilonNonterminals() {
        return this.epsilonNerminals;
    }

    public LinkedList<Node> getLanguarge() {
        return this.language;
    }

    public void printEpsilonNonterminals() {
        System.out.println("\u0421\u041f\u0418\u0421\u041e\u041a E-\u0442\u0435\u0440\u043c\u0456\u043d\u0430\u043b\u0456\u0432:");
        if (this.epsilonNerminals == null) {
            return;
        }
        for (int ii = 0; ii < this.epsilonNerminals.length; ++ii) {
            System.out.println("" + this.epsilonNerminals[ii] + "\t" + this.getLexemaText(this.epsilonNerminals[ii]));
        }
    }

    public void setEpsilonNonterminals(final int[] eps) {
        this.epsilonNerminals = eps;
    }

    public int[] createEpsilonNonterminals() {
        final int[] terminal = new int[this.nonterminals.length];
        int count = 0;
        for (final Node tmp : this.language) {
            tmp.setTeg(0);
        }
        boolean upr;
        do {
            upr = false;
            for (final Node tmp : this.language) {
                int[] role;
                int ii;
                int ii2;
                for (role = tmp.getRoole(), ii = 1; ii < role.length; ++ii) {
                    if (role[ii] > 0) {
                        break;
                    }
                    for (ii2 = 0; ii2 < count && terminal[ii2] != role[ii]; ++ii2) {}
                    if (ii2 == count) {
                        break;
                    }
                }
                if (ii == role.length) {
                    int ii3;
                    for (ii3 = 0; ii3 < count && terminal[ii3] != role[0]; ++ii3) {}
                    if (ii3 != count) {
                        continue;
                    }
                    terminal[count++] = role[0];
                    upr = true;
                }
            }
        } while (upr);
        final int[] rezult = new int[count];
        for (int ii = 0; ii < count; ++ii) {
            rezult[ii] = terminal[ii];
        }
        return rezult;
    }

    public void printGramma() {
        int count = 0;
        for (final Node tmp : this.language) {
            final int[] roole = tmp.getRoole();
            ++count;
            System.out.print("" + count);
            for (int ii = 0; ii < roole.length; ++ii) {
                if (ii == 1) {
                    System.out.print(" ::= ");
                }
                System.out.print(this.getLexemaText(roole[ii]) + " ");
                if (roole.length == 1) {
                    System.out.print(" ::= ");
                }
            }
            System.out.println("");
        }
    }

    private int[] createTerminals() {
        int count = 0;
        for (final TableNode tmp : this.lexemaTable) {
            if (tmp.getLexemaCode() > 0) {
                ++count;
            }
        }
        final int[] terminal = new int[count];
        count = 0;
        for (final TableNode tmp : this.lexemaTable) {
            if (tmp.getLexemaCode() > 0) {
                terminal[count] = tmp.getLexemaCode();
                ++count;
            }
        }
        return terminal;
    }

    private int[] createNonterminals() {
        int count = 0;
        for (final TableNode tmp : this.lexemaTable) {
            if (tmp.getLexemaCode() < 0) {
                ++count;
            }
        }
        final int[] nonterminal = new int[count];
        count = 0;
        for (final TableNode tmp : this.lexemaTable) {
            if (tmp.getLexemaCode() < 0) {
                nonterminal[count] = tmp.getLexemaCode();
                ++count;
            }
        }
        return nonterminal;
    }

    private void setAxioma(final int axiom0) {
        this.axioma = axiom0;
    }

    public int getAxioma() {
        return this.axioma;
    }

    private void readGrammat(final String fname) {
        final char[] lexema = new char[180];
        final int[] roole = new int[80];
        BufferedReader s;
        try {
            s = new BufferedReader(new FileReader(fname));
        }
        catch (FileNotFoundException ee) {
            System.out.print("File: " + fname + "not found\n");
            this.create = false;
            return;
        }
        for (int ii = 0; ii < lexema.length; ++ii) {
            lexema[ii] = '\0';
        }
        final int[] pravilo = new int[80];
        for (int ii2 = 0; ii2 < pravilo.length; ++ii2) {
            pravilo[ii2] = 0;
        }
        int pos = 0;
        final int poslex = 0;
        int q = 0;
        final int leftLexema = 0;
        int posRoole = 0;
        int line = 0;
        String readline = null;
        int readpos = 0;
        int readlen = 0;
        try {
            while (s.ready()) {
                if (readline == null || readpos >= readlen) {
                    readline = s.readLine();
                    if (line == 0 && readline.charAt(0) == '\ufeff') {
                        readline = readline.substring(1);
                    }
                    readlen = readline.length();
                    ++line;
                }
                for (readpos = 0; readpos < readlen; ++readpos) {
                    final char litera = readline.charAt(readpos);
                    switch (q) {
                        case 0: {
                            if (litera == ' ' || litera == '\t' || litera == '\r' || litera == '\n') {
                                break;
                            }
                            if (litera == '\b') {
                                break;
                            }
                            if (readpos == 0 && posRoole > 0 && (litera == '!' || litera == '#')) {
                                final Node nod = new Node(roole, posRoole);
                                this.language.add(nod);
                                if (litera == '!') {
                                    posRoole = 1;
                                    break;
                                }
                                posRoole = 0;
                            }
                            pos = 0;
                            if ((lexema[pos++] = litera) == '#') {
                                q = 1;
                                break;
                            }
                            if (litera == '\\') {
                                --pos;
                                q = 3;
                                break;
                            }
                            q = 2;
                            break;
                        }
                        case 1: {
                            lexema[pos++] = litera;
                            if (litera == '#' || readpos == 0) {
                                final String strTmp = new String(lexema, 0, pos);
                                final TableNode nodeTmp = new TableNode(strTmp, Integer.MIN_VALUE);
                                int ii3 = 1;
                                for (final TableNode tmp : this.lexemaTable) {
                                    if (nodeTmp.equals(tmp)) {
                                        ii3 = 0;
                                        break;
                                    }
                                }
                                if (ii3 == 1) {
                                    this.lexemaTable.add(nodeTmp);
                                }
                                final int newLexemaCode = this.getLexemaCode(strTmp, Integer.MIN_VALUE);
                                roole[posRoole++] = newLexemaCode;
                                pos = 0;
                                q = 0;
                                break;
                            }
                            break;
                        }
                        case 2: {
                            if (litera == '\\') {
                                --pos;
                                q = 3;
                                break;
                            }
                            if (litera == ' ' || readpos == 0 || litera == '#' || litera == '\r' || litera == '\t') {
                                final String strTmp = new String(lexema, 0, pos);
                                final TableNode nodeTmp = new TableNode(strTmp, 268435456);
                                int ii3 = 1;
                                for (final TableNode tmp : this.lexemaTable) {
                                    if (nodeTmp.equals(tmp)) {
                                        ii3 = 0;
                                        break;
                                    }
                                }
                                if (ii3 == 1) {
                                    this.lexemaTable.add(nodeTmp);
                                }
                                final int newLexemaCode = this.getLexemaCode(strTmp, 268435456);
                                roole[posRoole++] = newLexemaCode;
                                pos = 0;
                                q = 0;
                                --readpos;
                                break;
                            }
                            lexema[pos++] = litera;
                            break;
                        }
                        case 3: {
                            lexema[pos++] = litera;
                            q = 2;
                            break;
                        }
                    }
                }
            }
            if (pos != 0) {
                int code;
                if (q == 1) {
                    code = Integer.MIN_VALUE;
                }
                else {
                    code = 268435456;
                }
                final String strTmp2 = new String(lexema, 0, pos);
                final TableNode nodeTmp = new TableNode(strTmp2, code);
                int ii4 = 1;
                for (final TableNode tmp2 : this.lexemaTable) {
                    if (nodeTmp.equals(tmp2)) {
                        ii4 = 0;
                        break;
                    }
                }
                if (ii4 == 1) {
                    this.lexemaTable.add(nodeTmp);
                }
                final int newLexemaCode = this.getLexemaCode(strTmp2, code);
                roole[posRoole++] = newLexemaCode;
            }
            if (posRoole > 0) {
                final Node nod = new Node(roole, posRoole);
                this.language.add(nod);
            }
            this.create = true;
        }
        catch (IOException e) {
            System.out.println(e.toString());
            this.create = false;
        }
    }

    public class MyLang$1 {
    }
}