package yagov.rest.bills;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillController {


    @RequestMapping("/bill")
    public Bill getBill(@RequestParam(value="congress", defaultValue="115") Integer congress,
    		  			@RequestParam(value="name") String billName) {
        return new Bill(congress, billName);
    }
    
    @RequestMapping("/billtext")
    public BillText getBillText(@RequestParam(value="congress", defaultValue="115") Integer congress,
    		  			@RequestParam(value="name") String billName) {
        return new BillText(congress, billName);
    }
}