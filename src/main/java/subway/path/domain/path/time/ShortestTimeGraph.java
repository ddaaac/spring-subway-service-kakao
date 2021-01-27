package subway.path.domain.path.time;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.Multigraph;
import subway.path.domain.path.SubwayGraphEdge;
import subway.path.domain.path.graph.PathAndArrival;
import subway.path.domain.path.graph.SubwayGraphElement;
import subway.path.domain.path.graph.SubwayGraph;
import subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.List;

public class ShortestTimeGraph implements SubwayGraph {

    private final Graph<Station, SubwayGraphEdge> graph;

    private ShortestTimeGraph(Graph<Station, SubwayGraphEdge> graph) {
        this.graph = graph;
    }

    public static ShortestTimeGraph initialize(List<SubwayGraphElement> graphElements) {
        Graph<Station, SubwayGraphEdge> graph = new Multigraph<>(SubwayGraphEdge.class);
        for (SubwayGraphElement element : graphElements) {
            graph.addVertex(element.getSource());
            graph.addVertex(element.getTarget());
            graph.addEdge(element.getSource(), element.getTarget(), element.getEdge());
        }
        return new ShortestTimeGraph(graph);
    }

    @Override
    public PathAndArrival getPath(Station source, Station target, LocalDateTime departureTime) {
        return new ShortestTimePathFinder(new KShortestPaths<>(graph, 1000).getPaths(source, target))
                .getPath(departureTime);
    }
}
