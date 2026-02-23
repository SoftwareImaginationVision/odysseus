package ro.simavi.odysseus.platform.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.simavi.odysseus.platform.servicesImpl.ExtractDataFromMrzServiceImpl;

import javax.annotation.PostConstruct;

@RestController
public class ExtractDataFromMrzController {

    private final ExtractDataFromMrzServiceImpl extractDataFromMrzService;


    @Autowired
    public ExtractDataFromMrzController(ExtractDataFromMrzServiceImpl extractDataFromMrzService) {
        this.extractDataFromMrzService = extractDataFromMrzService;
    }

    @PostConstruct
    private void testMethods() {
        String[] dummyData = {
                // TD1: Trifold card MRZ (3 lines, 30 characters each)
                "I<CHNTWLM4GQYC8<<<<<<<<<<<<<<<5608065F2810043CHN<<<<<<<<<<<2LWKM<<QGR<<<<<<<<<<<<<<<<<<<<<",


                // TD2: Passport card MRZ (2 lines, 36 characters each)
                "I<INDAYFCKKP<<HPIANMG<<<<<<<<<<<<<<<XS14G5K5U2IND0204071F2705217<<<<<<<2",


                // TD3: Passport MRZ (2 lines, 44 characters each)
                "P<AUSISJCBVQ<<UTQNFKW<<<<<<<<<<<<<<<<<<<<<<<1045BT0T38AUS0801023F2606048<<<<<<<<<<<<<<08",


                "INVALIDDATA"

        };

        System.out.println(dummyData[0].length());
        System.out.println(dummyData[1].length());
        System.out.println(dummyData[2].length());
        System.out.println(extractDataFromMrzService.extractFromTd1(dummyData[0]));
        System.out.println(extractDataFromMrzService.extractFromTd2(dummyData[1]));
        System.out.println(extractDataFromMrzService.extractFromTd3(dummyData[2]));

//        System.out.println(extractDataFromMrzService.extractFromTd1(dummyData[3]));
//        System.out.println(extractDataFromMrzService.extractFromTd2(dummyData[3]));
//        System.out.println(extractDataFromMrzService.extractFromTd3(dummyData[3]));


    }

}
