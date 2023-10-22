package org.example.domain;

import java.io.Serializable;

public record DeliveryInstruction(SandwichOrder order) implements Serializable {

}
