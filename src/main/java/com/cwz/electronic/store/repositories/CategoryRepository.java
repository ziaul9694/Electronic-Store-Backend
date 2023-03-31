package com.cwz.electronic.store.repositories;

import com.cwz.electronic.store.dtos.CategoryDto;
import com.cwz.electronic.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,String> {

}
