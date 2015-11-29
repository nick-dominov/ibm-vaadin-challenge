package com.calango.FinalTask;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.viritin.fields.MTable;
import org.watson.visualrecognition.VisualRecognitionService;
import org.watson.visualrecognition.response.Image;
import org.watson.visualrecognition.response.Label;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.calango.FinalTask.MyAppWidgetset")
public class MyUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Panel panel = new Panel("Cool Image Storage");
	
	ByteArrayOutputStream baos;
	
	byte[] imageInByte;
	
	String name;
	
	FileOutputStream fos;
	
	/**
	 * 
	 */
	public File file;

	@Inject
	VisualRecognitionService service;

	MTable<Label> results = new MTable<>(Label.class);
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

     // Show uploaded file in this placeholder
     		final Embedded image = new Embedded("Uploaded Image");
     		image.setVisible(false);

     		// Implement both receiver that saves upload in a file and
     		// listener for successful upload
     		class ImageUploader implements Receiver, SucceededListener {
     			/**
     			 * 
     			 */
     			private static final long serialVersionUID = 1L;

     			public OutputStream receiveUpload(String filename, String mimeType) {
    				try {
    					// Open the file for writing.
    					file = new File(filename);
    					//BufferedImage originalImage = ImageIO.read(file);
    					fos = new FileOutputStream(file);
    				} catch (final IOException e) {
    					new Notification("Could not open file ", e.getMessage(), Notification.Type.ERROR_MESSAGE)
    							.show(Page.getCurrent());
    					return null;
    				}
    				return fos; // Return the output stream to write to
     			}

     			public void uploadSucceeded(SucceededEvent event) {
     				// Show the uploaded file in the image viewer
     				byte[] by = Util.imageToBytes(file);
     				image.setVisible(true);
     				
     				image.setSource(new FileResource(Util.bytesToImage(by)));
     				//image.setSource(new FileResource(file));
     				try {
     					service.init();
     				} catch (Exception ex) {
     					new Notification("Exc: " + ex.toString() + "\n could you please view the code: https://github.com/nick-dominov/ibm-vaadin-challenge\n" + 
     							"I looked on this & watsonAPI code and came to this conclusion:\n" +
     							"1) Transforming picture to bytes and vice versa is valid\n" + 
     							"2) I added IBM visual reconnition successfuly\n" +
     							"3) When I try to appeal the watson api it returnes me NullPointerException\n" + 
     							"I'm a student & it's my first experience with vaadin & watson, could you please view the code & give me a piece\n of advice how could I resoulved this situation?\n" +
     							"Thank you in advance, my mail is calango@mail.ua",ex.getMessage(), Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
     				}	
     					Image imageData = service.recognize(Util.imageToBytes(file));
     					
     				results.setBeans(imageData.getLabels());
     					
     			}
     		}
     		;
     		ImageUploader receiver = new ImageUploader();

     		// Create the upload with a caption and set receiver later
     		Upload upload = new Upload("Upload Image Here", receiver);
     		upload.setButtonCaption("Start Upload");
     		upload.addSucceededListener(receiver);
     		
        layout.addComponent(upload);
        layout.addComponent(image);
        layout.addComponent(results);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
    }
}
