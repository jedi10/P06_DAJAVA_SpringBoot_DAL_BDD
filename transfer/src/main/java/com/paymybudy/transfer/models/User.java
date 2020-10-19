package com.paymybudy.transfer.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bank_account_fk")
    @Getter
    @Setter
    private BankAccount bankAccount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="int_cash_account_fk")
    @Getter
    @Setter
    private InternalCashAccount internalCashAccount;

    @OneToOne(mappedBy = "user")
    @Getter
    @Setter
    private AppAccount appAccount;
/*
    @Getter
    private List<User> contactList;
*/

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

   */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}


//https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/table-annotation-unique-constraints.html
//https://code-examples.net/en/q/5c11f1
//https://stackoverflow.com/questions/3405229/specifying-an-index-non-unique-key-using-jpa
// indexes = {@Index(name = "USER_EMAIL_UNIQUE_INDEX", columnList = "email")})