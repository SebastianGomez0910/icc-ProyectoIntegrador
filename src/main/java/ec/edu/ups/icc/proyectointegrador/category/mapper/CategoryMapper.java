package ec.edu.ups.icc.proyectointegrador.category.mapper;

import org.springframework.stereotype.Component;

import ec.edu.ups.icc.proyectointegrador.category.dto.CategoryRequestDto;
import ec.edu.ups.icc.proyectointegrador.category.dto.CategoryResponseDto;
import ec.edu.ups.icc.proyectointegrador.category.entity.Category;

@Component
public class CategoryMapper {
    
    public Category toEntity(CategoryRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
    
        Category category = new Category();
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        
        return category;
    }

    public CategoryResponseDto toResponseDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(category.getId());
        responseDto.setName(category.getName());
        responseDto.setDescription(category.getDescription());
        
        return responseDto;
    }
}
