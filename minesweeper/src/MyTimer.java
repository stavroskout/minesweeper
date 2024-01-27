import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
* MyTimer 
* The MyTimer class implements a timer that counts down countdownDurationInSeconds seconds.
* When countdownDurationInSeconds becomes 0 MyTimer calls stopTimer whith boolean b as true which indicates that the message "Game Lost! With 00:00 left" will be set to timerlabel.
* As long as it does not hit 0 the MyTimer continues to count down.
* @author  Koutentakis Stavros
* @param tm is a java.util.Timer that can count seconds.
* @param StartTime how many seconds does the Clock have when it is initialized.
* @param timeLeft how many seconds are left in the Timer.
* @param Solution when Timer hits 0 we need to notify the Main class to show the Games Solution.
* @param clock_label is a javafx.Label which displays the seconds left in the Timer.
*/

public class MyTimer {
    /** The duration of the timer in seconds. */
    static int countdownDurationInSeconds;

    /** The timeline object used to run the timer. */
    static Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            
        @Override
        public void handle(ActionEvent event) {
            
            countdownDurationInSeconds--;
            
            if (countdownDurationInSeconds == 0) {
                //stopTimer(true);
                updateTimerLabel();
                App.appterm();
            } else {
                updateTimerLabel();
            }
        }
    }));
    
    /** The label object used to display the timer. */
    static Label timerLabel = new Label();

    /**
    * This is the constructor and ccreates a new instance of the MyTimer class.
    */
    public MyTimer(){
    }

    /**
    * Starts the countdown timer.
    */
    static void startTimer() {
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
    * Updates the timer label with the remaining time.
    */
    static void updateTimerLabel() {
        
        int minutes = countdownDurationInSeconds / 60;
        int seconds = countdownDurationInSeconds % 60;
        String time = String.format("%02d:%02d left", minutes, seconds);
        timerLabel.setText(time);
        timerLabel.setFont(new Font(25));
    }

    /**
    * Stops the countdown timer and triggers the timer listener event.
    * @param b the boolean flag indicating whether the timer was stopped due to succesful completion if b is false or due to unsuccesful completion or time runout if b i true
    */
    static void stopTimer(boolean b) {
        
        timeline.stop();
        if (!b){timerLabel.setText("You won! With "+timerLabel.getText()); App.winner="Player";}
        if(b){timerLabel.setText("Game Lost! With "+timerLabel.getText());App.winner="Computer";}
        //App.appterm();

    }
       
}
