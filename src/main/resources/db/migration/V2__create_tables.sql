CREATE TABLE room
(
    id          BIGINT IDENTITY (1,1) NOT NULL PRIMARY KEY,
    room_number INTEGER               NOT NULL,
    room_type   VARCHAR               NOT NULL
);

CREATE TABLE reservation
(
    id               BIGINT IDENTITY (1,1) NOT NULL PRIMARY KEY,
    user_name        VARCHAR               NOT NULL,
    number_of_people INT                   NOT NULL,
    start_date       DATE                  NOT NULL,
    end_date         DATE                  NOT NULL,
    room_id          BIGINT,
    FOREIGN KEY (room_id) REFERENCES room (id)
);
