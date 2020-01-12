package mazes.templates;

import mazes.Maze;

import java.util.List;

public class GridTemplate implements  MazeTemplate
{


  private int startX;
  private int startY;
  private int endX;
  private int endY;

  public GridTemplate(int startX, int startY, int endX, int endY)
  {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
  }

  @Override
  public List<List<Character>> apply(List<List<Character>> maze)
  {
    for(int y = startY; y <= endY; y++)
    {
      for (int x = startX; x <= endX; x++)
      {
        if(((x - startX) % 2 == 0) && ((y - startY) % 2 == 0))
        {
          maze.get(y).set(x, Maze.WALL);
        }
      }
    }
    return maze;
  }
}
