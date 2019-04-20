package com.st.novatech.librarianapp.service;

import java.util.List;

import com.st.novatech.librarianapp.entity.Branch;
import com.st.novatech.librarianapp.exception.TransactionException;


/**
 * A base interface that all service interfaces extend.
 * @author Al-amine AHMED MOUSSA
 */
public interface Service {
	/**
	 * Get a list (order should not be relied on) of all the library branches in the
	 * database.
	 *
	 * @return all the borrowers in the database.
	 */
	List<Branch> getAllBranches() throws TransactionException;
	/**
	 * Commit all outstanding operations to the database, if the backend supports transactions.
	 */
	void commit() throws TransactionException;
	/**
	 * Begin a transaction, if not already in one.
	 */
	void beginTransaction() throws TransactionException;
}
