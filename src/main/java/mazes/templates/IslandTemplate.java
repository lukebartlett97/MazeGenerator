package mazes.templates;

import mazes.Maze;

import java.util.List;

public class IslandTemplate implements MazeTemplate
{


  private final char piece;
  private int startX;
  private int startY;
  private int endX;
  private int endY;

  public IslandTemplate(int startX, int startY, int endX, int endY)
  {
    this(startX, startY, endX, endY, Maze.WALL);
  }

  public IslandTemplate(int startX, int startY, int endX, int endY, char piece)
  {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    this.piece = piece;
  }

  @Override
  public List<List<Character>> apply(List<List<Character>> maze)
  {
    for(int y = startY; y <= endY; y++)
    {
      for (int x = startX; x <= endX; x++)
      {
        maze.get(y).set(x, piece);
      }
    }
    return maze;
  }
}
