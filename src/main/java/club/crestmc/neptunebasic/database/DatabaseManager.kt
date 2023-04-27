package club.crestmc.neptunebasic.database

import club.crestmc.neptunebasic.NeptuneBasic
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import org.bson.Document

class DatabaseManager(private val plugin: NeptuneBasic) {

    val url = plugin.configManager.config?.getString("databaseUrl")

    lateinit var database: MongoDatabase
    lateinit var usersCollection: MongoCollection<Document>
    lateinit var mongoClient: MongoClient

    fun mongoConnect() {
        mongoClient = MongoClients.create(url)

        database = mongoClient.getDatabase("Basic")
        usersCollection = database.getCollection("users")
    }
}