/**
 * 
 */
package irdm.project.pagerank;

import org.terrier.matching.MatchingQueryTerms;
import org.terrier.matching.ResultSet;
import org.terrier.matching.dsms.SimpleStaticScoreModifier;
import org.terrier.structures.Index;
import org.terrier.utility.ApplicationSetup;
import org.terrier.utility.HeapSort;

import irdm.project.run.ApplicationConfig;

/**
 * @author Harsha Perera
 *
 */
public class TerrierPageRankScoreModifier extends SimpleStaticScoreModifier {

	double a;
	//int w;
	double k;
	
	static {

		ApplicationSetup.setProperty("ssa.input.file", ApplicationConfig.PageRankScoreFilePath);
		ApplicationSetup.setProperty("ssa.input.type", "docno2score");
		// how much of the top-ranked documents to alter. 0 means all documents,
		// defaults to 1000.
		ApplicationSetup.setProperty("ssa.modified.length", "0");
		// combination weight of the feature
		ApplicationSetup.setProperty("ssa.w", Double.toString(ApplicationConfig.PageRankWeighting));
		// ApplicationSetup.setProperty("ssa.normalise - if defined, available
		// options are "mean1" or "maxmin". Default is no normalisation.
	}

	@Override
	public boolean modifyScores(Index index, MatchingQueryTerms queryTerms, ResultSet set) {
		w = 1.8;
		k = 1;
		a = 0.6;
		init(index);
		initialise_parameters();
		int minimum = modifiedLength;
		//if the minimum number of documents is more than the
		//number of documents in the results, aw.length, then
		//set minimum = aw.length
		
		if (minimum > set.getResultSize() || minimum == 0)
			minimum = set.getResultSize();

		logger.info("Applying "+ this.getClass().getSimpleName() + " to " + minimum + " retrieved documents");

		int[] docids = set.getDocids();
		double[] scores = set.getScores();
		int start = 0;
		int end = minimum;
		int altered = 0;
		 
		for(int i=start;i<end;i++)
		{
			if (scores[i] != 0.0d)
			{
				scores[i] += (w * Math.pow(staticScores[docids[i]],a))/(Math.pow(k, a) + Math.pow(staticScores[docids[i]],a));
				altered++;
			}
		}
		
		logger.info("Altered " + altered + " doc scores");
		HeapSort.descendingHeapSort(scores, docids, set.getOccurrences(), set.getResultSize());
		return true;
	}	
	
	
}
