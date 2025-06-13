package com.vlad.store.store_management.controller;

import com.vlad.store.store_management.model.Product;
import com.vlad.store.store_management.service.ProductService;
import com.vlad.store.store_management.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    private ProductController productController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = List.of(
                new Product(1L, "Prod1", 10.0),
                new Product(2L, "Prod2", 20.0));
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].name").value("Prod1"));
    }

    @Test
    void testGetProductById_Found() throws Exception {
        Product product = new Product(1L, "Prod1", 10.0);
        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Prod1"));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddProduct() throws Exception {
        Product productToAdd = new Product(null, "ProdNew", 30.0);
        Product createdProduct = new Product(10L, "ProdNew", 30.0);

        when(productService.addProduct(any(Product.class))).thenReturn(createdProduct);

        String productJson = """
                {
                    "name": "ProdNew",
                    "price": 30.0
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value("ProdNew"));
    }

    @Test
    void testUpdateProduct_Found() throws Exception {
        Product updatedProduct = new Product(1L, "ProdUpdated", 40.0);
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        String productJson = """
                {
                    "name": "ProdUpdated",
                    "price": 40.0
                }
                """;

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ProdUpdated"))
                .andExpect(jsonPath("$.price").value(40.0));
    }

    @Test
    void testUpdateProduct_NotFound() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(null);

        String productJson = """
                {
                    "name": "ProdUpdated",
                    "price": 40.0
                }
                """;

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        // deleteProduct nu returneaza nimic, doar arunca exceptie daca nu gaseste produsul
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdatePrice_Success() throws Exception {
        Product updated = new Product(1L, "Prod1", 99.99);
        when(productService.updateProductPrice(1L, 99.99)).thenReturn(updated);

        mockMvc.perform(patch("/api/products/1/price")
                        .param("price", "99.99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    void testUpdatePrice_NotFound() throws Exception {
        when(productService.updateProductPrice(1L, 99.99)).thenThrow(new RuntimeException());

        mockMvc.perform(patch("/api/products/1/price")
                        .param("price", "99.99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductByName_Found() throws Exception {
        Product product = new Product(1L, "SpecialProd", 50.0);
        when(productService.getProductByName("SpecialProd")).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/by-name/SpecialProd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SpecialProd"));
    }

    @Test
    void testGetProductByName_NotFound() throws Exception {
        when(productService.getProductByName("SpecialProd")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/by-name/SpecialProd"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchProducts_WithResults() throws Exception {
        List<Product> products = List.of(
                new Product(1L, "Apple", 15.0),
                new Product(2L, "Apple Juice", 20.0));
        when(productService.getProductsByCustomCriteria("Apple", 10.0)).thenReturn(products);

        mockMvc.perform(get("/api/products/search")
                        .param("namePart", "Apple")
                        .param("minPrice", "10.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testSearchProducts_NoResults() throws Exception {
        when(productService.getProductsByCustomCriteria("Apple", 10.0)).thenReturn(List.of());

        mockMvc.perform(get("/api/products/search")
                        .param("namePart", "Apple")
                        .param("minPrice", "10.0"))
                .andExpect(status().isNoContent());
    }
}
