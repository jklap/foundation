package net.ntworld.foundation

import net.ntworld.foundation.cqrs.CommandBus
import net.ntworld.foundation.cqrs.QueryBus
import net.ntworld.foundation.eventSourcing.*
import kotlin.reflect.KClass

class MemorizedInfrastructure(base: Infrastructure) : InfrastructureWrapper(base) {
    private val _factories = mutableMapOf<KClass<*>, AggregateFactory<*>>()
    private val _stores = mutableMapOf<KClass<*>, AggregateStore<*>>()
    private val _idGenerators = mutableMapOf<KClass<*>, IdGenerator>()
    private var _queryBus: QueryBus? = null
    private var _commandBus: CommandBus? = null
    private var _eventBus: EventBus? = null
    private var _encryptor: Encryptor? = null
    private var _encryptors = mutableMapOf<String, Encryptor>()
    private var _eventStreams = mutableMapOf<String, EventStream>()
    private var _eventConverterClasses = mutableMapOf<KClass<*>, EventConverter<*>>()
    private var _eventConverters = mutableMapOf<String, EventConverter<*>>()
    private var _messageConverters = mutableMapOf<KClass<*>, MessageConverter<*>>()
    private var _snapshotStores = mutableMapOf<KClass<*>, SnapshotStore<*>>()

    override fun <A : Aggregate> factoryOf(type: KClass<A>): AggregateFactory<A> {
        if (!_factories.containsKey(type)) {
            _factories[type] = super.factoryOf(type)
        }
        return _factories[type] as AggregateFactory<A>
    }

    override fun <A : Aggregate> storeOf(type: KClass<A>): AggregateStore<A> {
        if (!_stores.containsKey(type)) {
            _stores[type] = super.storeOf(type)
        }
        return _stores[type] as AggregateStore<A>
    }

    override fun <T : Any> idGeneratorOf(type: KClass<T>): IdGenerator {
        if (!_idGenerators.containsKey(type)) {
            _idGenerators[type] = super.idGeneratorOf(type)
        }
        return _idGenerators[type] as IdGenerator
    }

    override fun queryBus(): QueryBus {
        if (null === _queryBus) {
            _queryBus = super.queryBus()
        }
        return _queryBus!!
    }

    override fun commandBus(): CommandBus {
        if (null === _commandBus) {
            _commandBus = super.commandBus()
        }
        return _commandBus!!
    }

    override fun eventBus(): EventBus {
        if (null === _eventBus) {
            _eventBus = super.eventBus()
        }
        return _eventBus!!
    }

    override fun encryptor(): Encryptor {
        if (null === _encryptor) {
            _encryptor = super.encryptor()
        }
        return _encryptor!!
    }

    override fun encryptor(cipherId: String, algorithm: String): Encryptor {
        val key = "$cipherId:$algorithm"
        if (!_encryptors.containsKey(key)) {
            _encryptors[key] = super.encryptor(cipherId, algorithm)
        }
        return _encryptors[key] as Encryptor
    }

    override fun eventStreamOf(eventSourced: AbstractEventSourced, version: Int): EventStream {
        val key = "${eventSourced.streamType}:${eventSourced.id}:$version"
        if (!_eventStreams.containsKey(key)) {
            _eventStreams[key] = super.eventStreamOf(eventSourced, version)
        }
        return _eventStreams[key] as EventStream
    }

    override fun eventConverterOf(event: Event): EventConverter<Event> {
        val key = event::class
        if (!_eventConverterClasses.containsKey(key)) {
            _eventConverterClasses[key] = super.eventConverterOf(event)
        }
        return _eventConverterClasses[key] as EventConverter<Event>
    }

    override fun eventConverterOf(type: String, variant: Int): EventConverter<Event> {
        val key = "$type:$variant"
        if (!_eventConverters.containsKey(key)) {
            _eventConverters[key] = super.eventConverterOf(type, variant)
        }
        return _eventConverters[key] as EventConverter<Event>
    }

    override fun <T : Any> messageConverterOf(type: KClass<T>): MessageConverter<T> {
        if (!_messageConverters.containsKey(type)) {
            _messageConverters[type] = super.messageConverterOf(type)
        }
        return _messageConverters[type] as MessageConverter<T>
    }

    override fun <T : Aggregate> snapshotStoreOf(type: KClass<T>): SnapshotStore<T> {
        if (!_snapshotStores.containsKey(type)) {
            _snapshotStores[type] = super.snapshotStoreOf(type)
        }
        return _snapshotStores[type] as SnapshotStore<T>
    }
}