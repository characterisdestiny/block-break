package blockbreaker;

import javazoom.jl.player.Player;

import java.io.*;

public class Music extends Thread {

    private Player player;
    private boolean isLoop;
    private File file;
    private FileInputStream fis;
    private BufferedInputStream bis;

    public Music(String name, boolean isLoop) {
        try {
            this.isLoop = isLoop;
            // 프로젝트 루트 기준 상대 경로
            file = new File(getClass().getResource("/music/" + name).toURI());

            // 파일 존재 확인
            if (!file.exists()) {
                System.out.println("음악 파일 없음: " + file.getAbsolutePath());
                return;
            }

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            player = new Player(bis);

        } catch (Exception e) {
            System.out.println("음악 로딩 실패: " + e.getMessage());
        }
    }

    public int getTime() {
        if (player == null) return 0;
        return player.getPosition();
    }

    public void close() {
        isLoop = false;
        if (player != null) player.close();
        this.interrupt();
    }

    @Override
    public void run() {
        try {
            do {
                if (player != null) player.play();
                if (isLoop) {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    player = new Player(bis);
                }
            } while (isLoop);
        } catch (Exception e) {
            System.out.println("음악 재생 오류: " + e.getMessage());
        }
    }
}
