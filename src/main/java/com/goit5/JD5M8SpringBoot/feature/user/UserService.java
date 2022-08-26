package com.goit5.JD5M8SpringBoot.feature.user;

import com.goit5.JD5M8SpringBoot.feature.user.dto.UserDTO;
import com.goit5.JD5M8SpringBoot.feature.user.dto.UserInfo;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager; // щось дуже схоже на гібернейт сешин фекторі (рівнем нижче)
    private final SessionFactory sessionFactory; //(ще рівнем нижче)
    private final NamedParameterJdbcTemplate jdbcTemplate; // (хардкорд рівень, типу най нищий) дозволяєвиконувати SQL запити, аналогія стейтмент

    /**
    @PostConstruct
    public void init() {
        //CREATE & UPDATE
        userRepository.save(new User());

        //READ
        userRepository.findAll();
        userRepository.findById("");

        //DELETE
        userRepository.delete(new User());
        userRepository.deleteById("");

        System.out.println(userRepository.getClass()); //class com.sun.proxy.$Proxy101
    }
    */

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean exists(String email) {
        if (email == null) {
            return false;
        }

        // високий рівень
        //return userRepository.existsById(email);

        // нежче високого рівеня
        //User user = entityManager.find(User.class, email);
        //return user != null;

        //ще нижче
        //Session session = sessionFactory.openSession();
        //User user = session.find(User.class, email);
        //session.close();
        //return user != null;

        // хард рівень
        Integer userCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM \"user\" WHERE email = :email",
                Map.of("email", email),
                Integer.class
        );
        //Alt+Enter / Inspection 'Constant conditions & exceptions' options / Suppress for statement

        //noinspection ConstantConditions
        return userCount == 1;
    }

    public void deleteById(String email) {
        userRepository.deleteById(email);
    }

    @Transactional //запит щось змінює має бути транзакційним
    public void deleteByIds(List<String> emails) {
        //@Transactional(rollbackOn = {NullPointerException.class}) --якщо винок ексепшин то потрібно робити ролбек
        //@Transactional(dontRollbackOn = {IOException.class}) --якщо винок ексепшин то НЕ потрібно робити ролбек

        //userRepository.deleteAllByEmails(emails);

        //з використанням хард рівня
        jdbcTemplate.update(
                "DELETE FROM \"user\" WHERE email IN (:emails)",
                Map.of("emails", emails)
        );
    }

    public List<User> search(String query)  {
        //return userRepository.search("%" + query + "%");
        //return userRepository.searchByNativeSqlQuery("%" + query + "%");

        //return userRepository.findAllById(
        //        userRepository.searchEmails("%" + query + "%")
        //);

        //хард рівень
        String sql =
                "SELECT email, full_name, birthday, gender\n" +
                "FROM \"user\"\n" +
                "WHERE lower(email) LIKE lower(:query) OR lower(full_name) LIKE lower(:query)";
        return jdbcTemplate.query(
                sql,
                Map.of("query", "%" + query + "%"),
                new UserRowMapper()
        );
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setEmail(rs.getString("email"));
            user.setFullName(rs.getString("full_name"));
            user.setBirthday(LocalDate.parse(rs.getString("birthday")));
            user.setGender(Gender.valueOf(rs.getString("gender")));
            return null;
        }
    }

    public int countPeopleOlderThan(int age) {
        //return (int) findAll().stream()
        //        .filter(u -> {
        //            int userAge = (int) ChronoUnit.YEARS.between(u.getBirthday(), LocalDate.now());
        //            return userAge > age;
        //        })
        //        .count();

        LocalDate maxBirthday = LocalDate.now().minusYears(age + 1);
        return userRepository.countOlderThan(maxBirthday);
    }

    public UserInfo getUserInfo(String email) {
        String sql =
                "SELECT u.email AS email, full_name, birthday, gender, address\n" +
                "FROM \"user\" u\n" +
                "LEFT JOIN user_address ua ON u.email = ua.email\n" +
                "WHERE u.email = :email";

        List<UserInfoItem> items = jdbcTemplate.query(
                sql,
                Map.of("email", email),
                new UserInfoItemMapper()
        );

        UserInfoItem firstItem = items.get(0);

        int age = (int) ChronoUnit.YEARS.between(firstItem.getBirthday(), LocalDate.now());
        UserDTO userDto = UserDTO.builder()
                .email(firstItem.getEmail())
                .fullName(firstItem.getFullName())
                .birthday(firstItem.getBirthday())
                .gender(firstItem.getGender())
                .age(age).build();

        List<String> addressList = items.stream()
                .map(UserInfoItem::getAddress)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        UserInfo result = new UserInfo();
        result.setUser(userDto);
        result.setAddresses(addressList);
        return result;
    }

    private static class UserInfoItemMapper implements RowMapper<UserInfoItem> {
        @Override
        public UserInfoItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return UserInfoItem.builder()
                    .email(rs.getString("email"))
                    .fullName(rs.getString("full_name"))
                    .birthday(LocalDate.parse(rs.getString("birthday")))
                    .gender(Gender.valueOf(rs.getString("gender")))
                    .address(rs.getString("address"))
                    .build();
        }
    }

    @Builder
    @Data
    private static class UserInfoItem {
        private String email;
        private String fullName;
        private LocalDate birthday;
        private Gender gender;
        private String address;
    }

    public UserInfo getUserInfoV2(String email) {
        User user = userRepository.findById(email).get();

        List<String> addressList = jdbcTemplate.queryForList(
                "SELECT address FROM user_address WHERE email = :email",
                Map.of("email", email),
                String.class
        );

        UserInfo result = new UserInfo();
        result.setUser(UserDTO.fromUser(user));
        result.setAddresses(addressList);

        return result;
    }

    public List<User> getUsersBetween(LocalDate start, LocalDate end) {
        //складний предикат3
        Specification<User> betweenSpec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.and( //умоза за якою об'єднані два предикати "і" (предикат3)
                        criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), start), //умова що день народження має бути >= параметра старт (предикат1)
                        criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), end) //умова що день народження маєбути <= end (предикат2)
                );
            }
        };
        return userRepository.findAll(betweenSpec);

        //скорочена версія тогож запису
        //return userRepository.findAll((root, cq, cb) -> cb.between(root.get("birthday"), start, end));
    }
}