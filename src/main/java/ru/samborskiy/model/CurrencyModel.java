package ru.samborskiy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table
public class CurrencyModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String nameUser;
    @Column(name = "`from`")
    private String from;
    @Column(name = "`to`")
    private String to;
    private Integer amount;
    private Double rate;
    private String date;
    private Double result;

    @Override
    public String toString() {
        return new StringBuilder().append("На ").append(this.date).append("\n")
                .append("Курс = ").append(String.format("%.2f", this.rate)).append("\n")
                .append("Сумма ").append(this.amount).append(" ").append(this.from).append(" = ")
                .append(String.format("%.2f",this.result)).append(" ").append(this.to).toString();
    }
}
