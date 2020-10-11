package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity(name = "USER")
@Table(name = "USER", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "first_name")
    @Getter
    @Setter
    private String firstName;

    @Column(name = "last_name")
    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    @Column(nullable = false)
    private String email;

    @Getter
    @Setter
    @Column(nullable = false)
    private String password;

    /*@Getter
    @Setter
    private BankAccount bankAccount;

    @Getter
    @Setter
    private InternalCashAccount internalCashAccount;

    @Getter
    private List<User> contactList;

    @Getter
    private List<ExternalTransaction> externalTransactionList;

    @Getter
    private List<InternalTransaction> internalTransactionList;*/

    /**
     * <b>User Constructor: all list will be created as empty</b>
     * @param firstName firstName
     * @param lastName lastName
     * @param email email
     * @param password password
     */
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        //setContactList(null);
        //setExternalTransactionList(null);
        //setInternalTransactionList(null);
    }

    public User() {
        super();
    }
    /*
    public void setContactList(List<User> userList){
        this.contactList = Optional.ofNullable(userList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
    }

    public void setExternalTransactionList(List<ExternalTransaction> externalList){
        this.externalTransactionList = Optional.ofNullable(externalList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
    }

    public void setInternalTransactionList(List<InternalTransaction> internalList){
        this.internalTransactionList = Optional.ofNullable(internalList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
    }*/

}


//https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/table-annotation-unique-constraints.html
//https://code-examples.net/en/q/5c11f1
//https://stackoverflow.com/questions/3405229/specifying-an-index-non-unique-key-using-jpa
// indexes = {@Index(name = "USER_EMAIL_UNIQUE_INDEX", columnList = "email")})