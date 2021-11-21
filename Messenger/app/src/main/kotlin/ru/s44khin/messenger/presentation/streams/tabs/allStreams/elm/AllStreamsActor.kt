package ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.s44khin.messenger.domain.LoadAllStreams
import ru.s44khin.messenger.domain.LoadTopics
import ru.s44khin.messenger.utils.resultStreamFromStreamAndTopics
import vivid.money.elmslie.core.ActorCompat

class AllStreamsActor(
    private val loadAllStreams: LoadAllStreams,
    private val loadTopics: LoadTopics
) : ActorCompat<Command, Event> {

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.LoadStreams -> loadAllStreams.execute()
            .flattenAsObservable { it.streams }
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