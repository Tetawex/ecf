package org.tetawex.ecf.core

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.utils.I18NBundle
import org.tetawex.ecf.model.Language
import org.tetawex.ecf.util.FontCharacters
import org.tetawex.ecf.util.PreferencesProvider
import java.util.*
import kotlin.collections.HashMap

class ECFGame(val actionResolver: ActionResolver) : Game() {
    lateinit var assetManager: AssetManager
        private set
    private var textureAtlas: TextureAtlas? = null
    lateinit var gameStateManager: GameStateManager
        private set
    private var i18NBundle: I18NBundle? = null
    private var music: Music? = null

    override fun create() {
        PreferencesProvider.getPreferences()
        assetManager = AssetManager()
        assetManager.load("atlas.atlas", TextureAtlas::class.java)

        assetManager.load("backgrounds/background.png", Texture::class.java)
        assetManager.load("backgrounds/randombackground.png", Texture::class.java)
        assetManager.load("backgrounds/editorbackground.png", Texture::class.java)
        assetManager.load("backgrounds/motbackground.png", Texture::class.java)
        assetManager.load("backgrounds/scbackground.png", Texture::class.java)

        assetManager.load("backgrounds/motbutton.png", Texture::class.java)
        assetManager.load("backgrounds/motbutton_pressed.png", Texture::class.java)

        assetManager.load("backgrounds/scbutton.png", Texture::class.java)
        assetManager.load("backgrounds/scbutton_pressed.png", Texture::class.java)

        assetManager.load("backgrounds/background_pause.png", Texture::class.java)
        assetManager.load("backgrounds/background_dialog.png", Texture::class.java)

        assetManager.load("backgrounds/text_logo.png", Texture::class.java)

        assetManager.load("sounds/click.ogg", Sound::class.java)
        assetManager.load("sounds/merge.ogg", Sound::class.java)
        assetManager.load("sounds/error.ogg", Sound::class.java)
        assetManager.load("sounds/win.ogg", Sound::class.java)
        assetManager.load("sounds/loss.ogg", Sound::class.java)

        assetManager.load("i18n/bundle", I18NBundle::class.java)

        loadFonts()

        assetManager.finishLoading()

        setupLocalisation()

        textureAtlas = assetManager.get("atlas.atlas", TextureAtlas::class.java)
        for (texture in textureAtlas!!.textures) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }

        gameStateManager = GameStateManager(this, GameStateManager.GameState.MAIN_MENU)

        music = Gdx.audio.newMusic(Gdx.files.internal("music/ambient.ogg"))
        music!!.isLooping = true
        music!!.volume = PreferencesProvider.getPreferences().musicVolume
        music!!.play()

        actionResolver.setBackPressedListener { gameStateManager!!.currentScreen!!.onBackPressed() }
    }

    fun setMusicVolume(volume: Float) {
        music!!.volume = volume
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameStateManager!!.renderCurrentScreen(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        assetManager.dispose()
        gameStateManager!!.dispose()
        PreferencesProvider.flushPreferences()
    }

    override fun resize(width: Int, height: Int) {
        gameStateManager!!.currentScreen!!.resize(width, height)
    }

    fun getTextureRegionFromAtlas(name: String): TextureRegion {
        return textureAtlas!!.findRegion(name)
    }

    fun getLocalisedString(id: String): String {
        return i18NBundle!!.get(id)
    }

    fun setupLocalisation() {
        val ru_RU = Language("Русский", "ru_RU")
        val en_US = Language("English", "en_US")
        FontCharacters.supportedLanguages = arrayOf(ru_RU, en_US)

        val preferences = PreferencesProvider.getPreferences()

        if (preferences.selectedLanguageCode == "default") {
            i18NBundle = assetManager.get("i18n/bundle", I18NBundle::class.java)
        } else {
            val baseFileHandle = Gdx.files.internal("i18n/bundle")
            val locale = Locale(preferences.selectedLanguageCode!!, preferences.selectedCountryCode!!)
            i18NBundle = I18NBundle.createBundle(baseFileHandle, locale)
        }
        FontCharacters.codeToLanguageMap = HashMap()
        FontCharacters.codeToLanguageMap["en_US"] = "English"
        FontCharacters.codeToLanguageMap["ru_RU"] = "Русский"
        FontCharacters.codeToLanguageMap["default_"] = getLocalisedString("auto")
    }

    private fun loadFonts() {
        val resolver = InternalFileHandleResolver()
        assetManager.setLoader<FreeTypeFontGenerator, FreeTypeFontGeneratorLoader.FreeTypeFontGeneratorParameters>(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assetManager.setLoader<BitmapFont, FreetypeFontLoader.FreeTypeFontLoaderParameter>(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))

        // load to fonts via the generator (implicitly done by the FreetypeFontLoader).
        // Note: you MUST specify a FreetypeFontGenerator defining the ttf font file name and the size
        // of the font to be generated. The names of the fonts are arbitrary and are not pointing
        // to a file on disk!


        val paramsS = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        paramsS.fontParameters.characters = FontCharacters.en + FontCharacters.ru
        paramsS.fontFileName = "fonts/font_main.ttf"
        paramsS.fontParameters.size = 48
        paramsS.fontParameters.minFilter = Texture.TextureFilter.Linear
        paramsS.fontParameters.magFilter = Texture.TextureFilter.Linear
        assetManager.load("fonts/font_main_small.ttf", BitmapFont::class.java, paramsS)

        val paramsM = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        paramsM.fontParameters.characters = FontCharacters.en + FontCharacters.ru
        paramsM.fontFileName = "fonts/font_main.ttf"
        paramsM.fontParameters.size = 75
        paramsM.fontParameters.minFilter = Texture.TextureFilter.Linear
        paramsM.fontParameters.magFilter = Texture.TextureFilter.Linear
        assetManager.load("fonts/font_main_medium.ttf", BitmapFont::class.java, paramsM)

        val paramsL = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        paramsL.fontParameters.characters = FontCharacters.en + FontCharacters.ru
        paramsL.fontFileName = "fonts/font_main.ttf"
        paramsL.fontParameters.size = 96
        paramsL.fontParameters.minFilter = Texture.TextureFilter.Linear
        paramsL.fontParameters.magFilter = Texture.TextureFilter.Linear
        assetManager.load("fonts/font_main_large.ttf", BitmapFont::class.java, paramsL)
    }
}