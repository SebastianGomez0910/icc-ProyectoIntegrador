package ec.edu.ups.icc.proyectointegrador.category.service;

import java.util.List;

import ec.edu.ups.icc.proyectointegrador.category.dto.CategoryRequestDto;
import ec.edu.ups.icc.proyectointegrador.category.dto.CategoryResponseDto;

public interface CategoryService {
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(Long id);
    CategoryResponseDto createCategory(CategoryRequestDto requestDto);
    CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto);
    void deleteCategory(Long id);
}
