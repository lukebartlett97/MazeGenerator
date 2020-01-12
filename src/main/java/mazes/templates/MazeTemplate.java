package mazes.templates;

import java.util.List;

public interface MazeTemplate
{

  List<List<Character>> apply(List<List<Character>> maze);
}
