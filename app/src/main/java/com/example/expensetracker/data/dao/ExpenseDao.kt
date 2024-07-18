package com.example.expensetracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for accessing and managing ExpenseEntity data in the database.
 */
@Dao
interface ExpenseDao {

    /**
     * Retrieves an expense by its unique identifier.
     *
     * @param id The identifier of the expense to retrieve.
     * @return Flow representing the expense entity with the given ID, or null if not found.
     */
    @Query("SELECT * FROM expense_table WHERE id = :id")
    fun getExpenseById(id: Int): Flow<ExpenseEntity?>

    /**
     * Inserts a new expense entity into the database.
     *
     * @param expenseEntity The expense entity to insert.
     */
    @Insert
    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    /**
     * Deletes an expense entity from the database.
     *
     * @param expenseEntity The expense entity to delete.
     */
    @Delete
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)

    /**
     * Updates an existing expense entity in the database.
     *
     * @param expense The updated expense entity.
     */
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    /**
     * Retrieves all expenses from the database.
     *
     * @return Flow representing a list of all expense entities.
     */
    @Query("SELECT * FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    /**
     * Retrieves all expenses from the database, ordered by date in descending order.
     *
     * @return Flow representing a list of all expense entities ordered by date descending.
     */
    @Query("SELECT * FROM expense_table ORDER BY date DESC")
    fun getAllExpensesOrderedByDateDesc(): Flow<List<ExpenseEntity>>

    /**
     * Retrieves expenses of a specific type from the database, ordered by date in descending order.
     *
     * @param expenseType The type of expenses to retrieve (e.g., "Income" or "Expense").
     * @return Flow representing a list of expense entities of the specified type ordered by date descending.
     */
    @Query("SELECT * FROM expense_table WHERE type = :expenseType ORDER BY date DESC")
    fun getExpensesByType(expenseType: String): Flow<List<ExpenseEntity>>

    /**
     * Deletes all expense entities from the database.
     */
    @Query("DELETE FROM expense_table")
    suspend fun deleteAll()

    /**
     * Retrieves expenses that are bookmarked (bookmark = true) from the database.
     *
     * @return Flow representing a list of expense entities that are bookmarked.
     */
    @Query("SELECT * FROM expense_table WHERE bookmark = 1")
    fun getExpensesByBookmarkTrue(): Flow<List<ExpenseEntity>>
}
