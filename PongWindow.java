import objectdraw.FilledRect;
import objectdraw.Location;
import objectdraw.WindowController;

public class PongWindow extends WindowController {
    Location mouseLocation;
    Pong theGame;

    public void begin() {
        mouseLocation = new Location(canvas.getWidth()/2.0,canvas.getHeight()/2.0);
        theGame = new Pong(mouseLocation, canvas);
    }

    public void onMouseMove(Location point) {
        FilledRect leftP = theGame.leftPaddle;

        int P_HEIGHT = Pong.PADDLE_HEIGHT;
        int heightTB = theGame.frameHeight;
        int setPaddle = heightTB + P_HEIGHT;

        if (point.getY() >= heightTB) {
            leftP.moveTo(leftP.getX(), Math.min(point.getY(), canvas.getHeight()-(setPaddle)));
        }
    }
}