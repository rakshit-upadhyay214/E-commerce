package domain.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import domain.users.Client;


public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private Double price;
	private String description;


	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Client buyerOfTheProduct;

	public ProductDTO() {

	}

	public ProductDTO(Integer id, String name, Double price, Client buyerOfTheProduct, String description) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.buyerOfTheProduct = buyerOfTheProduct;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Client getBuyerOfTheProduct() {
		return buyerOfTheProduct;
	}

	public void setBuyerOfTheProduct(Client buyerOfTheProduct) {
		this.buyerOfTheProduct = buyerOfTheProduct;
	}
	
	 
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static boolean isSold(ProductDTO obj) {
    	
    	if(obj.getBuyerOfTheProduct() == null) {
    		return false;
    	}
    	return true;
    }



}
