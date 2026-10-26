package utilities;

import io.restassured.specification.RequestSpecification;

public class BaseRequest {
    protected RequestSpecification getRequest(){
        return new RequestProvider().get();
    }
}
