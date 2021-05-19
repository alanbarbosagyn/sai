package br.com.abce.sai.persistence.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Corretor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	@Setter
	private Long id;

	@Column(nullable = false, scale = 10)
	@Getter
	@Setter
	private Long numCreci;

	@Column(nullable = false, scale = 10)
	@Getter
	@Setter
	private int tipo;
}
