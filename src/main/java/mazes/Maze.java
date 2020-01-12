package mazes;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Maze extends Canvas
{
  public static final char START = 'S';
  public static final char END = 'E';
  public static final char WALL = '#';
  public static final char OPEN = '.';
  public static final char CURRENT = 'X';
  public static final char MARKER = 'M';

  private List<List<Point>> maze = new ArrayList<>();
  private List<Point> path = new ArrayList<>();
  private JFrame frame;
  private int numMarkers = 0;
  private boolean failed = false;
  private boolean drawn = false;

  public Maze(List<List<Character>> input)
  {
    drawn = true;
    frame = new JFrame(("Maze"));
    setSize(400, 400);
    frame.add(this);
    frame.pack();
    frame.setVisible(true);
    for (int y = 0; y < input.size(); y++)
    {
      List<Point> line = new ArrayList<>();
      for (int x = 0; x < input.get(0).size(); x++)
      {
        char inputVal = input.get(y).get(x);
        Point point = new Point(x, y, inputVal, 0);
        line.add(point);
        if(inputVal == Maze.MARKER)
        {
          numMarkers++;
          point.magicNum = numMarkers;
        }
      }
      maze.add(line);
    }
  }

  public Maze(List<List<Point>> input, boolean drawn)
  {
    if(drawn)
    {
      this.drawn = true;
      frame = new JFrame(("Maze"));
      setSize(400, 400);
      frame.add(this);
      frame.pack();
      frame.setVisible(true);
    }
    maze = input;
    numMarkers = searchForAll(Maze.MARKER).size();
  }

  public boolean isDrawn()
  {
    return drawn;
  }

  public void destroy()
  {
    if(drawn)
    {
      frame.dispose();
      this.drawn = false;
    }
  }

  Point get(int x, int y)
  {
    if(y >= maze.size() || y < 0 || x >= maze.get(y).size() || x < 0)
    {
      return null;
    }
    return maze.get(y).get(x);
  }

  List<List<Point>> getMap()
  {
    return maze;
  }


  Point getStartPoint()
  {
    return searchFor(START);
  }

  private Point searchFor(char target)
  {
    for (List<Point> line : maze)
    {
      for(Point point : line)
      {
        if(point.space == target)
        {
          return point;
        }
      }
    }
    return null;
  }

  public List<Point> searchForAll(char target)
  {
    List<Point> points = new ArrayList<>();
    for (List<Point> line : maze)
    {
      for(Point point : line)
      {
        if(point.space == target)
        {
          points.add(point);
        }
      }
    }
    return points;
  }

  Point getEndPoint()
  {
    return searchFor(Maze.END);
  }

  void paintSquare(Graphics g, Point point)
  {
    if(!isDrawn())
    {
      return;
    }
    if(g == null)
    {
      g = getGraphics();
    }
    int x = point.x;
    int y = point.y;
    int height = getHeight() / maze.size();
    int width = getWidth() / maze.get(y).size();
    int maxValue = getLargestValue();
    switch(point.space)
    {
      case Maze.WALL:
        g.setColor(Color.black);
        g.fillRect(x*width, y*height, width, height);
        break;
      case Maze.START:
        g.setColor(Color.green);
        g.fillRect(x*width, y*height, width, height);
        break;
      case Maze.END:
        g.setColor(Color.red);
        g.fillRect(x*width, y*height, width, height);
        break;
      case Maze.CURRENT:
        g.setColor(Color.blue);
        g.fillRect(x*width, y*height, width, height);
        break;
      case Maze.MARKER:
        int blue = 200-(150 * (point.magicNum - 1) / numMarkers);
        g.setColor(new Color(0,0,blue));
        g.fillRect(x*width, y*height, width, height);
        break;
      default:
        int val = maxValue == 0 ? 150 : getGreyVal(point.value, maxValue);
        g.setColor(new Color(val,val,val));
        g.fillRect(x*width, y*height, width, height);
        if(path.contains(point))
        {
          g.setColor(Color.orange);
          g.fillRect(x*width + (width/4), y*height + (height/4), width/2, width/2);
        }
        break;
    }

  }

  private int getGreyVal(int value, int maxValue)
  {
    BigDecimal pointVal = new BigDecimal(value);
    BigDecimal maxVal = new BigDecimal(maxValue);
    BigDecimal lightest = new BigDecimal(200);
    try
    {
      BigDecimal val = lightest.subtract(pointVal.divide(maxVal, 5, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(150)));
      return val.intValue();
    } catch(Exception e)
    {
      return maxValue;
    }
  }

  @Override
  public void paint(Graphics g) {
    for (int y = 0; y < maze.size(); y++)
    {
      for (int x = 0; x < maze.get(y).size(); x++)
      {
        Point point = maze.get(y).get(x);
        paintSquare(g, point);
      }
    }
  }

  void setPath(List<Point> path, boolean draw)
  {
    List<Point> oldPath = new ArrayList<>(this.path);
    this.path = path;
    if(draw && isDrawn())
    {
      for(Point point : oldPath)
      {
        paintSquare(null, point);
      }
      for(Point point : path)
      {
        paintSquare(null, point);
      }
    }
  }

  List<Point> getPath()
  {
    return path;
  }


  int getLargestValue()
  {
    int maxValue = 0;
    for (List<Point> line : maze)
    {
      for(Point point : line)
      {
        maxValue = Math.max(maxValue, point.value);
      }
    }
    return maxValue;
  }

  public Maze clone(boolean drawn)
  {
    List<List<Point>> copyMaze = new ArrayList<>();
    for(List<Point> line : maze)
    {
      List<Point> copyLine = new ArrayList<>();
      for(Point point : line)
      {
        copyLine.add(point.clone());
      }
      copyMaze.add(copyLine);
    }
    Maze out = new Maze(copyMaze, drawn);
    out.numMarkers = numMarkers;
    return out;
  }

  boolean isOpen(int x, int y)
  {
    Point point = get(x, y);
    return point != null && (point.space == Maze.OPEN || point.space == Maze.MARKER || point.space == Maze.END || point.space == Maze.START);
  }

  public void printMaze()
  {
//    StringBuilder sbCount = new StringBuilder();
//    sbCount.append(" ");
//    for(int i = 0; i < maze.get(0).size(); i++)
//    {
//      sbCount.append(i % 10);
//    }
//    System.out.println(sbCount.toString());
//    int count = 0;
    for(List<Point> line : maze)
    {
      StringBuilder sb = new StringBuilder();
      //sb.append(count % 10);
      //count++;
      for(Point point : line)
      {
        sb.append(point.space);
      }
      System.out.println(sb.toString());
    }
  }

  public int getNumMarkers()
  {
    return numMarkers;
  }

  public void setNumMarkers(int numMarkers)
  {
    this.numMarkers = numMarkers;
  }

  public int getNumberAdjacentSpaces(Point point)
  {
    int count = 0;
    int x = point.x;
    int y = point.y;
    count += isOpen(x + 1, y) ? 1 : 0;
    count += isOpen(x, y + 1) ? 1 : 0;
    count += isOpen(x - 1, y) ? 1 : 0;
    count += isOpen(x, y - 1) ? 1 : 0;
    return count;
  }

  public void setFailed(boolean failed)
  {
    this.failed = failed;
  }

  public boolean isFailed()
  {
    return failed;
  }

  public void addMarker(Point marker)
  {
    if(marker.space != Maze.MARKER)
    {
      marker.space = Maze.MARKER;
      numMarkers++;
      marker.magicNum = numMarkers;
    }
  }
}
