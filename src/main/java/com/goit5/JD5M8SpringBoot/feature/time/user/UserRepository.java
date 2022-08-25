package com.goit5.JD5M8SpringBoot.feature.time.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // класс репозиторій для роботи с ентіті
    //JpaRepository<1,2> //1 - якого типу ентіті. 2 - якого типу первинний ключь

    @Query("from User u where lower(u.email) like lower(:query) or lower(u.fullName) like lower(:query)") //HQL
    List<User> search(@Param("query") String query);

    @Query(nativeQuery = true, value =
            "SELECT email, full_name, birthday, gender\n" +
            "FROM \"user\"\n" +
            "WHERE lower(email) LIKE lower(:query) OR lower(full_name) LIKE lower(:query)") //SQL
    List<User> searchByNativeSqlQuery(@Param("query") String query);

    @Query(nativeQuery = true, value =
            "SELECT email\n" +
            "FROM \"user\"\n" +
            "WHERE lower(email) LIKE lower(:query) OR lower(full_name) LIKE lower(:query)")
    List<String> searchEmails(@Param("query") String query);

    @Query(nativeQuery = true, value =
            "SELECT count(*) FROM \"user\" WHERE birthday < :maxBirthday")
    int countOlderThan(LocalDate maxBirthday);
}