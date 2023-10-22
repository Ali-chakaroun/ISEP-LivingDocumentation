package org.example.domain;

import java.io.Serializable;

public record SandwichOrder(Bread bread, Protein protein, String address) implements Serializable {

}
