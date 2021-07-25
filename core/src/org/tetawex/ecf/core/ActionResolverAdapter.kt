package org.tetawex.ecf.core

import com.badlogic.gdx.Gdx

/**
 * Created by Tetawex on 12.06.2017.
 */
class ActionResolverAdapter : ActionResolver {
    override fun setBackPressedListener(listener: () -> Boolean) =Unit
    override fun externalStorageAccessible() = false
    override fun openUrl(url: String) = Unit
}
