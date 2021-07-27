package org.tetawex.ecf.presentation.screen.settings

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.model.Language
import org.tetawex.ecf.presentation.*
import org.tetawex.ecf.presentation.widget.OutlineTextButton
import org.tetawex.ecf.presentation.widget.SafeAreaContainer
import org.tetawex.ecf.presentation.widget.background.CommonBackground
import org.tetawex.ecf.util.FontCharacters

private const val LIST_LABEL_HEIGHT = DEFAULT_PADDING

class LanguageSelectModal(
    val game: ECFGame,
    onBackPressed: () -> Unit,
    onLanguageSelected: (language: Language) -> Unit,
    onDefaultLanguageSelected: () -> Unit
) : Stack() {
    init {
        setFillParent(true)
        add(CommonBackground(game))

        val languageSelectTable = Table()
        add(SafeAreaContainer(languageSelectTable))

        val languageListTable = Table()

        val defaultButton = LanguageListElement(game, game.getLocalisedString("auto"))
        defaultButton.left()
        languageListTable
            .add(defaultButton)
            .fillX()
            .row()

        defaultButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                onDefaultLanguageSelected()
            }
        })
        for (language in FontCharacters.supportedLanguages) {
            val button = LanguageListElement(game, language.name)
            languageListTable
                .add(button)
                .growX()
                .row()
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    onLanguageSelected(language)
                }
            })
        }

        languageSelectTable.add(languageListTable).growX().padBottom(DEFAULT_PADDING).row()

        val languageSelectBackButton = OutlineTextButton(game.getLocalisedString("back"), game)
        languageSelectBackButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                onBackPressed()
            }
        })
        languageSelectTable.add(languageSelectBackButton).center().row()

        languageListTable.debug = true
    }
}

fun LanguageListElement(game: ECFGame, languageName: String): TextButton {
    val button =
        TextButton(languageName, StyleFactory.generateLanguageMenuButtonStyle(game))

    button.label.setAlignment(Align.topLeft)
    button.height = LIST_LABEL_HEIGHT

    return button
}