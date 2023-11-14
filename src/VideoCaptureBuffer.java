import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import java.util.LinkedList;
import java.util.Queue;

public class VideoCaptureBuffer {
    private static Queue<Frame> frameBuffer = new LinkedList<>();
    private static FrameGrabber grabber;

    public static void startBuffering(int captureDurationSeconds, int frameRate) {
        try {
            grabber = new FFmpegFrameGrabber("desktop"); // Utilisation de VideoInputFrameGrabber
            grabber.setFormat("gdigrab");
            grabber.setFrameRate(frameRate);
            grabber.start();

            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                Frame frame;
                while (true) {
                    try {
                        frame = grabber.grab();
                        if (frameBuffer.size() >= captureDurationSeconds * frameRate) {
                            frameBuffer.remove();
                        }
                        frameBuffer.add(frame.clone());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void saveBuffer(String outputPath, int frameRate) throws Exception {
        if (grabber == null) {
            throw new IllegalStateException("Grabber has not been initialized.");
        }

        FrameRecorder recorder = FrameRecorder.createDefault(outputPath, grabber.getImageWidth(), grabber.getImageHeight());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        recorder.setFrameRate(frameRate); // Doit correspondre au taux de trame du grabber
        recorder.setVideoBitrate(10000000); // Ajustez en fonction de vos besoins
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // Format de pixel standard pour H.264
        recorder.start();

        // On lit une copie de frameBuffer pour éviter de modifier le buffer original
        Queue<Frame> frameBufferCopy = new LinkedList<>(frameBuffer);
        while (!frameBufferCopy.isEmpty()) {
            recorder.record(frameBufferCopy.remove());
        }

        recorder.stop();
        recorder.release();

        System.out.println("Video saved to " + outputPath);

        Overlay.isSavingFiles = false;
    }
}
