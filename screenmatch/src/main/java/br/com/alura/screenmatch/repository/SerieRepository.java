package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.modelos.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

//informe qual a tabela que você esta trabalhando (ou a entidade, que no caso é "Serie"), e qual o tipo do "ID" que nesse caso é "Long"
public interface SerieRepository extends JpaRepository<Serie,Long>{



}
