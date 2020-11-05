

INSERT INTO user (user_id, email, first_name, last_name, password) VALUES
                        (1, 'tobiac@paymybuddy.com','Tobiac','Bonterre','XXXX'),
                        (2, 'nono@paymybuddy.com','Nono','Cyborg','XXXX'),
                        (3, 'gille@paymybuddy.com','Gille','Rotchield','XXXX');

INSERT INTO app_account (app_account_id, app_account_status, last_connection, status_date, user_fk) VALUES
            (1, 'CONFIRMED', current_timestamp(), '2020-10-22', 1),
            (2, 'CONFIRMED', current_timestamp(), '2020-10-25', 2),
            (3, 'CONFIRMED', current_timestamp(), '2020-10-26', 3);

INSERT INTO bank_account (bank_account_id, address, name, tel_number) VALUES
            (1, '35 place vendome 75000 Paris', 'BNR', '0154564433'),
            (2, '22 Centre des Halles 75000 Paris', 'PashiBank', '0122334455'),
            (3, '4 allée de la souche 75000 Paris', 'TustowBank', '0199887766');

INSERT INTO int_cash_account (int_cash_account_id, amount, currency, libelle, number) VALUES
            (1, '3000', 'Euros', 'Compte Mr Tobiac Bonterre', '13456789654'),
            (2, '2500', 'Euros', 'Compte Mr Nono Cyborg', '01234565433'),
            (3, '500000', 'Euros', 'Compte Mr Gille Rotchield', '01234567430');

UPDATE user SET `bank_account_fk` = '1', `int_cash_account_fk` = '1' WHERE `user`.`user_id` = 1;
UPDATE user SET `bank_account_fk` = '2', `int_cash_account_fk` = '2' WHERE `user`.`user_id` = 2;
UPDATE user SET `bank_account_fk` = '3', `int_cash_account_fk` = '3' WHERE `user`.`user_id` = 3;

INSERT INTO external_transaction (ext_transaction_id, amount, description, status, status_date, int_cash_account_fk, bank_account_fk) VALUES
            (1, '3000', 'Virement Externe 2345654', 'FINISHED', current_timestamp(), 1, 1),
            (2, '2500', 'Virement Externe 2356453', 'FINISHED', current_timestamp(), 2, 2),
            (3, '500000', 'Virement Externe 65434453', 'FINISHED', current_timestamp(), 3, 3);

INSERT INTO user_friends (`user_fk`, `friend_fk`) VALUES
            (3, 1), (1, 3),
            (1, 2), (2, 1);

INSERT INTO internal_transaction (int_transaction_id, amount, description, status, status_date, transaction_message) VALUES
            (1, '500', 'paiement réparation voiture', 'FINISHED', current_timestamp(), 'OK'),
            (2, '215', 'Paiement coupe des arbres', 'FINISHED', current_timestamp(), 'OK');

INSERT INTO money_transfer_type (`int_cash_account_id`, `int_transaction_id`, `is_credit`) VALUES
            (3, 1, 0), (1, 1, 1),
            (1, 2, 0), (2, 2, 1);

#https://www.baeldung.com/spring-boot-data-sql-and-schema-sql
#https://dev.mysql.com/doc/refman/8.0/en/charset-database.html
#http://zetcode.com/springboot/datasourcebuilder/