package com.example.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Room database class for managing expense data.
 */
@Database(entities = [ExpenseEntity::class], version = 2)
abstract class ExpenseDatabase : RoomDatabase() {

    /**
     * Provides access to ExpenseDao for interacting with the database.
     *
     * @return ExpenseDao instance.
     */
    abstract fun expenseDao(): ExpenseDao

    companion object {

        /**
         * Database name constant.
         */
        const val DATABASE_NAME = "expense_database"

        /**
         * Migration from version 1 to version 2 of the database schema.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic to modify schema from version 1 to version 2
                database.execSQL("ALTER TABLE expense_table ADD COLUMN bookmark INTEGER DEFAULT 0 NOT NULL")
            }
        }

        /**
         * Singleton instance of the database.
         */
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        /**
         * Retrieves the singleton instance of the ExpenseDatabase.
         * If the instance is not initialized, creates a new instance.
         *
         * @param context Application context used to create the database.
         * @return ExpenseDatabase instance.
         */
        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    DATABASE_NAME
                )
                    // Add migrations to handle database schema changes
                    .addMigrations(MIGRATION_1_2)
                    // Add callback to populate initial data upon database creation
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Initialize basic data on database creation
                            initBasicData(context)
                        }

                        /**
                         * Initializes basic data in the database upon creation.
                         *
                         * @param context Application context used to access resources.
                         */
                        private fun initBasicData(context: Context) {
                            // CoroutineScope to launch a coroutine in IO dispatcher
                            CoroutineScope(Dispatchers.IO).launch {
                                // Access ExpenseDao from the database instance
                                val dao = getDatabase(context).expenseDao()
                                // Insert sample expense records into the database
                                dao.insertExpense(ExpenseEntity(1, "Salary", 6200.70, System.currentTimeMillis(), "Salary", "Income"))
                                dao.insertExpense(ExpenseEntity(2, "Netflix", 1000.70, System.currentTimeMillis(), "Netflix", "Expense"))
                                dao.insertExpense(ExpenseEntity(3, "Paypal", 500.30, System.currentTimeMillis(), "Paypal", "Expense"))
                                dao.insertExpense(ExpenseEntity(4, "Upwork", 2000.00, System.currentTimeMillis(), "Upwork", "Income"))
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
