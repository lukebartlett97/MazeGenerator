package mazes.templates;

import java.util.List;

public class ManualPieceTemplate implements  MazeTemplate
{


  private int x;
  private int y;
  private char piece;

  public ManualPieceTemplate(int x, int y, char piece)
  {
    this.x = x;
    this.y = y;

    this.piece = piece;
  }

  @Override
  public List<List<Character>> apply(List<List<Character>> maze)
  {
    maze.get(y).set(x, piece);
    return maze;
  }
}
