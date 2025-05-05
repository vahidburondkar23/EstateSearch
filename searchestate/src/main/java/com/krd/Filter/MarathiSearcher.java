package com.krd.Filter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class MarathiSearcher {
    public void search(String indexPath, String queryStr) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new MarathiAnalyzer();

        QueryParser parser = new QueryParser("content", analyzer);
        Query query = parser.parse(QueryParser.escape(queryStr) + "~1");

        TopDocs topDocs = searcher.search(query, 10);
        Set<String> seen = new HashSet<>();

        for (ScoreDoc sd : topDocs.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            String content = doc.get("content");
            if (!seen.add(content)) continue; // skip duplicates

            String highlighted = highlightMatchedTerms(content, queryStr);
            System.out.println(highlighted + " (score: " + sd.score + ")");
        }

        reader.close();
    }

    private String highlightMatchedTerms(String text, String query) {
        String[] words = text.split("\\s+");
        String[] queries = query.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            boolean matched = false;
            for (String q : queries) {
                if (levenshtein(word, q) <= 2) {
                    result.append("<b><span style='color:red'>").append(word).append("</span></b> ");
                    matched = true;
                    break;
                }
            }
            if (!matched) result.append(word).append(" ");
        }
        return result.toString().trim();
    }

    private int levenshtein(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}