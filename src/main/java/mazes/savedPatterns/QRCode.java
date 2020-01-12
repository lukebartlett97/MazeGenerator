package mazes.savedPatterns;

import mazes.Maze;
import mazes.templates.ManualPieceTemplate;
import mazes.templates.MazeTemplate;
import mazes.templates.RingsTemplate;
import mazes.templates.WallTemplate;

import java.util.ArrayList;
import java.util.List;

public class QRCode {

    public static List<MazeTemplate> getTemplates(int size)
    {
        List<MazeTemplate> templates = new ArrayList<>();
        WallTemplate wallTemplate = new WallTemplate();
        RingsTemplate ringsTemplate = new RingsTemplate(1, 1, size / 3, size / 3);
        RingsTemplate ringsTemplate2 = new RingsTemplate(size * 2 / 3, 1, size - 2, size / 3);
        RingsTemplate ringsTemplate3 = new RingsTemplate(1, size  * 2 / 3, size / 3, size - 2);
        ManualPieceTemplate markerTemplate = new ManualPieceTemplate(size / 6, 2, Maze.MARKER);
        ManualPieceTemplate markerTemplate2 = new ManualPieceTemplate(size * 5 / 6, 4, Maze.MARKER);
        ManualPieceTemplate markerTemplate3 = new ManualPieceTemplate(6, size * 5 / 6, Maze.MARKER);
        templates.add(wallTemplate);
        templates.add(markerTemplate);
        templates.add(markerTemplate2);
        templates.add(markerTemplate3);
        templates.add(ringsTemplate);
        templates.add(ringsTemplate2);
        templates.add(ringsTemplate3);
        return templates;
    }
}
