package ru.s44khin.messenger.presentation.main.streams.tabs.allStreams.elm

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.domain.LoadAllStreams
import ru.s44khin.messenger.domain.LoadTopics
import ru.s44khin.messenger.utils.resultStreamFromStreamAndTopics
import vivid.money.elmslie.core.ActorCompat

class AllStreamsActor(
    private val loadAllStreams: LoadAllStreams,
    private val loadTopics: LoadTopics
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {

        is Command.LoadStreamsNetwork -> loadAllStreams.fromNetwork()
            .flattenAsObservable { it.streams }
            .flatMap {
                Observable.zip(
                    Observable.just(it),
                    loadTopics.execute(it.streamId).subscribeOn(Schedulers.io()).toObservable()
                ) { stream, topics -> resultStreamFromStreamAndTopics(stream, topics.topics) }
            }
            .toList()
            .doOnSuccess { loadAllStreams.saveToDataBase(it) }
            .mapEvents(
                { allStreams -> Event.Internal.StreamsLoadedNetwork(allStreams) },
                { error -> Event.Internal.ErrorLoadingNetwork(error) }
            )

        is Command.LoadStreamsDB -> loadAllStreams.fromDataBase()
            .mapEvents(
                { allStreams ->
                    if (allStreams.isEmpty())
                        Event.Internal.ErrorLoadingDB(null)
                    else
                        Event.Internal.StreamsLoadedDB(allStreams)
                },
                { error -> Event.Internal.ErrorLoadingDB(error) }
            )

        is Command.SubscribeToStream -> loadAllStreams.subscribeToStream(
            command.streamName,
            command.description
        )
            .doAfterSuccess {
                MessengerApplication.instance.subsStreamsComponent.subsStreamsStore.accept(
                    ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.elm.Event.Ui.LoadStreamsNetwork
                )
            }
            .mapEvents(
                { result ->
                    if (result.result == "success")
                        Event.Internal.SuccessfulSubscriptionToStream
                    else
                        Event.Internal.ErrorSubscribeToStream
                },
                { Event.Internal.ErrorSubscribeToStream }
            )
    }
}