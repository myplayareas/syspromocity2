CREATE TABLE users (
  username VARCHAR(50)  NOT NULL PRIMARY KEY,
  password VARCHAR(255) NOT NULL,
  enabled  BOOLEAN      NOT NULL
)
  ENGINE = InnoDb;
CREATE TABLE authorities (
  username  VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (username) REFERENCES users (username),
  UNIQUE INDEX authorities_idx_1 (username, authority)
)
  ENGINE = InnoDb;

ALTER TABLE users ADD id BIGINT NOT NULL AUTO_INCREMENT,
    ADD INDEX (id);
ALTER TABLE users ID BIGINT NOT NULL AUTO_INCREMENT=1;

ALTER TABLE authorities ADD id BIGINT NOT NULL AUTO_INCREMENT;

--Insert and update admin data
insert users values('armando', '123456', true, null);

update users
set password='$2a$10$m5hVTpkyjUc8d5vKiNqKAOlLywOe11nw.QIo1dxh7DiUvMLg4VOMy'
where id=1	

insert authorities values('armando', 'USER');