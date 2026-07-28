package ec.edu.ups.icc.proyectointegrador.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.proyectointegrador.category.dto.CategoryRequestDto;
import ec.edu.ups.icc.proyectointegrador.category.dto.CategoryResponseDto;
import ec.edu.ups.icc.proyectointegrador.category.entity.Category;
import ec.edu.ups.icc.proyectointegrador.category.mapper.CategoryMapper;
import ec.edu.ups.icc.proyectointegrador.category.repository.CategoryRepository;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ConflictException;
import ec.edu.ups.icc.proyectointegrador.common.exception.domain.ResourceNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        if (categoryRepository.existsByName(requestDto.getName())) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + requestDto.getName());
        }

        Category category = categoryMapper.toEntity(requestDto);
        
        Category savedCategory = categoryRepository.save(category);
        
        return categoryMapper.toResponseDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        if (!category.getName().equals(requestDto.getName()) && 
            categoryRepository.existsByName(requestDto.getName())) {
            throw new ConflictException("Ya existe otra categoría con el nombre: " + requestDto.getName());
        }

        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
