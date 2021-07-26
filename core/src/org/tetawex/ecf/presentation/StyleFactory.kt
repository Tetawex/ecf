package org.tetawex.ecf.presentation

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.presentation.screen.EditorScreen

/**
 * Created by Tetawex on 23.05.2017.
 */
object StyleFactory {
    fun generatePauseButtonStyle(game: ECFGame): TextButton.TextButtonStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val textButtonStyle = TextButton.TextButtonStyle()
        textButtonStyle.font = game.assetManager.get("fonts/font_main_small.ttf", BitmapFont::class.java)
        textButtonStyle.up = skin.getDrawable("button_pause")
        textButtonStyle.down = skin.getDrawable("button_pause")
        return textButtonStyle
    }

    fun generateManaButtonStyle(game: ECFGame): TextButton.TextButtonStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val textButtonStyle = TextButton.TextButtonStyle()
        textButtonStyle.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        textButtonStyle.up = skin.getDrawable("mana")
        textButtonStyle.down = skin.getDrawable("mana")
        textButtonStyle.fontColor = Color(0f, 0f, 0f, 0.87f)
        return textButtonStyle
    }

    fun generateStandardLabelStyle(game: ECFGame): Label.LabelStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = Label.LabelStyle()
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(1f, 1f, 1f, 0.87f)
        return style
    }

    fun generateLargeStandardLabelStyle(game: ECFGame): Label.LabelStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = Label.LabelStyle()
        style.font = game.assetManager.get("fonts/font_main_large.ttf", BitmapFont::class.java)
        style.fontColor = Color(1f, 1f, 1f, 0.87f)
        return style
    }

    fun generateSmallStandardLabelStyle(game: ECFGame): Label.LabelStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = Label.LabelStyle()
        style.font = game.assetManager.get("fonts/font_main_small.ttf", BitmapFont::class.java)
        style.fontColor = Color(1f, 1f, 1f, 0.87f)
        return style
    }

    fun generateDarkLabelStyle(game: ECFGame): Label.LabelStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = Label.LabelStyle()
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(0.8f, 0.58f, 0.58f, 0.87f)
        return style
    }

    fun generateDarkerLabelStyle(game: ECFGame): Label.LabelStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = Label.LabelStyle()
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(0.3f, 0.11f, 0.18f, 0.87f)
        return style
    }

    fun generateDarkestLabelStyle(game: ECFGame): Label.LabelStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = Label.LabelStyle()
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(0f, 0f, 0f, 0.87f)
        return style
    }

    fun generateStandardSliderStyle(game: ECFGame): Slider.SliderStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = Slider.SliderStyle()
        style.knob = skin.getDrawable("slider")
        style.background = skin.getDrawable("dark_line")

        return style
    }

    fun generateStandardScrollPaneStyle(game: ECFGame): ScrollPane.ScrollPaneStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        return ScrollPane.ScrollPaneStyle()
    }

    fun generateStandardMenuButtonStyle(game: ECFGame): TextButton.TextButtonStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = TextButton.TextButtonStyle()
        var ninePatch = NinePatchDrawable(skin.getPatch("button_menu"))
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(1f, 1f, 1f, 0.87f)
        style.downFontColor = Color(1f, 1f, 1f, 0.35f)
        style.up = Image(ninePatch).drawable
        style.checked = Image(ninePatch).drawable
        ninePatch = NinePatchDrawable(skin.getPatch("button_menu_pressed"))
        style.down = Image(ninePatch).drawable

        return style
    }

    fun generateMotMenuButtonStyle(game: ECFGame): TextButton.TextButtonStyle {
        val style = TextButton.TextButtonStyle()
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(1f, 1f, 1f, 0.87f)
        style.downFontColor = Color(1f, 1f, 1f, 0.35f)
        style.up = Image(game.assetManager
                .get("backgrounds/motbutton.png", Texture::class.java)).getDrawable()
        style.checked = style.up
        style.down = Image(game.assetManager
                .get("backgrounds/motbutton_pressed.png", Texture::class.java)).getDrawable()

        return style
    }

    fun generateScMenuButtonStyle(game: ECFGame): TextButton.TextButtonStyle {
        val style = TextButton.TextButtonStyle()
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(1f, 1f, 1f, 0.87f)
        style.downFontColor = Color(1f, 1f, 1f, 0.35f)
        style.up = Image(game.assetManager
                .get("backgrounds/scbutton.png", Texture::class.java)).getDrawable()
        style.checked = style.up
        style.down = Image(game.assetManager
                .get("backgrounds/scbutton_pressed.png", Texture::class.java)).getDrawable()

        return style
    }

    fun generateStandardTutorialButtonStyle(game: ECFGame): TextButton.TextButtonStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = TextButton.TextButtonStyle()
        val ninePatch = NinePatchDrawable(skin.getPatch("tutorial_note"))
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(0.3f, 0.11f, 0.18f, 0.87f)
        style.up = Image(ninePatch).drawable
        style.checked = Image(ninePatch).drawable
        style.down = Image(ninePatch).drawable

        return style
    }

    fun generateLanguageMenuButtonStyle(game: ECFGame): TextButton.TextButtonStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = TextButton.TextButtonStyle()
        var ninePatch = NinePatchDrawable(skin.getPatch("button_menu_bottom"))
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(1f, 1f, 1f, 0.87f)
        style.downFontColor = Color(0.8f, 0.58f, 0.58f, 0.87f)
        style.up = Image(ninePatch).drawable
        style.checked = Image(ninePatch).drawable
        ninePatch = NinePatchDrawable(skin.getPatch("button_menu_bottom"))
        style.down = Image(ninePatch).drawable

        return style
    }

    fun generateActionButtonStyle(game: ECFGame, action: EditorScreen.ButtonAction): TextButton.TextButtonStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val textButtonStyle = TextButton.TextButtonStyle()
        textButtonStyle.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        textButtonStyle.fontColor = Color(1f, 1f, 1f, 0.87f)
        when (action) {
            EditorScreen.ButtonAction.ADD_FIRE -> {
                textButtonStyle.up = skin.getDrawable("element_fire")
                textButtonStyle.down = skin.getDrawable("element_fire")
            }
            EditorScreen.ButtonAction.ADD_WATER -> {
                textButtonStyle.up = skin.getDrawable("element_water")
                textButtonStyle.down = skin.getDrawable("element_water")
            }
            EditorScreen.ButtonAction.ADD_AIR -> {
                textButtonStyle.up = skin.getDrawable("element_air")
                textButtonStyle.down = skin.getDrawable("element_air")
            }
            EditorScreen.ButtonAction.ADD_EARTH -> {
                textButtonStyle.up = skin.getDrawable("element_earth")
                textButtonStyle.down = skin.getDrawable("element_earth")
            }
            EditorScreen.ButtonAction.ADD_SHADOW -> {
                textButtonStyle.up = skin.getDrawable("element_shadow")
                textButtonStyle.down = skin.getDrawable("element_shadow")
            }
            EditorScreen.ButtonAction.ADD_LIGHT -> {
                textButtonStyle.up = skin.getDrawable("element_light")
                textButtonStyle.down = skin.getDrawable("element_light")
            }
            EditorScreen.ButtonAction.ADD_TIME -> {
                textButtonStyle.up = skin.getDrawable("element_time")
                textButtonStyle.down = skin.getDrawable("element_time")
            }
            EditorScreen.ButtonAction.REMOVE_CELL -> {
                textButtonStyle.up = skin.getDrawable("button_hexagon")
                textButtonStyle.down = skin.getDrawable("button_hexagon")
            }
            EditorScreen.ButtonAction.SAVE_LEVEL -> {
                textButtonStyle.up = skin.getDrawable("button_pause")
                textButtonStyle.down = skin.getDrawable("button_pause")
            }
            EditorScreen.ButtonAction.LOAD_LEVEL -> {
                textButtonStyle.up = skin.getDrawable("button_pause")
                textButtonStyle.down = skin.getDrawable("button_pause")
            }
            EditorScreen.ButtonAction.PLAY_LEVEL -> {
                textButtonStyle.up = skin.getDrawable("button_pause")
                textButtonStyle.down = skin.getDrawable("button_pause")
            }
        }
        return textButtonStyle
    }

    fun generateEditorTextFieldStyle(game: ECFGame): TextField.TextFieldStyle {
        val skin = Skin()
        skin.addRegions(game.assetManager.get("atlas.atlas", TextureAtlas::class.java))
        val style = TextField.TextFieldStyle()
        val ninePatch = NinePatchDrawable(skin.getPatch("button_menu_bottom"))
        style.font = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.fontColor = Color(1f, 1f, 1f, 0.87f)
        style.background = Image(ninePatch).drawable
        return style
    }

    fun generateFileChooserStyle(game: ECFGame): Window.WindowStyle {
        val style = Window.WindowStyle()
        style.titleFont = game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java)
        style.titleFontColor = Color(1f, 1f, 1f, 0.87f)
        style.background = Image(game.assetManager
                .get("backgrounds/background_dialog.png", Texture::class.java)).getDrawable()
        return style
    }
}
