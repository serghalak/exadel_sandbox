package com.exadel.sandbox.controllers;

import com.exadel.sandbox.dto.ProductDto;
import com.exadel.sandbox.dto.pagelist.ProductPagedList;
import com.exadel.sandbox.service.ProductService;
import com.exadel.sandbox.ui.mappers.UIProductMapper;
import com.exadel.sandbox.ui.pagelist.ProductResponsePagedList;
import com.exadel.sandbox.ui.request.ProductRequest;
import com.exadel.sandbox.ui.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/")
@RestController
public class ProductController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 3;
    private static final String DEFAULT_FIELD_SORT = "name";

    private final ProductService productService;
    private final UIProductMapper uiProductMapper;

    @DeleteMapping(path = {"product/{productId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("productId") Long productId) {

        log.debug(">>>>>>>>>>controller delete product by Id");
        log.debug(">>>>>>>>this method is not implemented yet");
        //productService.deleteProductById(productId);
    }

    @PutMapping(path = {"product/{productId}"}, produces = {"application/json"})
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("productId") Long productId,
                                                         @Valid @RequestBody ProductRequest productRequest) {

        if (productRequest.getName() == null || productRequest.getName().equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var productDto = uiProductMapper.productRequestToProductDto(productRequest);
        ProductDto updateProduct = productService.updateProduct(productId, productDto);
        var productResponse = uiProductMapper.productDtoToProductResponse(updateProduct);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping(produces = {"application/json"}, path = "productname/{name}")
    public ResponseEntity<ProductPagedList> getProductsByPartOfName(
            @PathVariable("name") String productName,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        log.debug(">>>>>>>>>>Product List by part of name: " + productName);

        pageNumber = getPageNumber(pageNumber);
        pageSize = getPageSize(pageSize);


        ProductPagedList productList = productService.listProductsByPartOfName(productName,
                PageRequest.of(pageNumber, pageSize, Sort.by(DEFAULT_FIELD_SORT).ascending()));

        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping(produces = {"application/json"}, path = "product/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("productId") Long productId) {

        log.debug(">>>>>>getProductById " + productId);

        var productDto = productService.findProductById(productId);
        var productResponse = uiProductMapper.productDtoToProductResponse(productDto);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping(produces = {"application/json"}, path = "product")
    public ResponseEntity<ProductResponsePagedList> listProducts(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        log.debug(">>>>List all products");

        pageNumber = getPageNumber(pageNumber);
        pageSize = getPageSize(pageSize);

        Page<ProductDto> productDtoPage = productService.listProducts(
                PageRequest.of(pageNumber, pageSize, Sort.by(DEFAULT_FIELD_SORT).ascending()));

        var productResponses = getProductResponsePagedList(productDtoPage);

        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }

    @PostMapping(produces = {"application/json"},
            consumes = {"application/json"},
            path = "product/create")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {

        String productName = productRequest.getName();

        if (productName == null || productName.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (productService.isProductNameExists(productName)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var productDto = uiProductMapper.productRequestToProductDto(productRequest);
        var savedProductDto = productService.saveProduct(productDto);
        var productResponse = uiProductMapper.productDtoToProductResponse(savedProductDto);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    private int getPageNumber(Integer pageNumber) {
        return pageNumber == null || pageNumber < 0 ? DEFAULT_PAGE_NUMBER : pageNumber;
    }

    private int getPageSize(Integer pageSize) {
        return pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    private ProductResponsePagedList getProductResponsePagedList(Page<ProductDto> productDtoPage) {
        return new ProductResponsePagedList(
                productDtoPage.getContent().stream()
                        .map(uiProductMapper::productDtoToProductResponse)
                        .collect(Collectors.toList()),
                PageRequest.of(productDtoPage.getPageable().getPageNumber(),
                        productDtoPage.getPageable().getPageSize(),
                        productDtoPage.getPageable().getSort()),
                productDtoPage.getTotalElements());
    }
}
