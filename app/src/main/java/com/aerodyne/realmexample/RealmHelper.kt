package com.aerodyne.realmexample

import com.aerodyne.realmexample.model.Frog
import com.aerodyne.realmexample.model.Pond
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.dynamic.DynamicMutableRealm
import io.realm.kotlin.dynamic.DynamicMutableRealmObject
import io.realm.kotlin.dynamic.DynamicRealm
import io.realm.kotlin.dynamic.DynamicRealmObject
import io.realm.kotlin.migration.AutomaticSchemaMigration
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmScalarQuery
import kotlinx.coroutines.flow.Flow

/**
 *
 * Created by Ganesan Gopal on 30/01/2024.
 */
object RealmHelper {

    @JvmStatic
    val realm: Realm

    init {
        /*val migration =
            AutomaticSchemaMigration { context: AutomaticSchemaMigration.MigrationContext ->
                val oldRealm: DynamicRealm = context.oldRealm
                val newRealm: DynamicMutableRealm = context.newRealm
                // dynamic realm gives access
                // to realm data
                // through a generic string
                // based API
                context.enumerate(Frog::class.simpleName!!) {
                        oldObject: DynamicRealmObject,
                        newObject: DynamicMutableRealmObject? ->
                    newObject?.run {
                        // Change type
                        //set("age", null)
                        set("frog2", null)
                    }
                }
            }*/
        val realmBuilder = RealmConfiguration.Builder(schema = setOf(Frog::class, Pond::class))//
            .schemaVersion(3)//.migration(migration, true)
        val configuration =
            realmBuilder.build()//RealmConfiguration.create(schema = setOf(User::class))
        realm = Realm.open(configuration)
    }

    fun saveFrog(frog: Frog) : Frog? {
        try {
            return realm.writeBlocking { // this : MutableRealm
                try {
                    return@writeBlocking copyToRealm(frog)
                } catch (t: Throwable) {
                    t.printStackTrace()
                    return@writeBlocking null
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            return null
        }
    }

    suspend fun clearAllFrog() {
        realm.write {
            val frogsLeftInTheRealm = query(Frog::class).find()
            // Pass the query results to delete()
            delete(frogsLeftInTheRealm)
        }
    }

    fun getAllFrog(): List<Frog> {
        return realm.query(Frog::class).find()
    }

    fun getAllFrogFlow(): Flow<ResultsChange<Frog>> {
        return realm.query(Frog::class).asFlow()
    }

    fun getFrogCount(): Long {
        return realm.query(Frog::class).count().find()
    }
}