package ru.s44khin.messenger.presentation.main.members

interface OnClick {

    fun onClick(
        avatar: String,
        name: String,
        email: String
    )
}