package domain;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.Set;

public class Bootcamp {
    private String nome;
    private String descricao;
    private final LocalDate dataInicio = LocalDate.now();
    private final LocalDate dataFim = dataInicio.plusDays(45);
    private Set<Dev> devsInscritos = new HashSet<>();
    private Set<Conteudo> conteudos = new LinkedHashSet<>();
    
    public String getNome() {  return nome;  }
    public void setNome(String nome) {  this.nome = nome;  }
    public String getDescricao() {  return descricao;  }
    public void setDescricao(String descricao) {  this.descricao = descricao;  }
    public LocalDate getDataInicio() {  return dataInicio;  }
    public LocalDate getDataFim() {  return dataFim;  }
    public Set<Dev> getDevsInscritos() {  return devsInscritos;  }
    public void setDevsInscritos(Set<Dev> devsInscritos) {  this.devsInscritos = devsInscritos;  }
    public Set<Conteudo> getConteudos() {  return conteudos;  }
    public void setConteudos(Set<Conteudo> conteudos) {  this.conteudos = conteudos;  }
}
