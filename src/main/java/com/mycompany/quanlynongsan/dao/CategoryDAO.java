/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.dao;

import java.util.List;

import com.mycompany.quanlynongsan.model.Category;
import com.mycompany.quanlynongsan.repository.CategoryRepository;

/**
 *
 * @author nghiem
 */
public class CategoryDAO {

    private CategoryRepository categoryRepository = new CategoryRepository();

    public CategoryDAO() {
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
