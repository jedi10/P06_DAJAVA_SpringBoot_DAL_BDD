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

@Entity(name = "User")
@Table(name = "USER", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "first_name", length = 55)
    @Getter
    @Setter
    private String firstName;

    @Column(name = "last_name", length = 55)
    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    @Column(nullable = false, length = 100)
    private String email;

    @Getter
    @Setter
    @Column(nullable = false, length = 100)
    private String password;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="bank_account_fk")
    @Getter
    @Setter
    private BankAccount bankAccount;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="int_cash_account_fk")
    @Getter
    @Setter
    private InternalCashAccount internalCashAccount;

    @OneToOne(mappedBy = "user")
    @Getter
    @Setter
    private AppAccount appAccount;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_fk", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_fk", referencedColumnName = "user_id"))
    @Getter
    private List<User> contactList;


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
        setContactList(null);
    }

    public User() {
        super();
    }

    public void setContactList(List<User> userList){
        this.contactList = Optional.ofNullable(userList)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.toList());
        if(userList != null && userList.size() > 0){
            userList.forEach(e -> {
                if (!e.getContactList().contains(this)){
                    e.addOneContact(this);
                }
            });
        }
    }

    public void addOneContact(User contact){
        if (!this.contactList.contains(contact)){
            this.contactList.add(contact);
        }
        if (!contact.getContactList().contains(this)){
            contact.addOneContact(this);
        }
    }



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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", bankAccount=" + bankAccount +
                ", internalCashAccount=" + internalCashAccount +
                ", appAccount=" + appAccount +
                '}';
    }
}


//https://stuetzpunkt.wordpress.com/2013/10/19/jpa-recursive-manytomany-relationship/
//https://www.baeldung.com/jpa-many-to-many

//https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/table-annotation-unique-constraints.html
//https://code-examples.net/en/q/5c11f1
//https://stackoverflow.com/questions/3405229/specifying-an-index-non-unique-key-using-jpa
// indexes = {@Index(name = "USER_EMAIL_UNIQUE_INDEX", columnList = "email")})