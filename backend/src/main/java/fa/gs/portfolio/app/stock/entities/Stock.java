/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fa.gs.portfolio.app.stock.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Fabio A. González Sosa
 */
@Entity
@Table(name = "producto", schema = "app")
@Data
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "id_producto", nullable = false)
    @Basic(optional = false)
    private Integer idProducto;

    @Column(name = "descripcion", nullable = false)
    @Basic(optional = false)
    private Integer precio;

    @Column(name = "categoria", nullable = false)
    @Basic(optional = false)
    private Integer stock;

}
