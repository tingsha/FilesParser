package com.naname.fileparser.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private LocalDateTime date;
    private String size;
    private String content;

    public File(){
    }

    public File(String name, String size, String content) {
        this.name = name;
        this.size = size;
        this.content = content;
    }
}
