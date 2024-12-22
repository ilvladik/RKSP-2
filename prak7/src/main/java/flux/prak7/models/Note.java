package flux.prak7.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("note")
public class Note {
    @Id
    private Long id;
    @Column("title")
    private String title;
    @Column("description")
    private String description;
}
