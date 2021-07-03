package platformer.ninjamenu;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import platformer.audio.ClipPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NinjaMenuItem extends Pane {
    private Text text;

    private Effect shadow = new DropShadow(15, Color.WHITE);
    private Effect blur = new BoxBlur(0, 0, 3);
    private Effect bgblur = new BoxBlur(2, 2, 6);
    private static ClipPlayer menuHover = new ClipPlayer("/resources/SFX/menu-hover.mp3");
    private static List<ClipPlayer> menuClick = new ArrayList<ClipPlayer>(){{
        add(new ClipPlayer("/resources/SFX/slash1.mp3"));
        add(new ClipPlayer("/resources/SFX/slash2.mp3"));
        add(new ClipPlayer("/resources/SFX/slash3.mp3"));
    }};
    private static Random rand = new Random();

    public NinjaMenuItem(String name) {
        Polygon bg = new Polygon(
                0, 0,
                200, 0,
                215, 25,
                200, 50,
                0, 50
        );
        bg.setStroke(Color.color(1, 1, 1, 0.75));
        bg.fillProperty().bind(
                Bindings.when(pressedProperty())
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
        bg.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                menuClick.get(rand.nextInt(3)).play();
            }
        });

        text = new Text(name);
        text.setTranslateX(10);
        text.setTranslateY(32);
        text.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 20));
        text.setFill(Color.WHITE);

        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );
        text.setMouseTransparent(true);

        getChildren().addAll(bg, text);
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> action.run());
    }
}