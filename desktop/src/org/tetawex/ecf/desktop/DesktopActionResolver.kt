package org.tetawex.ecf.desktop

import org.tetawex.ecf.core.ActionResolver
import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException

class DesktopActionResolver : ActionResolver {
    override fun externalStorageAccessible() = true
    override fun setBackPressedListener(listener: () -> Boolean) = Unit
    override fun openUrl(url: String) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(URI(url))
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
    }
}