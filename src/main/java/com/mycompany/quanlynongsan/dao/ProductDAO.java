/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.dao;

import com.mycompany.quanlynongsan.dto.ProductDTO;
import com.mycompany.quanlynongsan.model.Category;
import com.mycompany.quanlynongsan.model.ImageProduct;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.repository.CategoryRepository;
import com.mycompany.quanlynongsan.repository.ImageProductRepository;
import com.mycompany.quanlynongsan.repository.OrderProductRepository;
import com.mycompany.quanlynongsan.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author nghiem
 */
public class ProductDAO {

    private ProductRepository productRepository = new ProductRepository();

    private ImageProductRepository imageProductRepository = new ImageProductRepository();

    private CategoryRepository categoryRepository = new CategoryRepository();

    private OrderProductRepository orderProductRepository = new OrderProductRepository();

    public ProductDAO() {
    }

    public List<ProductDTO> find10(Integer categoryId) {
        List<Product> products = productRepository.find10(categoryId);
        return products.stream().map(product -> {
            List<ImageProduct> imageProduct = imageProductRepository.findAllByProductId(product.getProductId());
            List<Category> categories = categoryRepository.findCategoriesByProductId(product.getProductId());
            Double rates = orderProductRepository.getAverageRateByProductId(product.getProductId());
            Integer reviewerQuantity = orderProductRepository.getNumberOfReviewsByProductId(product.getProductId());
            return new ProductDTO(
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getStatus(),
                    product.getIsSell(),
                    product.getIsBrowse(),
                    product.getPlaceOfManufacture(),
                    product.getIsActive(),
                    product.getHolderId(),
                    product.getCreatedDate(),
                    imageProduct.stream().map(p -> p.getUrlImage()).collect(Collectors.toList()),
                    rates,
                    reviewerQuantity,
                    categories.stream().map(c -> c.getName()).collect(Collectors.toList())
            );
        }).collect(Collectors.toList());
    }

    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> {
            List<ImageProduct> imageProduct = imageProductRepository.findAllByProductId(product.getProductId());
            List<Category> categories = categoryRepository.findCategoriesByProductId(product.getProductId());
            Double rates = orderProductRepository.getAverageRateByProductId(product.getProductId());
            Integer reviewerQuantity = orderProductRepository.getNumberOfReviewsByProductId(product.getProductId());
            return new ProductDTO(
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getStatus(),
                    product.getIsSell(),
                    product.getIsBrowse(),
                    product.getPlaceOfManufacture(),
                    product.getIsActive(),
                    product.getHolderId(),
                    product.getCreatedDate(),
                    imageProduct.stream().map(p -> p.getUrlImage()).collect(Collectors.toList()),
                    rates,
                    reviewerQuantity,
                    categories.stream().map(c -> c.getName()).collect(Collectors.toList())
            );
        }).collect(Collectors.toList());
    }

    public ProductDTO findById(Integer id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            return null;
        }
        List<ImageProduct> imageProduct = imageProductRepository.findAllByProductId(product.getProductId());
        List<Category> categories = categoryRepository.findCategoriesByProductId(product.getProductId());
        Double rates = orderProductRepository.getAverageRateByProductId(product.getProductId());
        Integer reviewerQuantity = orderProductRepository.getNumberOfReviewsByProductId(product.getProductId());
        return new ProductDTO(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getStatus(),
                product.getIsSell(),
                product.getIsBrowse(),
                product.getPlaceOfManufacture(),
                product.getIsActive(),
                product.getHolderId(),
                product.getCreatedDate(),
                imageProduct.stream().map(p -> p.getUrlImage()).collect(Collectors.toList()),
                rates,
                reviewerQuantity,
                categories.stream().map(c -> c.getName()).collect(Collectors.toList())
        );
    }

    public List<ProductDTO> searchProducts(String name, String[] placeOfManufacture, Double minPrice, Double maxPrice, Integer categoryId) {
        List<Product> products = productRepository.searchProducts(name, placeOfManufacture, minPrice, maxPrice, categoryId);

        return products.stream().map(product -> {
            List<ImageProduct> imageProduct = imageProductRepository.findAllByProductId(product.getProductId());
            List<Category> categories = categoryRepository.findCategoriesByProductId(product.getProductId());
            Double rates = orderProductRepository.getAverageRateByProductId(product.getProductId());
            Integer reviewerQuantity = orderProductRepository.getNumberOfReviewsByProductId(product.getProductId());

            return new ProductDTO(
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getStatus(),
                    product.getIsSell(),
                    product.getIsBrowse(),
                    product.getPlaceOfManufacture(),
                    product.getIsActive(),
                    product.getHolderId(),
                    product.getCreatedDate(),
                    imageProduct.stream().map(p -> p.getUrlImage()).collect(Collectors.toList()),
                    rates,
                    reviewerQuantity,
                    categories.stream().map(c -> c.getName()).collect(Collectors.toList())
            );
        }).collect(Collectors.toList());
    }
}
