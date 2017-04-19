package irdm.project.run;

import irdm.project.index.IndexBuilder;

/**
 * @author Shruti Sinha
 * @author Harsha Perera
 *
 */
public class RunIndexing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TerrierInitialiser.InitTerrier();
		IndexBuilder indexBuilder = new IndexBuilder(ApplicationConfig.CrawlPath, ApplicationConfig.IndexPath,
				ApplicationConfig.AnchorIndexPath, ApplicationConfig.SeedUrl, ApplicationConfig.CrawlMaxDepth);
		indexBuilder.indexWebsite();
		indexBuilder.write();
	}

}
