/**
 * 
 */
package irdm.project.pagerank;

import org.terrier.matching.dsms.SimpleStaticScoreModifier;
import org.terrier.utility.ApplicationSetup;

import irdm.project.run.ApplicationConfig;

/**
 * @author Harsha Perera
 *
 */
public class TerrierPageRankScoreModifier extends SimpleStaticScoreModifier {

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
	
}
