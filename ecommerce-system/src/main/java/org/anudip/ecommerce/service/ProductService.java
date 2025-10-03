package org.anudip.ecommerce.service;

import org.anudip.ecommerce.dao.ProductDAO;
import org.anudip.ecommerce.model.Product;

import java.util.List;

public class ProductService {
    private final ProductDAO dao = new ProductDAO();

    public int add(Product p) {
        return dao.addProduct(p);
    }

    public List<Product> listAll() { return dao.getAllProducts(); }

    public Product getById(int id) { return dao.getProductById(id); }

    public boolean update(Product p) { return dao.updateProduct(p); }

    public boolean delete(int productId) { return dao.deleteProduct(productId); }
}
