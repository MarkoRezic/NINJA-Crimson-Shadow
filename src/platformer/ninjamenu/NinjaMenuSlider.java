package platformer.ninjamenu;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import platformer.audio.AudioPlayer;
import platformer.audio.ClipPlayer;

public class NinjaMenuSlider extends Pane {
    private Text text;

    private Effect shadow = new DropShadow(15, Color.WHITE);
    private Effect blur = new BoxBlur(0, 0, 3);
    private Effect bgblur = new BoxBlur(2, 2, 6);
    private static ClipPlayer menuHover = new ClipPlayer("/resources/SFX/menu-hover.mp3");
    public static Slider slider;

    public NinjaMenuSlider(String name, double min, double max, double value, String property) {

        text = new Text(name + ": " + (int)value);
        text.setTranslateX(10);
        text.setTranslateY(22);
        text.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 18));
        text.setFill(Color.WHITE);

        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );
        text.setMouseTransparent(true);

        slider = new Slider(min, max, value);
        slider.setTranslateX(5);
        slider.setTranslateY(26);
        slider.setPrefWidth(190);
        slider.setShowTickMarks(true);
        if(property.equalsIgnoreCase("volume")){
            slider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    NinjaMenuApp.masterVolume = ((float)slider.getValue())/100;
                    NinjaMenuApp.menuMusic.mediaPlayer.setVolume(NinjaMenuApp.masterVolume);
                    text.setText(name + ": " + (int)slider.getValue());
                }
            });
        }

        Polygon bg = new Polygon(
                0, 0,
                200, 0,
                215, 25,
                200, 50,
                0, 50
        );
        bg.setStroke(Color.color(1, 1, 1, 0.75));
        bg.fillProperty().bind(
                Bindings.when(slider.pressedProperty())
                        .then(Color.color(0, 0, 0, 0.75))
                        .otherwise(Color.color(0, 0, 0, 0.25))
        );
        bg.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(bgblur)
        );
        bg.hoverProperty().addListener((obs, wasHovered, isNowHovered)->{
            if(isNowHovered){
                menuHover.play();
            }
        });


        getChildren().addAll(bg, text, slider);
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> action.run());
    }
}