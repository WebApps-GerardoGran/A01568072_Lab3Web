/*
 * ProductDAOJdbcImpl
 * Version 1.0
 * August 21, 2021 
 * Copyright 2021 Tecnologico de Monterrey
 */
package mx.tec.web.lab.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import mx.tec.web.lab.service.CommentsService;
import mx.tec.web.lab.vo.ProductVO;
import mx.tec.web.lab.vo.SkuVO;

/**
 * @author Enrique Sanchez
 *
 */
@Component("jdbc")
public class ProductDAOJdbcImpl implements ProductDAO {
	/** Id field **/
	public static final String ID = "id";
	
	/** Name field **/
	public static final String NAME = "name";
	
	/** Description field **/
	public static final String DESCRIPTION = "description";

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CommentsService commentService;
	
	@Override
	public List<ProductVO> findAll() {
		String sql = "SELECT id, name, description FROM Product";

		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			List<ProductVO> list = new ArrayList<>();

			while(rs.next()){
				ProductVO product = new ProductVO(
					rs.getLong(ID),
					rs.getString(NAME), 
					rs.getString(DESCRIPTION), 
					this.findChildSkus(rs.getLong(ID)),
					commentService.getComments()
				);

				list.add(product);
			}
			
			return list;
		});
	}

	@Override
	public Optional<ProductVO> findById(long id) {
        String sql = "SELECT id, name, description FROM Product WHERE id = ?";
        
		return jdbcTemplate.query(sql, new Object[]{id}, new int[]{java.sql.Types.INTEGER}, (ResultSet rs) -> {
			Optional<ProductVO> optionalProduct = Optional.empty();

			if(rs.next()){
				ProductVO product = new ProductVO(
					rs.getLong(ID),
					rs.getString(NAME), 
					rs.getString(DESCRIPTION), 
					new ArrayList<>(),
					commentService.getComments()
				);
				
				optionalProduct = Optional.of(product);
			}
			
			return optionalProduct;
		});
	}

	@Override
	public List<ProductVO> findByNameLike(String pattern) {
		String sql = "SELECT id, name, description FROM product WHERE name like ?";

		return jdbcTemplate.query(sql, new Object[]{"%" + pattern + "%"}, new int[]{java.sql.Types.VARCHAR}, (ResultSet rs) -> {
			List<ProductVO> list = new ArrayList<>();

			while(rs.next()){
				ProductVO product = new ProductVO(
					rs.getLong(ID),
					rs.getString(NAME), 
					rs.getString(DESCRIPTION), 
					new ArrayList<>(),
					commentService.getComments()
				);
				
				list.add(product);
			}
			
			return list;
		});
	}

	@Override
	public ProductVO insert(ProductVO newProduct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(ProductVO existingProduct) {
		String sql = "DELETE FROM Sku WHERE parentProduct_id = " + existingProduct.getId();
		jdbcTemplate.update(sql);

		sql = "DELETE FROM Product WHERE id = " + existingProduct.getId();
		jdbcTemplate.update(sql);
	}

	@Override
	public void update(ProductVO existingProduct) {
		// TODO Auto-generated method stub

	}
	
	public List<SkuVO> findChildSkus(final long id){
		String sql = "SELECT * FROM Sku WHERE parentProduct_id = " + id;
        
		return jdbcTemplate.query(sql, (ResultSet rs) -> {
			List<SkuVO> list = new ArrayList<>();

			while(rs.next()){
				SkuVO sku = new SkuVO(
						rs.getLong(ID),
						rs.getString("color"),
						rs.getString("size"),
						rs.getDouble("listPrice"),
						rs.getDouble("salePrice"),
						rs.getLong("quantityOnHand"),
						rs.getString("smallImageUrl"),
						rs.getString("mediumImageUrl"),
						rs.getString("largeImageUrl")
				);

				list.add(sku);
			}
			
			return list;
		});

	}

}
