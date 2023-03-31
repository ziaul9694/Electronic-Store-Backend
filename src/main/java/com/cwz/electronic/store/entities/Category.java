package com.cwz.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @Column(name = "Id")
    private String categoryId;
    @Column(name = "category_title", length = 55, nullable = false)
    private String title;
    @Column(name = "category_description", length = 500)
    private String description;
    private String coverImage;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "category")
    private List<Product> productList= new ArrayList<>();
}
