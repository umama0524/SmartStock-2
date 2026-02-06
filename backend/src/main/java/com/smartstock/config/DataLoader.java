package com.smartstock.config;

import com.smartstock.entity.*;
import com.smartstock.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, ProductRepository productRepository,
                      InventoryRepository inventoryRepository, SupplierRepository supplierRepository,
                      CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.supplierRepository = supplierRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            loadUsers();
            loadProducts();
            loadInventory();
            loadSuppliers();
            loadCustomers();
            log.info("初期データの投入が完了しました");
        }
    }

    private void loadUsers() {
        // 管理者
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .name("管理者")
                .email("admin@smartstock.com")
                .role(User.UserRole.ADMIN)
                .isActive(true)
                .build();
        userRepository.save(admin);

        // 在庫担当
        User inventory = User.builder()
                .username("inventory")
                .password(passwordEncoder.encode("password"))
                .name("在庫 太郎")
                .email("inventory@smartstock.com")
                .role(User.UserRole.INVENTORY)
                .isActive(true)
                .build();
        userRepository.save(inventory);

        // 発注担当
        User purchase = User.builder()
                .username("purchase")
                .password(passwordEncoder.encode("password"))
                .name("発注 花子")
                .email("purchase@smartstock.com")
                .role(User.UserRole.PURCHASE)
                .isActive(true)
                .build();
        userRepository.save(purchase);

        // 販売担当
        User sales = User.builder()
                .username("sales")
                .password(passwordEncoder.encode("password"))
                .name("販売 花子")
                .email("sales@smartstock.com")
                .role(User.UserRole.SALES)
                .isActive(true)
                .build();
        userRepository.save(sales);

        log.info("ユーザーデータを投入しました");
    }

    private void loadProducts() {
        createProduct("SKU-001", "A4用紙（500枚）", BigDecimal.valueOf(250), 100, 50, "倉庫A-01");
        createProduct("SKU-045", "ボールペン（黒）", BigDecimal.valueOf(80), 150, 80, "倉庫A-02");
        createProduct("SKU-089", "クリアファイル", BigDecimal.valueOf(120), 200, 100, "倉庫B-12");
        createProduct("SKU-123", "ノート（B5）", BigDecimal.valueOf(180), 150, 80, "倉庫A-05");
        createProduct("SKU-156", "ホッチキス", BigDecimal.valueOf(450), 50, 30, "倉庫C-03");
        createProduct("SKU-201", "付箋紙セット", BigDecimal.valueOf(210), 300, 150, "倉庫B-08");
        createProduct("SKU-234", "マーカーペン（蛍光5色）", BigDecimal.valueOf(320), 100, 50, "倉庫A-03");
        createProduct("SKU-267", "カッターナイフ", BigDecimal.valueOf(150), 80, 40, "倉庫C-05");

        log.info("商品データを投入しました");
    }

    private void createProduct(String sku, String name, BigDecimal price, int reorderPoint, int safetyStock, String location) {
        Product product = Product.builder()
                .sku(sku)
                .productName(name)
                .unitPrice(price)
                .reorderPoint(reorderPoint)
                .safetyStock(safetyStock)
                .location(location)
                .unit("個")
                .isActive(true)
                .build();
        productRepository.save(product);
    }

    private void loadInventory() {
        createInventory("SKU-001", 45, 10);
        createInventory("SKU-045", 120, 20);
        createInventory("SKU-089", 580, 50);
        createInventory("SKU-123", 340, 40);
        createInventory("SKU-156", 25, 5);
        createInventory("SKU-201", 890, 90);
        createInventory("SKU-234", 200, 20);
        createInventory("SKU-267", 100, 10);

        log.info("在庫データを投入しました");
    }

    private void createInventory(String sku, int quantity, int reserved) {
        Product product = productRepository.findBySku(sku).orElseThrow();
        Inventory inventory = Inventory.builder()
                .product(product)
                .quantity(quantity)
                .reservedQuantity(reserved)
                .build();
        inventoryRepository.save(inventory);
    }

    private void loadSuppliers() {
        Supplier s1 = Supplier.builder()
                .name("オフィス用品株式会社")
                .contactPerson("山田太郎")
                .email("yamada@office-goods.co.jp")
                .phone("03-1234-5678")
                .address("東京都中央区日本橋1-1-1")
                .paymentTerms("月末締め翌月末払い")
                .isActive(true)
                .build();
        supplierRepository.save(s1);

        Supplier s2 = Supplier.builder()
                .name("文具センター")
                .contactPerson("鈴木花子")
                .email("suzuki@bungu-center.co.jp")
                .phone("03-2345-6789")
                .address("東京都港区芝公園2-2-2")
                .paymentTerms("前払い")
                .isActive(true)
                .build();
        supplierRepository.save(s2);

        Supplier s3 = Supplier.builder()
                .name("事務機器販売")
                .contactPerson("佐藤一郎")
                .email("sato@jimuki.co.jp")
                .phone("03-3456-7890")
                .address("東京都新宿区西新宿3-3-3")
                .paymentTerms("月末締め翌月末払い")
                .isActive(true)
                .build();
        supplierRepository.save(s3);

        log.info("サプライヤデータを投入しました");
    }

    private void loadCustomers() {
        Customer c1 = Customer.builder()
                .name("株式会社ABCコーポレーション")
                .contactPerson("佐藤一郎")
                .email("sato@abc-corp.co.jp")
                .phone("03-9876-5432")
                .address("東京都千代田区丸の内1-1-1")
                .shippingAddress("〒100-0001 東京都千代田区千代田1-1-1 ABCビル5F")
                .isActive(true)
                .build();
        customerRepository.save(c1);

        Customer c2 = Customer.builder()
                .name("有限会社XYZ商事")
                .contactPerson("田中花子")
                .email("tanaka@xyz-corp.co.jp")
                .phone("03-8765-4321")
                .address("東京都港区虎ノ門2-2-2")
                .shippingAddress("〒105-0001 東京都港区虎ノ門2-2-2 XYZビル3F")
                .isActive(true)
                .build();
        customerRepository.save(c2);

        Customer c3 = Customer.builder()
                .name("株式会社DEFエンタープライズ")
                .contactPerson("山田次郎")
                .email("yamada@def-enterprise.co.jp")
                .phone("03-7654-3210")
                .address("東京都渋谷区道玄坂3-3-3")
                .shippingAddress("〒150-0043 東京都渋谷区道玄坂3-3-3 DEFタワー10F")
                .isActive(true)
                .build();
        customerRepository.save(c3);

        log.info("顧客データを投入しました");
    }
}
