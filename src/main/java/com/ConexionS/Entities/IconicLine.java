package com.ConexionS.Entities;

import lombok.*;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "iconicline")
public class IconicLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_IconicLine;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_brand", nullable = false)
    private Brand brand;
}