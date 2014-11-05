package eu.fbk.irst.dkm.owlapi.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;
import org.semanticweb.owlapi.io.OWLFunctionalSyntaxOntologyFormat;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import eu.fbk.irst.dkm.owlapi.apibinding.OWLManager;
import eu.fbk.irst.dkm.owlapi.io.OWLLatexStyleSyntaxOntologyFormat;

/**
 * Author: Matteo Matassoni<br>
 * FBK-IRST<br>
 * Data & Knowledge Management Unit<br>
 * Date: 11-Nov-2013<br><br>
 */
public class App {
	
	private enum Syntax { RDFXML,OWLXML,TURTLE,MANCHESTER,FUNCTIONAL,TEXOWL }
	@Option(name="-s",usage="Specifies the export syntax. If not specified RDFXML is used",metaVar="syntax")
	private Syntax syntax;
	
	@Option(name="-o",usage="Output to this file",metaVar="target")
    private File output;
	
	@Argument(index=0,usage="Input file",metaVar="source")
	private File input;

	public static void main(String[] args) {
		new App().doMain(args);
	}

	private void doMain(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
			
			if (input == null) {
				throw new CmdLineException(parser, "No argument is given.");
			} else if (!input.isFile()) {
				throw new CmdLineException(parser, "The argument is no valid input file.");
			}
			
			OutputStream out = null;
			if (output == null) {
				out = System.out;
			} else {
				if (!output.exists()) {
					output.createNewFile();
				}
				if (!output.isFile()) {
					throw new CmdLineException(parser, "-o is no valid output file.");
				}
				out = new FileOutputStream(output, false);
			}
			
			OWLOntologyFormat outputFormat = null;
			switch (syntax) {
			case RDFXML:
				outputFormat = new RDFXMLOntologyFormat();
				break;
			case OWLXML:
				outputFormat = new OWLXMLOntologyFormat();
				break;
			case TURTLE:
				outputFormat = new TurtleOntologyFormat();
				break;
			case MANCHESTER:
				outputFormat = new ManchesterOWLSyntaxOntologyFormat();
				break;
			case FUNCTIONAL:
				outputFormat = new OWLFunctionalSyntaxOntologyFormat();
				break;
			case TEXOWL:
				outputFormat = new OWLLatexStyleSyntaxOntologyFormat();
				break;
			}
			
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(input);
			
			OWLOntologyConverter.convert(ontology, outputFormat, out);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
            System.err.println("java OWLConverter [-s syntax] [-o target] source");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();

            // print option sample. This is useful some time
            System.err.println("Example: java OWLConverter" + parser.printExample(ExampleMode.REQUIRED));
		} catch (IOException e) {
			System.out.println("Could not export the ontology to output file."); 
		} catch (UnparsableOntologyException e) {
			System.out.println("Input file cannot be parsed as an ontology.");
		} catch (OWLOntologyCreationException e) {
			System.out.println("Could not load the ontology from input file.");
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			System.out.println("Could not export the ontology to output file.");
		}
	}
}
