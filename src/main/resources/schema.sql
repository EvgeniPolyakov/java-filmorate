﻿CREATE TABLE users (
    email VARCHAR(255) PRIMARY KEY,
    login VARCHAR(255) NOT NULL,
    user_name VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS films (
    film_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    release_date DATE,
    duration INT,
    mpa_rating_id INT
);

CREATE TABLE IF NOT EXISTS mpa_rating (
    rating_id INT PRIMARY KEY,
    rating_value VARCHAR(4)
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id INT NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id INT PRIMARY KEY,
    genre VARCHAR(11)
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id INT NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    PRIMARY KEY (film_id, user_email)
);

CREATE TABLE IF NOT EXISTS users_friends (
    user_email VARCHAR(255) NOT NULL,
    friend_email VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_email, friend_email)
);

ALTER TABLE mpa_rating ADD CONSTRAINT fk_mpa_rating_rating_id FOREIGN KEY(rating_id)
    REFERENCES films (mpa_rating_id);

ALTER TABLE film_genre ADD CONSTRAINT fk_film_genre_film_id FOREIGN KEY(film_id)
    REFERENCES films (film_id) ON DELETE CASCADE;

ALTER TABLE film_genre ADD CONSTRAINT fk_film_genre_genre_id FOREIGN KEY(genre_id)
    REFERENCES genre (genre_id) ON DELETE CASCADE;

ALTER TABLE film_likes ADD CONSTRAINT fk_film_likes_film_id FOREIGN KEY(film_id)
    REFERENCES films (film_id) ON DELETE CASCADE;

ALTER TABLE film_likes ADD CONSTRAINT fk_film_likes_user_email FOREIGN KEY(user_email)
    REFERENCES users (email) ON DELETE CASCADE;

ALTER TABLE users_friends ADD CONSTRAINT fk_users_friends_user_email FOREIGN KEY(user_email)
    REFERENCES users (email) ON DELETE CASCADE;

ALTER TABLE users_friends ADD CONSTRAINT fk_users_friends_friend_email FOREIGN KEY(friend_email)
    REFERENCES users_friends (user_email) ON DELETE CASCADE;
