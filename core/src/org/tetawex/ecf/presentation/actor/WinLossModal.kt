package org.tetawex.ecf.presentation.actor

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.model.GameData
import org.tetawex.ecf.presentation.PAUSE_BUTTON_HEIGHT
import org.tetawex.ecf.presentation.PAUSE_BUTTON_PAD
import org.tetawex.ecf.presentation.PAUSE_BUTTON_WIDTH
import org.tetawex.ecf.presentation.screen.StyleFactory
import java.util.*

class WinLossModal(
    val game: ECFGame,
    val preferences: ECFPreferences,
    val onNextPressed: () -> Unit,
    val onRetryPressed: () -> Unit,
    val onQuitPressed: () -> Unit
) :
    Table() {
    private val wlLabel: Label
    private val wlScore: Label
    private val wlSpareMana: Label
    private val wlTotal: Label
    private val wlReasonLabel: Label
    private val winLossMenuButtonNext: TextButton

    private val winSound: Sound = game.assetManager.get("sounds/win.ogg", Sound::class.java)
    private val lossSound: Sound = game.assetManager.get("sounds/loss.ogg", Sound::class.java)

    private var starImages: Array<Image?> = arrayOf()
    private val starRegion: TextureRegion = game.getTextureRegionFromAtlas("star")
    private val starDisabledRegion: TextureRegion = game.getTextureRegionFromAtlas("star_ungained")


    private val lcToStringMap: MutableMap<GameData.LossCondition, String>

    init {
        lcToStringMap = EnumMap(GameData.LossCondition::class.java)
        lcToStringMap[GameData.LossCondition.NO_MANA] = "lc_no_mana"

        lcToStringMap[GameData.LossCondition.NO_FIRE] = "lc_no_fire"
        lcToStringMap[GameData.LossCondition.NO_WATER] = "lc_no_water"

        lcToStringMap[GameData.LossCondition.NO_AIR] = "lc_no_air"
        lcToStringMap[GameData.LossCondition.NO_EARTH] = "lc_no_earth"

        lcToStringMap[GameData.LossCondition.NO_SHADOW] = "lc_no_shadow"
        lcToStringMap[GameData.LossCondition.NO_LIGHT] = "lc_no_light"

        //win/loss ui
        wlLabel = Label("", StyleFactory.generateLargeStandardLabelStyle(game))
        wlReasonLabel = Label("", StyleFactory.generateStandardLabelStyle(game))

        wlReasonLabel.wrap = true
        wlReasonLabel.setAlignment(Align.center)

        add<Label>(wlLabel).pad(20f).row()
        add<Label>(wlReasonLabel).pad(20f).width(PAUSE_BUTTON_WIDTH).row()

        val starsTable = Table()
        starImages = Array(3) { null }

        for (i in starImages.indices) {
            val image = Image(starDisabledRegion)
            starImages[i] = image
            starsTable.add(image).size(200f).pad(20f)
        }

        val scoreTable = Table()
        scoreTable.add(
            Label(
                game.getLocalisedString("score"),
                StyleFactory.generateStandardLabelStyle(game)
            )
        )
        wlScore = Label("", StyleFactory.generateStandardLabelStyle(game))
        scoreTable.add<Label>(wlScore).padRight(40f)
        add(scoreTable).pad(20f).row()

        val spareTable = Table()
        spareTable.add(
            Label(
                game.getLocalisedString("spare_mana"),
                StyleFactory.generateStandardLabelStyle(game)
            )
        )
        wlSpareMana = Label("", StyleFactory.generateStandardLabelStyle(game))
        spareTable.add<Label>(wlSpareMana)
        add(spareTable).pad(20f).row()

        val totalTable = Table()
        totalTable.add(
            Label(
                game.getLocalisedString("total"),
                StyleFactory.generateStandardLabelStyle(game)
            )
        )
        wlTotal = Label("", StyleFactory.generateStandardLabelStyle(game))
        totalTable.add<Label>(wlTotal)
        add(totalTable).pad(20f).row()
        add(starsTable).pad(20f).row()

        winLossMenuButtonNext = TextButton(
            game.getLocalisedString("next"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )

        winLossMenuButtonNext.addListener(
            object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    onNextPressed()
                }
            })
        add<TextButton>(winLossMenuButtonNext)
            .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
            .center().pad(PAUSE_BUTTON_PAD).row()

        val winLossMenuButtonRetry = TextButton(
            game.getLocalisedString("retry"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        winLossMenuButtonRetry.addListener(
            object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    onRetryPressed()
                }
            })

        add(winLossMenuButtonRetry)
            .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
            .center().pad(PAUSE_BUTTON_PAD).row()


        val winLossMenuButtonQuit = TextButton(
            game.getLocalisedString("quit"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        winLossMenuButtonQuit.addListener(
            object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    onQuitPressed()
                }
            })

        add(winLossMenuButtonQuit)
            .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
            .center().pad(PAUSE_BUTTON_PAD).row()
    }

    fun setData(payload: GameData.WinLossPayload) {
        winLossMenuButtonNext.isVisible = payload is GameData.WinLossPayload.Win

        val (rawScore, manaScore, totalScore, stars, maxScore) = payload.totals

        wlScore.setText(" $rawScore")
        wlSpareMana.setText(" $manaScore")
        wlTotal.setText(" $totalScore")

        when (payload) {
            is GameData.WinLossPayload.Win -> {
                winLossMenuButtonNext.isVisible = true
                winSound.play(preferences.soundVolume)

                winLossMenuButtonNext.height = PAUSE_BUTTON_HEIGHT
                wlReasonLabel.isVisible = false
                wlLabel.setText(game.getLocalisedString("level_success"))

                for (i in 0 until stars) {
                    starImages[i]!!.drawable = TextureRegionDrawable(starRegion)
                }
            }
            is GameData.WinLossPayload.Loss -> {
                val lossCondition = payload.lossCondition
                lossSound.play(preferences.soundVolume)
                winLossMenuButtonNext.height = 0f
                wlLabel.setText(game.getLocalisedString("level_fail"))
                wlReasonLabel.setText(game.getLocalisedString(lcToStringMap[lossCondition]!!))
                wlReasonLabel.isVisible = true
            }
        }
    }
}