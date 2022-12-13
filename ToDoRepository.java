package com.efsauto.erste_schritte.repositories;

import com.efsauto.erste_schritte.models.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo,Long> {
    boolean existsByTitle(String title);
    List<ToDo> findByChecked(boolean isChecked);
}
