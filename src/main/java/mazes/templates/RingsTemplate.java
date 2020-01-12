package mazes.templates;

import mazes.Maze;
import mazes.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RingsTemplate implements MazeTemplate
{


  private int startX;
  private int startY;
  private int endX;
  private int endY;

  public RingsTemplate(int startX, int startY, int endX, int endY)
  {
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
  }

  @Override
  public List<List<Character>> apply(List<List<Character>> maze)
  {
    int x1 = startX;
    int y1 = startY;
    int x2 = endX;
    int y2 = endY;
    Random random = new Random();
    while(x2 > x1 && y2 > y1)
    {
      List<Point> points = new ArrayList<>();
      for(int y = y1; y <= y2; y++)
      {
        for (int x = x1; x <= x2; x++)
        {
          int count = 0;
          count += y == y1 ? 1 : 0;
          count += y == y2 ? 1 : 0;
          count += x == x1 ? 1 : 0;
          count += x == x2 ? 1 : 0;

          if (count > 0)
          {
            maze.get(y).set(x, Maze.WALL);
            if(count == 1)
            {
              points.add(new Point(x, y));
            }
          }
        }
      }
      if(points.size() > 0)
      {
        for(int i = 0; i <= (points.size() / 10) + 2; i++)
        {
          Point selected = points.get(random.nextInt(points.size()));
          maze.get(selected.y).set(selected.x, Maze.OPEN);
        }
      }

      x1 += 2;
      y1 += 2;
      x2 -= 2;
      y2 -= 2;
    }
    return maze;
  }
}
