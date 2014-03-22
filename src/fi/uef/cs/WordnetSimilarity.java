package fi.uef.cs;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.*;
import edu.sussex.nlp.jws.*;
import edu.mit.jwi.*;
import edu.mit.jwi.data.*;
import edu.mit.jwi.data.compare.*;
import edu.mit.jwi.data.parse.*;
import edu.mit.jwi.item.*;
import edu.mit.jwi.morph.*;

public class WordnetSimilarity {

	static final String dir = "D:/Software/WordNet";
	static final String version = "2.1";

	JWS ws = new JWS(dir, "2.1");
	WuAndPalmer wup = ws.getWuAndPalmer();
	JiangAndConrath jiang = ws.getJiangAndConrath();

	public double getWordnetSimilarity(String word1, String word2, String type,
			int method, boolean firstSenseOnly) {
		// 1 Jiang
		JiangAndConrath jiang = ws.getJiangAndConrath();
		// 2 Wu
		WuAndPalmer wu = ws.getWuAndPalmer();
		// 3 Lin
		Lin lin = ws.getLin();
		// 4 Path
		Path path = ws.getPath();
		double similarityValue = -1;
		if (firstSenseOnly) {
			if (method == 1) {
				similarityValue = jiang.jcn(word1, 1, word2, 1, type);
			} else if (method == 2) {
				similarityValue = wup.wup(word1, 1, word2, 1, type);
			} else if (method == 3) {
				similarityValue = lin.lin(word1, 1, word2, 1, type);
			} else if (method == 4) {
				similarityValue = path.path(word1, 1, word2, 1, type);
			}
		} else {
			if (method == 1) {
				similarityValue = jiang.max(word1, word2, type);
			} else if (method == 2) {
				similarityValue = wu.max(word1, word2, type);
			} else if (method == 3) {
				similarityValue = lin.max(word1, word2, type);
			} else if (method == 4) {
				similarityValue = path.max(word1, word2, type);
			} else {
				similarityValue = -1;
			}
		}
		return similarityValue;
	}


	public static HashMap<Integer, String> getWordGloss(String keyword,
			String type) throws IOException {
		HashMap<Integer, String> glossMap = new HashMap<Integer, String>();
		POS postype = null;

		if (type.equalsIgnoreCase("n")) {
			postype = POS.NOUN;
		} else if (type.equalsIgnoreCase("v")) {
			postype = POS.VERB;
		} else if (type.equalsIgnoreCase("a")) {
			postype = POS.ADJECTIVE;
		} else if (type.equalsIgnoreCase("r")) {
			postype = POS.ADVERB;
		}

		String path = dir + File.separator + version + File.separator + "dict";

		URL url = new URL("file", null, path);

		IDictionary dict = new Dictionary(url);
		dict.open();

		IIndexWord idxWord = dict.getIndexWord(keyword, postype);

		IWordID wordID = null;

		for (int i = 0;; i++) {
			try {
				wordID = idxWord.getWordIDs().get(i);
				IWord word = dict.getWord(wordID);
				glossMap.put(i, word.getSynset().getGloss());
			} catch (ArrayIndexOutOfBoundsException e) {
				break;
			}
		}
		return glossMap;
	}

}