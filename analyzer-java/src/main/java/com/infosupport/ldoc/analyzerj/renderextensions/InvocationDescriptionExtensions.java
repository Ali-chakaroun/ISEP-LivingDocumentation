package com.infosupport.ldoc.analyzerj.renderextensions;

import com.infosupport.ldoc.analyzerj.descriptions.ArgumentDescription;
import com.infosupport.ldoc.analyzerj.descriptions.ConstructorDescription;
import com.infosupport.ldoc.analyzerj.descriptions.Description;
import com.infosupport.ldoc.analyzerj.descriptions.InvocationDescription;
import com.infosupport.ldoc.analyzerj.descriptions.MethodDescription;
import com.infosupport.ldoc.analyzerj.descriptions.ParameterDescription;
import java.util.ArrayList;
import java.util.List;

public class InvocationDescriptionExtensions {

  /**
   * Check if the given {@link InvocationDescription} invokes the given {@link MethodDescription}
   *
   * @param invocation {@link InvocationDescription}.
   * @param method     Either {@link MethodDescription} or {@link ConstructorDescription}.
   * @return True if the invocation invokes the method, false if not.
   */
  public static boolean invocationMatchesMethod(InvocationDescription invocation,
      Description method) {
    boolean isMethod = checkParameters(invocation, method);
    String methodName = isMethod ? ((MethodDescription) method).member().name()
        : ((ConstructorDescription) method).member().name();
    return invocation.name().equals(methodName) && invocationMatchesMethodParameters(invocation,
        method);
  }

  /**
   * Check if the invocation arguments match the method parameters by amount and type. Currently,
   * the implementation of optional parameters is commented out, as the Java Analyser does not yet
   * support the hasBooleanValue field of {@link ParameterDescription}.
   *
   * @param invocation {@link InvocationDescription}
   * @param method     Either {@link MethodDescription} or {@link ConstructorDescription}.
   * @return True if arguments and parameters match, false if they don't.
   */
  public static boolean invocationMatchesMethodParameters(InvocationDescription invocation,
      Description method) {
    boolean isMethod = checkParameters(invocation, method);

    List parameters = isMethod ? ((MethodDescription) method).parameters()
        : ((ConstructorDescription) method).parameters();

    if (invocation.arguments().size() == 0) {
      return parameters.size() == 0;
    }

    List<Description> arguments = invocation.arguments();
    List invokedWithTypes = new ArrayList();
    for (Description argument : arguments) {
      invokedWithTypes.add(((ArgumentDescription) argument).type());
    }
    if (invokedWithTypes.size() > parameters.size()) {
      return false;
    }

    int optionalArguments = 0;
    for (int i = 0; i < parameters.size(); i++) {
//      if ((ParameterDescription) parameters.get(i)).hasDefaultValue()) {
//        optionalArguments += 1;
//      }
    }

    int checkSize = invokedWithTypes.size();

    if (optionalArguments == 0) {
      checkSize = arguments.size();
    }

    List parameterTypes = new ArrayList();
    for (int i = 0; i < checkSize; i++) {
      parameterTypes.add(((ParameterDescription) parameters.get(i)).type());
    }
    return parameterTypes.containsAll(invokedWithTypes) && invokedWithTypes.containsAll(
        parameterTypes);
  }

  /**
   * Helper method. Throws error if the given descriptions are null. Returns whether the given
   * method is a {@link MethodDescription} or {@link ConstructorDescription}. Throws error if the
   * given method is neither.
   *
   * @param invocation {@link InvocationDescription}
   * @param method     {@link Description}
   * @return True if method is an instance of {@link MethodDescription}, false if method is an
   * instance of {@link ConstructorDescription}.
   */
  private static boolean checkParameters(InvocationDescription invocation, Description method) {
    if (invocation == null) {
      throw new IllegalArgumentException("Invocation parameter cannot be null");
    }
    if (method == null) {
      throw new IllegalArgumentException("Method parameter cannot be null");
    }
    boolean isMethod;
    if (method instanceof MethodDescription) {
      isMethod = true;
    } else if (method instanceof ConstructorDescription) {
      isMethod = false;
    } else {
      throw new IllegalArgumentException(
          "Method parameter must be either a MethodDescription or a ConstructorDescription");
    }
    return isMethod;
  }
}
