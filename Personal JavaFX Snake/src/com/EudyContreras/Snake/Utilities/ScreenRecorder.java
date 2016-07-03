package com.EudyContreras.Snake.Utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
import javafx.util.Duration;

public class ScreenRecorder {
	private int recording_time;
	private int frame = 0;
	private int timer = 0;
	private double size_scale = 1.0;
	private Pane screen;
	private Boolean allowRecording;
	private Boolean allowPlayback;
	private Timeline videoRecorder;
	private Timeline videoPlayer;
	private GameManager game;
	private ArrayList<Image> recorded_frames;
	private ArrayList<ImageView> video_frames;
	private ArrayList<ImageView> temp_video;
	private ScheduledExecutorService scheduledExecutor;
	private Indicator recording =  new Indicator(Color.RED, " Recording..");
	private Indicator playing =  new Indicator(Color.GREEN, " Playing..");
	private Indicator idle =  new Indicator(Color.YELLOW, "");
	private final String PATH_ROOT = System.getProperty("user.home")+"/Desktop" + File.separator + "Snake Game" + File.separator;
	private final String PATH_VIDEO = PATH_ROOT  + "player_names.ser";
	private static final SnapshotParameters parameters = new SnapshotParameters();
	// private static final String ROOT_DIRECTORY = "." + File.separator +
	// "Recordings" + File.separator;
	private Pane video_screen;

	public ScreenRecorder(GameManager game, Pane screen, int record_time) {
		this.game = game;
		this.screen = screen;
		this.recording_time = record_time*60;
		this.initStorages();
	}

	private void initStorages() {
		video_frames = new ArrayList<ImageView>();
		recorded_frames = new ArrayList<Image>();
	}
	private void resetStorage(){
		if(video_frames!=null)
		video_frames.clear();
		if(recorded_frames!=null)
		recorded_frames.clear();
		if(video_screen!=null)
		video_screen.getChildren().clear();
	}
	public void startRecorder() {
		resetStorage();
		showIndicator(game.getMainRoot().getChildren(),recording);
//		backgroundScheduledThread();
		backgroundWorkerTwo();
//		if(videoRecorder==null)
//		recordScreen();
//		else{
//			videoRecorder.play();
//		}
		allowRecording(true);
		logState("recording...");
	}

	public void stopRecorder() {
		if( allowRecording){
		showIndicator(game.getMainRoot().getChildren(), idle);
//		videoRecorder.stop();
		allowRecording(false);
		logState("stopped recording..");
		logState("Amount of frames: "+recorded_frames.size());
		processVideo();
		}
	}

	public void starPlayer(Pane output_screen) {
		video_screen = output_screen;
		showIndicator(game.getMainRoot().getChildren(),playing);
		if (recorded_frames.size() > 0) {
			if(videoPlayer==null)
			playRecording();
			else{
				videoPlayer.play();
			}
			allowPlayback(true);
		}
		logState("Video playback");
	}

	public void stopPlayer() {
		videoPlayer.stop();
		allowPlayback(false);
	}

	public void setVideoScale(double scale) {
		this.size_scale = scale;
	}

	private void recordScreen() {

		videoRecorder = new Timeline();
		videoRecorder.setCycleCount(Timeline.INDEFINITE);

		KeyFrame keyFrame = new KeyFrame(Duration.seconds( 1.0 / 60.0), // 60FPS

				new EventHandler<ActionEvent>() {

					public void handle(ActionEvent e) {

						if (allowRecording && recording_time > 0) {
							recorded_frames.add(create_frame(screen));
						}

						recordingTimer();
					}
				});

		videoRecorder.getKeyFrames().add(keyFrame);
		videoRecorder.play();

	}
	private void backgroundScheduledThread() {
        scheduledExecutor = Executors.newScheduledThreadPool(4);
        scheduledExecutor.scheduleAtFixedRate(() -> {

            Platform.runLater(() -> {

            	if (allowRecording && recording_time > 0) {
					recorded_frames.add(create_frame(screen));
				}

				recordingTimer();
            });

        }, 0, 16, TimeUnit.MILLISECONDS);
    }
	 private void backgroundWorkerTwo(){
		    Task<Void> task = new Task<Void>() {
		          @Override
		          public Void call() throws Exception {
		            while (true) {
		              Platform.runLater(new Runnable() {
		                @Override
		                public void run() {
		                   	if (allowRecording && recording_time > 0) {
		    					recorded_frames.add(create_frame(screen));
		    				}

		    				recordingTimer();
		                }
		              });
		              Thread.sleep(16);
		            }
		          }
		        };
		        Thread th = new Thread(task);
		        th.setDaemon(true);
		        th.start();
		    }
	private void playRecording() {

		videoPlayer = new Timeline();
		videoPlayer.setCycleCount(Timeline.INDEFINITE);

		KeyFrame keyFrame = new KeyFrame(Duration.seconds( 1.0 / 60.0), // 60FPS

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
			recording_time -= 1;
			timer = 0;
			if (recording_time <= 0) {
				recording_time = 0;
			}
		}
	}

	private void processVideo() {
		logState("Processing video...");
		Runnable processor = new Runnable() {
			@Override
			public void run() {

				for (int i = 0; i < recorded_frames.size(); i++) {
					video_frames.add(new ImageView(recorded_frames.get(i)));
				}
				temp_video = video_frames;
				logState("Video has been processed.");
			}
		};

		Thread processingThread = new Thread(processor);
		processingThread.start();
	}

	private final Pane playbackVideo() {
		if (video_screen.getChildren().size() > 0)
			video_screen.getChildren().remove(0);
		video_screen.getChildren().add(temp_video.get(frame));
		frame += 1;
		if (frame > temp_video.size() - 1) {
			frame = 0;
		}
		return video_screen;
	}

	public void scaleVideo(double scale) {
//		this.size_scale = scale;
		if(temp_video!=null){
		logState("Scaling video...");
		Runnable scaler = new Runnable() {
			@Override
			public void run() {

				for (int i = 0; i < temp_video.size(); i++) {
					temp_video.get(i).setFitWidth(temp_video.get(i).getImage().getWidth() * size_scale);
					temp_video.get(i).setFitHeight(temp_video.get(i).getImage().getHeight() * size_scale);
				}
				logState("Video has been scaled!");
			}

		};

		Thread scalingThread = new Thread(scaler);
		scalingThread.start();
		}
	}

	public void allowRecording(Boolean state) {
		allowRecording = state;
		logState("allowed recording: " +state);
	}

	public void allowPlayback(Boolean state) {
		allowPlayback = state;
		logState("allowed playback: " +state);
	}

	public void setLocation(double x, double y){
		video_screen.setTranslateX(x);
		video_screen.setTranslateY(y);
	}
	private void showIndicator(ObservableList<Node> rootPane, Indicator indicator) {
		rootPane.removeAll(playing,idle,recording);
        indicator.setTranslateX(GameSettings.WIDTH - GameManager.ScaleX(150));
        indicator.setTranslateY(GameManager.ScaleY(100));
        rootPane.add(indicator);
    }

	public void saveRecording() {
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
					ObjectOutputStream out = new ObjectOutputStream(bufferedStream);
					out.writeObject(video_frames);
					out.close();
					fileOut.close();
					logState("Saved.");

				} catch(IOException e) {
					logState("Failed to save, I/O exception");
				}
			}
		};

		Thread saveThread = new Thread(saveRunnable);

		saveThread.start();
	}
	public void retrieveVideo(){

	}
	public void resetRecording() {

	}
	public void resetPlayback(){
		frame = 0;
	}
	private synchronized Image create_frame(Node node) {
//		parameters.setFill(Color.TRANSPARENT);
		WritableImage wi = new WritableImage(GameSettings.WIDTH, GameSettings.HEIGHT);
		if(node!=null)
		game.getScene().snapshot(wi);
		try{
		return wi;
		}
		finally{
			wi = null;
		}

	}
	private void logState(String state){
		System.out.println("SCREEN RECORDER: "+state);
	}
	public enum RECORDING_STATE {
		RECORDING, LOADING,
	}

	public enum VIDEO_PLAY {
		PLAY_CONTINUOUSLY, CONTINUOUS_REPLAY, PLAY_ONCE,
	}

	@SuppressWarnings("unused")
	private static String loadResource(String image) {
		String url = GameSettings.IMAGE_SOURCE_DIRECTORY + image;
		return url;
	}
	private class Indicator extends HBox{
		public Indicator(Color color, String message){
			Circle indicator = new Circle(GameManager.ScaleX_Y(15),color);
			Text label = new Text(message);
			label.setFont(Font.font("", FontWeight.EXTRA_BOLD, GameManager.ScaleX_Y(20)));
			label.setFill(Color.WHITE);
			setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
			getChildren().addAll(indicator, label);
		}
	}
}
