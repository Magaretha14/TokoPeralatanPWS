/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.a.TokoPeralatan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.PageAttributes;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@CrossOrigin
public class myController {
    
    Daftarbarang brg = new Daftarbarang();
    DaftarbarangJpaController ctrl = new DaftarbarangJpaController();
    
    //List<Daftarbarang> barangList = new ArrayList<Daftarbarang>();
    
    @GetMapping(value = "/GET", produces = APPLICATION_JSON_VALUE)//Menampilkan data
    public List<Daftarbarang> viewAll(){
        try{
            List<Daftarbarang> buffer = new ArrayList<>();
            buffer = ctrl.findDaftarbarangEntities();
            
            return buffer; //return data jika data tersedia
        } catch (Exception e) {
            return List.of(); //data tidak tersedia
        }
    }
    
    @PostMapping(value = "/POST", consumes = APPLICATION_JSON_VALUE)//Menambahkan data
    public String postBarang(HttpEntity<String> kirimdata) throws JsonProcessingException//mengambil data dari tabel daftarbarang
    {
        String feedback = "Do Nothing";
        ObjectMapper mapper =new ObjectMapper();
        brg = mapper.readValue(kirimdata.getBody(), Daftarbarang.class);
        try{
            ctrl.create(brg);
            feedback = brg.getNama() + " Saved";
        }catch(Exception e){
            feedback = e.getMessage();
        }
        return feedback;
    }
    
    /*@GetMapping("/{id}")//Menampilkan data menggunakan id
    public List<Daftarbarang> viewDatabyId(@PathVariable("id") int id){
        try{
            brg = ctrl.findDaftarbarang(id); //get data from entity
            barangList.clear(); //clear data in list
            barangList.add(brg); //fill list
            return barangList; //show data
        } catch (Exception e){
            return List.of(); //data is empty
        }
    }*/
    
    //Mengedit data
    @PutMapping(value = "/PUT", consumes = APPLICATION_JSON_VALUE)
    public String editData(HttpEntity<String> kirimdata) throws JsonProcessingException {
        
        String feedback = "Do Nothing";
        ObjectMapper mapper = new ObjectMapper();
        brg = mapper.readValue(kirimdata.getBody(), Daftarbarang.class);
        try{
            ctrl.edit(brg);
            feedback = brg.getNama() + " Edited";
        } catch (Exception e){
            feedback = e.getMessage();
        }
        
        return feedback;
    }
    
    //Menghapus data  
    @DeleteMapping(value = "/DELETE", consumes = APPLICATION_JSON_VALUE)
    public String delete(HttpEntity<String> kirimdata) throws JsonProcessingException{
        
        String feedback = "Do Nothing";
        ObjectMapper mapper = new ObjectMapper();
        brg = mapper.readValue(kirimdata.getBody(), Daftarbarang.class);
        try{
            ctrl.destroy(brg.getId());
            feedback = "Data has been Deleted";
        }catch (Exception e){
            feedback = e.getMessage();
        }
        
        return feedback;
    }
}
    

