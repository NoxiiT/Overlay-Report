import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import java.util.LinkedList;
import java.util.Queue;
import org.bytedeco.javacv.FFmpegLogCallback;

public class AudioCaptureBuffer {
    private static Queue<Frame> audioBuffer = new LinkedList<>();
    private static FrameGrabber grabber;

    public static void startBuffering(int captureDurationSeconds, int sampleRate, String deviceName) {
        try {
            grabber = new FFmpegFrameGrabber("audio=" + deviceName); // Utilisation de VideoInputFrameGrabber
            grabber.setFormat("dshow"); // Pour Windows, utilisez "dshow". Pour Linux, "alsa". Pour macOS, "avfoundation".
            grabber.setAudioChannels(1); // Mono (1) ou stéréo (2)
            grabber.setSampleRate(sampleRate); // Fréquence d'échantillonnage standard pour l'audio
            grabber.start();

            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                Frame audio;
                while (true) {
                    try {
                        audio = grabber.grab();
                        if (audioBuffer.size() >= captureDurationSeconds * sampleRate) {
                            audioBuffer.remove();
                        }
                        audioBuffer.add(audio.clone());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void saveBuffer(String outputPath, int sampleRate) throws Exception {
        if (grabber == null) {
            throw new IllegalStateException("Grabber has not been initialized.");
        }

        FFmpegLogCallback.set();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputPath, grabber.getAudioChannels());
        recorder.setSampleRate(sampleRate);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_MP3);
        recorder.start();

        // On fait une copie de audioBuffer pour éviter de modifier le buffer original
        Queue<Frame> audioBuffer = new LinkedList<>(AudioCaptureBuffer.audioBuffer);
        while (!audioBuffer.isEmpty()) {
            recorder.record(audioBuffer.remove());
        }

        recorder.stop();
        recorder.release();

        System.out.println("Audio saved to " + outputPath);
    }
}
