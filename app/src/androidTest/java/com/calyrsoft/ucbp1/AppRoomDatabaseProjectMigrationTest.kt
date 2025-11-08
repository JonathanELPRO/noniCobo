package com.calyrsoft.ucbp1

import android.content.Context
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.calyrsoft.ucbp1.features.auth.data.database.AppRoomDatabaseProject
import com.calyrsoft.ucbp1.features.auth.data.database.MIGRATION_6_7
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppRoomDatabaseProjectMigrationTest {

    private val TEST_DB = "migration-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppRoomDatabaseProject::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate6To7_addTimestampColumnToLodgings() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Crear base de datos en versión 6 (antes de la migración)
        helper.createDatabase(TEST_DB, 6).apply {
            // Insertar una fila de ejemplo
            execSQL(
                """
                INSERT INTO lodgings (id, name, type, district, address, contactPhone, open24h, ownerAdminId, latitude, longitude, stayOptions, roomOptions, placeImageUri, licenseImageUri)
                VALUES (1, 'Test Lodge', 'Hotel', 'Centro', 'Av. Siempre Viva', '77777777', 0, 10, NULL, NULL, '[]', '[]', NULL, NULL)
                """
            )
            close()
        }

        // Ejecutar migración
        helper.runMigrationsAndValidate(TEST_DB, 7, true, MIGRATION_6_7)

        // Verificar que la nueva columna existe
        val db = Room.databaseBuilder(
            context,
            AppRoomDatabaseProject::class.java,
            TEST_DB
        )
            .addMigrations(MIGRATION_6_7)
            .allowMainThreadQueries()
            .build()

        val cursor = db.query(SimpleSQLiteQuery("SELECT timestamp FROM lodgings WHERE id = 1"))
        assert(cursor.moveToFirst())
        val timestampValue = cursor.getLong(0)
        assert(timestampValue > 0)
        cursor.close()
        db.close()
    }
}