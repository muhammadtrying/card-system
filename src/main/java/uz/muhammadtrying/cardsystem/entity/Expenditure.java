package uz.muhammadtrying.cardsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Expenditure extends BaseEntity {
    @ManyToOne
    @NotNull
    private Card card;

    @Builder
    public Expenditure(UUID id, LocalDateTime createdAt, Integer amount, Card card) {
        super(id, createdAt, amount);
        this.card = card;
    }
}
