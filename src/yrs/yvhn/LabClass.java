package yrs.yvhn;

import java.beans.Expression;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import TeacherLiba.*;

public class LabClass
{
    private static final String PROGRAM = "program";
    private static final String IDN = "IDN";
    private static final String SEMICOLON = ";";
    private static final String PROC_NAME = "PROC_NAME";
    private static final String USES = "uses";
    private static final String CONST = "const";
    private static final String EQUALS = "=";
    private static final String DIGIT = "DIGIT";
    private static final String LITER_STR = "LITER_STR";
    private static final String VAR = "var";
    private static final String COLON = ":";
    private static final String COMMA = ",";
    private static final String BEGIN = "begin|Begin";
    private static final String END = "end|End";
    private static final String LBRACKET = "\\(";
    private static final String SLBRACKET = "\\[";
    private static final String RBRACKET = "\\)";
    private static final String SRBRACKET = "\\]";
    private static final String ASSIGNMENT = "\\:\\=";
    private static String OPERATION;

    private MyLangExt _myLang;

    private int _lexCode;
    private String _lexText;
    private int _lexPos;

    private LinkedList<Node> _language;
    private ArrayList<String> _lexemes;


    public LabClass(MyLangExt langExt, String fileName) throws IOException
    {

        _myLang = langExt;
        _lexPos = 0;
        _language = _myLang.getLanguarge();

        PascalRegexLexicalAnalyzer analyzer = new PascalRegexLexicalAnalyzer(fileName);
        analyzer.Analyze();
        OPERATION = PascalRegexLexicalAnalyzer.Operator;
        _lexemes = analyzer.GetAnalyzed();
    }

    public boolean ParseRec()
    {
        String lex = _lexemes.toArray()[_lexPos].toString();
        _lexText = lex;
        _lexCode = _myLang.getLexemaCode(lex.getBytes(StandardCharsets.UTF_8), lex.length(), 0);

        return IsProgramValid();
    }

    private boolean IsProgramValid()
    {
        ArrayList<Integer> firstFollow = new ArrayList<>();

        for(int i = 1; i < _language.size(); ++i)
        {
            firstFollow = _myLang.GetFirstFollowForRule(i);
            String lexTest = _myLang.getLexemaText(_lexCode);

            if(firstFollow.contains(_lexCode))
            {
                if(!CheckRecursive(i))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean CheckRecursive(int ruleIndex)
    {
        int[] rule = _language.get(ruleIndex).getRoole();

        for (int k = 1; k < rule.length; k++)
        {
            if (rule[k] > 0)
            {
                if(rule[k] == _lexCode)
                {
                    ++_lexPos;
                    String lex = _lexemes.toArray()[_lexPos].toString();
                    _lexCode = _myLang.getLexemaCode(lex.getBytes(StandardCharsets.UTF_8), lex.length(), 0);
                    return true;
                }
            }
            else
            {
                for (int j = 0; j < rule.length; j++)
                {
                    int[] rule1 = _language.get(j).getRoole();
                    if(rule1[0]==rule[k]) {
                        CheckRecursive(j);
                    }
                }

            }
        }
        return false;
    }

    private void Next()
    {
        _lexText = _lexemes.get(_lexPos);
        _lexCode = _myLang.getLexemaCode(_lexText.getBytes(StandardCharsets.UTF_8), _lexText.length(), 0);
        ++_lexPos;
    }

    private boolean Accept(String token)
    {
        return _lexText.matches(token);
    }

    private boolean Expect(String token) throws Exception
    {
        if (!Accept(token)) throw new Exception("Unexpected token");
        return true;
    }

    private void Directive()
    {
        while (Accept(PROC_NAME)) Next();
    }

    private boolean Program() throws Exception
    {
        Expect(PROGRAM);
        Next();
        Expect(IDN);
        Next();
        Expect(SEMICOLON);
        Next();
        Uses();
        Next();
        Const();
        Declaration();
        Block();
        return true;
    }

    private void Uses() throws Exception
    {
        Expect(USES);
        Next();
        Expect(IDN);
        Next();
        Expect(SEMICOLON);
    }

    private void Const() throws Exception
    {
        if (Accept(CONST))
        {
            Next();
            while (Const_Assignment()) Next();
        }
    }

    private boolean Const_Assignment() throws Exception
    {
        if (!Accept(IDN)) return false;
        Next();
        Expect(EQUALS);
        Next();
        Expect(DIGIT);
        Next();
        Expect(SEMICOLON);

        return true;
    }

    private void Declaration() throws Exception
    {
        while (Accept(VAR)) {
            Next();
            Expect(IDN);
            Next();
            while (Accept(COMMA)) {
                Next();
                Expect(IDN);
            }
            Expect(COLON);
            Next();
            Expect(IDN);
            Next();
            Expect(SEMICOLON);
            Next();
        }
    }

    private void Block() throws Exception
    {
        Expect(BEGIN);
        Next();
        Block_Inner();
        Next();
        Expect(END);
    }

    private void Block_Inner() throws Exception
    {
        while (Statement()) Next();
    }

    private boolean Statement() throws Exception
    {
        if (Accept(END)) return false;
        if (Accept(IDN))
        {
            Next();
            if (Accept(LBRACKET))
            {
                Next();
                if (!(Accept(LITER_STR) || Accept(DIGIT) || Accept(IDN) || Expression()))
                    Expect(LITER_STR);

                Next();
                Expect(RBRACKET);
                Next();
            }
            else if (Accept(ASSIGNMENT))
            {
                Next();
                Expression();
            }
        }

        Expect(SEMICOLON);
        return true;
    }

    private boolean Expression() throws Exception
    {
        while (Accept(IDN) || Accept(DIGIT) || Accept(LITER_STR))
        {
            Next();
            if (!(Accept(OPERATION) || Accept(SEMICOLON) || ArrayIndex() || Accept(DIGIT)
                                    || Accept(RBRACKET) || Accept(SRBRACKET) || Accept(IDN)))
                Expect(OPERATION);
            if (Accept(SEMICOLON)) return true;
            if (Accept(RBRACKET)) return true;
            if (Accept(SRBRACKET)) return true;
            Next();
        }
        return true;
    }

    private boolean ArrayIndex() throws Exception
    {
        if (!Accept(SLBRACKET)) return false;
        Next();
        Expression();
        Expect(SRBRACKET);
        Next();
        return true;
    }

    public boolean Parse() throws Exception
    {
        Next();
        Directive();
        return Program();
    }
}
