package platformer.ninjamenu;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class NinjaTitle extends Pane {
    private Text text;

    public NinjaTitle(String name) {
        String spread = "";
        for (char c : name.toCharArray()) {
            spread += c + " ";
        }

        text = new Text(spread);
        text.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 100));
        text.setFill(Color.BLACK);
        text.setEffect(new DropShadow(15, Color.BLACK));
        text.setTranslateY(-30);

        getChildren().addAll(text);
    }

    public double getTitleWidth() {
        return text.getLayoutBounds().getWidth();
    }

    public double getTitleHeight() {
        return text.getLayoutBounds().getHeight();
    }
}
