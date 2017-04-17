/**
 * 
 */
package irdm.project.run;

/**
 * @author Harsha Perera
 *
 */
public class TerrierInitialiser {

	public static void InitTerrier(){
		System.setProperty("terrier.home", ApplicationConfig.HomePath);
		System.setProperty("terrier.index.path", ApplicationConfig.IndexPath);				
	}
}
