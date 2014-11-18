package eu.fbk.irst.dkm.owlapi.converter;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

/**
 * Author: Matteo Matassoni<br>
 * FBK-IRST<br>
 * Data & Knowledge Management Unit<br>
 * Date: 23-Sep-2014<br><br>
 */
public class OWLOntologyConverter {

	public static void convert(OWLOntology ontology, OWLOntologyFormat outputFormat, java.io.OutputStream outputStream) throws OWLOntologyStorageException {
		OWLOntologyManager manager = ontology.getOWLOntologyManager();
		OWLOntologyFormat inputFormat = manager.getOntologyFormat(ontology);
		
		// copy prefixes from read format to the output one
		if (inputFormat.isPrefixOWLOntologyFormat() && outputFormat.isPrefixOWLOntologyFormat()) {
			outputFormat.asPrefixOWLOntologyFormat().copyPrefixesFrom(inputFormat.asPrefixOWLOntologyFormat());
		}
		
		// save ontology
		manager.saveOntology(ontology, outputFormat, outputStream);
	}
}
