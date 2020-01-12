package mazes.templates;

import mazes.Maze;

import java.math.BigDecimal;
import java.util.List;

public class CircleTemplate implements MazeTemplate
{


  private final char piece;
  private int centreX;
  private int centreY;
  private int radius;

  public CircleTemplate(int centreX, int centreY, int radius)
  {
    this(centreX, centreY, radius, Maze.WALL);
  }

  public CircleTemplate(int centreX, int centreY, int radius, char piece)
  {
    this.centreX = centreX;
    this.centreY = centreY;
    this.radius = radius;
    this.piece = piece;
  }

  @Override
  public List<List<Character>> apply(List<List<Character>> maze)
  {
    for(int y = centreY - radius; y <= centreY + radius; y++)
    {
      for (int x = centreX - radius; x <= centreX + radius; x++)
      {
        BigDecimal value = getDistance(x, y).subtract(new BigDecimal(radius).pow(2));
        if (value.compareTo(BigDecimal.ZERO) <= 0)
        {
          maze.get(y).set(x, piece);
        }
      }
    }
    return maze;
  }

  private BigDecimal getDistance(int myX, int myY)
  {
    BigDecimal x = new BigDecimal(Math.abs(myX - centreX));
    BigDecimal y = new BigDecimal(Math.abs(myY - centreY));
    return x.pow(2).add(y.pow(2));
  }
}
