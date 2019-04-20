package com.st.novatech.librarianapp.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.st.novatech.librarianapp.entity.Author;
import com.st.novatech.librarianapp.entity.Book;
import com.st.novatech.librarianapp.entity.Publisher;

/**
 * Tests of book DAO.
 *
 * @author Al Amine Ahmed Moussa
 * @author Jonathan Lovelace (integration and polishing)
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AlBookDaoTest {
	/**
	 * The DAO under test.
	 */
	@Autowired
	private BookDao bookDao;


	/**
	 * Test that creating a book works.
	 * @throws SQLException on database error
	 */
	@Test
	public void createTest() throws SQLException {
		final String str1 = "testTitle";

		final Author a = new Author(300, "testname");
		final Publisher p = new Publisher(300, "testname", "testaddress", "testphone");

		final Book book = bookDao.create(str1, a, p);
		assertEquals(str1, book.getTitle(), "created book has expected title");
	}

	/**
	 * Test that getting a book works.
	 * @throws SQLException on database error
	 */
	@Test
	public void testGet() throws SQLException {
		
		final Author a = new Author(300, "testname");
		final Publisher p = new Publisher(300, "testname", "testaddress", "testphone");
		
		final Book b = bookDao.create("50 down", a, p);

		assertEquals(b.getTitle(), bookDao.findById(b.getId()).get().getTitle(),
				"retrieved book has expected title");
	}
}
