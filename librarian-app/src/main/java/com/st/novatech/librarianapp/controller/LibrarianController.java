package com.st.novatech.librarianapp.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.st.novatech.librarianapp.entity.Book;
import com.st.novatech.librarianapp.entity.Branch;
import com.st.novatech.librarianapp.entity.BranchCopies;
import com.st.novatech.librarianapp.exception.RetrieveException;
import com.st.novatech.librarianapp.exception.TransactionException;
import com.st.novatech.librarianapp.service.LibrarianService;

/**
 * Controller for Librarian Services.
 * @author Al-amine AHMED MOUSSA
 */
@RestController
@RequestMapping("/librarian")
public final class LibrarianController {
	/**
	 * Service class used to handle requests.
	 */
	@Autowired
	private LibrarianService service;

	@RequestMapping({"/branches", "/branches/"})
	public List<Branch> getbranchs() throws TransactionException {
		return service.getAllBranches();
	}

	@RequestMapping({"/books", "/books/"})
	public List<Book> getBooks() throws TransactionException {
		return service.getAllBooks();
	}
	
	@RequestMapping({"/branch/{branchId}", "/branch/{branchId}/"})
	public ResponseEntity<Branch>  getBranch(@PathVariable("branchId") final int branchId)
			throws TransactionException {
		final Branch branch = service.getbranch(branchId);
		if (branch == null) {
			throw new RetrieveException("Branch not found");
		} else {
			
			return new ResponseEntity<>(branch, HttpStatus.OK);

		}
	}
	
	
	@RequestMapping({"/book/{bookId}", "/book/{bookId}/"})
	public ResponseEntity<Book> getBook(@PathVariable("bookId") final int bookId)
			throws TransactionException {
		final Book book = service.getBook(bookId);
		if (book == null) {
			throw new RetrieveException("Book not found");
		} else {
			return new ResponseEntity<>(book, HttpStatus.OK);
		}
	}

	@RequestMapping(path = { "/branch/{branchId}", "/branch/{branchId}/" }, method = RequestMethod.PUT)
	public Branch updateBranch(@PathVariable("branchId") final int branchId,@RequestBody Branch input)
			throws TransactionException {
		final Branch branch = service.getbranch(branchId);
		if (branch == null) {
			throw new RetrieveException("Branch not found");
		} else {
			branch.setName(input.getName());
			branch.setAddress(input.getAddress());
			service.updateBranch(branch);
			Branch updatedBranch = service.getbranch(branchId);
			return updatedBranch;
//			return new ResponseEntity<>(updatedBranch, HttpStatus.OK);
			
		}
	}

	
	@RequestMapping(path = { "/branch/{branchId}/book/{bookId}",
			"/branch/{branchId}/book/{bookId}/" }, method = RequestMethod.PUT)
	public ResponseEntity<BranchCopies> setBranchCopies(@PathVariable("branchId") int branchId,
			@PathVariable("bookId") int bookId, @RequestParam("noOfCopies") int copies)

			throws TransactionException {

		final Branch branch = service.getbranch(branchId);
		final Book book = service.getBook(bookId);
		
		 if (branch == null) {
				throw new RetrieveException("Requested branch not found");
			} else if (book == null) {
				throw new RetrieveException("Requested book not found");
			} else {

		service.setBranchCopies(branch, book, copies);

		int foundNumberOfCopies = service.getCopies(service.getBook(bookId), service.getbranch(branchId));

		BranchCopies branchCopies = new BranchCopies(service.getBook(bookId), service.getbranch(branchId),
				foundNumberOfCopies);

		return new ResponseEntity<>(branchCopies, HttpStatus.OK);
		
			}

	}
	
	@RequestMapping(path = { "/branch/{branchId}/book/{bookId}",
	                          "/branch/{branchId}/book/{bookId}" }, method = RequestMethod.GET)
	public ResponseEntity<BranchCopies> getBranchCopies(@PathVariable("branchId") int branchId,
			        @PathVariable("bookId") int bookId) throws TransactionException {
		
		final Branch branch = service.getbranch(branchId);
		final Book book = service.getBook(bookId);
		
		 if (branch == null) {
				throw new RetrieveException("Requested branch not found");
			} else if (book == null) {
				throw new RetrieveException("Requested book not found");
			} else {
		
		BranchCopies branchcopies = new BranchCopies(book,branch,
				service.getCopies(book, branch));
		
		return new ResponseEntity<>(branchcopies, HttpStatus.OK);
		 	
			}
		
	}
	
	
	@RequestMapping({"/branches/books/copies", "/branches/books/copies/"})
	public Map<Branch, Map<Book, Integer>> getAllCopies() throws TransactionException {
		return service.getAllCopies();
	}
}