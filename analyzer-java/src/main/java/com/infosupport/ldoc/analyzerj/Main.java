package com.infosupport.ldoc.analyzerj;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

  public static void main(String[] args) {
    Options options = new Options()
        .addRequiredOption(null, "output", true, "The file path to save the output JSON to.")
        .addRequiredOption(null, "project", true, "The root directory of the project to analyze.");

    CommandLineParser parser = new DefaultParser();

    try {
      CommandLine commandLine = parser.parse(options, args);

      String project = commandLine.getOptionValue("project");
      String output = commandLine.getOptionValue("output");

      System.out.printf("Would now analyze %s and write result to %s\n", project, output);
    } catch (ParseException e) {
      System.err.println(e.getMessage());
      HelpFormatter helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("analyzer-java", options, true);
      System.exit(1);
    }
  }
}
