# java-filmorate

## Структура базы данных:
![БД filmorate](https://i.ibb.co/NyfLY1M/Untitled.png)

* **films** - содержит основную информацию о фильмах;
* **users** - содержит основную информацию о пользователях;
* **likes** - содержит информацию о пользователях, которым понравился фильм;
* **genres** - содержит список всех возможных жанров фильмов;
* **film_genres** - содержит список жанров, которые относятся к конкретному фильму;
* **friends** - содержит информацию о подружившихся пользователях и статусе дружбы.

## Примеры запросов:

Список пользователей, которым понравился фильм:
```SQL
SELECT user_id
    FROM likes
    WHERE film_id = {id}
```

Список всех жанров фильма:
```SQL
SELECT genres.name
FROM film_genres
WHERE film_id = {id}
LEFT JOIN genres ON genres.genre_id = films_genres.genre_id
```
