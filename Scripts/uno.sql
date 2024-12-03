DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS SendsMessage;
DROP TABLE IF EXISTS HasContacts;

CREATE TABLE User(
Username VARCHAR(50),
Password VARCHAR(16));

CREATE TABLE SendsMessage(
Msg VARCHAR(500),
FromUser VARCHAR(50),
ToUser VARCHAR(50),
MsgDate DATE);

CREATE TABLE HasContacts(
User        VARCHAR(50),
Contact     VARCHAR(50));

ALTER TABLE User
ADD CONSTRAINT User_Username_PK PRIMARY KEY(Username);

ALTER TABLE SendsMessage
ADD CONSTRAINT SendsMessage_fromUser_toUser_pk PRIMARY KEY(FromUser, ToUser);

ALTER TABLE HasContacts
ADD CONSTRAINT HasContacts_fromUser_toUser_pk PRIMARY KEY(User, Contact);

ALTER TABLE SendsMessage
ADD CONSTRAINT user_touser_fk FOREIGN KEY(ToUser)
REFERENCES User(Username);

ALTER TABLE SendsMessage
ADD CONSTRAINT user_fromuser_fk FOREIGN KEY(FromUser)
REFERENCES User(Username);

ALTER TABLE HasContacts
ADD CONSTRAINT user_user_fk FOREIGN KEY(User)
REFERENCES User(Username);

ALTER TABLE HasContacts
ADD CONSTRAINT user_contact_fk FOREIGN KEY(Contact)
REFERENCES User(Username);