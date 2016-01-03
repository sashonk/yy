package com.me.test.video;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenShot implements Runnable
{
    private static int fileCounter = 0;
    private Pixmap pixmap;
    static String sourceDirName = "D:\\tmp2";


    @Override
    public void run()
    {
        saveScreenshot();
    }

    public void prepare()
    {
        getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    public void saveScreenshot()
    {
        FileHandle file = new FileHandle(sourceDirName+"/shot_"+ String.format("%06d",  fileCounter++) + ".png");
        PixmapIO.writePNG(file, pixmap);
        pixmap.dispose();
    }

    public void getScreenshot(int x, int y, int w, int h, boolean flipY)
    {
        Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
        pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixmap.getPixels());
    }
}
