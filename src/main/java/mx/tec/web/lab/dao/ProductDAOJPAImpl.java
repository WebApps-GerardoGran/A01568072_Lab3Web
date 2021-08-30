/*
 * ProductDAOJPAImpl
 * Version 1.0
 * August 21, 2021 
 * Copyright 2021 Tecnologico de Monterrey
 */
package mx.tec.web.lab.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.tec.web.lab.mapper.ProductMapper;
import mx.tec.web.lab.vo.ProductVO;
import mx.tec.web.lab.repository.ProductRepository;
import mx.tec.web.lab.service.CommentsService;

/**
 * JPA Implementation for Product Data Access Object
 * @author Enrique Sanchez
 */
@Component("jpa")
public class ProductDAOJPAImpl implements ProductDAO {
	/** A reference to the Product Repository */
	@Resource
	private ProductRepository productRepository;
	
	/** A reference to the Product Mapper */
	@Resource
    private ProductMapper productMapper;	
	
	@Autowired
	CommentsService commentService;
	
	/** {@inheritDoc} */
	@Override
	public List<ProductVO> findAll() {
		List<ProductVO> productList = productMapper.convertToVOList(productRepository.findAll());
		for (ProductVO product: productList) {
			product.setComments(commentService.getComments());
		}
		return productList;
				
	}

	/** {@inheritDoc} */
	@Override
	public Optional<ProductVO> findById(long id) {
		return productMapper.convertToOptionalVO(productRepository.findById(id));
	}

	/** {@inheritDoc} */
	@Override
	public List<ProductVO> findByNameLike(final String pattern) {
		return productMapper.convertToVOList(productRepository.findByNameLike(pattern));
	}

	/** {@inheritDoc} */
	@Override
	public ProductVO insert(ProductVO newProduct) {
		return productMapper.convertToVO(productRepository.save(productMapper.convertToEntity(newProduct)));
	}

	/** {@inheritDoc} */
	@Override
	public void remove(ProductVO existingProduct) {
		productRepository.delete(productMapper.convertToEntity(existingProduct));
	}

	/** {@inheritDoc} */
	@Override
	public void update(ProductVO existingProduct) {
		productRepository.save(productMapper.convertToEntity(existingProduct));
	}
}
