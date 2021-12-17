package ru.s44khin.messenger.presentation.chat

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.s44khin.messenger.MessengerApplication
import ru.s44khin.messenger.R
import ru.s44khin.messenger.databinding.ActivityChatBinding
import ru.s44khin.messenger.presentation.chat.adapters.ChatAdapter
import ru.s44khin.messenger.presentation.chat.changeTopicFragment.ChangeTopicFragment
import ru.s44khin.messenger.presentation.chat.elm.*
import ru.s44khin.messenger.presentation.chat.pagination.PaginationAdapterHelper
import ru.s44khin.messenger.presentation.chat.selectTopicFragment.SelectTopicFragment
import ru.s44khin.messenger.presentation.main.profile.ProfileFragment
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.core.store.Store
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class ChatActivity : ElmActivity<Event, Effect, State>(), MenuHandler {

    companion object {
        const val STREAM_ID = "STREAM_ID"
        const val STREAM_NAME = "STREAM_NAME"
        const val TOPIC_NAME = "TOPIC_NAME"
        const val STREAM_COLOR = "STREAM_COLOR"

        fun createIntent(
            context: Context,
            streamId: Int,
            streamName: String,
            topicName: String?,
            color: String?
        ) = Intent(context, ChatActivity::class.java)
            .putExtra(STREAM_ID, streamId)
            .putExtra(STREAM_NAME, streamName)
            .putExtra(TOPIC_NAME, topicName)
            .putExtra(STREAM_COLOR, color)
    }

    override val initEvent = Event.Ui.LoadNextPage

    override fun createStore(): Store<Event, Effect, State> {
        val chatActor = ChatActor(
            loadMessages = MessengerApplication.instance.chatComponent.loadMessages,
            streamId = streamId,
            streamName = streamName,
            topicName = topicName
        )

        return ChatStoryFactory(chatActor).provide()
    }

    private val adapter by lazy {
        ChatAdapter(
            paginationAdapterHelper = PaginationAdapterHelper {
                store.accept(Event.Ui.LoadNextPage)
            },
            reactionSender = this,
            color = color
        )
    }

    private val streamName by lazy {
        intent.getStringExtra(STREAM_NAME)!!
    }

    private val streamId by lazy {
        intent.getIntExtra(STREAM_ID, 0)
    }

    private val topicName by lazy {
        intent.getStringExtra(TOPIC_NAME)
    }

    private val color by lazy {
        intent.getStringExtra(STREAM_COLOR)
    }

    private val getContent = registerForActivityResult(GetContent()) { uri: Uri? ->
        if (uri != null) {
            val bitmap = if (Build.VERSION.SDK_INT >= 28) {
                val source = ImageDecoder.createSource(this.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }

            binding.imageSendImage.setImageBitmap(bitmap)
            binding.cardSendImage.visibility = View.VISIBLE
            binding.progressBarSendImage.visibility = View.VISIBLE

            val file = File(cacheDir, "image123")
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.JPEG, 50, bos)
            val bitmapdata = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()

            val filePart = MultipartBody.Part.createFormData(
                "file",
                file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            )
            store.accept(Event.Ui.SendPicture(filePart))
        }
    }

    private var isLoadFirst = false
    private var isSentMessage = false

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerView()
        initToolBar()
        initSendButton()
        initColor()
        initDownButton()
        initAttachButton()
    }

    override fun render(state: State) {
        binding.progressBar.isVisible = state.isLoadingDB
        binding.progressIndicator.isVisible = state.isLoadingNetwork

        if (state.messages != null) {
            adapter.items = state.messages

            if (!isLoadFirst) {
                binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
                isLoadFirst = true
            }

            if (isSentMessage)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                    isSentMessage = false
                }, 100)
        }

        if (state.imageUri != null) {
            binding.messageInput.message.setText("[image123](${state.imageUri})")
            binding.progressBarSendImage.visibility = View.GONE
            state.imageUri = null
        }
    }

    override fun showProfile(avatar: String, name: String, email: String) {
        ProfileFragment.newInstance(avatar, name, email)
            .show(supportFragmentManager, ProfileFragment.TAG)
    }

    override fun addReaction(messageId: Int, emojiName: String) {
        store.accept(Event.Ui.AddReaction(messageId, emojiName))
    }

    override fun removeReaction(messageId: Int, emojiName: String) {
        store.accept(Event.Ui.RemoveReaction(messageId, emojiName))
    }

    override fun copyTextToClipboard(content: String) {
        val clipboardManager =
            ContextCompat.getSystemService(this, android.content.ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", content)
        clipboardManager?.setPrimaryClip(clip)
    }

    override fun deleteMessage(id: Int) {
        store.accept(Event.Ui.DeleteMessage(id))
    }

    override fun sendMessageToTopic(content: String, topic: String) {
        store.accept(Event.Ui.SendMessageToTopic(content, topic))
        binding.messageInput.message.setText("")
        binding.cardSendImage.visibility = View.GONE
        isSentMessage = true
    }

    override fun editTopic(id: Int, topic: String) {
        store.accept(Event.Ui.EditMessageTopic(id, topic))
    }

    override fun showEditTopicMenu(id: Int) {
        val newColor = if (color == null)
            ContextCompat.getColor(
                this,
                R.color.primary
            )
        else
            Color.parseColor(color)

        ChangeTopicFragment.newInstance(this, streamId, id, newColor)
            .show(supportFragmentManager, ChangeTopicFragment.TAG)
    }

    override fun editMessage(message: ChatItem) {
        binding.messageInput.message.setText(message.content)

        binding.messageInput.send.hide()
        binding.messageInput.edit.show()

        binding.messageInput.edit.setOnClickListener {
            store.accept(
                Event.Ui.EditMessage(
                    message.id,
                    binding.messageInput.message.text.toString()
                )
            )
            binding.messageInput.message.setText("")

            binding.messageInput.edit.hide()
        }
    }

    private fun initRecyclerView() = binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
        adapter = this@ChatActivity.adapter
    }

    private fun initToolBar() = binding.apply {
        streamName.text = this@ChatActivity.streamName
        topicName.text = this@ChatActivity.topicName ?: resources.getText(R.string.all_topics)
        backButton.setOnClickListener { finish() }
    }

    private fun initSendButton() = binding.messageInput.message.doAfterTextChanged { text ->
        binding.messageInput.send.setOnClickListener {
            store.accept(Event.Ui.SendMessage(text.toString()))
            binding.messageInput.message.setText("")
            binding.cardSendImage.visibility = View.GONE
            isSentMessage = true
        }

        if (text?.length != 0) {
            binding.messageInput.send.show()
        } else {
            binding.messageInput.send.hide()
        }

        if (topicName == null) {
            binding.messageInput.send.setOnLongClickListener {
                val newColor = if (color == null)
                    ContextCompat.getColor(
                        this,
                        R.color.primary
                    )
                else
                    Color.parseColor(color)

                SelectTopicFragment.newInstance(this, text.toString(), streamId, newColor)
                    .show(supportFragmentManager, SelectTopicFragment.TAG)

                true
            }
        }
    }

    private fun initAttachButton() {
        binding.messageInput.attach.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun initDownButton() {
        binding.down.hide()

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 30) {
                    binding.down.hide()
                } else if (dy < 0) {
                    binding.down.show()
                }
            }
        })

        binding.down.setOnClickListener {
            binding.recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun initColor() {
        if (color != null) {
            val newColor = Color.parseColor(color)

            binding.backButton.setColorFilter(newColor)
            binding.streamName.setTextColor(newColor)
            binding.progressIndicator.setIndicatorColor(newColor)
            binding.progressIndicator.trackColor = Color.argb(
                100,
                Color.red(newColor),
                Color.green(newColor),
                Color.blue(newColor)
            )
            binding.messageInput.send.backgroundTintList = ColorStateList.valueOf(newColor)
        }
    }
}