package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Information on an individual case section from a switch statement. Meant to be contained in a
 * list of SwitchSections, in a {@link SwitchDescription}.
 *
 * @param labels     List of case labels (strings). If empty, this is a default case.
 * @param statements List of statements from the case body.
 */
public record SwitchSection(
    @JsonProperty("Labels")
    @JsonInclude(Include.NON_EMPTY)
    List<String> labels,

    @JsonProperty("Statements")
    @JsonInclude(Include.NON_EMPTY)
    List<Description> statements
) implements Description {

}
