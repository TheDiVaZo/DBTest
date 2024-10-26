package me.thedivazo.dbtest;

import me.thedivazo.dbtest.db.tables.Author;
import me.thedivazo.dbtest.db.tables.records.AuthorRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        DSLContext dslContext = DSL.using(connection);

        //Так как у меня БД в оперативной памяти для упрощения тестирования, я ее здесь инициализирую таблицами
        //По идее она уже должна быть инициализирована

        System.out.println("Инициализация базы");
        dslContext.createTable(Author.AUTHOR).columns(Author.AUTHOR.fields()).primaryKey(Author.AUTHOR.ID).execute();

        //Далее идет работа с базой
        System.out.println("Добавляем автора через ActiveRecord");
        AuthorRecord authorRecord = dslContext.newRecord(Author.AUTHOR);
        authorRecord.setId(1);
        authorRecord.setFirstName("Чпок");
        authorRecord.setLastName("Пупок");
        authorRecord.setDateOfBirth(LocalDate.of(1977, 7, 7));
        //insert into
        authorRecord.store();

        System.out.println("Добавляем автора через dslContext");
        dslContext.insertInto(Author.AUTHOR)
                .set(Author.AUTHOR.ID, 2)
                .set(Author.AUTHOR.FIRST_NAME, "Евгений")
                .set(Author.AUTHOR.LAST_NAME, "Джугошвили")
                .set(Author.AUTHOR.DATE_OF_BIRTH, LocalDate.of(1966, 6, 6))
                .execute();

        //Выводим всех авторов
        dslContext.selectFrom(Author.AUTHOR).forEach(System.out::println);
    }
}