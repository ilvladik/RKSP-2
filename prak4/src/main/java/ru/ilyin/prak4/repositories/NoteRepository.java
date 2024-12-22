package ru.ilyin.prak4.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ilyin.prak4.entities.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findNoteById(Long id);
}
