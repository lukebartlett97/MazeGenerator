package mazes;

import java.util.ArrayList;
import java.util.List;

public class LeftSolver extends RunSolver
{
  private static final boolean VERBOSE = false;
  String RESOURCE_PATH = "/maze1/";

  public LeftSolver()
  {
    setResourcePath(RESOURCE_PATH);
  }

  @Override
  String solve(Maze maze) throws InterruptedException
  {

    List<Point> path = new ArrayList<>();
    List<Point> fullPath = new ArrayList<>();
    Point current = new Point(maze.getStartPoint());
    int direction = 0;
    int startChances = maze.getNumberAdjacentSpaces(current) - 1;
    boolean failed = false;
    boolean moved = false;
    while(current.space != Maze.END || !hitAllMarkers(path, maze))
    {
      if(current.space == Maze.START && moved)
      {
        System.out.println("Back to start");
        if(startChances == 0)
        {
          failed = true;
          break;
        } else {
          startChances--;
        }
      }
      int targetX = current.x;
      int targetY = current.y;
      switch(direction)
      {
        case 0:
          targetX -= 1;
          break;
        case 1:
          targetY -= 1;
          break;
        case 2:
          targetX += 1;
          break;
        case 3:
          targetY += 1;
          break;
        default:
          break;
      }
      Point targetPoint = maze.get(targetX, targetY);
      if(targetPoint != null && targetPoint.space != Maze.WALL && (!path.contains(targetPoint) || path.get(path.size() - 2) == targetPoint))
      {
        current = targetPoint;
        List<Point> updated = new ArrayList<>();
        if(path.contains(targetPoint))
        {
          updated.addAll(path.subList(path.indexOf(targetPoint),path.size()));
          path = path.subList(0, path.indexOf(targetPoint));
        }
        path.add(targetPoint);
        fullPath.add(targetPoint);
        maze.setPath(path, false);
        updated.add(targetPoint);
        for(Point point : updated)
        {
          maze.paintSquare(null, point);
        }
        printMaze(maze, current, direction, path, fullPath);
        direction = direction > 0 ? direction - 1 : 3;
        moved = true;
      } else {
        direction = direction < 3 ? direction + 1 : 0;
        moved = false;
      }
    }
    maze.setFailed(failed);
    printInfo(path.toString());
    printInfo(fullPath.toString());
    return Integer.toString(path.size());
  }

  private boolean hitAllMarkers(List<Point> path, Maze maze)
  {
    int acc = 0;
    for(Point point : path)
    {
      if(point.space == Maze.MARKER)
      {
        acc++;
      }
    }
    return acc == maze.getNumMarkers();
  }

  private void printMaze(Maze maze, Point current, int direction, List<Point> path, List<Point> fullPath) throws InterruptedException
  {
    if(VERBOSE)
    {
      for(List<Point> line : maze.getMap())
      {
        StringBuilder sb = new StringBuilder();
        for(Point point : line)
        {
          if(point.equals(current))
          {
            switch (direction)
            {
              case 0:
                sb.append('<');
                break;
              case 1:
                sb.append('^');
                break;
              case 2:
                sb.append('>');
                break;
              case 3:
                sb.append('v');
                break;
              default:
                sb.append(Maze.CURRENT);
                break;
            }
          }
          else if (path.contains(point))
          {
            sb.append('x');
          }
          else if (fullPath.contains(point))
          {
            sb.append('o');
          }
          else
          {
            sb.append(point.space);
          }
        }
        printInfo(sb.toString());
      }
      printInfo("");
    }
    if(maze.isDrawn())
    {
      //Thread.sleep(50);
    }
  }
}
