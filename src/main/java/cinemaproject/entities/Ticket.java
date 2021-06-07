package cinemaproject.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Ticket  {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 70)
	private String nomClient;
	private double prix;
	@Column(unique = true, nullable = true)
	private Integer codePayment;
	private boolean reserver;
	@ManyToOne()
	private Place place;
	@ManyToOne
	private Projection projection;
	
}
