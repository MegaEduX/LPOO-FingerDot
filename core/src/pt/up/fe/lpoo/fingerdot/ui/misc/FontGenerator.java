package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * fingerdot
 * <p/>
 * Created by MegaEduX on 31/05/14.
 */
public class FontGenerator {
    private static final String kDefaultFontFileName = "hecubus.ttf";

    static public BitmapFont generateBitmapFont(int size) {
        FileHandle fontFile = Gdx.files.internal(kDefaultFontFileName);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = size;

        BitmapFont font = generator.generateFont(param);

        generator.dispose();

        return font;
    }
}
