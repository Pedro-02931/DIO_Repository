package engine;

import domain.Conteudo;
import domain.Curso;
import domain.Mentoria;

import java.util.Set;

public class LearningEngine {

    public static double calcularTotalXpParalelo(Set<Conteudo> conteudosConcluidos) {
        return conteudosConcluidos
                .parallelStream()
                .mapToDouble(conteudo -> {
                    double rawXp = 0;

                    if (conteudo instanceof Curso curso) {
                        rawXp = 10d * curso.getCargaHoraria();
                    } else if (conteudo instanceof Mentoria) {
                        rawXp = 30d;
                    } else {
                        rawXp = 10d;
                    }

                    
                    return Math.log1p(rawXp) * 15; 
                })
                .sum();
    }
}
