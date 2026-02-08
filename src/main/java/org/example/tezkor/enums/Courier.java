package org.example.tezkor.enums;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "couriers")
@Getter
@Setter
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // Kuryer foydalanuvchisi (login va parol shu yerda)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportType transportType; // Transport turi

    private Boolean available = true; // Hozir ishlayaptimi (zakaz qabul qila oladimi)
    private Boolean isActive = true;  // Kuryer faolmi (owner tomonidan)

    // Kuryer hozirgi joylashuvi
    private Double currentLatitude;
    private Double currentLongitude;

    // 🔥 STATISTIKA
    private Integer totalDeliveries = 0;  // Jami yetkazib berilgan zakazlar
    private Long totalWorkMinutes = 0L;   // Jami ishlagan daqiqalar
    private Double totalEarnings = 0.0;   // Jami daromad

    // 🥚 Easter Egg: Kuryer transport turini tekshirish
    @PostLoad
    @PostPersist
    public void checkTransportEasterEgg() {
        if (this.transportType == TransportType.PIYODA && this.user != null) {
            System.out.println("🚶 Piyoda kuryer qo'shildi! Sog'lom turmush tarzi tarafdori! 💪");
        } else if (this.transportType == TransportType.ELEKTRO_SKUTER) {
            System.out.println("⚡ Elektro skuter! Kelajak hozirdan boshlandi! 🚀");
        } else if (this.transportType == TransportType.MASHINA && this.user != null
                && this.user.getFullname() != null
                && this.user.getFullname().toLowerCase().contains("fast")) {
            System.out.println("🏎️ Fast and Furious! Bu kuryer juda tez yetkazib beradi! 😎");
        }

        // 🥚 Bonus Easter Egg: Erkin kuryer
        if (this.available && this.user != null) {
            System.out.println("🦅 ERKIN KURYER! Barcha do'konlardan zakaz qabul qiladi! 🌍");
        }
    }
}