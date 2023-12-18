package dev.jay.rxjava;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ProductService {
    @GET("/products")
    Observable<List<ProductItem>> getProducts();
}
