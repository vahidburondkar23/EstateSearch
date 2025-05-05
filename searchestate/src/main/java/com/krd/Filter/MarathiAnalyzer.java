package com.krd.Filter;
import com.ibm.icu.text.Normalizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharTokenizer;

import java.io.IOException;

public class MarathiAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(new MarathiTokenizer());
    }

    private static class MarathiTokenizer extends CharTokenizer {
        @Override
        protected boolean isTokenChar(int c) {
            return Character.isLetter(c) || Character.getType(c) == Character.NON_SPACING_MARK;
        }

        
        protected int normalize(int c) {
            return Normalizer.normalize(Character.toString((char) c), Normalizer.NFC).charAt(0);
        }
    }
}