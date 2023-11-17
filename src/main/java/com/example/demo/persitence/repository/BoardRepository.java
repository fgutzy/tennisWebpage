package com.example.demo.persitence.repository;

import com.example.demo.persitence.Board;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, Long> {
}
