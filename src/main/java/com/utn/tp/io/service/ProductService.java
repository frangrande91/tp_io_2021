package com.utn.tp.io.service;

import com.sun.source.tree.Tree;
import com.utn.tp.io.model.ModelType;
import com.utn.tp.io.model.Product;
import com.utn.tp.io.model.Sale;
import com.utn.tp.io.model.ZoneProduct;
import com.utn.tp.io.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SaleService saleService;

    @Autowired
    public ProductService(ProductRepository productRepository, @Lazy SaleService saleService) {
        this.productRepository = productRepository;
        this.saleService = saleService;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product add(Product product) {
        return productRepository.save(product);
    }

    public Product getById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    public Product getByScan(String scan) {
        return productRepository.findByScan(scan);
    }

    public void updateStock(String scan, Integer movement) {
        Product p = getByScan(scan);
        p.setStock(p.getStock() + movement);
        if (p.getStock() < 0) {
            p.setStock(0);
        }
        productRepository.save(p);
    }

    public void updateLastDays(String scan, Integer quantityDays) {
        /*Obtengo el producto*/
        Product product = getByScan(scan);

        /*Calcula la fecha de hoy*/
        Date from = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        /*Calcula la fecha de hace 30 días*/
        Date to = Date.from(LocalDate.now().minusDays(quantityDays).atStartOfDay(ZoneId.systemDefault()).toInstant());

        /* Obtengo las ventas de los ultimos 30 días del producto*/
        List<Sale> salesInLastDays = saleService.getBetweenDatesAndProduct(from,to,product);

        double avgDemand = Sale.calculateAvgDemand(salesInLastDays, quantityDays);
        double disDemand = Sale.calculateDisDemand(salesInLastDays,avgDemand);
        double reorderPoint = Product.calculateReorderPoint(product);

        product.setAvgDemand(avgDemand);
        product.setDisDemand(disDemand);
        product.setReorderPoint(reorderPoint);

        this.productRepository.save(product);
    }

    public void determinateZoneABC(){

        List<Product> products = getAll();

        //TreeMaps para que las inserciones sean ordenadas me menor a mayor
        TreeMap<Double, Product> cavPerProduct = new TreeMap<>();
        Map.Entry<Double, Product> pair = null;
        TreeMap<Double, Product> percentProduct = new TreeMap<>();
        Product p;
        Double cava = 0D;
        Double aux = 0D;

        //Calculo del CAV de cada producto y CAVA
        for(Product product : products){
            cava = cava + product.getCostUnit() * product.getAvgDemand();
            cavPerProduct.put(product.getCostUnit() * product.getAvgDemand() * 300, product);   //Calculo CAV
        }

        //Calculo del porcentaje de cada producto sobre el CAVA
        while (cavPerProduct.size() > 0) {
            pair = cavPerProduct.pollLastEntry();       //Retorna el último elemento (mayor) y lo elimina
            percentProduct.put((pair.getKey()/cava) * 100, pair.getValue());
        }

        //Clasificación de cada producto
        while (percentProduct.size() > 0) {
            pair = percentProduct.pollLastEntry();

            if (aux < 0.85){        //Los productos que representan el 85% del CAVA pertenecen a la zona A

                p = pair.getValue();
                p.setZone(ZoneProduct.ZONE_A);
                productRepository.save(p);
                aux = aux + pair.getKey();

            }else if (aux < 0.90){  //%5 porciento a la zona B

                p = pair.getValue();
                p.setZone(ZoneProduct.ZONE_B);
                productRepository.save(p);
                aux = aux + pair.getKey();

            }else{                 //%10 restante a la zona C
                p = pair.getValue();
                p.setZone(ZoneProduct.ZONE_C);
                productRepository.save(p);
            }
        }
    }

    public ModelType suggestedModel(Integer id){
        Product p = getById(id);
        p.getZone();
        if (p.getZone() == ZoneProduct.ZONE_C || (p.getZone() == null && p.getSupplier().isPresale()) ) {
            return ModelType.P_MODEL;
        }
        return ModelType.Q_MODEL;
    }

    public void updateProduct(Integer id, Product upd) {
        Product p = getById(id);

        p.setScan(upd.getScan());
        p.setModel(upd.getModel());
        p.setDescription(upd.getDescription());
        p.setSupplier(upd.getSupplier());
        p.setCostUnit(upd.getCostUnit());
        p.setStock(upd.getStock());
        p.setModelType(upd.getModelType());
        p.setServiceLevel(upd.getServiceLevel());
        p.setAvgDemand(upd.getAvgDemand());
        p.setDisDemand(upd.getAvgDemand());
        p.setReorderPoint(upd.getReorderPoint());
        productRepository.save(p);
    }
}
