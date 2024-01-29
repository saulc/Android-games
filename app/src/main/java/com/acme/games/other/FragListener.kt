package com.acme.games.other

import com.acme.games.BlankFragment


interface FragListener {
    fun onClick(info: String)
    fun gameConnected(blankFragment: BlankFragment)

}