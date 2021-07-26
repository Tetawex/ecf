package org.tetawex.ecf.util

import java.util.Arrays
import java.util.Comparator

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.presentation.StyleFactory

open class FileChooser(title: String, style: Window.WindowStyle, private val game: ECFGame) : Dialog(title, style) {
    private var directory: FileHandle? = null
    private var file: FileHandle? = null

    fun getFile(): FileHandle? {
        return file
    }

    override fun show(stage: Stage): Dialog {
        return super.show(stage)
    }

    fun setDirectory(directory: FileHandle) {
        if (this.directory !== directory) {
            this.directory = directory
            this.file = null
            buildList()
        }
    }

    fun setFile(file: FileHandle) {
        if (this.file !== file) {
            if (this.file != null) {
                val label = this.findActor<Actor>(this.file!!.name()) as Label
                label.color = Color(1f, 1f, 1f, 0.87f)
            }
            val label = this.findActor<Actor>(file.name()) as Label
            label.color = Color(0.3f, 0.11f, 0.18f, 0.87f)
            this.file = file
        }
    }

    init {
        this.getCell(buttonTable).expandX().fill()
        this.buttonTable.defaults().expandX().pad(16f).fill()
        this.button("Load", FileHandle(""), StyleFactory.generateStandardMenuButtonStyle(game))
        this.button("Cancel", null, StyleFactory.generateStandardMenuButtonStyle(game))
        this.isModal = true
    }

    private fun buildList() {
        val files = directory!!.list()
        Arrays.sort(files, Comparator { o1, o2 ->
            if (o1.isDirectory && !o2.isDirectory) {
                return@Comparator -1
            }
            if (o2.isDirectory && !o1.isDirectory) {
                +1
            } else o1.name().compareTo(o2.name(), ignoreCase = true)
        })
        val pane = ScrollPane(null, StyleFactory.generateStandardScrollPaneStyle(game))
        val table = Table().top().left()
        table.defaults().left()
        val fileClickListener = object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                val target = event.target as Label
                if (target.name == "...") {
                    setDirectory(directory!!.parent())
                } else {
                    val handle = directory!!.child(target.name)
                    if (handle.isDirectory) {
                        setDirectory(handle)
                    } else {
                        setFile(handle)
                    }
                }
            }
        }
        table.row()
        var label = Label("...", StyleFactory.generateStandardLabelStyle(game))
        label.name = "..."
        label.addListener(fileClickListener)
        table.add(label).expandX().pad(16f).fillX()
        for (file in files) {
            table.row()
            label = Label(file.name(), StyleFactory.generateStandardLabelStyle(game))
            label.name = file.name()
            label.addListener(fileClickListener)
            table.add(label).expandX().pad(16f).fillX()
        }
        pane.widget = table
        this.contentTable.reset()
        this.contentTable.add(pane).size(1200f, 1200f)
    }

}
