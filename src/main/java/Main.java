import mazes.DrawMazes;
import mazes.FlowSolver;
import mazes.Maze;
import mazes.MazeGenerator;
import mazes.savedPatterns.QRCode;
import mazes.savedPatterns.Smile;
import mazes.templates.ManualPieceTemplate;
import mazes.templates.MazeTemplate;
import mazes.templates.RingsTemplate;
import mazes.templates.WallTemplate;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main
{
  public static void main(String[] args) throws IOException, InterruptedException
  {
    MazeGenerator gen = new MazeGenerator();
    Maze maze = gen.createMaze(60,60, 0, 20, 59, 40, QRCode.getTemplates(60), 0);
    maze.printMaze();
    //FlowSolver solution = new FlowSolver();
    //solution.solve(maze);
//    solution.solveExample(true);
//    DrawMazes drawMazes = new DrawMazes();
//    drawMazes.printSolution(false);

  }
}



