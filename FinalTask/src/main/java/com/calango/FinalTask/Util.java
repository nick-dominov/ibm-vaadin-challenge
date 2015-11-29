/**
 * 
 */
package com.calango.FinalTask;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
 
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * @author calango
 *
 */
public class Util {

	
	public static byte[] imageToBytes(File image) {
		try (FileInputStream fis = new FileInputStream(image)) {
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
				byte[] buf = new byte[1024];
			    
			    for (int readNum; (readNum = fis.read(buf)) != -1;) {
			    	//Writes to this byte array output stream
			    	bos.write(buf, 0, readNum); 
				}
			    return bos.toByteArray();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static File bytesToImage(byte[] byteImage) {
		File image = new File("extrafile.jpg");
		try (ByteArrayInputStream bis = new ByteArrayInputStream(byteImage)) {
			Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
			//ImageIO is a class containing static methods for locating ImageReaders
	        //and ImageWriters, and performing simple encoding and decoding. 
	 
	        ImageReader reader = (ImageReader) readers.next();
	        Object source = bis; 
	        ImageInputStream iis = ImageIO.createImageInputStream(source); 
	        reader.setInput(iis, true);
	        ImageReadParam param = reader.getDefaultReadParam();
	 
	        Image image1 = reader.read(0, param);
	        //got an image file
	 
	        BufferedImage bufferedImage = new BufferedImage(image1.getWidth(null), image1.getHeight(null), BufferedImage.TYPE_INT_RGB);
	        //bufferedImage is the RenderedImage to be written
	 
	        Graphics2D g2 = bufferedImage.createGraphics();
	        g2.drawImage(image1, null, null);
	        ImageIO.write(bufferedImage, "jpg", image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
}
