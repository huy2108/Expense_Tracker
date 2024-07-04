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

@Database(entities = [ExpenseEntity::class], version = 2)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {

        const val DATABASE_NAME = "expense_database"

        // Migration from version 1 to version 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Modify the schema as required
                database.execSQL("ALTER TABLE expense_table ADD COLUMN bookmark INTEGER DEFAULT 0 NOT NULL")
            }
        }

        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    DATABASE_NAME
                ).addMigrations(MIGRATION_1_2).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        initBasicData(context)
                    }

                    fun initBasicData(context: Context) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = getDatabase(context).expenseDao()
                            dao.insertExpense(ExpenseEntity(1, "Salary", 6200.70, System.currentTimeMillis(), "Salary", "Income"))
                            dao.insertExpense(ExpenseEntity(2, "Netflix", 1000.70, System.currentTimeMillis(), "Netflix", "Expense"))
                            dao.insertExpense(ExpenseEntity(3, "Paypal", 500.30, System.currentTimeMillis(), "Paypal", "Expense"))
                            dao.insertExpense(ExpenseEntity(4, "Upwork", 2000.00, System.currentTimeMillis(), "Upwork", "Income"))
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
