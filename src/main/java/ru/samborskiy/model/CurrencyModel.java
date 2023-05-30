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
    private String nameUser;
    private String currencyFrom;
    private String currencyTo;
    private Integer amount;
    private Double rate;
    private String date;
    private Double result;

    @Override
    public String toString() {
        return new StringBuilder().append("На ").append(this.date).append("\n")
                .append("Курс = ").append(String.format("%.2f", this.rate)).append("\n")
                .append("Сумма ").append(this.amount).append(" ").append(this.currencyFrom).append(" = ")
                .append(String.format("%.2f",this.result)).append(" ").append(this.currencyTo).toString();
    }
}
