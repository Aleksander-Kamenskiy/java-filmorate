DROP TABLE IF EXISTS users, films, rating_mpa, genre, genre_type, film_genre, likes, friends, friends_status CASCADE;

CREATE TABLE IF NOT EXISTS users (
     user_id  INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     name     VARCHAR(100) NOT NULL,
     email    VARCHAR(100) NOT NULL,
     login    VARCHAR(100) NOT NULL,
     birthday DATE NULL
);

CREATE TABLE IF NOT EXISTS friends_status (
    status_id INT UNIQUE NOT NULL,
    status_name varchar(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    friends_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id_1 INT REFERENCES users(user_id) ON DELETE CASCADE,
    user_id_2 INT REFERENCES users(user_id) ON DELETE CASCADE,
    friends_status_id INT REFERENCES friends_status(status_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS rating_mpa (
    mpa_id   INT UNIQUE NOT NULL,
    mpa_name VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id   INT UNIQUE NOT NULL,
    genre_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(1000) NOT NULL,
    description TEXT NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    rating_mpa_id INT REFERENCES rating_mpa(mpa_id) ON DELETE SET NULL,
    rate INT NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
    film_id INT REFERENCES films(film_id) ON DELETE CASCADE,
    genre_id INT REFERENCES genre(genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
    user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
    film_id INT REFERENCES films(film_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, film_id)
);












