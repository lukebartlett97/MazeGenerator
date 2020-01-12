package mazes;

import mazes.templates.MazeTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mazes.Maze.*;

public class MazeGenerator
{
  private List<Point> failedPoints = new ArrayList<>();
  private int failCount = 0;
  List<Point> lastBlocked = new ArrayList<>();

  public Maze createMaze(int myX, int myY, int startX, int startY, int endX, int endY, List<MazeTemplate> templates, int numMarkers) throws InterruptedException
  {
    Maze maze = createInitialMaze(myX, myY, startX, startY, endX, endY, templates == null ? new ArrayList<>() : templates);
    List<Point> validPoints = getValidPoints(maze.clone(false));
    failCount = myX * myY;
    failedPoints.clear();
    List<Point> route = new ArrayList<>();
    route.add(maze.getStartPoint());
    route.addAll(maze.searchForAll(MARKER));
    for(int i = 0; i < numMarkers; i++)
    {
      Point marker = chooseRandomPoint(maze, validPoints);
      if(marker != null)
      {
        maze.addMarker(marker);
        addFailedPoint(marker, "Set as marker");
        route.add(marker);
      }
    }
    route.add(maze.getEndPoint());
    maze.repaint();
    while(failedPoints.size() < failCount)
    {
      Point selected = chooseRandomPoint(maze, validPoints);
      if(selected == null){
        break;
      }
      Maze copyMaze = maze.clone(false);
      copyMaze.get(selected.x, selected.y).space = WALL;
      FlowSolver flowSolver = new FlowSolver();
      flowSolver.solveBasic(copyMaze);
      if(isFlowValid(copyMaze, validPoints))
      {
        System.out.println("---" + selected);
        if(route.size() < 3 || isRouteValid(copyMaze, new ArrayList<>(route), new ArrayList<>()))
        {
          maze.get(selected.x, selected.y).space = WALL;
          maze.paintSquare(null, maze.get(selected.x, selected.y));
          addFailedPoint(selected, "Made into wall");
        }
        else {
          addFailedPoint(selected, "Makes route invalid");
        }
      }
      else
      {
        addFailedPoint(selected, "Makes flow invalid");
      }
      copyMaze.destroy();
    }
    List<Point> path = new ArrayList<>();
    for(Point point : lastBlocked)
    {
      path.add(maze.get(point.x, point.y));
    }
    maze.setPath(path, true);
    maze.repaint();
    return maze;
  }

  private List<Point> getValidPoints(Maze maze) throws InterruptedException
  {
    FlowSolver flowSolver = new FlowSolver();
    flowSolver.solveBasic(maze);
    List<Point> out = new ArrayList<>();
    for(List<Point> line : maze.getMap())
    {
      for(Point point : line)
      {
        if(point.value > 0)
        {
          out.add(point);
        }
      }
    }
    return out;
  }

  private boolean isRouteValid(Maze maze, List<Point> route, List<Point> blocked) throws InterruptedException
  {
    System.out.println("route " + route);
    if(route == null || route.size() < 2)
    {
      lastBlocked = new ArrayList<>(blocked);
      System.out.println(true);
      return true;
    }
    Point start = route.get(0);
    Point end = route.get(1);
    Maze mazeCopy = maze.clone(false);
    mazeCopy.getStartPoint().space = WALL;
    mazeCopy.getEndPoint().space = WALL;
    if(route.size() > 2)
    {
      for(Point point : route.subList(2, route.size()))
      {
        mazeCopy.get(point.x, point.y).space = WALL;
      }
    }
    for(Point point : blocked)
    {
      mazeCopy.get(point.x, point.y).space = WALL;
    }
    mazeCopy.get(start.x, start.y).space = START;
    mazeCopy.get(end.x, end.y).space = END;
    FlowSolver flowSolver = new FlowSolver();
    if(route.size() < 3)
    {
//      System.out.println("Basic solve between " + start + end);
      System.out.println(mazeCopy.getLargestValue());
      flowSolver.solveBasic(mazeCopy, blocked);
      flowSolver.printMaze(mazeCopy, true);
    } else {
//      System.out.println("Full solve between " + start + end);
      flowSolver.solveFull(mazeCopy, blocked);
//      flowSolver.printMaze(mazeCopy, true);
    }
    List<Point> path = mazeCopy.getPath();
    System.out.println("path " + path);
    if(path.isEmpty() || path.get(0) !=  mazeCopy.getStartPoint())
    {
      mazeCopy.destroy();
      System.out.println(false);
      return false;
    }
    blocked.addAll(mazeCopy.getPath());
    route.remove(0);
    mazeCopy.destroy();
    return isRouteValid(maze, route, blocked);
  }

  public boolean isFlowValid(Maze copyMaze, List<Point> validPoints)
  {
    for(Point point : validPoints)
    {
      Point currentPoint = copyMaze.get(point.x, point.y);
      if(currentPoint.space != WALL && currentPoint.value == 0)
      {
        return false;
      }
    }
    return true;
  }

  private Point chooseRandomPoint(Maze maze, List<Point> validPoints)
  {
    Random r = new Random();
    while(failedPoints.size() < failCount)
    {
      int y = r.nextInt(maze.getMap().size());
      int x = r.nextInt(maze.getMap().get(y).size());
      Point selected = maze.get(x, y);
      if(!isPointInList(failedPoints, selected))
      {
        if(selected.space == Maze.OPEN && isPointInList(validPoints, selected))
        {
          int count = maze.getNumberAdjacentSpaces(selected);
          if(count > 1){
            return selected;
          }else {
            addFailedPoint(selected, "Not enough open neighbours");
          }
        } else {
          addFailedPoint(selected, "Not open and/or valid");
        }
      }
    }
    return null;
  }

  private boolean isPointInList(List<Point> validPoints, Point selected)
  {
    for(Point point : validPoints)
    {
      if(point.x == selected.x && point.y == selected.y)
      {
        return true;
      }
    }
    return false;
  }

  private void addFailedPoint(Point point, String reason)
  {
    failedPoints.add(point);
    //System.out.println("Failed point #" + failedPoints.size() + " added at (" + point.x + "," + point.y + ") with reason: " + reason);
  }

  private Maze createInitialMaze(int myX, int myY, int startX, int startY, int endX, int endY, List<MazeTemplate> templates)
  {
    List<List<Character>> maze = new ArrayList<>();
    for(int y = 0; y < myY; y++)
    {
      List<Character> line = new ArrayList<>();
      for(int x = 0; x < myX; x++)
      {
        line.add('.');
      }
      maze.add(line);
    }
    for(MazeTemplate template : templates)
    {
      maze = template.apply(maze);
    }
    maze.get(startY).set(startX, 'S');
    maze.get(endY).set(endX, 'E');
    for(List<Character> line : maze)
    {
      StringBuilder sb = new StringBuilder();
      for(Character letter : line)
      {
        sb.append(letter);
      }
      System.out.println(sb.toString());
    }
    return new Maze(maze);
  }

}
