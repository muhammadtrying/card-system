package uz.muhammadtrying.cardsystem.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.muhammadtrying.cardsystem.entity.enums.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Transaction {
    @NotBlank
    private Type type;
    @NotNull
    @Min(1)
    private Integer amount;
    private LocalDateTime createdAt;
    private UUID cardId;
}
