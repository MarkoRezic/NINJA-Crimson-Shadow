package platformer.ninjamenu;

import com.sun.javafx.geom.BaseBounds;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import platformer.model.HighscoreListModel;

import java.util.ArrayList;

public class NinjaMenuHSList extends Pane {

    private Effect glow = new DropShadow(15, Color.WHITE);
    private Effect shadow = new DropShadow(10, Color.BLACK);
    private Effect blur = new BoxBlur(0, 0, 3);

    private Text mainTitle = new Text("HIGHSCORES");
    private Text[] titles = new Text[3];
    private ArrayList<ObservableList<HighscoreListModel>> level_highscore_list = new ArrayList<ObservableList<HighscoreListModel>>();
    private Text[][] text_highscore_list = new Text[3][10];
    private Text[][] number_highscore_list = new Text[3][10];


    public NinjaMenuHSList() {
        titles[0] = new Text("Level 1");
        titles[1] = new Text("Level 2");
        titles[2] = new Text("Level 3");
        level_highscore_list.add(0, HighscoreListModel.highscoreList(1));
        level_highscore_list.add(1, HighscoreListModel.highscoreList(2));
        level_highscore_list.add(2, HighscoreListModel.highscoreList(3));

        for(int level = 0; level < 3; level++){
            titles[level].setTranslateX(200 + 350 * level);
            titles[level].setTranslateY(150);
            titles[level].setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 45));
            titles[level].setFill(Color.WHITE);
            titles[level].setEffect(glow);
            for(int pos = 0; pos < 10; pos++){
                text_highscore_list[level][pos] = new Text(190 + 350 * level, 200 + 35 * pos, level_highscore_list.get(level).get(pos).toString());
                text_highscore_list[level][pos].setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 16));
                text_highscore_list[level][pos].setFill(Color.WHITE);
                text_highscore_list[level][pos].setEffect(shadow);

                number_highscore_list[level][pos] = new Text(330 + 350 * level, 200 + 35 * pos, level_highscore_list.get(level).get(pos).getHighscore().toString());
                number_highscore_list[level][pos].setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 16));
                number_highscore_list[level][pos].setFill(Color.WHITE);
                number_highscore_list[level][pos].setEffect(glow);
                number_highscore_list[level][pos].setBoundsType(TextBoundsType.VISUAL);
                number_highscore_list[level][pos].setWrappingWidth(40);
                number_highscore_list[level][pos].setTextAlignment(TextAlignment.RIGHT);
            }
        }
        System.out.println(level_highscore_list);
        System.out.println(text_highscore_list);


        mainTitle.setFont(Font.loadFont(NinjaMenuApp.class.getResource("/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 60));
        mainTitle.setFill(Color.WHITE);
        mainTitle.setEffect(glow);
        mainTitle.setTranslateX(NinjaMenuApp.WIDTH / 2 - mainTitle.getLayoutBounds().getWidth()/2);
        mainTitle.setTranslateY(70);

        mainTitle.setMouseTransparent(true);

        Polygon bg = new Polygon(
                150, 90,
                1100, 90,
                1100, 600,
                150, 600
        );
        bg.setStroke(Color.color(1, 1, 1, 0.5));
        bg.setFill(Color.color(0, 0, 0, 0.4));

        getChildren().addAll(bg, mainTitle);
        getChildren().addAll(titles);
        for(int level = 0; level < 3; level++) {
            getChildren().addAll(text_highscore_list[level]);
            getChildren().addAll(number_highscore_list[level]);
        }
    }
}