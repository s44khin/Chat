package ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm

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
        is Command.LoadStreams -> loadSubsStreams.execute()
            .flattenAsObservable { it.subscriptions }
            .flatMap {
                Observable.zip(
                    Observable.just(it),
                    loadTopics.execute(it.streamId).subscribeOn(Schedulers.io()).toObservable()
                ) { stream, topics -> resultStreamFromStreamAndTopics(stream, topics.topics) }
            }
            .toList()
            .mapEvents(
                { allStreams -> Event.Internal.StreamsLoaded(allStreams) },
                { error -> Event.Internal.ErrorLoading(error) }
            )
    }
}