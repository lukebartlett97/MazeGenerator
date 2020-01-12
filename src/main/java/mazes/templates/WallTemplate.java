package mazes.templates;

import mazes.Maze;

import java.util.List;

public class WallTemplate implements MazeTemplate
{
  @Override
  public List<List<Character>> apply(List<List<Character>> maze)
  {
    int maxY =maze.size();
    for(int y = 0; y < maxY; y++)
    {
      int maxX = maze.get(y).size();
      for (int x = 0; x < maxX; x++)
      {

        if ((y == 0 || y == maxY - 1 || x == maxX - 1 || x == 0))
        {
          maze.get(y).set(x, Maze.WALL);
        }
      }
    }
    return maze;
  }
}
