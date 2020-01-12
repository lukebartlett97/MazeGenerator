package mazes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract public class RunSolver
{

  protected String solve(List<String> data) throws InterruptedException
  {
    List<List<Character>> converted = new ArrayList<>();
    for(String line : data)
    {
      converted.add(line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }
    Maze maze = new Maze(converted);
    return solve(maze);
  }

  abstract String solve(Maze maze) throws InterruptedException;


  private String resourcePath;

  private boolean verbose = true;

  public void printSolution(boolean verbose) throws IOException, InterruptedException
  {
    getProblem().forEach(System.out::println);
    solveExample(verbose);
    List<String> data = getData();
    System.out.println("Real Data:");
    System.out.println(data);
    System.out.println("Solution:");
    System.out.println(solve(data));
  }

  public void solveExample(boolean verbose) throws IOException, InterruptedException
  {
    this.verbose = verbose;
    List<String> exampleData = getExample();
    if(exampleData != null)
    {
      System.out.println("Example Data:");
      System.out.println(exampleData);
      System.out.println("Solution:");
      System.out.println(solve(exampleData));
    }
  }

  protected List<String> getData() throws IOException
  {
    URL resource = this.getClass().getResource(resourcePath + "data.txt");
    return readFile(resource);
  }

  protected List<String> getExample() throws IOException
  {
    URL resource = this.getClass().getResource(resourcePath + "example.txt");
    return resource == null ? null : readFile(resource);
  }

  protected List<String> getProblem() throws IOException
  {
    URL resource = this.getClass().getResource(resourcePath + "problem.txt");
    return readFile(resource);
  }

  private List<String> readFile (URL file) throws IOException
  {
    BufferedReader in = new BufferedReader(
            new InputStreamReader(file.openStream()));

    String inputLine;
    List<String> lines = new ArrayList<String>();
    while ((inputLine = in.readLine()) != null)
      lines.add(inputLine);
    in.close();
    return lines;
  }

  public void setResourcePath(String resourcePath)
  {
    this.resourcePath = resourcePath;
  }

  protected List<Integer> convertToIntegerList(List<String> strings)
  {
    return strings.stream().map(Integer::parseInt).collect(Collectors.toList());
  }

  protected List<Long> convertToLongList(List<String> strings)
  {
    return strings.stream().map(Long::parseLong).collect(Collectors.toList());
  }

  protected void printInfo(String line)
  {
    if(verbose)
    {
      System.out.println(line);
    }
  }
}
