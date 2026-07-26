package ec.edu.ups.icc.proyectointegrador.category.service;

import java.util.List;

import ec.edu.ups.icc.proyectointegrador.category.dto.CategoryResponseDto;

public interface CategoryService {
    List<CategoryResponseDto> getAllCategories();
}
