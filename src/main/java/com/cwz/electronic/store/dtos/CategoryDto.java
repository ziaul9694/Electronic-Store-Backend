package com.cwz.electronic.store.dtos;

import com.cwz.electronic.store.entities.Category;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String categoryId;
    @NotBlank(message = "title is required.")
    @Size(min = 4,message = "Minimum 4 character is required.")
    private String title;
    @NotBlank(message = "Describe about the product.")
    private String description;
//    @NotBlank(message = "Cover Image is required.")
    private String coverImage;
}
