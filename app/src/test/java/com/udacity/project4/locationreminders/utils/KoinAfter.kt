package com.udacity.project4.locationreminders.utils

import org.junit.After
import org.junit.Before
import org.koin.core.context.stopKoin


open class KoinAfter {

    @Before
    fun before(){
        stopKoin()
    }

    @After
    fun cacnelKoin(){
        stopKoin()
    }
}