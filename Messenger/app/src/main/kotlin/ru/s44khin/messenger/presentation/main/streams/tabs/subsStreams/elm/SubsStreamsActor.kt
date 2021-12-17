package ru.s44khin.messenger.presentation.main.streams.tabs.subsStreams.elm

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.domain.LoadSubsStreams
import ru.s44khin.messenger.domain.LoadTopics
import ru.s44khin.messenger.utils.resultStreamFromStreamAndTopics
import vivid.money.elmslie.core.ActorCompat

class SubsStreamsActor(
    private val loadSubsStreams: LoadSubsStreams,
    private val loadTopics: LoadTopics
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {

        is Command.LoadStreamsNetwork -> loadSubsStreams.fromNetwork()
            .flattenAsObservable { it.subscriptions }
            .flatMap {
                Observable.zip(
                    Observable.just(it),
                    loadTopics.execute(it.streamId).subscribeOn(Schedulers.io()).toObservable()
                ) { stream, topics ->
                    resultStreamFromStreamAndTopics(
                        stream = stream,
                        subscription = true,
                        topics = topics.topics
                    )
                }
            }
            .toList()
            .doOnSuccess { loadSubsStreams.saveToDataBase(it) }
            .mapEvents(
                { subsStreams -> Event.Internal.StreamsLoadedNetwork(subsStreams.sortedByDescending { it.pinToTop }) },
                { error -> Event.Internal.ErrorLoadingNetwork(error) }
            )

        is Command.LoadStreamsDB -> loadSubsStreams.fromDataBase()
            .mapEvents(
                { subsStreams ->
                    if (subsStreams.isEmpty())
                        Event.Internal.ErrorLoadingDB(null)
                    else
                        Event.Internal.StreamsLoadedDB(subsStreams.sortedByDescending { it.pinToTop })
                },
                { error -> Event.Internal.ErrorLoadingDB(error) }
            )

        is Command.UnsubscribeFromStream -> loadSubsStreams.unsubscribeFromStream(command.streamName)
            .mapEvents(
                { result ->
                    if (result.result == "success")
                        Event.Internal.SuccessfulUnsubscribeFromStream
                    else
                        Event.Internal.ErrorUnsubscribeFromStream
                },
                { Event.Internal.ErrorUnsubscribeFromStream }
            )

        is Command.SetStreamColor -> loadSubsStreams.setStreamColor(command.streamId, command.color)
            .mapEvents(
                { result ->
                    if (result.result == "success")
                        Event.Internal.SuccessfulSetStreamColor
                    else
                        Event.Internal.ErrorSetStreamColor
                },
                { Event.Internal.ErrorSetStreamColor }
            )

        is Command.PinStreamToTop -> loadSubsStreams.pinStreamToTop(command.streamId)
            .mapEvents(
                { result ->
                    if (result.result == "success")
                        Event.Internal.SuccessfulPinned
                    else
                        Event.Internal.ErrorPinned
                },
                { Event.Internal.ErrorPinned }
            )

        is Command.UnpinFromTop -> loadSubsStreams.unpinStreamFromTop(command.streamId)
            .mapEvents(
                { result ->
                    if (result.result == "success")
                        Event.Internal.SuccessfulUnpinned
                    else
                        Event.Internal.ErrorUnpinned
                },
                { Event.Internal.ErrorUnpinned }
            )

        is Command.CreateNewStream -> loadSubsStreams.createNewStream(
            command.streamName,
            command.description
        )
            .mapEvents(
                { result ->
                    if (result.result == "success")
                        Event.Internal.StreamCreated
                    else
                        Event.Internal.ErrorCreateStream
                },
                { Event.Internal.ErrorCreateStream }
            )
    }
}