package TeacherLiba;

import java.util.ArrayList;

public class MyLangExt extends MyLang {

    public MyLangExt(String fileLang, int llk1)
    {
        super(fileLang, llk1);
    }

    public ArrayList<Integer> GetFirstFollowForRule(int rule){

        Node rl = language.get(rule);
        LlkContext firstFollowK = language.get(rule).getFirstFollowK();

        ArrayList<Integer> resultList = new ArrayList<>();

        for (int[] arr : firstFollowK.GetArray())
        {
            for (int symb : arr)
            {
                resultList.add(symb);
            }
        }
        return resultList;
    }

    private Integer ToInt(int[] word)
    {
        int result = 0;
        for (int i = 0; i < word.length; ++i)
        {
            result += word[word.length - i - 1] * Math.pow(10, i);
        }
        return result;
    }
}
