package com.me.test.video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class TestVid {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("begin");
		    String format = "png";
	     String processingFilePattern = "shot_";
	     String convertedFilePattern = "norm_";
		
		File file = new File(ScreenShot.sourceDirName+ "\\shot_000018.png"); 
		BufferedImage originalImage = ImageIO.read(file);
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
 
		BufferedImage resizeImageHintPng = VideoPacker.resizeImageWithHint(originalImage, type);
		String name = file.getName().replace(processingFilePattern, convertedFilePattern);
		ImageIO.write(resizeImageHintPng, format, new File("d:\\", name));

		System.out.println("end");
	}

}
