package ru.s44khin.messenger.di

import ru.s44khin.messenger.data.network.ZulipRepository
import ru.s44khin.messenger.data.network.api.RequestManager
import ru.s44khin.messenger.domain.*
import ru.s44khin.messenger.presentation.members.elm.MembersActor
import ru.s44khin.messenger.presentation.members.elm.MembersStoreFactory
import ru.s44khin.messenger.presentation.profile.elm.ProfileActor
import ru.s44khin.messenger.presentation.profile.elm.ProfileStoreFactory
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.AllStreamsActor
import ru.s44khin.messenger.presentation.streams.tabs.allStreams.elm.AllStreamsStoreFactory
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm.SubsStreamsActor
import ru.s44khin.messenger.presentation.streams.tabs.subsStreams.elm.SubsStreamsStoreFactory

class GlobalDI private constructor() {

    companion object {
        lateinit var INSTANCE: GlobalDI

        fun init() {
            INSTANCE = GlobalDI()
        }
    }

    private val repository by lazy { ZulipRepository(RequestManager.service) }
    private val loadProfile by lazy { LoadProfile(repository) }
    private val profileActor by lazy { ProfileActor(loadProfile) }
    val profileStoreFactory by lazy { ProfileStoreFactory(profileActor) }

    private val loadMembers by lazy { LoadMembers(repository) }
    private val membersActor by lazy { MembersActor(loadMembers) }
    val membersStoreFactory by lazy { MembersStoreFactory(membersActor) }

    private val loadTopics by lazy { LoadTopics(repository) }

    private val loadAllStreams by lazy { LoadAllStreams(repository) }
    private val allStreamsActor by lazy { AllStreamsActor(loadAllStreams, loadTopics) }
    val allStreamsStoreFactory by lazy { AllStreamsStoreFactory(allStreamsActor) }

    private val loadSubsStreams by lazy { LoadSubsStreams(repository) }
    private val subStreamsActor by lazy { SubsStreamsActor(loadSubsStreams, loadTopics) }
    val subsStreamsStoreFactory by lazy { SubsStreamsStoreFactory(subStreamsActor) }
}