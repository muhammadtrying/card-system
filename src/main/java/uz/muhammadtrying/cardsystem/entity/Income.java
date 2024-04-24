package uz.muhammadtrying.cardsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Income extends BaseEntity {
    @ManyToOne
    @NotNull
    private Card card;

    @Builder
    public Income(UUID id, LocalDateTime createdAt, Integer amount, Card card) {
        super(id, createdAt, amount);
        this.card = card;
    }
}
