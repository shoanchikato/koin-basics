package com.sample

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.PrintLogger
import org.koin.core.qualifier.named
import org.koin.dsl.module

class App: KoinComponent {
    private val serviceOne: Service by inject()
    private val serviceTwo by inject<Service>(qualifier = named("serviceTwo"))

    fun execute() {
        serviceOne.printMessage()
        serviceTwo.printMessage()
    }
}

fun main(args: Array<String>) {

    // initialize recommended logger for koin core
    // from https://insert-koin.io/docs/reference/koin-core/logging
    val logger = PrintLogger()

    // initialize koin
    startKoin {
        logger(logger)
        modules(moduleOne)
    }

    val app = App()
    app.execute()

}


data class Message(val data: String)

interface Repository {
    val message: Message
}

interface Service {
    fun printMessage()
}

val moduleOne = module {
//    single { RepositoryImpl() } bind Repository::class // for matching RepositoryImpl and Repository at the same time
    single<Repository> { RepositoryImpl() }
    single<Service> { ServiceOne(get()) }
    single<Service>(qualifier = named("serviceTwo")) { ServiceTwo(get()) }
}

class RepositoryImpl: Repository {
    override val message = Message(data = "message body")
}


class ServiceOne(val repository: Repository) : Service {

    override fun printMessage() {
        println("from service one: ${repository.message}")
    }
}

class ServiceTwo(val repository: Repository) : Service {

    override fun printMessage() {
        println("from service two ${repository.message}")
    }

}

