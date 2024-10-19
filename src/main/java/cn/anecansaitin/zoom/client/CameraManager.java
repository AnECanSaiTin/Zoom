package cn.anecansaitin.zoom.client;

public class CameraManager {
    private static Mode mode = Mode.NULL;

/*    public static boolean enableMode(Mode mode) {
        switch (mode) {
            case NULL -> {
                CameraManager.mode = mode;
                return true;
            }
            case FREE -> {
                boolean b = switch (CameraManager.mode) {
                    case NULL -> true;
                    case FREE -> false;
                    case FPS_PLUS -> false;
                };

                if (b) {
                    CameraManager.mode = mode;
                }

                return b;
            }
        }
    }*/

    public enum Mode {
        NULL,
        FREE,
        FPS_PLUS
    }
}
