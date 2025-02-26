import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.jsmart.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "UserDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE users (id TEXT PRIMARY KEY, name TEXT, email TEXT, profilePhoto TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun insertUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", user.id)
            put("name", user.name)
            put("email", user.email)
            put("profilePhoto", user.profilePhoto)
        }
        db.insert("users", null, values)
        db.close()
    }

    fun getUserById(userId: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", arrayOf(userId))
        return if (cursor.moveToFirst()) {
            User(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            )
        } else null
    }
    fun updateUserProfilePhoto(userId: String, imagePath: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("profilePhoto", imagePath) // Save local image path

        db.update("users", values, "userId = ?", arrayOf(userId))
        db.close()
    }

    fun getUserProfilePhoto(userId: String): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT profilePhoto FROM users WHERE userId = ?", arrayOf(userId))
        var imagePath: String? = null
        if (cursor.moveToFirst()) {
            imagePath = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return imagePath
    }


}
