package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity(name = "BANK_ACCOUNT")
@Table(name = "BANK_ACCOUNT")
@NoArgsConstructor
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_account_id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String telNumber;
    @Getter
    @Setter
    private String address;

    @OneToOne(mappedBy = "bankAccount")
    private User user;

    @OneToMany(targetEntity = ExternalTransaction.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "accountDebit")
    @OrderBy("date ASC")
    @Getter
    private List<ExternalTransaction> externalTransactionList;



    /**
     * <b>BankAccount constructor</b>
     * @param name bank name
     * @param telNumber telephone
     * @param address address
     */
    public BankAccount(String name, String telNumber, String address) {
        this.name = name;
        this.telNumber = telNumber;
        this.address = address;
        setExternalTransactionList(null);
    }


    public void setExternalTransactionList(List<ExternalTransaction> externalList){
        this.externalTransactionList = Optional.ofNullable(externalList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
    }
}

//https://www.codeflow.site/fr/article/jpa-join-column
//http://www.java2s.com/Tutorials/Java/JPA/0920__JPA_ManyToOne_Join_Column.htm
//https://www.baeldung.com/jpa-join-column
//https://en.wikibooks.org/wiki/Java_Persistence/OneToMany#Example_of_a_JPA_2.x_unidirectional_OneToMany_relationship_annotations