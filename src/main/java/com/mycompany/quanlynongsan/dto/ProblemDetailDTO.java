// ProblemDetailDTO.java
package com.mycompany.quanlynongsan.dto;

import com.mycompany.quanlynongsan.model.Problem;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.model.User;

public class ProblemDetailDTO {

    private Problem problem;
    private Product product;
    private User user;

    public ProblemDetailDTO(Problem problem, Product product, User user) {
        this.problem = problem;
        this.product = product;
        this.user = user;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
