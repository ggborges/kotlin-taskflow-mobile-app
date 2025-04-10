package borges.gustavo.taskflow.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import borges.gustavo.taskflow.database.dao.TaskDao
import borges.gustavo.taskflow.database.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}