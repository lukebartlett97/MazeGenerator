package mazes;

import java.util.ArrayList;
import java.util.List;

public class FlowSolver extends RunSolver
{
  private static final boolean VERBOSE = false;
  String RESOURCE_PATH = "/maze1/";

  public FlowSolver()
  {
    setResourcePath(RESOURCE_PATH);
  }

  @Override
  public String solve(Maze maze) throws InterruptedException
  {
    solveFull(maze);
    printMaze(maze);
    maze.repaint();
    return Integer.toString(maze.getEndPoint().value - 1);
  }

  public void solveFull(Maze maze) throws InterruptedException
  {
    solveFull(maze, new ArrayList<>());
  }

  public void solveFull(Maze maze, List<Point> existingPath) throws InterruptedException
  {
    solveInner(maze, existingPath, false);
  }

  public void solveBasic(Maze maze, List<Point> existingPath) throws InterruptedException
  {
   solveInner(maze, existingPath, true);
  }

  public void solveBasic(Maze maze) throws InterruptedException
  {
    solveBasic(maze, new ArrayList<>());
  }

  private void solveInner(Maze maze, List<Point> existingPath, boolean basic) throws InterruptedException
  {
    List<Point> newExistingPath = new ArrayList<>();
    for(Point point : existingPath)
    {
      newExistingPath.add(maze.get(point.x, point.y));
    }
    solvePoint(maze, maze.getStartPoint(), 1, newExistingPath, basic);
    List<Point> path = getPath(maze, maze.getEndPoint());
    maze.setPath(path, true);
  }

  @SuppressWarnings("Duplicates")
  private List<Point> getPath(Maze maze, Point current)
  {
    if(current == maze.getStartPoint())
    {
      List<Point> path = new ArrayList<>();
      path.add(current);
      return path;
    }
    Point bestPoint = null;
    Point point = maze.get(current.x, current.y - 1);
    if(point != null && point.value > 0 && point.value < current.value)
    {
      bestPoint = point;
    }
    point = maze.get(current.x + 1, current.y );
    if(point != null && point.value > 0 && point.value < current.value && (bestPoint == null || point.value < bestPoint.value))
    {
      bestPoint = point;
    }
    point = maze.get(current.x, current.y + 1);
    if(point != null && point.value > 0 && point.value < current.value && (bestPoint == null || point.value < bestPoint.value))
    {
      bestPoint = point;
    }

    point = maze.get(current.x - 1, current.y);
    if(point != null && point.value > 0 && point.value < current.value && (bestPoint == null || point.value < bestPoint.value))
    {
      bestPoint = point;
    }
    if(bestPoint != null)
    {
      //System.out.println("Going to " + bestPoint + " with value " + bestPoint.value);
      List<Point> path = getPath(maze, bestPoint);
      path.add(current);
      return path;
    }
    return new ArrayList<>();
  }

  private void solvePoint(Maze maze, Point currentPoint, int value, List<Point> path, boolean basic) throws InterruptedException
  {
    if(currentPoint == null || currentPoint.space == Maze.WALL)
    {
      return;
    }
    if(currentPoint.value == 0 || (currentPoint.value > value && !basic))
    {
      int count = 0;
      count += path.contains(maze.get(currentPoint.x, currentPoint.y - 1)) ? 1 : 0;
      count += path.contains(maze.get(currentPoint.x - 1, currentPoint.y)) ? 1 : 0;
      count += path.contains(maze.get(currentPoint.x, currentPoint.y + 1)) ? 1 : 0;
      count += path.contains(maze.get(currentPoint.x + 1, currentPoint.y)) ? 1 : 0;
      if(count > 1)
      {
        return;
      }
      currentPoint.value = value;
      maze.paintSquare(null, currentPoint);
      printMaze(maze);
      path.add(currentPoint);
      if(currentPoint.space != Maze.END)
      {
        List<Point> newPath1 = new ArrayList<>(path);
        solvePoint(maze, maze.get(currentPoint.x, currentPoint.y - 1), value + 1, newPath1, basic);
        List<Point> newPath2 = new ArrayList<>(path);
        solvePoint(maze, maze.get(currentPoint.x + 1, currentPoint.y), value + 1, newPath2, basic);
        List<Point> newPath3 = new ArrayList<>(path);
        solvePoint(maze, maze.get(currentPoint.x, currentPoint.y + 1), value + 1, newPath3, basic);
        List<Point> newPath4 = new ArrayList<>(path);
        solvePoint(maze, maze.get(currentPoint.x - 1, currentPoint.y), value + 1, newPath4, basic);
      }
    }
  }

  private void printMaze(Maze maze) throws InterruptedException {
    printMaze(maze, false);
  }

  public void printMaze(Maze maze, boolean override) throws InterruptedException
  {
    int maxLength = Integer.toString(maze.getLargestValue()).length();
    if(VERBOSE || override)
    {
      for(List<Point> line : maze.getMap())
      {
        StringBuilder sb = new StringBuilder();
        for(Point point : line)
        {
          if(point.space == Maze.WALL) {
            for (int i = 0; i < maxLength; i++)
            {
              sb.append('#');
            }
            sb.append(" ");
          }
          else
          {
            int length = Integer.toString(point.value).length();
            sb.append(point.value);
            sb.append(" ");
            for(int i = 0; i < maxLength - length; i++)
            {
              sb.append(" ");
            }
          }
        }
        sb.append("\n");
        printInfo(sb.toString());
      }
    }
    if(maze.isDrawn())
    {
      Thread.sleep(100);
    }
  }
}
