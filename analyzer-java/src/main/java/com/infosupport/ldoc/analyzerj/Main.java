package com.infosupport.ldoc.analyzerj;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

  public static final Options OPTIONS = new Options()
      .addRequiredOption(null, "output", true, "The file path to save the output JSON to.")
      .addRequiredOption(null, "project", true, "The root directory of the project to analyze.")
      .addOption("p", "pretty", false, "Indent (pretty-print) JSON output.");

  private static AnalysisJob jobFromArgs(String[] args) throws ParseException {
    CommandLineParser parser = new DefaultParser();
    CommandLine commandLine = parser.parse(OPTIONS, args);

    return new AnalysisJob(
        Path.of(commandLine.getOptionValue("project")),
        Path.of(commandLine.getOptionValue("output")),
        commandLine.hasOption("pretty"));
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Analyzer analyzer = new Analyzer(objectMapper);

    try {
      analyzer.analyze(jobFromArgs(args));
    } catch (ParseException e) {
      System.err.println(e.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("analyzer-java", OPTIONS, true);
      System.exit(1);
    }
  }
}
