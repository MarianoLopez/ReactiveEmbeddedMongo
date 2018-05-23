package com.z.reactive.reactive.Configuration


import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value
import javax.annotation.PreDestroy
import java.io.IOException
import java.io.File
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.data.mongodb.MongoDbFactory
import java.net.UnknownHostException
import java.nio.file.Files

@Configuration
class EmbeddedMongoDB {
        @Value("\${mongo.db.url}")
        private val MONGO_DB_URL: String? = null
        @Value("\${mongo.db.port}")
        private val MONGO_DB_PORT: String? = null
        @Value("\${mongo.db.dirPath}")
        private val MONGO_DB_DIRPATH: String? = null

        internal var starter = MongodStarter.getDefaultInstance()
        internal var mongodExecutable: MongodExecutable? = null

        @Bean
        @Throws(UnknownHostException::class)
        fun mongoDbFactory(): MongoDbFactory {
            val mongo = MongoClient(MONGO_DB_URL, Integer.valueOf(MONGO_DB_PORT!!))
            return SimpleMongoDbFactory(mongo, "reactive")
        }

        @Bean
        @Throws(UnknownHostException::class)
        fun mongoTemplate(): MongoTemplate {
            return MongoTemplate(mongoDbFactory())
        }

        @PostConstruct
        @Throws(UnknownHostException::class, IOException::class)
        fun construct() {
            deleteLock()
            val mongodConfig = MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .replication(Storage(MONGO_DB_DIRPATH, null, 0))
                    .net(Net(MONGO_DB_URL, Integer.valueOf(MONGO_DB_PORT!!), true))
                    .build()
            mongodExecutable = starter.prepare(mongodConfig)
            mongodExecutable!!.start()
        }

        private fun deleteLock() {
            try {
                Files.deleteIfExists(File(MONGO_DB_DIRPATH!! + "\\mongod.lock").toPath())
            } catch (ex: IOException) {
                Logger.getLogger(EmbeddedMongoDB::class.java.name).log(Level.SEVERE, null, ex)
            }

        }

        @PreDestroy
        fun destroy() {
            if (mongodExecutable != null) {
                mongodExecutable!!.stop()
            }
        }

}