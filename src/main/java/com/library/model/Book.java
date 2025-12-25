package com.library.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table("books")
public class Book {

    @Id
    @Column("id")
    private Long id;
    @Column("title")
    private String title;
    @Column("author")
    private String author;
    @Column("publication_Year")
    public Integer publicationYear;

    @Override
    public String toString(){
        return String.format(
                "Book[id=%d, title='%s', author='%s', publicationYear=%d]",
                id, title, author, publicationYear
        );
    }

}
