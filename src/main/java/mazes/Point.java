package mazes;

public class Point
{
  public int x;
  public int y;
  public char space;
  public int value;
  public int magicNum = 0;

  public Point(int myX, int myY)
  {
    x = myX;
    y = myY;
  }

  public Point(int myX, int myY, char mySpace, int myValue)
  {
    x = myX;
    y = myY;
    space = mySpace;
    value = myValue;
  }

  public Point(int myX, int myY, char mySpace, int myValue, int myMagicNum)
  {
    x = myX;
    y = myY;
    space = mySpace;
    value = myValue;
    magicNum = myMagicNum;
  }

  public Point(Point copy)
  {
    x = copy.x;
    y = copy.y;
    space = copy.space;
    value = copy.value;
  }

  public Point clone()
  {
    return clone(true);
  }

  public Point clone(boolean reset)
  {
    return new Point(x, y, space, reset ? 0 : value, magicNum);
  }

  @Override
  public String toString()
  {
    return "(" + x + "," + y + ")";
  }
}
