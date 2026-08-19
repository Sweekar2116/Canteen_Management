package com.canteen.service;

import com.canteen.dto.CategoryRequest;
import com.canteen.dto.CategoryResponse;
import com.canteen.entity.Category;
import com.canteen.exception.ResourceNotFoundException;
import com.canteen.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuditLogService auditLogService;

    public CategoryService(CategoryRepository categoryRepository, AuditLogService auditLogService) {
        this.categoryRepository = categoryRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllActiveCategories() {
        return categoryRepository.findByActiveTrue().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = getEntity(id);
        return mapToResponse(category);
    }

    @Transactional
    public CategoryResponse createCategory(Long adminId, CategoryRequest request) {
        Category category = new Category(request.getName(), request.getDescription(), request.getImageUrl());
        category.setActive(request.isActive());
        Category saved = categoryRepository.save(category);

        auditLogService.log(adminId, "CREATE_CATEGORY", "Category", saved.getId(), "Created category: " + saved.getName());
        return mapToResponse(saved);
    }

    @Transactional
    public CategoryResponse updateCategory(Long adminId, Long id, CategoryRequest request) {
        Category category = getEntity(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setActive(request.isActive());
        Category updated = categoryRepository.save(category);

        auditLogService.log(adminId, "UPDATE_CATEGORY", "Category", updated.getId(), "Updated category: " + updated.getName());
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteCategory(Long adminId, Long id) {
        Category category = getEntity(id);
        category.setActive(false);
        categoryRepository.save(category);

        auditLogService.log(adminId, "DEACTIVATE_CATEGORY", "Category", id, "Deactivated category: " + category.getName());
    }

    public Category getEntity(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getImageUrl(),
            category.isActive()
        );
    }
}
