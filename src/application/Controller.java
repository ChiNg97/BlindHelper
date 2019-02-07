package application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.sun.javafx.geom.Vec3f;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.Utilities;
import javax.swing.JFileChooser;
import java.lang.Object;

public class Controller {
	
	@FXML
	private ImageView imageView; // the image display window in the GUI
	
	//start modifying
	@FXML
	private Slider slider;//adding slider control bar
	
	//A VideoCapture object is used to manipulate the video
	
	private VideoCapture capture; 
	private ScheduledExecutorService timer;



	private Mat image;
	
	private int width;
	private int height;
	private double[][] bigarr;
	
	@FXML
	private void initialize() {
		// Optional: You should modify the logic so that the user can change these values
		// You may also do some experiments with different values
		width = 32;
		height=32;
		

	}
	
	
	protected void createFrameGrabber() throws InterruptedException {
		  if (capture != null && capture.isOpened()) { // the video must be open
		    double framePerSecond = capture.get(Videoio.CAP_PROP_FPS);
		    double total_frame = capture.get(Videoio.CAP_PROP_FRAME_COUNT);
		    //System.out.println(total_frame);
		    double current_frame_count=  capture.get(Videoio.CAP_PROP_POS_FRAMES);
		   
		    // create a runnable to fetch new frames periodically
		    Runnable frameGrabber = new Runnable() {
			      @Override
			      public void run() {
				  Mat frame = new Mat();
				  //System.out.println("frame number is "+index);
				  if (capture.read(frame)) { // decode successfully
					  if (!frame.empty()){
						  Mat resizedImage = new Mat();
				    		 Imgproc.resize(frame, resizedImage, new Size(width, height));  
				    		 resizedImage= rgb_to_rb(resizedImage);
				    		 //int count=0;
				    		 double currentFrameNumber = capture.get(Videoio.CAP_PROP_POS_FRAMES);
				    		 capture.set(Videoio.CAP_PROP_POS_FRAMES,currentFrameNumber+1 );
				    		 Mat nextframe = grabFrame();
							 Mat resizedImage_frame2 = new Mat();
					    	 Imgproc.resize(nextframe, resizedImage_frame2, new Size(width, height));
					    	 resizedImage_frame2=rgb_to_rb(resizedImage_frame2);
					    	 double [][]hist1=makehis(resizedImage);
					    	 double [][]hist2=makehis(resizedImage_frame2);
				    		 int ivalue=ivalue(hist1,hist2);
				    		 System.out.println(ivalue);
			    		    //System.out.println(resizedImage.size());
					    	// System.out.println( resizedImage_frame2.size());
					    	/* int ix; int jx;
					    	// System.out.println(resizedImage.height());
					    	// int count=0;
					    	 
			    		   for ( ix=0; ix<resizedImage.width();ix++){
			    		    	//we want to read each column at the time 
			    			   double [][]hist1=new double[6][6];
			    		    	double [][]hist2=new double[6][6];
			    		    	System.out.println(hist1.length);
			    		    	int i; int j;
			    				 for(i=0;i<hist1.length;i++){
			    					 for ( j=0;j<hist1.length; j++){
			    						 hist1[i][j]=0.0;
			    					 	 hist2[i][j]=0.0;
			    					    // System.out.println(hist2[i][j]);
			    					 }
			    				 }
								   
								    				 //System.out.println(resizedImage.height());
								    				  for ( jx=0; jx <resizedImage.height();jx++){
								    					  
								    					  double[] data =resizedImage.get(ix, jx);
								    					  double G = data[1]/255;
								    					  double R = data[2]/225;
								    					 // System.out.println(G);
								    					  double[] data2 =resizedImage_frame2.get(ix, jx);
								    					  double G2 = data2[1]/255;
								    					  double R2 = data2[2]/225;
								    					  
										    		    	
								    					 // System.out.println(G2);
								    					 // count++;
								    					 // hist1[(int)Math.floor(R*6)][(int)Math.floor(G*6)]++;
								    					 // hist2[(int)Math.floor(R2*6)][(int)Math.floor(G2*6)]++;
								    					  //System.out.println( hist1[(int)Math.floor(R*6)][(int)Math.floor(G*6)]);
								    					  
								    				  }
								    				 /* double sum_el_1=0.0;
									    			  double sum_el_2=0.0;
									    			  System.out.println(sum_el_1);
								    				  for(i=0;i<hist1.length;i++){
									    					 for ( j=0;j<hist1.length; j++){
								    						  sum_el_1+=hist1[i][j];
								    						  sum_el_2+=hist2[i][j];
								    						 // System.out.println(hist1[i][j]);
								    						 // System.out.println(hist2[i][j]);
								    					  }
								    				  }
								    				  for( i=0;i<hist1.length;i++){
								    					  for(j=0;j<hist1.length ;j++){
								    						  hist1[i][j]/=sum_el_1;
								    						  hist1[i][j]/=sum_el_2;
								    						 // System.out.println(hist1[i][j]);
								    						 // System.out.println(hist2[i][j]);
								    						 // count++;
								    					  }
								    				  }
			    				  
								    				 int ivalue=ivalue(hist1,hist2) ;
								    				 //System.out.println(ivalue);
								    				  
								    				  
			    				  
			    				  
			    		    }*/
			    		  // System.out.println(count);
			    		   double newcurrentFrameNumber = capture.get(Videoio.CAP_PROP_POS_FRAMES);
				    	   capture.set(Videoio.CAP_PROP_POS_FRAMES,newcurrentFrameNumber-1 );
			    		  // System.out.println("total count is "+count);
			    		    
			    		  // float[][] frame1= makehis(resizedImage);
						  Image im = Utilities.mat2Image(resizedImage); 
						  
						  Utilities.onFXThread(imageView.imageProperty(), im); 
						  //index++;
						 // capture.set(Videoio.CAP_PROP_POS_FRAMES, index);
						  
					  }
				  }
				  else{
					  capture.release();
				  }
			  }
			  };
		    
		    // terminate the timer if it is running 
		    if (timer != null && !timer.isShutdown()) {
		      timer.shutdown();
		      timer.awaitTermination(Math.round(1000/framePerSecond), TimeUnit.MILLISECONDS);
		    }
				
		    // run the frame grabber
		    timer = Executors.newSingleThreadScheduledExecutor();
		    timer.scheduleAtFixedRate(frameGrabber, 0, Math.round(1000/framePerSecond), TimeUnit.MILLISECONDS);
		  }
		    
		    
		  
		  
		  
		}
	
	
	@FXML
	protected void openImage(ActionEvent event) throws InterruptedException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		   // System.out.println("Selected file: " + selectedFile.getAbsolutePath());

		capture = new VideoCapture(selectedFile.getAbsolutePath()); // open video file
		  if (capture.isOpened()) { // open successfully
			  createFrameGrabber();
		        
			    ChangeListener<Number> changeListener = new ChangeListener<Number>() {
				      @Override 
				      public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				    	  try {
						  		if (capture.isOpened()) { 
						  			
									double totalFrameCount = capture.get(Videoio.CAP_PROP_FRAME_COUNT);
									double percentageOfFramesToBeSkipped =  slider.getValue()/(slider.getMax()-slider.getMin());
									double numberOfFramesToBeSkipped = totalFrameCount * percentageOfFramesToBeSkipped;
									double framePerSecond = capture.get(Videoio.CAP_PROP_FPS);

									
									if (timer != null && !timer.isShutdown()) {
										timer.shutdown();
										timer.awaitTermination(Math.round(1000/framePerSecond), TimeUnit.MILLISECONDS);
									}
						  			
									
									capture.set(Videoio.CAP_PROP_POS_FRAMES, numberOfFramesToBeSkipped);
									
									
									createFrameGrabber();
									Mat frame = new Mat();
									if (capture.read(frame)) { 
										Mat frameCopy = new Mat();
										frame.copyTo(frameCopy);
										
										Image im = Utilities.mat2Image(frameCopy);
										imageView.setImage(im);
									}
									
								}
				    	  } catch (InterruptedException e) {
				  			System.err.println("Something is wrong: " + e);;
				    	  }		
				      }
				 };
			
		        slider.valueProperty().addListener(changeListener);

			  
			  
		  }
		}
		
	}
	
	
	protected Mat rgb_to_rb(Mat resizedImage){
		for (int i=0; i<resizedImage.height();i++){
			  
			  for (int j=0; j <resizedImage.width();j++){
				  try{
				  double[] data =resizedImage.get(i, j);
				  double B =data[0];
				  double G = data[1];
				  double R = data[2];
				  float r = (float) (R/(R+G+B))*255;
	              float g = (float) (G/(R+G+B))*255;
	              float b=  (float) (B/(R+G+B))*255;
	              data[0] = b;
	              data[1] = g;
	              data[2] = r;
	            /*  System.out.println("b is "+b);
	              System.out.println("g is "+g);
	              System.out.println("r is "+r);*/
	              resizedImage.put(i, j, data);
				  }
				  catch(NullPointerException e)
				  {	
					  System.out.println("Error");
					  System.out.println("width = " + resizedImage.width());
					  System.out.println("height = " + resizedImage.height());

					  			    					  
					  System.out.println("i = " + i);
					  System.out.println("j = " + j);
					  capture.release();
					  
				  }
			  }
		  }
		  return resizedImage;
		
	}
	
	protected Mat grabFrame()
	{
		// init everything
		Mat frame = new Mat();
		
		// check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);
				
				// if the frame is not empty, process it
				
				
			}
			catch (Exception e)
			{
				// log the error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		
		return frame;
	}

	
	protected int ivalue(double[][]hist1,double[][]hist2){
		if (hist1.length!=hist1.length){
			throw new IllegalArgumentException("the histograms have different size");
			
		}
		int I=0;
		for (int i=0; i<hist1[0].length; i++){
			for(int j=0; j<hist1[0].length;j++){
				if (hist1[i][j]<hist2[i][j]){
					I+=hist1[i][j];
				}
				else{
					I+=hist2[i][j];
				}
			}
		}
		return I;
		
	}
	
	protected double[][] makehis(Mat image){
		 int N=(int) Math.floor(((1+Math.log(image.width())/Math.log(2))));
		 double [][] ch=new double[N][N];
		 for(int i=0;i<N;i++){
			 for (int j=0;j<N; j++)
				 ch[i][j]=0;
		 }
		 double sum_el=0.0;
		  
		 
		  for (int i = 0; i< image.height(); i++){
		
			  for (int j = 0; j < image.width(); j++){
				  try{
				  double[] data = image.get(i, j);
				  double G = data[1]/255;
				  double R = data[2]/225;
				  //System.out.println("R value = " + Math.floor(R*N));
				  //System.out.println("G value = " + Math.floor(G*N));
				  ch[(int)Math.floor(R*N)][(int)Math.floor(G*N)]++;
				  }
  			  catch(IndexOutOfBoundsException e){
  				 // System.out.println("i = " + i);
  				  //System.out.println("j = " + j);
  			  }
			  }
		  }
		  for(int i=0;i<N;i++){
			  for(int j=0;j<N;j++){
				  sum_el+=ch[i][j];
				  //System.out.println(ch[i][j]);
			  }
		  }
		  
		  for(int i=0;i<N;i++){
			  for(int j=0;j<N;j++){
				  ch[i][j]/=sum_el;
				 // System.out.println(ch[i][j]);
			  }
		  }
		  
		return ch;
		
	}
}