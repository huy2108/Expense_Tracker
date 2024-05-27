package com.example.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase(){

    abstract fun expenseDao(): ExpenseDao

    companion object{

        val DATABASE_NAME = "expense_database"

        @JvmStatic
        fun getDatabase(context: Context): ExpenseDatabase {
            return Room.databaseBuilder(
                context,
                ExpenseDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase){
                    super.onCreate(db)
                    initBasicData(context)
                }

                fun initBasicData(context: Context){
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = getDatabase(context).expenseDao()
                        dao.insertExpense(ExpenseEntity(1,"Salary", 6200.70, System.currentTimeMillis(), "Salary","Income"))
                        dao.insertExpense(ExpenseEntity(2,"Netflix", 1000.70, System.currentTimeMillis(), "Netflix","Expense"))
                        dao.insertExpense(ExpenseEntity(3,"Paypal", 500.30, System.currentTimeMillis(), "Paypal","Expense"))
                        dao.insertExpense(ExpenseEntity(4,"Upwork", 2000.00, System.currentTimeMillis(), "Upwork","Income"))
                    }
                }
            }).build()
        }
    }

}