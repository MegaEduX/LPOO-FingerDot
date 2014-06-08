//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontGenerator {
    private static final String kDefaultFontFileName = "hecubus.ttf";

    /**
     * Generates a bitmap font with a given size.
     *
     * @param size The size of the font.
     * @return The bitmap font.
     */

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
