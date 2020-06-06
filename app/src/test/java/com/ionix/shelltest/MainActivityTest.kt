package com.ionix.shelltest

import android.content.Context
import com.ionix.shelltest.model.DataUser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityTest {

    private val ma = MainActivity()

    @Mock
    var mMockContext: Context? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun check_user() {
        assertNotEquals("If User in't null", ma.checkIfUserCreated(""), false)
        assertEquals("If User is null", ma.checkIfUserCreated(""), true)
    }

}