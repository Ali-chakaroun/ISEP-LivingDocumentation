package com.infosupport.ldoc.springexample.util;

import java.io.PrintWriter;

public class PlantUMLBuilder {

  public static void renderParticipant(PrintWriter out, String name) {
    out.printf("participant \"%s\" as %s\n", StringOperations.humanizeName(name), name);
  }

  public static void renderInteraction(PrintWriter out, String sender, String receiver,
      String event) {
    out.printf("%s -[#ForestGreen]> %s : %s\n", sender, receiver, StringOperations.stripName(event));
  }
}
