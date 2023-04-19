package io.hasibix.hasiutils;

import java.util.ArrayList;
import java.util.List;

public class ObjUtils {
    public static Boolean EqualsArray(Object obj, Object[] array) {
        for (Object i : array) {
            if(obj.equals(i)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    public static class Long {
        public static Boolean IsEmpty(java.lang.Integer val) {
            return val == null || val < 0;
        }
    }

    public static class Integer {
        public static Boolean IsEmpty(java.lang.Integer val) {
            return val == null || val < 0;
        }
    }

    public static class String {
        public static List<List<java.lang.String>> SplitAllLinesByWords(java.lang.String str) {
            java.lang.String[] line = str.trim().split("\n");
            List<List<java.lang.String>> linedWords = new ArrayList<List<java.lang.String>>();

            for (java.lang.String string : line) {
                java.lang.String[] unfilteredWords = string.trim().split(" ");
                List<java.lang.String> filteredWords = new ArrayList<java.lang.String>();

                for(java.lang.String word : unfilteredWords) {
                    if(word.trim().length() > 0) {
                        filteredWords.add(word);
                    }
                }
                linedWords.add(filteredWords);
            }

            return linedWords;
        }
        public static Boolean IsEmpty(java.lang.String val) {
            return val == null || val.trim() == "";
        }
    }
}
