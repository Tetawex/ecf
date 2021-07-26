import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.presentation.DEFAULT_PADDING_HALF
import org.tetawex.ecf.presentation.VIEWPORT_WIDTH
import org.tetawex.ecf.presentation.screen.StyleFactory

class ElementCounterWidget(game: ECFGame) : Table() {
    companion object {
        private const val ELEMENT_COUNTER_IMAGE_SIZE = 130f
    }

    private val fireCounterLabel: Label
    private val waterCounterLabel: Label

    private val airCounterLabel: Label
    private val earthCounterLabel: Label

    private val shadowCounterLabel: Label
    private val lightCounterLabel: Label

    init {
        val elementLabelStyle = StyleFactory.generateDarkestLabelStyle(game)
        fireCounterLabel = Label("0", elementLabelStyle)
        waterCounterLabel = Label("0", elementLabelStyle)
        airCounterLabel = Label("0", elementLabelStyle)
        earthCounterLabel = Label("0", elementLabelStyle)
        shadowCounterLabel = Label("0", elementLabelStyle)
        lightCounterLabel = Label("0", elementLabelStyle)

        //fire-water
        val fwTable = Table()
        val fireTable = Table()
        val waterTable = Table()

        fireTable.add(
            Image(
                game.getTextureRegionFromAtlas("element_fire")
            )
        ).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        fireTable.add<Label>(fireCounterLabel)
        waterTable.add(
            Image(
                game.getTextureRegionFromAtlas("element_water")
            )
        ).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        waterTable.add<Label>(waterCounterLabel)

        fwTable.add(fireTable)
        fwTable.add(waterTable)

        //air-earth
        val aeTable = Table()
        val airTable = Table()
        val earthTable = Table()

        airTable.add(
            Image(
                game.getTextureRegionFromAtlas("element_air")
            )
        ).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        airTable.add<Label>(airCounterLabel)
        earthTable.add(
            Image(
                game.getTextureRegionFromAtlas("element_earth")
            )
        ).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        earthTable.add<Label>(earthCounterLabel)

        aeTable.add(airTable)
        aeTable.add(earthTable)

        //shadow-light
        val slTable = Table()
        val shadowTable = Table()
        val lightTable = Table()

        shadowTable.add(
            Image(
                game.getTextureRegionFromAtlas("element_shadow")
            )
        ).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        shadowTable.add<Label>(shadowCounterLabel)
        lightTable.add(
            Image(
                game.getTextureRegionFromAtlas("element_light")
            )
        ).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        lightTable.add<Label>(lightCounterLabel)

        slTable.add(shadowTable)
        slTable.add(lightTable)

        add(fwTable).pad(DEFAULT_PADDING_HALF)
        add(aeTable).prefWidth(VIEWPORT_WIDTH)
        add(slTable).pad(DEFAULT_PADDING_HALF)
    }

    fun setElementCount(
        fire: Int,
        water: Int,
        air: Int,
        earth: Int,
        shadow: Int,
        light: Int,
        time: Int
    ) {
        fireCounterLabel.setText(fire.toString())
        waterCounterLabel.setText(water.toString())
        airCounterLabel.setText(air.toString())
        earthCounterLabel.setText(earth.toString())
        shadowCounterLabel.setText(shadow.toString())
        lightCounterLabel.setText(light.toString())
    }
}