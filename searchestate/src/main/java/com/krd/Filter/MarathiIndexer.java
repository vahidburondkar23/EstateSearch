package com.krd.Filter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MarathiIndexer {
    public void indexDocuments(String indexPath, String[] contents) throws IOException {
        Files.createDirectories(Paths.get(indexPath));

        Analyzer analyzer = new MarathiAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // Ensure clean index

        try (Directory dir = FSDirectory.open(Paths.get(indexPath));
             IndexWriter writer = new IndexWriter(dir, config)) {
            for (String content : contents) {
                Document doc = new Document();
                doc.add(new TextField("content", content, Field.Store.YES));
                writer.addDocument(doc);
            }
        }
    }
}