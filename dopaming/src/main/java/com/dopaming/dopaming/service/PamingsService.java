package com.dopaming.dopaming.service;

import com.dopaming.dopaming.domain.Pamings;
import com.dopaming.dopaming.repository.PamingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PamingsService {
    @Autowired
    private PamingsRepository pamingsRepository;

    public List<Pamings> getAllPamings(){
        return pamingsRepository.findAll();
    }

    public Pamings getPamingById(Long id){
        return pamingsRepository.findById(id).orElse(null);
    }

    public Pamings createPaming(Pamings pamings){
        return pamingsRepository.save(pamings);
    }

    public Pamings updatePaming(Long id, Pamings updatePaming){
        Optional<Pamings> pamingsOptional = pamingsRepository.findById(id);
        if(pamingsOptional.isPresent()){
            Pamings pamings = pamingsOptional.get();
            pamings.setPaming_title(updatePaming.getPaming_title());
            pamings.setStart_date(updatePaming.getStart_date());
            pamings.setEnd_date(updatePaming.getEnd_date());
            pamings.setInfo(updatePaming.getInfo());
            pamings.setPub_priv(updatePaming.isPub_priv());
            pamings.setCategory(updatePaming.getCategory());
            pamings.setRegion(updatePaming.getRegion());

            return pamingsRepository.save(pamings);
        }
//        if(pamings != null){
//            pamings.setPaming_title(updatePaming.getPaming_title());
//            pamings.setStart_date(updatePaming.getStart_date());
//        }
        return null;
    }
}
