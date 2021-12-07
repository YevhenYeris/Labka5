package yrs.yvhn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PascalRegexLexicalAnalyzer {

    public static final String Operator = ":=|\\+|-|\\*|/|%|==|<>|>|<|>=|<=|and|then" +
            "|or|else|not|&|\\||!|[^:]=|~|<<|>>|xor|shl|shr|div|mod";

    private static final String KeywordRegex = "uses|const|begin" +
            "|case|do|downto|else" +
            "|end|End|for|function|goto" +
            "|if|label|nil|of" +
            "|packed|procedure|program" +
            "|record|in|repeat|file" +
            "|set|type|until" +
            "|var|while|with|to";

    private static final String DelimiterRegex = ";|\\.|:$";

    private static final String NumberRegex = "^(\\+|-)?[0-9]+.?[0-9]*$|^0x([0-9]|[A-F])*$";

    private static final String CharConstRegex = "^'.*'$";

    private static final String DirectiveRegex = "^\\$([A-Z]|[0-9])+$";

    private static final String CommentRegex = "^\\(\\*.*\\*\\)$|^\\{.*}$|^//.*$";

    private static final String IdentifierRegex = "^[A-z]+([A-z]|[0-9])*$";

    private static final String BracketRegex = "\\(|\\)|\\[|]";

    private String _code = "";
    private String _resOfAnalysis = "";

    private String[] _lexemes;

    public PascalRegexLexicalAnalyzer(String fileName) throws IOException
    {
        _code = Files.readString(Paths.get(fileName));
        Tokenize();
    }

    private void Tokenize()
    {
        String[] lexemes = _code.split("//[^\\n]*\\n");
        _code = String.join("", lexemes);
        lexemes = _code.split("\\(\\*[^(**)]*\\*\\)");
        _code = String.join("", lexemes);
        lexemes = _code.split("\\{[^{}]*}");
        _code = String.join("", lexemes);
        lexemes = _code.split("\\n+|\\t+|\\s+|\\r+");

        _lexemes = lexemes;
        FindStrings();
        FindDelimiters();
        FindBrackets();
        FindOperators();
    }

    public void FindDelimiters()
    {
        List<String> lexemesWithDelims = new ArrayList<>();

        for (String item : _lexemes)
        {
            String[] divided = item.split("(?=" + DelimiterRegex + ")|(?<=" + DelimiterRegex + ")");

            // Check for decimals
            for (int i = 0; i < divided.length; ++i)
            {
                if (divided[i].equals(".") && i > 0 && isNumber(divided[i-1])
                        && i < divided.length-1 && isNumber(divided[i+1]))
                {
                    lexemesWithDelims.add(divided[i - 1] + divided[i] + divided[i + 1]);
                    i += 2;
                }
                else
                {
                    lexemesWithDelims.add(divided[i]);
                }
            }
        }
        _lexemes = new String[lexemesWithDelims.size()];
        lexemesWithDelims.toArray(_lexemes);
    }

    public void FindBrackets()
    {
        List<String> lexemesWithDelims = new ArrayList<>();

        for (String item : _lexemes)
        {
            String[] divided = item.split("(?=" + BracketRegex + ")|(?<=" + BracketRegex + ")");

            for (int i = 0; i < divided.length; ++i)
            {
                lexemesWithDelims.add(divided[i]);
            }
        }
        _lexemes = new String[lexemesWithDelims.size()];
        lexemesWithDelims.toArray(_lexemes);
    }

    public void FindStrings()
    {
        List<String> lexems = new ArrayList<>();
        int i = -1;

        while (++i < _lexemes.length)
        {
            // First quotemark
            if (_lexemes[i].contains("'"))
            {
                if (_lexemes[i].indexOf("'") > 0)
                {
                    lexems.add(_lexemes[i].substring(0, _lexemes[i].indexOf("'")));

                    int pos1 = _lexemes[i].indexOf("'");
                    String strConst = _lexemes[i].substring(pos1);

                    while (++i < _lexemes.length)
                    {
                        // Last quote
                        if (_lexemes[i].contains("'"))
                        {
                            int pos = _lexemes[i].indexOf("'");
                            strConst += _lexemes[i].substring(0, pos + 1);

                            lexems.add(strConst);
                            if (pos + 1 < _lexemes[i].length()) {
                                lexems.add(_lexemes[i].substring(pos + 1));
                            }
                            break;
                        }
                        else
                        {
                            strConst += _lexemes[i];
                        }
                        if (i == _lexemes.length - 1)
                        {
                            lexems.add(strConst);
                        }
                    }
                }
                continue;
            }
            lexems.add(_lexemes[i]);
        }
        _lexemes = new String[lexems.size()];
        lexems.toArray(_lexemes);
    }

    public void FindOperators()
    {
        List<String> lexemes = new ArrayList<>();

        for (int i = 0; i < _lexemes.length; ++i)
        {
            if (GetClass(_lexemes[i]).equals("undefined"))
            {
                String[] operands = _lexemes[i].split("(?=" + Operator + ")|(?<=" + Operator + ")");

                lexemes.addAll(Arrays.asList(operands));
                continue;
            }
            lexemes.add(_lexemes[i]);
        }
        _lexemes = new String[lexemes.size()];
        lexemes.toArray(_lexemes);
    }

    public String GetResult()
    {
        return _resOfAnalysis;
    }

    public void Analyze()
    {
        for (String item : _lexemes)
        {
            String lexemeClass = GetClass(item);
            _resOfAnalysis += item + " - " + lexemeClass + "\n";
        }
    }

    public ArrayList<String> GetAnalyzed()
    {
        ArrayList<String> lexemes = new ArrayList<>();
        for (String item : _lexemes)
        {
            String lexemeClass = GetClass(item);

            if (lexemeClass.equals("number")) lexemes.add("DIGIT");
            else if (lexemeClass.equals("preprocessor directive")) lexemes.add("PROC_NAME");
            else if (lexemeClass.equals("character constant")) lexemes.add("LITER_STR");
            else if (lexemeClass.equals("identifier")) lexemes.add("IDN");
            else lexemes.add(item);
        }
        return lexemes;
    }

    public String GetClass(String lexeme)
    {
        if (isKeyWord(lexeme))
        {
            return  "keyword";
        }
        if (isDirective(lexeme))
        {
            return "preprocessor directive";
        }
        if (isNumber(lexeme))
        {
            return  "number";
        }
        if (isOperator(lexeme))
        {
            return  "operator";
        }
        if (isDelimiter(lexeme))
        {
            return "delimiter";
        }
        if (isCharConst(lexeme))
        {
            return  "character constant";
        }
        if (isBracket(lexeme))
        {
            return "bracket";
        }
        if (isIdentifier(lexeme))
        {
            return "identifier";
        }
        return "undefined";
    }

    public boolean isNumber(String text)
    {
        return text.matches(NumberRegex);
    }

    public boolean isCharConst(String text)
    {
        return text.matches(CharConstRegex);
    }

    public boolean isDirective(String text)
    {
        return text.matches(DirectiveRegex);
    }

    public boolean isComment(String text)
    {
        return text.matches(CommentRegex);
    }

    public boolean isBracket(String text)
    {
        return text.matches(BracketRegex);
    }

    public boolean isKeyWord(String text)
    {
        return text.matches(KeywordRegex);
    }

    public boolean isOperator(String text)
    {
        return text.matches(Operator);
    }

    public boolean isDelimiter(String text)
    {
        return text.matches(DelimiterRegex);
    }

    public boolean isIdentifier(String text)
    {
        return text.matches(IdentifierRegex);
    }
}
