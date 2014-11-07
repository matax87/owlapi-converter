owlapi-converter
================

A command-line tool that converts OWL ontologies using OWL API.

## Installation

Clone this repository or download it as a zip. Open a terminal and change to the `owlapi-converter` downloaded directory and generate the executable jar using maven:

```bash
mvn package
```

## Usage

Generally, the previous step generate the jar in the `target` folder placed inside the `owlapi-converter` root folder.

To use the converter tool, type:

```bash
java -jar target/owlapi-converter.jar [-s syntax] [-o target] source
```

where:

* `source` (required argument) is the input ontology file. 
* `-o target` (optional) specifies the output file.
* `-s [RDFXML | OWLXML | TURTLE | MANCHESTER | FUNCTIONAL | TEXOWL]` (optional) specifies the export syntax. If it is not specified RDFXML is used.

For example:

```bash
java -jar target/owlapi-converter.jar -s TEXOWL examples/pizza.owl -o OUTPUT
``` 