package com.utn.tp.io.service;

import com.sun.source.tree.Tree;
import com.utn.tp.io.model.ModelType;
import com.utn.tp.io.model.Product;
import com.utn.tp.io.model.Sale;

import com.utn.tp.io.model.Supplier;

import com.utn.tp.io.model.ZoneProduct;

import com.utn.tp.io.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.*;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SaleService saleService;
    private final SupplierService supplierService;

    @Autowired
    public ProductService(ProductRepository productRepository, @Lazy SaleService saleService, SupplierService supplierService) {
        this.productRepository = productRepository;
        this.saleService = saleService;
        this.supplierService = supplierService;
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
        Date to = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        /*Calcula la fecha de hace 30 días*/
        Date from = Date.from(LocalDate.now().minusDays(quantityDays).atStartOfDay(ZoneId.systemDefault()).toInstant());

        /* Obtengo las ventas de los ultimos 30 días del producto*/
        List<Sale> salesInLastDays = saleService.getBetweenDatesAndProduct(from,to,product);
        if(salesInLastDays.size() > 0) {
            double avgDemand = Sale.calculateAvgDemand(salesInLastDays, quantityDays);


            double disDemand = Sale.calculateDisDemand(salesInLastDays,avgDemand);
            Double reorderPoint = null;

            if(product.getModelType().equals(ModelType.Q_MODEL)) {
                reorderPoint = Product.calculateReorderPoint(product); //si es null lanzar exception
            }

            System.out.println("Reorden point:"+product.getReorderPoint());

            product.setAvgDemand(avgDemand);
            product.setDisDemand(disDemand);
            product.setReorderPoint(reorderPoint);

            this.productRepository.save(product);
            determinateZoneABC();
        }


    }

    public List<Product> getBySupplier(Integer idSupplier){
        List<Product> products = new ArrayList<>();
        for (Product p : this.getAll()) {
            if(p.getSupplier().getId().equals(idSupplier)){
                products.add(p);
            }
        }
        return products;
    }


    public List<Product> getToCheckModelP() {
        List<Supplier> revisionSuppliers = new ArrayList<Supplier>();
        List<Product> productsToCheck = new ArrayList<>();

        //Guardo en una lista a todos los proveedores que están en período de revision
        for (Supplier s : supplierService.getAll()) {
            if (s.isRevisonPeriod()) {
                revisionSuppliers.add(s);
            }
        }

        //Guardo en una lista a todos los productos que pertenecen a un proveedor que está en período de revisión
        for (Product p : this.getAll()) {
            if (p.getModelType() == ModelType.P_MODEL && revisionSuppliers.contains(p.getSupplier())) {
                productsToCheck.add(p);
            }
        }
        return productsToCheck;
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
            cavPerProduct.put(product.getCostUnit() * product.getAvgDemand() * 365, product);   //Calculo CAV
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
        if (p.getZone() == ZoneProduct.ZONE_C || (p.getZone() == null && p.getSupplier().getIsPresale()) ) {
            return ModelType.P_MODEL;
        }
        return ModelType.Q_MODEL;
    }

    public void updateProduct(Integer id, Product upd) {
        //Product p = getById(id);

        /*p.setScan(upd.getScan());
        p.setModel(upd.getModel());
        p.setDescription(upd.getDescription());
        p.setSupplier(upd.getSupplier());
        p.setCostUnit(upd.getCostUnit());
        p.setStock(upd.getStock());
        p.setModelType(upd.getModelType());
        p.setServiceLevel(upd.getServiceLevel());
        p.setAvgDemand(upd.getAvgDemand());
        p.setDisDemand(upd.getAvgDemand());
        p.setReorderPoint(upd.getReorderPoint());*/

        productRepository.save(upd);
    }

    public List<Product> getToCheckModelQ() {

        List<Product> toOrder = new ArrayList<>();

        for(Product p : this.getAll()){
            if (p.getStock() <= p.getReorderPoint()){
                toOrder.add(p);
            }
        }
        return toOrder;
    }
}
