package com.infosupport.ldoc.analyzerj;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/** Command-line entry point to use the analyzer standalone (not through the Maven plugin). */
public class Main {

  private static Options options() {
    Options options = new Options();
    options.addRequiredOption(null, "output", true, "The file path to save the output JSON to.");
    options.addRequiredOption(null, "project", true, "Root directory of the project to analyze.");
    options.addOption("p", "pretty", false, "Indent (pretty-print) JSON output.");
    options.addOption(null, "classpath", true,
        "(Semi)colon-separated list of JAR paths to use during type resolution.");
    return options;
  }

  private static AnalysisJob jobFromArgs(String[] args) throws ParseException {
    CommandLineParser parser = new DefaultParser();
    CommandLine commandLine = parser.parse(options(), args);

    return new AnalysisJob(
        Path.of(commandLine.getOptionValue("project")),
        Path.of(commandLine.getOptionValue("output")),
        List.of(commandLine.getOptionValue("classpath", "").split(File.pathSeparator)),
        commandLine.hasOption("pretty"));
  }

  /** Entry point. */
  public static void main(String[] args) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Analyzer analyzer = new Analyzer(objectMapper);

    try {
      analyzer.analyze(jobFromArgs(args));
    } catch (ParseException e) {
      System.err.println(e.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("analyzer-java", options(), true);
      System.exit(1);
    }
  }
}
