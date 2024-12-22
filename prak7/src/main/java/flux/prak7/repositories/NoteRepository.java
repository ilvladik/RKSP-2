package flux.prak7.repositories;

import flux.prak7.models.Note;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
// Ильин Владислав Викторович ИКБО-01-21
@Repository
public interface NoteRepository extends R2dbcRepository<Note, Long> {}
