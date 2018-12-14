package org.tetawex.ecf.util

/**
 * Created by tetawex on 08.08.17.
 */
@FunctionalInterface
interface BasicListener : () -> Unit {
    fun call()
    override fun invoke() = call()
}
