package ru.s44khin.messenger.di

import android.content.Context
import androidx.room.Room
import ru.s44khin.messenger.data.dataBase.MessengerDataBase
import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.data.network.api.RequestManager
import ru.s44khin.messenger.domain.*
import ru.s44khin.messenger.presentation.members.elm.MembersActor
import ru.s44khin.messenger.presentation.members.elm.MembersStoreFactory
import ru.s44khin.messenger.presentation.profile.elm.ProfileActor
import ru.s44khin.messenger.presentation.profile.elm.ProfileStoreFactory
import ru.s44khin.messenger.presentation.streams.elm.StreamsActor
import ru.s44khin.messenger.presentation.streams.elm.StreamsStoreFactory
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.AllStreamsActor
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.AllStreamsStoreFactory
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm.SubsStreamsActor
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm.SubsStreamsStoreFactory

class GlobalDI private constructor() {

    companion object {
        lateinit var INSTANCE: GlobalDI
        lateinit var dataBase: MessengerDataBase

        fun init(context: Context) {
            INSTANCE = GlobalDI()
            dataBase = Room.databaseBuilder(
                context,
                MessengerDataBase::class.java,
                "DataBase"
            ).build()
        }
    }

    private val repository by lazy { ZulipRepository(RequestManager.service) }

    private val loadProfile by lazy { LoadProfile(repository, dataBase.profileDao()) }
    private val profileActor by lazy { ProfileActor(loadProfile) }
    val profileStore by lazy { ProfileStoreFactory(profileActor).provide() }

    private val loadMembers by lazy { LoadMembers(repository, dataBase.profileDao()) }
    private val membersActor by lazy { MembersActor(loadMembers) }
    val membersStore by lazy { MembersStoreFactory(membersActor).provide() }

    private val loadTopics by lazy { LoadTopics(repository) }

    private val loadAllStreams by lazy { LoadAllStreams(repository, dataBase.streamsDao()) }
    private val allStreamsActor by lazy { AllStreamsActor(loadAllStreams, loadTopics) }
    val allStreamsStore by lazy { AllStreamsStoreFactory(allStreamsActor).provide() }

    private val loadSubsStreams by lazy { LoadSubsStreams(repository, dataBase.streamsDao()) }
    private val subStreamsActor by lazy { SubsStreamsActor(loadSubsStreams, loadTopics) }
    val subsStreamsStore by lazy { SubsStreamsStoreFactory(subStreamsActor).provide() }

    private val streamsActor by lazy { StreamsActor(loadProfile) }
    val streamsStore by lazy { StreamsStoreFactory(streamsActor).provide() }
}