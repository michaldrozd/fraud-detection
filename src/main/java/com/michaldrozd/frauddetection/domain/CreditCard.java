package com.michaldrozd.frauddetection.domain;

import com.michaldrozd.frauddetection.domain.types.CardType;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Represents a credit card used by a user. Not PCI DSS compliant!
 */
@Getter
@Setter
@Node("CreditCard")
public class CreditCard {
    @Id
    @GeneratedValue
    private Long id;

    // TODO: Yes I know, we should rather use single 'cardToken' field for number and cvv instead of plain values, of course. Sorry PCI DSS :)
    private String number;
    private String cardHolderName;
    private CardType cardType;
    private LocalDate expiryDate;
    private String cvv; // TODO: Yes I know sorry :)

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private User user;
}