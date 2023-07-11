package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {
    private final FilmDBStorage filmDBStorage;
    private final UserDBStorage userDBStorage;
    private final GenreDBStorage genreDBStorage;
    private final LikeDBStorage likeDBStorage;
    private final MPADBStorage mpaDBStorage;
    private final FriendDBStorage friendDBStorage;

    @DisplayName("Тест получения имени жанра по ID")
    @Test
    public void getGenreByIdTest() {
        Genre genre = genreDBStorage.getGenreById(1);
        Assertions.assertEquals("Комедия", genre.getName(), "Ожидался корректное имя жанра");
    }

    @DisplayName("Тест получения жанров по Film ID")
    @Test
    public void getGenresForFilmTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(2, "Драма")), new MPA(1, "G"), 0);
        filmDBStorage.createFilm(actualFilm);
        List<Genre> genres = new ArrayList<>(genreDBStorage.getGenresForFilm(1));
        Assertions.assertEquals("Драма", genres.get(0).getName(), "Ожидался корректный жанр");
    }

    @DisplayName("Тест получения всего списка жанров ")
    @Test
    public void getAllGenresTest() {
        Assertions.assertEquals(6, genreDBStorage.getAllGenre().size(),
                "Ожидались корректный список жанров");
    }


    @DisplayName("Тест получения названия рейтинга по ID")
    @Test
    public void getMPAByIdTest() {
        MPA mpa = new MPA(4, "R");
        Assertions.assertEquals(mpa, mpaDBStorage.getMPAById(4), "Ожидался корректный рейтинг");
    }

    @DisplayName("Тест получения всех рейтингов")
    @Test
    public void getAllMpaTest() {
        Assertions.assertEquals(5, mpaDBStorage.getAllMpa().size(),
                "Ожидались корректный список рейтингов");
    }


    @DisplayName("Тест создания новго пользователя")
    @Test
    public void createUserTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        userDBStorage.createUser(actualUser);
        Assertions.assertEquals(actualUser, userDBStorage.getUserById(1),
                "Ожидался корректный новый пользователь");
    }

    //
    @DisplayName("Тест обновления данных у существующего пользователя")
    @Test
    public void updateUserTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        User expectedUser = new User(1, "yandex@yandex.ru", "UpdateTyler", "Update: The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        userDBStorage.createUser(actualUser);
        userDBStorage.updateUser(expectedUser);
        Assertions.assertEquals(expectedUser, userDBStorage.getUserById(1), "Ожидался корректный обновленный пользователь");
    }

    @DisplayName("Тест получения существующего пользователя по ID")
    @Test
    public void getUserById() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        userDBStorage.createUser(actualUser);
        Assertions.assertEquals(actualUser, userDBStorage.getUserById(1), "Ожидался корректный пользователь");
    }

    @DisplayName("Тест получения всех пользователей")
    @Test
    public void getAllUsersTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        User expectedUser = new User(1, "yandex@yandex.ru", "UpdateTyler", "Update: The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());

        userDBStorage.createUser(actualUser);
        userDBStorage.createUser(expectedUser);
        Assertions.assertEquals(2, userDBStorage.getAllUsers().size(), "Ожидался коррктный список пользователей");
    }

    @DisplayName("Тест создания новго фильма")
    @Test
    public void createFilmTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(2, "Драма")), new MPA(1, "G"), 0);

        filmDBStorage.createFilm(actualFilm);
        Assertions.assertEquals(actualFilm, filmDBStorage.getFilmById(1), "Ожидался корректный новый фильм");
    }

    @DisplayName("Тест обновления данных существующего фильма")
    @Test
    public void updateFilmTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(2, "Драма")), new MPA(1, "G"), 0);
        Film expectedFilm = new Film(1, "UPDATE INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(3, "Мультфильм")), new MPA(4, "R"), 1);

        filmDBStorage.createFilm(actualFilm);
        filmDBStorage.updateFilm(expectedFilm);
        Assertions.assertEquals(expectedFilm, filmDBStorage.getFilmById(1), "Ожидался корректный обновленный фильм");
    }

    @DisplayName("Тест получения всех фильмов")
    @Test
    public void readAllFilms() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(2, "Драма")), new MPA(1, "G"), 0);
        Film expectedFilm = new Film(1, "UPDATE INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(3, "Мультфильм")), new MPA(4, "R"), 1);

        filmDBStorage.createFilm(actualFilm);
        filmDBStorage.createFilm(expectedFilm);
        Assertions.assertEquals(2, filmDBStorage.getAllFilms().size(), "Ожидались корректный список фильмов");
    }

    @DisplayName("Тест получения фильма по ID")
    @Test
    public void getFilmByIdTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(2, "Драма")), new MPA(1, "G"), 0);
        filmDBStorage.createFilm(actualFilm);
        Assertions.assertEquals(actualFilm, filmDBStorage.getFilmById(1), "Ожидался определенный фильм с ID");
    }

    @DisplayName("Тест создания Like у фильма")
    @Test
    public void addLikeTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(2, "Драма")), new MPA(1, "G"), 0);
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        filmDBStorage.createFilm(actualFilm);
        userDBStorage.createUser(actualUser);
        likeDBStorage.addLike(1, 1);
        Assertions.assertEquals(1, filmDBStorage.getFilmById(1).getRate(),
                "Ожидались новый Like у фильма");
    }

    @DisplayName("Тест удаления Like у фильма")
    @Test
    public void deleteLikeTest() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(2, "Драма")), new MPA(1, "G"), 0);
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        filmDBStorage.createFilm(actualFilm);
        userDBStorage.createUser(actualUser);
        likeDBStorage.addLike(1, 1);
        Assertions.assertEquals(1, filmDBStorage.getFilmById(1).getRate(),
                "Ожидались новый Like у фильма");

        likeDBStorage.deleteLike(1, 1);
        Assertions.assertEquals(0, filmDBStorage.getFilmById(1).getRate(),
                "Ожидалось удаление старого Like у фильма");
    }

    @DisplayName("Тест получения ID пользователей которым понравился фильм")
    @Test
    public void getAllLikesTestByFilmId() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh", LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(2, "Драма")), new MPA(1, "G"), 0);
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        User expectedUser = new User(1, "yandex@yandex.ru", "UpdateTyler", "Update: The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        filmDBStorage.createFilm(actualFilm);
        userDBStorage.createUser(actualUser);
        userDBStorage.createUser(expectedUser);

        likeDBStorage.addLike(1, 1);
        likeDBStorage.addLike(1, 2);

        Integer[] expected = {1, 2};
        Assertions.assertArrayEquals(expected, likeDBStorage.getLikesFilmId(1).toArray(),
                "Ожидалось получение ID пользователей которым понравился фильм");
    }

    @DisplayName("Тест получения популярных фильмов")
    @Test
    public void getPopularFilms() {
        Film actualFilm = new Film(1, "INTERSTELLAR", "qwertyasdfgh",
                LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(2, "Драма")), new MPA(1, "G"), 9);

        Film expectedFilm = new Film(1, "UPDATE INTERSTELLAR", "qwertyasdfgh",
                LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(3, "Мультфильм")), new MPA(4, "R"), 10);

        Film expectedFilm1 = new Film(1, "UPDATE INTERSTELLAR", "qwertyasdfgh",
                LocalDate.of(2014, 11, 6),
                169, Set.of(new Genre(3, "Мультфильм")), new MPA(4, "R"), 1);
        filmDBStorage.createFilm(actualFilm);
        filmDBStorage.createFilm(expectedFilm);
        filmDBStorage.createFilm(expectedFilm1);

        Film[] expected = {expectedFilm, actualFilm};
        Assertions.assertArrayEquals(expected, filmDBStorage.getPopularFilms(2).toArray());
    }

    @DisplayName("Тест добавления к пользователю друга")
    @Test
    public void addFriendTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        User expectedUser = new User(1, "yandex@yandex.ru", "UpdateTyler", "Update: The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        userDBStorage.createUser(actualUser);
        userDBStorage.createUser(expectedUser);
        friendDBStorage.addFriend(1, 2);
        User[] expected = {expectedUser};
        Assertions.assertArrayEquals(expected, friendDBStorage.getFriendsById(1).toArray(),
                "Ожидалось добавление к пользователю друга");
    }

    @DisplayName("Тест удаления у пользователя друга")
    @Test
    public void deleteFriendByUserIdTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        User expectedUser = new User(1, "yandex@yandex.ru", "UpdateTyler", "Update: The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        userDBStorage.createUser(actualUser);
        userDBStorage.createUser(expectedUser);
        friendDBStorage.addFriend(1, 2);
        User[] expected = {expectedUser};
        Assertions.assertArrayEquals(expected, friendDBStorage.getFriendsById(1).toArray(),
                "Ожидалось добавление к пользователю друга");
        friendDBStorage.deleteFriend(1, 2);
        User[] expected1 = {};
        Assertions.assertArrayEquals(expected1, friendDBStorage.getFriendsById(1).toArray(),
                "Ожидалось удаление у пользователя друга");


    }

    @DisplayName("Тест получения всех друзей пользователя")
    @Test
    public void getAllFriendByUserIdTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        User expectedUser = new User(1, "yandex@yandex.ru", "UpdateTyler", "Update: The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        User expectedUser1 = new User(1, "yandex@yandex.ru", "UpdateTyler1", "Update: The Creator1",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        userDBStorage.createUser(actualUser);
        userDBStorage.createUser(expectedUser);
        userDBStorage.createUser(expectedUser1);
        friendDBStorage.addFriend(1, 2);
        friendDBStorage.addFriend(1, 3);
        User[] expected = {expectedUser1, expectedUser};
        Assertions.assertArrayEquals(expected, friendDBStorage.getFriendsById(1).toArray(),
                "Ожидалось получение всех друзей пользователя");
    }

    @DisplayName("Тест получения общих друзей между двумя пользователями")
    @Test
    public void getCommonFriendsTest() {
        User actualUser = new User(1, "yandex@yandex.ru", "Tyler", "The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        User expectedUser = new User(1, "yandex@yandex.ru", "UpdateTyler", "Update: The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        User expectedactualUser = new User(1, "yandex@yandex.ru", "UpdateTyler", "Update: The Creator",
                LocalDate.of(2000, 1, 2), new HashSet<>());
        userDBStorage.createUser(actualUser);
        userDBStorage.createUser(expectedUser);
        userDBStorage.createUser(expectedactualUser);
        friendDBStorage.addFriend(1, 2);
        friendDBStorage.addFriend(3, 2);
        User[] expected = {expectedUser};
        Assertions.assertArrayEquals(expected, friendDBStorage.getCommonFriends(1, 3).toArray(),
                "Ожидалось получение общего друга между пользователями");
    }
}