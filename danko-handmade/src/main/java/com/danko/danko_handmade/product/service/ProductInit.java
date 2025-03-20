package com.danko.danko_handmade.product.service;

import com.danko.danko_handmade.product.model.Product;
import com.danko.danko_handmade.product.model.ProductSection;
import com.danko.danko_handmade.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProductInit implements CommandLineRunner {
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductInit(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!productService.getAll().isEmpty()) {
            return;
        }

        String description1 = readDescriptionFromFile("product1.txt");
        String description2 = readDescriptionFromFile("product2.txt");
        String description3 = readDescriptionFromFile("product3.txt");
        String description4 = readDescriptionFromFile("product4.txt");
        String description5 = readDescriptionFromFile("product5.txt");
        String description6 = readDescriptionFromFile("product6.txt");
        String description7 = readDescriptionFromFile("product7.txt");
        String description8 = readDescriptionFromFile("product8.txt");
        String description9 = readDescriptionFromFile("product9.txt");
        String description10 = readDescriptionFromFile("product10.txt");

        Product initialProduct1 = Product.builder()
                .listingTitle("Unique Handmade Fish Mug - Wheel Thrown Stoneware Teacup - Artistic Pottery by Danko - Perfect Kitchen Decor or Gift!")
                .description(description1)
                .price(new BigDecimal("42"))
                .productCode("C&M-1001")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742466219/jwkiyem6bcvfz8vd1zgh.jpg")
                .additionalPhotosUrls(List.of(
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742466220/bijjbitqkqgfhjhsr5nz.jpg",
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742466221/k0npcqyu534wht4jiln1.jpg",
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742466222/imnigfy8lhpv5dvnweho.jpg",
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742466223/n1ubofn7wk08mjqyrjy1.jpg",
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742466224/pj3ij7y15mydqp9faeaw.jpg"
                ))
                .productSection(ProductSection.CUPS_AND_MUGS)
                .addedOn(LocalDateTime.now())
                .stockQuantity(5)
                .active(true)
                .itemsSold(18)
                .build();

        productRepository.save(initialProduct1);

        Product initialProduct2 = Product.builder()
                .listingTitle("Handmade Grumpy Pumpkin and Bat Halloween Mug - 20 oz Ceramic Coffee Cup - Funny Jack O' Lantern Gift - Cute Fall Decor")
                .description(description2)
                .price(new BigDecimal("45"))
                .productCode("HAllO-1002")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742458976/awhpcf96cgbhh1iaarwu.png")
                .additionalPhotosUrls(List.of(
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742458979/oc9gu3ofostztkyc9bc1.png",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742458981/yjmovl868odwa71rh9aq.png",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742458983/pmni4pntl93wgp1i5ldi.png",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742458985/p6l1sih4n9xwk9obtqhu.png",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742458988/nk7fvn5kifo9dot0phy4.png"
                ))
                .productSection(ProductSection.HALLOWEEN)
                .addedOn(LocalDateTime.now())
                .stockQuantity(10)
                .active(true)
                .itemsSold(1)
                .build();

        productRepository.save(initialProduct2);

        Product initialProduct3 = Product.builder()
                .listingTitle("Dragonflies - Handmade Porcelain Teapot for Two - Wheel-Thrown Handle, Ceramic Strainer - Wedding Gift - Unique Tea Pot White Blue Orange")
                .description(description3)
                .price(new BigDecimal("106"))
                .productCode("TEA-1003")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742460014/eplaetl4qrjbohcvmndb.avif")
                .additionalPhotosUrls(List.of(
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742460016/t6whjc1lyaakof7hnb8w.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742460017/kmgs5ldoejw4yx4zm5vs.avif",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742460018/ryem8rattjxqa5wmpmr8.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742460020/sg2hv6htxonl2tds95un.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742460021/wo5edgesvlfzrrk0buhh.jpg"
                ))
                .productSection(ProductSection.TEAPOTS)
                .addedOn(LocalDateTime.now())
                .stockQuantity(1)
                .active(true)
                .itemsSold(0)
                .build();

        productRepository.save(initialProduct3);

        Product initialProduct4 = Product.builder()
                .listingTitle("Handmade Stoneware Sugar Bowl with Fish - Unique Pottery, Wedding Gift, Kitchen Canister, Serving Bowl, White, Blue, Orange, Black Colors")
                .description(description4)
                .price(new BigDecimal("57"))
                .productCode("SCC-1004")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742465365/rwi84oz9vfex7avhvr75.jpg")
                .additionalPhotosUrls(List.of(
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742465366/jyvjjbxhxfe5lrepza2j.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742465367/kavsndvtc4onvkpkunba.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742465368/gbbo2eylauddsvsikvbq.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742465369/jnot4e9fmezvceuxyxb3.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742465370/dgvfkp0thmcj5zjirdqu.jpg"
                ))
                .productSection(ProductSection.SUGAR_BOWLS)
                .addedOn(LocalDateTime.now())
                .stockQuantity(10)
                .active(true)
                .itemsSold(2)
                .build();

        productRepository.save(initialProduct4);

        Product initialProduct5 = Product.builder()
                .listingTitle("Handmade Ceramic Water Pitcher - Unique Green, Beige, Blue, Ochre Jug - Art Pottery for Home & Living - Perfect Wedding or Anniversary Gift")
                .description(description5)
                .price(new BigDecimal("93"))
                .productCode("PJB-1005")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742461004/m48euydhfrzv6om1dvbx.jpg")
                .additionalPhotosUrls(List.of(
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461006/c4lobqmhhveyy4xzlw2t.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461007/nfqtp3selgtl9gslef73.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461009/jvogkusebzwkkos8ykcg.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461010/ndp2qw4srylqwf4d78uf.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461011/lcygkcg3mpxeuidimtba.jpg"
                ))
                .productSection(ProductSection.PITCHERS)
                .addedOn(LocalDateTime.now())
                .stockQuantity(3)
                .active(true)
                .itemsSold(5)
                .build();

        productRepository.save(initialProduct5);

        Product initialProduct6 = Product.builder()
                .listingTitle("Hand-Painted Ceramic Christmas Gift Set – Bottle and Two Mugs, Danko Pottery, Red Beige Colors, Perfect Gift for Two")
                .description(description6)
                .price(new BigDecimal("99"))
                .productCode("SET-1006")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742461547/udxcgdvq7drveil7zqpg.jpg")
                .additionalPhotosUrls(List.of(
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461548/qxtilz4umrk1nd5oulbg.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461549/mhzxfvvwkttbx1z23p4h.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461551/rrxmetfg3mperr6c3zl2.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461552/jnecu3yfejfja6xxuw2q.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461553/m4lu4zoy81yqotqjvytm.jpg"
                ))
                .productSection(ProductSection.SETS)
                .addedOn(LocalDateTime.now())
                .stockQuantity(6)
                .active(true)
                .itemsSold(4)
                .build();

        productRepository.save(initialProduct6);

        Product initialProduct7 = Product.builder()
                .listingTitle("Handpainted Porcelain Tray with Mackerel Fish - Multi-Use Dish for Tapas, Fruit, and More! | Ceramic Art Gift Bowl")
                .description(description7)
                .price(new BigDecimal("85"))
                .productCode("TPW-1007")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742461978/gngfpgyfi7znixtgqk5o.avif")
                .additionalPhotosUrls(List.of(
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461979/pphegfyf7qo3xxzijyza.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461981/rj6qjufqcitaxmu0c8m6.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461982/whbv0lsbjy2n4paxz4tg.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461983/d4nqssgjdh8majgvb65b.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742461985/iy1kme1lbol9fypt3ty8.jpg"
                ))
                .productSection(ProductSection.TRAYS)
                .addedOn(LocalDateTime.now())
                .stockQuantity(18)
                .active(true)
                .itemsSold(6)
                .build();

        productRepository.save(initialProduct7);

        Product initialProduct8 = Product.builder()
                .listingTitle("Decorative Blue and White Ceramic Teapot with Iron Accents - Hand Painted Decor - Pottery Pitcher - Unique Home Decor - Wedding Gift")
                .description(description8)
                .price(new BigDecimal("165"))
                .productCode("HOME-1008")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742462426/tg7f0gh63cdniwwn3h53.jpg")
                .additionalPhotosUrls(List.of(
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462428/ejycg5hsrmgjcnalx2bi.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462429/r4sb82n9txp2vosqhh6x.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462430/gbg52p6lnod9zv6friyz.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462432/xvfslvu0adzzt4kmp7q1.jpg",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462433/nw1bjvh5njaptpadqnaa.jpg"
                ))
                .productSection(ProductSection.HOME_DECOR)
                .addedOn(LocalDateTime.now())
                .stockQuantity(2)
                .active(true)
                .itemsSold(9)
                .build();

        productRepository.save(initialProduct8);

        Product initialProduct9 = Product.builder()
                .listingTitle("Parque das Nações - Hand-painted Porcelain Tile Triptych - Lisbon Azulejo Pottery Wall Decor - Set of 3 - Blue, Black, Orange, White")
                .description(description9)
                .price(new BigDecimal("86"))
                .productCode("TILE-1009")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742462776/xmmpdabx2w2sus7thfv8.webp")
                .additionalPhotosUrls(List.of(
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462777/ehsl5wa30bdhhaz3ywea.webp",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462779/qhnhafpl6vzlvymzghmt.webp",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462780/mv752rlmcaf0wacibti2.webp",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462781/xvxiktyp4fibzterbzfu.webp",
                        "https://res.cloudinary.com/dsqhkts82/image/upload/v1742462782/arog5ucre8cqnyhfzflu.webp"
                ))
                .productSection(ProductSection.TILES)
                .addedOn(LocalDateTime.now())
                .stockQuantity(20)
                .active(true)
                .itemsSold(70)
                .build();

        productRepository.save(initialProduct9);

        Product initialProduct10 = Product.builder()
                .listingTitle("Hand-Painted Cat Mug - Unique Pottery Coffee Cup for Cat Lovers - Red, Green, Beige, Black - 380ml - Dishwasher & Microwave Safe")
                .description(description10)
                .price(new BigDecimal("41"))
                .productCode("C&M-1010")
                .mainPhotoUrl("https://res.cloudinary.com/dsqhkts82/image/upload/v1742463057/epoodlwgmmcxrli7linu.jpg")
                .additionalPhotosUrls(List.of(
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742463058/mamuy1jee9gtxymohixv.jpg",
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742463060/dfmzyq1gsz9jmiama1r2.jpg",
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742463061/r3qzdgmrqt9ldokwbvpm.jpg",
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742463063/pz6m2i8geqavd4whzdv7.jpg",
                       "https://res.cloudinary.com/dsqhkts82/image/upload/v1742463064/c2tgi8j928wclo2dcc76.jpg"
                ))
                .productSection(ProductSection.CUPS_AND_MUGS)
                .addedOn(LocalDateTime.now())
                .stockQuantity(60)
                .active(true)
                .itemsSold(120)
                .build();

        productRepository.save(initialProduct10);
    }


    private String readDescriptionFromFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("InitialProductDescriptions/" + fileName);
        Path path = resource.getFile().toPath();
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
