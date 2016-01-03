package com.me.test.video;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class VideoPacker {

	/**
	 * @param args
	 * @throws Exception 
	 */
	static int IMG_WIDTH = 1280;
    static int IMG_HEIGHT= 720;
    static String outputDirName ="D:\\png2\\";
    static String processingFilePattern = "shot_";
    static String convertedFilePattern = "norm_";
    static String audioFileName = "d:\\music\\undercover3.wav";
    static String format = "png";
    static String mencoderPath = "D:\\mplayer\\mencoder";
    static String outputVideoFileName = "D:\\video\\google30.avi";
    static final int fps = 30;
    
    static final boolean noaudio = true;
    static final boolean bypass_processing = false;
    
	
    public static synchronized void println(PrintStream ps , String message){
    	ps.println(message);
    }
    
	public static void main(String[] args) throws Exception {
		System.out.println("begin");
		long timeStart = System.currentTimeMillis();
		

		
		if(!bypass_processing){
			final File outputDir = new File(outputDirName);
			if(!outputDir.exists()){
				outputDir.mkdir();
			}
			
			
			
			File[] rawImagesFiles = new File(ScreenShot.sourceDirName).listFiles();
			
			ExecutorService service = Executors.newFixedThreadPool(8);
			for(final File file : rawImagesFiles){
				service.submit(new Runnable() {
					
					@Override
					public void run() {
						try{
						if(!file.getName().endsWith("."+format) || !file.getName().contains(processingFilePattern)){
							return;
						}
						
						BufferedImage originalImage = ImageIO.read(file);
						int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
				 
						BufferedImage resizeImageHintPng = resizeImageWithHintNoScale(originalImage, type);
						String name = file.getName().replace(processingFilePattern, convertedFilePattern);
						ImageIO.write(resizeImageHintPng, format, new File(outputDir, name));
						println(System.out,"processed "+file.getName());
						}
						catch(Exception ex){
							println(System.err,ex.toString());
						}
					}
				});
			}
/*			for(File file : rawImagesFiles){
				if(!file.getName().endsWith("."+format) || !file.getName().contains(processingFilePattern)){
					continue;
				}
				
				BufferedImage originalImage = ImageIO.read(file);
				int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		 
				BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
				String name = file.getName().replace(processingFilePattern, convertedFilePattern);
				ImageIO.write(resizeImageHintPng, format, new File(outputDir, name));
				System.out.println("processed "+file.getName()); 
			}*/
			
			service.shutdown();
			service.awaitTermination(Integer.MAX_VALUE, TimeUnit.MINUTES);
		}
		
		String[] cmd = new String[3];
        cmd[0] = "cmd.exe" ;
        cmd[1] = "/C";
        cmd[2] = noaudio ? String.format("cd %s & %s mf://%s*.png -mf w=1080:h=720:fps=%d:type=png -ovc x264  -o %s -x264encopts bitrate=5000:bframes=2:subq=6:frameref=3:pass=1:nr=2000",outputDirName,mencoderPath, convertedFilePattern,fps,outputVideoFileName) :        		
        		String.format("cd %s & %s mf://%s*.png -mf w=1080:h=720:fps=%d:type=png -ovc x264 -audiofile %s -oac copy -o %s -x264encopts bitrate=5000:bframes=2:subq=6:frameref=3:pass=1:nr=2000",outputDirName,mencoderPath, convertedFilePattern,fps, audioFileName,outputVideoFileName); 
        
        
        try
        {            

            
            Runtime rt = Runtime.getRuntime();
        
            Process proc = rt.exec(cmd);
            
            // any error message?
            StreamGobbler errorGobbler = new 
                StreamGobbler(proc.getErrorStream(), "ERROR");            
            
            // any output?
            StreamGobbler outputGobbler = new 
                StreamGobbler(proc.getInputStream(), "OUTPUT");
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
                                    
            // any error???
            int exitVal = proc.waitFor();
            System.out.println("ExitValue: " + exitVal);        
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
		
		
		
        System.out.println();
        System.out.println("==============================");
		System.out.println("finished. total time = " + formatTime(System.currentTimeMillis() - timeStart));
	}
	
	static String formatTime(long millis){
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		return String.format("%02d:%02d:%02d:%d", hour, minute, second, millis);
	}
	
    public static BufferedImage resizeImageWithHintNoScale(BufferedImage originalImage, int type){
   	 
		BufferedImage resizedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), type);
		Graphics2D g = resizedImage.createGraphics();
		
		double rotationRequired = Math.PI;
		double locationX = originalImage.getWidth() / 2;
		double locationY = originalImage.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);		
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		g.drawImage(op.filter(originalImage, null), 0, 0, null);
		
		tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-resizedImage.getWidth(null), 0);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		resizedImage = op.filter(resizedImage, null);
		
		
		g.dispose();	
		g.setComposite(AlphaComposite.Src);
	 
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
 
		return resizedImage;
    }
	
    public static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){
    	 
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		
		double rotationRequired = Math.PI;
		double locationX = originalImage.getWidth() / 2;
		double locationY = originalImage.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);		
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		g.drawImage(op.filter(originalImage, null), (IMG_WIDTH-originalImage.getWidth())/2, 0, null);
		
		tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-resizedImage.getWidth(null), 0);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		resizedImage = op.filter(resizedImage, null);
		
		
		g.dispose();	
		g.setComposite(AlphaComposite.Src);
	 
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
 
		return resizedImage;
    }

}

class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    
    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is, "cp866");
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                System.out.println(type + ">" + line);    
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }
}
