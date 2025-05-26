package engine;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParallelProcessor {
    public static <T, R> List<R> processInParallel(List<T> items, Function<T, R> processor) {
        return items.parallelStream()
                    .map(processor)
                    .collect(Collectors.toList());
    }
    public static <T> void processInParallelAndConsume(List<T> items, Function<T, Void> consumer) {
        items.parallelStream()
              .forEach(consumer::apply);
    }
}
