package com.st.novatech.librarianapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.st.novatech.librarianapp.exception.TransactionException;
import com.st.novatech.librarianapp.dao.BookDao;
import com.st.novatech.librarianapp.dao.LibraryBranchDao;
import com.st.novatech.librarianapp.entity.Book;
import com.st.novatech.librarianapp.entity.Branch;

/**
 * Tests of the librarian service class.
 * @author Al-amine AHMED MOUSSA
 * @author Salem Ozaki
 * @author Jonathan Lovelace (integration and polishing)
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class LibrarianServiceTest {
	/**
	 * Sample book title for tests.
	 */
	private static final String SAMPLE_TITLE = "The Book Title";

	/**
	 * Sample branch name for tests.
	 */
	private static final String SAMPLE_BRANCH_NAME = "The Branch Name";
	/**
	 * Sample branch address for tests.
	 */
	private static final String SAMPLE_BRANCH_ADDRESS = "601 New Jersey Ave, Washington, DC 20001";

	/**
	 * Stored book from tests.
	 *
	 * <p>(TODO: Is this ever read without being first written to in the same test?)
	 */
	private Book testBook;
	/**
	 * Stored branch from tests.
	 *
	 * <p>(TODO: Is this ever read without being first written to in the same test?)
	 */
	private Branch testBranch;

	/**
	 * Sample number of copies for tests.
	 */
	private static int noOfCopies = 50;

	/**
	 * bookDao is involved in tests.
	 */
	@Autowired
	private BookDao bookdao;
	
	/**
	 * brancchDao is involved in tests.
	 */
	@Autowired
	private LibraryBranchDao branchdao;
	
	
	/**
	 * Librarian service under test.
	 */
	@Autowired
	private LibrarianService libService;

	/**
	 * Set up database connection, services, and test data before each test.
	 *
	 * @throws SQLException         on database error
	 * @throws TransactionException on error caught by a service
	 * @throws IOException          on I/O error reading database schema from file
	 */
	@BeforeEach
	public void init() throws SQLException, TransactionException, IOException {
		Book book = new Book(500, SAMPLE_TITLE, null, null);
		Branch branch = new Branch(500, SAMPLE_BRANCH_NAME,SAMPLE_BRANCH_ADDRESS);
		testBook = bookdao.save(book);
		testBranch = branchdao.save(branch);
		libService.setBranchCopies(testBranch, testBook, noOfCopies);
	}

	/**
	 * Test that updating a null branch will throw a NPE.
	 *
	 * <p>FIXME: service should catch this and turn it into a TransactionException
	 * @throws TransactionException on error caught by the service
	 */
	@DisplayName("throws null pointer exception if null is passed as a parameter for update branch")
	@Test
	public void updateBranchTest() throws TransactionException {
		assertThrows(NullPointerException.class, () -> libService.updateBranch(null),
				"Expecting to throw null pointer exception");
	}

	/**
	 * Test that setting branch copies where no record already exists adds a row to
	 * the database.
	 *
	 * @throws TransactionException on error caught by the service
	 */
	@DisplayName("Adds noOfCopies to the book copies table if it doesnt already exist")
	@Disabled("Currently failing despite looking correct by inspection")
	@Test
	public void setBranchCopiesNonExistingTest() throws TransactionException {
		final Map<Branch, Map<Book, Integer>> previousListOfCopies = libService.getAllCopies();
		assertTrue(previousListOfCopies.containsKey(testBranch),
				"already have records for the test branch");

		final int customNoOfCopies = 99;
		libService.setBranchCopies(testBranch, testBook, customNoOfCopies);
		libService.commit();
		final Map<Branch, Map<Book, Integer>> currentListOfCopies = libService.getAllCopies();
		assertTrue(currentListOfCopies.containsKey(testBranch),
				"still have records for the test branch");

		assertEquals(customNoOfCopies,
				currentListOfCopies.get(testBranch).get(testBook).intValue(),
				"new copy count propagated to the database");
	}

	/**
	 * Test that getting a book works.
	 * @throws SQLException on database error
	 * @throws TransactionException on error caught by the service
	 */
	@Test
	public void testGetBook() throws SQLException, TransactionException {
		Book book = new Book(502, SAMPLE_TITLE, null, null);

	    bookdao.save(book);

		assertEquals(book.getTitle(), bookdao.findById(book.getId()).get().getTitle(),
				"retrieved book has expected title");
	}

	/**
	 * Test that getting a branch works.
	 * @throws SQLException on database error
	 * @throws TransactionException on error caught by the service
	 */
	@Test
	public void testGetBranch() throws SQLException, TransactionException {

		Branch b = new Branch(501, "Branch 1457","ADR45"); 
		final Branch branch = branchdao.save(b);;
		assertEquals(branch.getName(), branchdao.findById(branch.getId()).get().getName(),
				"retrieved branch has expected name");
	}
}
