import objectdraw.*;
import java.util.ArrayList;
import java.awt.*;

public class Pong extends ActiveObject {
    private static final int BALL_SIZE = 10;
    private static final int DELAY_TIME = 30;

    private static final int INIT_Y_SPEED = 3;
    private static final int INIT_X_SPEED = 6;

    private static final int PADDLE_WIDTH = 10;
    public static final int PADDLE_HEIGHT = 50;

    private static final int LINE_SIDE = 10;

    private static final int DISTANCE_F_CANVAS = 10;
    private static final double RIGHT_PADDLE_SPEED = 2.75;

    private static final int FONT_SIZE = 100;
    private static final String FONT = "SansSerif-BOLD-18";

    private static final int WINNER_SCORE = 1;

    int playerScore;
    int computerScore;

    private FilledOval theBall;
    private final DrawingCanvas canvas;

    Text playerScoreText;
    Text computerScoreText;
    Text endScreenText;

    FilledRect leftPaddle;
    FilledRect rightPaddle;

    FilledRect top;
    FilledRect bottom;

    ArrayList<FilledRect> dottedLine;

    double xVel;
    double yVel;

    int frameHeight = 20;

    boolean gameOver = false;
    boolean win = false;

    public Pong(Location initialLocation, DrawingCanvas aCanvas) {
        canvas = aCanvas;

        // Oval Ball
        theBall = new FilledOval(initialLocation,
                BALL_SIZE,
                BALL_SIZE, Color.WHITE,
                canvas);

        //Right & Left Rect Paddles
        leftPaddle = new FilledRect(DISTANCE_F_CANVAS,canvas.getHeight()/2.0-(PADDLE_HEIGHT/2.0),
        PADDLE_WIDTH, PADDLE_HEIGHT, Color.WHITE, canvas);

        rightPaddle = new FilledRect(canvas.getWidth()-(PADDLE_WIDTH + DISTANCE_F_CANVAS),
        (canvas.getHeight()/2.0)-(PADDLE_HEIGHT/2.0), PADDLE_WIDTH, PADDLE_HEIGHT, Color.WHITE, canvas);

        //Top & Bottom Frame
        top = new FilledRect(0,0,canvas.getWidth(), frameHeight, Color.WHITE, canvas);
        bottom = new FilledRect(0,canvas.getHeight()-frameHeight,canvas.getWidth(),frameHeight,
                Color.WHITE, canvas);

        playerScore = 0;
        playerScoreText = new Text(0, canvas.getWidth()*.25, canvas.getHeight()/4.0, Color.WHITE, canvas);
        playerScoreText.setFontSize(FONT_SIZE);

        computerScore = 0;
        computerScoreText = new Text(0, canvas.getWidth()*.455 + FONT_SIZE , canvas.getHeight()/4.0,
                Color.WHITE, canvas);
        computerScoreText.setFontSize(FONT_SIZE);

        //Velocity of ball
        xVel = INIT_X_SPEED;
        yVel = INIT_Y_SPEED;

        //Background color
        canvas.setBackground(Color.BLACK);

        //Dotted Line
        dottedLine = new ArrayList<>();

        for(int i = 0; i < canvas.getHeight()/(2*LINE_SIDE); i++) {
            dottedLine.add(new FilledRect((canvas.getWidth()/2.0) - (LINE_SIDE/2.0), (LINE_SIDE*2*i) + 5,
                    LINE_SIDE, LINE_SIDE, Color.WHITE, canvas));
        }

        start();
    }

    public void run() {
        while (!gameOver && !win) {

            // BALL BEHAVIOR
            {
                theBall.move(xVel, yVel);
                if (theBall.overlaps(leftPaddle)) {
                    xVel = Math.abs(xVel);
                }

                if (theBall.overlaps(rightPaddle)) {
                    xVel = -Math.abs(xVel);
                }

                if (theBall.overlaps(top)) {
                    yVel = Math.abs(yVel);
                }

                if (theBall.overlaps(bottom)) {
                    yVel = -Math.abs(yVel);
                }

                if (theBall.getX() < 0) {
                    theBall.removeFromCanvas();
                    theBall = new FilledOval(canvas.getWidth() / 2.0, canvas.getHeight() / 2.0,
                            BALL_SIZE,
                            BALL_SIZE, Color.WHITE,
                            canvas);
                    computerScore++;
                    computerScoreText.setText(Integer.toString(computerScore));
                    if (computerScore == WINNER_SCORE) {
                        lose();
                    }
                }
            }

            // AI BEHAVIOR
            if (rightPaddle.getY() + PADDLE_HEIGHT/2 > theBall.getY() + BALL_SIZE / 2) {
                if (!rightPaddle.overlaps(top)) {
                    rightPaddle.move(0, -RIGHT_PADDLE_SPEED);
                }
            } else if (rightPaddle.getY() + PADDLE_HEIGHT/2 < theBall.getY() + BALL_SIZE/2) {
                if (!rightPaddle.overlaps(bottom)) {
                    rightPaddle.move(0, RIGHT_PADDLE_SPEED);
                }
            }

            if (theBall.getX() > canvas.getWidth()) {
                theBall.removeFromCanvas();
                theBall = new FilledOval(canvas.getWidth()/2.0, canvas.getHeight()/2.0,
                        BALL_SIZE, BALL_SIZE, Color.WHITE, canvas);
                playerScore++;
                playerScoreText.setText(Integer.toString(playerScore));

                if (playerScore == WINNER_SCORE) {
                    win();
                }
            }

            pause(DELAY_TIME);
        }

        canvas.repaint();
    }

    private void win() {
        endScreenText = new Text(true,0,0,Color.WHITE, canvas);
        endScreenText.setFont(FONT);
        endScreenText.setText("YOU WIN!!");
        endScreenText.moveTo(calculateTextCenter(endScreenText.getWidth(),endScreenText.getHeight()));
        theBall.removeFromCanvas();
        rightPaddle.removeFromCanvas();
        leftPaddle.removeFromCanvas();
        playerScoreText.removeFromCanvas();
        computerScoreText.removeFromCanvas();
        win = true;

        for (FilledRect filledRect : dottedLine) {
            filledRect.removeFromCanvas();
        }
    }
    private void lose() {
        endScreenText = new Text(true,0,0,Color.WHITE,canvas);
        endScreenText.setFont(FONT);
        endScreenText.setText("YOU LOSE!!");
        endScreenText.moveTo(calculateTextCenter(endScreenText.getWidth(),endScreenText.getHeight()));

        for (FilledRect filledRect : dottedLine) {
            filledRect.removeFromCanvas();
        }

        theBall.removeFromCanvas();
        rightPaddle.removeFromCanvas();
        leftPaddle.removeFromCanvas();
        gameOver = true;
        playerScoreText.removeFromCanvas();
        computerScoreText.removeFromCanvas();
    }

    private Location calculateTextCenter(double textWidth, double textHeight) {
        return new Location(canvas.getWidth()/2.0 - textWidth/2.0,
                canvas.getHeight()/2.0-textHeight/2);
    }
}