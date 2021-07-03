package platformer.ninjamenu;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class NinjaSubtitle extends Pane {
    private Text text;

    public NinjaSubtitle(String name) {
        String spread = "";
        for (char c : name.toCharArray()) {
            spread += c + " ";
        }

        text = new Text(spread);
        text.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/BloodLust.ttf").toExternalForm(), 60));
        text.setFill(Color.DARKRED);
        text.setEffect(new DropShadow(15, Color.BLACK));
        text.setTranslateY(-30);
        text.setScaleX(0.4);

        getChildren().addAll(text);
    }

    public double getTitleWidth() {
        return text.getLayoutBounds().getWidth();
    }

    public double getTitleHeight() {
        return text.getLayoutBounds().getHeight();
    }
}