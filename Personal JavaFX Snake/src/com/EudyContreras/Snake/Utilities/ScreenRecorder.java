package com.EudyContreras.Snake.Utilities;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

public class ScreenRecorder {
	private int frame = 0;
	private int timer = 0;
	private int play_time;
	private int counter = 0;
	private long capture_rate = (long) (1000f / 60.0f);
	private double width;
	private double height;
	private double frameCap = 1.0 / 60.0f;
	private double video_size_scale = 1.0;
	private double bounds_scale_X = 0.5;
	private double bounds_scale_Y = 0.5;
	private boolean saveFrames = false;
	private boolean loadFrames = false;
	private boolean allowRecording = false;
	private boolean allowPlayback = false;
	private boolean showIndicators = false;
	private Pane indicator_layer;
	private Pane video_screen;
	private Scene scene;
	private Timeline videoPlayer;
	private ArrayList<Image> recorded_frames;
	private ArrayList<ImageView> video_frames;
	private ArrayList<byte[]> temp_frames;
	private final SnapshotParameters parameters = new SnapshotParameters();
	private final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
	private final Indicator recording = new Indicator(Color.RED, " Recording..");
	private final Indicator playing = new Indicator(Color.GREEN, " Playing..");
	private final Indicator idle = new Indicator(Color.YELLOW, "paused..");
	private final String VIDEO_NAME = "recording.ser";
	private final String FRAME_NAME = "image";
	private final String DIRECTORY_NAME = "Snake Game Videos"+ File.separator;
	private final String PATH_ROOT = System.getProperty("user.home") + "/Desktop" + File.separator +DIRECTORY_NAME;
	private final String FILE_EXTENSION = "png";
	private final String PATH_FRAME = PATH_ROOT + FRAME_NAME;
	private final String PATH_VIDEO = PATH_ROOT + VIDEO_NAME;

	public ScreenRecorder(Scene scene, Pane indicatorLayer, int record_time) {
		if(showIndicators)
		this.indicator_layer = indicatorLayer;
		this.scene = scene;
		this.width = scene.getWidth();
		this.height = scene.getHeight();
		this.play_time = record_time * 60;
		this.initStorages();
		this.loadRecording();
		this.scaleResolution(0, 0, false);
	}

	private void initStorages() {
		video_frames = new ArrayList<ImageView>();
		recorded_frames = new ArrayList<Image>();
		temp_frames = new ArrayList<byte[]>();
	}

	private void loadRecording(){
		if (loadFrames) {
			loadFromFile();
		} else {
			retrieveRecording(temp_frames);
		}
	}
	private void resetStorage() {
		if (video_frames != null)
			video_frames.clear();
		if (recorded_frames != null)
			recorded_frames.clear();
		if (video_screen != null)
			video_screen.getChildren().clear();
	}

	public void startRecorder() {
		if (!allowRecording) {
			resetStorage();
			if(showIndicators)
			showIndicator(indicator_layer.getChildren(), recording);
			videoRecorder();
			allowRecording(true);
			logState("Recording...");
		}
	}

	public void stopRecorder() {
		if (allowRecording) {
			if(showIndicators)
			showIndicator(indicator_layer.getChildren(), idle);
			allowRecording(false);
			logState("Recording stopped");
			logState("Amount of recorded frames: " + recorded_frames.size());
			processVideo();
			saveVideo();
		}
	}

	public void starPlayer(Pane output_screen) {
		video_screen = output_screen;
		if(showIndicators)
		showIndicator(indicator_layer.getChildren(), playing);
		if (video_frames.size() > 0) {
			if (videoPlayer == null)
				videoPlayer();
			else {
				videoPlayer.play();
			}
			allowPlayback(true);
		}
		logState("Video playback..");
	}

	public void stopPlayer() {
		if(showIndicators)
		showIndicator(indicator_layer.getChildren(), idle);
		if (videoPlayer != null)
			videoPlayer.stop();
		logState("Playback stopped");
		allowPlayback(false);
	}

	private void videoRecorder() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				while (true) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (allowRecording && play_time > 0) {
								recorded_frames.add(create_frame());
							}

							recordingTimer();
						}
					});
					Thread.sleep(capture_rate);
				}
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

	private void videoPlayer() {

		videoPlayer = new Timeline();
		videoPlayer.setCycleCount(Timeline.INDEFINITE);

		KeyFrame keyFrame = new KeyFrame(Duration.seconds(frameCap), // 60FPS

				new EventHandler<ActionEvent>() {

					public void handle(ActionEvent e) {

						if (allowPlayback) {

							playbackVideo();

						}
					}
				});

		videoPlayer.getKeyFrames().add(keyFrame);
		videoPlayer.play();

	}

	private void recordingTimer() {
		timer++;
		if (allowRecording && timer >= 60) {
			play_time -= 1;
			timer = 0;
			if (play_time <= 0) {
				play_time = 0;
			}
		}
	}

	private void processVideo() {
		logState("Processing video...");
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				for (int i = 0; i < recorded_frames.size(); i++) {
					video_frames.add(new ImageView(recorded_frames.get(i)));
				}
				logState("Video has been processed.");
				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

	private final Pane playbackVideo() {
		if (video_screen.getChildren().size() > 0)
			video_screen.getChildren().remove(0);
		video_screen.getChildren().add(video_frames.get(frame));
		frame += 1;
		if (frame > video_frames.size() - 1) {
			frame = 0;
		}
		return video_screen;
	}

	public void setVideoScale(double scale) {
		this.video_size_scale = scale;
	}

	public void scaleVideo(double scale) {
		this.video_size_scale = scale;
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				if (video_frames.size() > 0) {
					logState("Scaling video...");
					for (int i = 0; i < video_frames.size(); i++) {
						video_frames.get(i).setFitWidth(video_frames.get(i).getImage().getWidth() * video_size_scale);
						video_frames.get(i).setFitHeight(video_frames.get(i).getImage().getHeight() * video_size_scale);
					}
					logState("Video has been scaled!");
				}
				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

	public void saveVideo() {
		File root = new File(PATH_ROOT);
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				root.mkdirs();
				for (int i = 0; i < recorded_frames.size(); i++) {
					saveToFile(recorded_frames.get(i));
				}
				saveRecording(temp_frames);
				logState("Amount of compiled frames: " + temp_frames.size());
				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

	public void loadFrames(ArrayList<byte[]> list) {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				logState("loading frames...");
				for (int i = 0; i < list.size(); i++) {
					video_frames.add(byteToImage(list.get(i)));
				}
				logState("frames have been added!");
				scaleVideo(video_size_scale);
				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

	private void showIndicator(ObservableList<Node> rootPane, Indicator indicator) {
		rootPane.removeAll(playing, idle, recording);
		indicator.setTranslateX(width - ScaleX(150));
		indicator.setTranslateY(ScaleY(100));
		rootPane.add(indicator);
	}

	public void saveToFile(Image image) {
		counter += 1;
		BufferedImage BImage = SwingFXUtils.fromFXImage(image, null);
		temp_frames.add(ImageToByte(BImage));
		if (saveFrames) {
			File video = new File(PATH_FRAME + counter + "." + FILE_EXTENSION);
			try {
				ImageIO.write(BImage, FILE_EXTENSION, video);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void loadFromFile() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				for (int i = 1; i < 513; i++) {
					File video = new File(PATH_FRAME + i + "." + FILE_EXTENSION);
					video_frames.add(new ImageView(new Image(video.toURI().toString())));
				}
				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

	public void saveRecording(ArrayList<byte[]> list) {
		Runnable saveRunnable = new Runnable() {
			@Override
			public void run() {
				File root = new File(PATH_ROOT);
				File video = new File(PATH_VIDEO);
				video.delete();
				try {
					root.mkdirs();
					FileOutputStream fileOut = new FileOutputStream(PATH_VIDEO);
					BufferedOutputStream bufferedStream = new BufferedOutputStream(fileOut);
					ObjectOutputStream outputStream = new ObjectOutputStream(bufferedStream);
					outputStream.writeObject(list);
					outputStream.close();
					fileOut.close();
					logState("Video saved.");

				} catch (IOException e) {
					logState("Failed to save, I/O exception");
					e.printStackTrace();
				}
			}
		};

		Thread saveThread = new Thread(saveRunnable);

		saveThread.start();
	}

	@SuppressWarnings("unchecked")
	private void retrieveRecording(ArrayList<byte[]> list) {

		File root = new File(PATH_ROOT);
		File video = new File(PATH_VIDEO);

		if (root.exists() && video.exists()) {
			try {
				FileInputStream fileIn = new FileInputStream(PATH_VIDEO);
				ObjectInputStream inputStream = new ObjectInputStream(fileIn);
				temp_frames = (ArrayList<byte[]>) inputStream.readObject();
				inputStream.close();
				fileIn.close();
				logState("Loaded.");
				loadFrames(temp_frames);
			} catch (IOException | ClassNotFoundException e) {
				logState("Failed to load " + e.getLocalizedMessage());
			}
		} else {
			logState("Nothing to load.");
		}

	}


	private synchronized Image create_frame() {
		WritableImage wi = new WritableImage((int)width, (int)height);
		if (scene != null)
			scene.snapshot(wi);
		try {
			return wi;
		} finally {
			wi = null;
		}
	}

	@SuppressWarnings("unused")
	private synchronized Image create_node_frame(Node node) {
		parameters.setFill(Color.TRANSPARENT);
		WritableImage wi = new WritableImage((int)node.getBoundsInLocal().getWidth(), (int) node.getBoundsInLocal().getHeight());
		node.snapshot(parameters, wi);
		return wi;

	}

	public void scaleResolution(double scaleX, double scaleY, boolean manualScaling) {
		double resolutionX = Screen.getPrimary().getBounds().getWidth();
		double resolutionY = Screen.getPrimary().getBounds().getHeight();
		double baseResolutionX = 1920;
		double baseResolutionY = 1080;
		bounds_scale_X = baseResolutionX / resolutionX;
		bounds_scale_Y = baseResolutionY / resolutionY;
		if(manualScaling==true){
			bounds_scale_X = bounds_scale_X*scaleX;
			bounds_scale_Y = bounds_scale_Y*scaleY;
		}
	}

	public void allowRecording(boolean state) {
		allowRecording = state;
		logState("allowed recording: " + state);
	}

	public void allowPlayback(boolean state) {
		allowPlayback = state;
		logState("allowed playback: " + state);
	}

	public void setLocation(double x, double y) {
		video_screen.setTranslateX(x);
		video_screen.setTranslateY(y);
	}

	public void setDimensions(double width, double height) {
		video_screen.setPrefSize(width, height);;
	}

	public void resetPlayback() {
		this.frame = 0;
	}

	public double Scale(double value) {
		double newSize = value * (bounds_scale_X + bounds_scale_Y)/2;
		return newSize;
	}

	public double ScaleX(double value) {
		double newSize = value * bounds_scale_X;
		return newSize;
	}

	public double ScaleY(double value) {
		double newSize = value * bounds_scale_Y;
		return newSize;
	}

	public double getVideoWidth(){
		return video_frames.get(0).getImage().getWidth() * video_size_scale;
	}

	public double getVideoHeight(){
		return video_frames.get(0).getImage().getWidth() * video_size_scale;
	}

	@SuppressWarnings("unused")
	private String loadResource(String image) {
		String url = PATH_ROOT + image;
		return url;
	}

	public final byte[] ImageToByte(BufferedImage image) {

		byte[] imageInByte = null;
		try {
			if (image != null) {
				ImageIO.write(image, FILE_EXTENSION, byteOutput);
				imageInByte = byteOutput.toByteArray();
				byteOutput.flush();
			}
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		try {
			return imageInByte;
		} finally {
			byteOutput.reset();
		}
	}

	public final ImageView byteToImage(byte[] data) {
		BufferedImage newImage = null;
		ImageView imageView = null;
		Image image = null;
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			newImage = ImageIO.read(inputStream);
			inputStream.close();
			image = SwingFXUtils.toFXImage(newImage, null);
			imageView = new ImageView(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageView;
	}

	private void logState(String state) {
		System.out.println("JAVA_FX SCREEN RECORDER: " + state);
	}

	public enum RECORDING_STATE {
		RECORDING, LOADING,
	}

	public enum VIDEO_PLAY {
		PLAY_CONTINUOUSLY, CONTINUOUS_REPLAY, PLAY_ONCE,
	}

	private class Indicator extends HBox {
		public Indicator(Color color, String message) {
			Circle indicator = new Circle(Scale(15), color);
			Text label = new Text(message);
			label.setFont(Font.font("", FontWeight.EXTRA_BOLD, Scale(20)));
			label.setFill(Color.WHITE);
			setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
			getChildren().addAll(indicator, label);
		}
	}
}
