package com.krd.Filter;


public class DataFilter {
	 public static void main(String[] args) throws Exception {
	        String indexPath = "marathi_index";

	        String[] data = {
	            "मुंबई विद्यापीठ",
	            "नाशिक जिल्हा परिषद",
	            "पुणे महानगरपालिका",
	            "नाशिक जिल्हा परिषदनाशिक जिल्हा परिषदनाशिक जिल्हा परिषद मुंबई विद्यापीठ नाशिक जिल्हा परिषद",
	            "naresh agarwal"
	        };

	        MarathiIndexer indexer = new MarathiIndexer();
	        indexer.indexDocuments(indexPath, data);

	        MarathiSearcher searcher = new MarathiSearcher();
	        searcher.search(indexPath, "naresh agarwal");
	    }
}