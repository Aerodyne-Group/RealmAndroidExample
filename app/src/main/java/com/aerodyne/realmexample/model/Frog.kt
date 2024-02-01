package com.aerodyne.realmexample.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

/**
 *
 * Created by Ganesan Gopal on 30/01/2024.
 */
class Frog : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var name: String? = null
    var age: Long? = null
    var pond: Pond? = null
}