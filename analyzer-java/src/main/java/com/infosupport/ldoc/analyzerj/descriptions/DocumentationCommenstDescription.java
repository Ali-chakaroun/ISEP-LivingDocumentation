package com.infosupport.ldoc.analyzerj.descriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.List;

public record DocumentationCommenstDescription(

        @JsonProperty("DocumentationComments")
        @JsonInclude(Include.NON_EMPTY)
        List<Description> DocumentationComments
) implements Description {

}
