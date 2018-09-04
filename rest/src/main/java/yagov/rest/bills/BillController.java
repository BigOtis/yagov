package yagov.rest.bills;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillController {


    @RequestMapping("/bill")
    public Bill greeting(@RequestParam(value="congress", defaultValue="115") Integer congress,
    		  			@RequestParam(value="billName") String billName) {
        return new Bill(congress, billName);
    }
}