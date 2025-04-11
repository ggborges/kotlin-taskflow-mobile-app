package borges.gustavo.taskflow.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import borges.gustavo.taskflow.database.dao.TaskDao
import borges.gustavo.taskflow.database.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 2, exportSchema = false)
abstract class RoomDB : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Tenta adicionar a coluna isCompleted
                try {
                    database.execSQL("ALTER TABLE task ADD COLUMN isCompleted INTEGER NOT NULL DEFAULT 0")
                } catch (e: Exception) {
                    // Caso a tabela 'task' não exista, cria ela do zero
                    database.execSQL("""
                        CREATE TABLE IF NOT EXISTS `task` (
                            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            `title` TEXT NOT NULL,
                            `description` TEXT,
                            `priority` INTEGER NOT NULL,
                            `isCompleted` INTEGER NOT NULL DEFAULT 0
                        )
                    """)
                }
            }
        }

        fun getDatabase(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "tasks"
                )
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2) // Adicionando a migração
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}