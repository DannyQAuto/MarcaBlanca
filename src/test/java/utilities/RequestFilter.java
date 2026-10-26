package utilities;

import io.restassured.filter.FilterContext;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.Header;
import io.restassured.internal.support.Prettifier;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.List;

public class RequestFilter extends RequestLoggingFilter {
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext ctx
    ) {
        final var response = ctx.next(requestSpec, responseSpec);
        final var requestInfo = getRequestInfo(requestSpec);
        final var responseInfo = getResponseInfo(response);
        logMessage(requestInfo, responseInfo);
        return response;
    }

    private String getRequestInfo(FilterableRequestSpecification requestSpec) {
        final var requestMethodURL = String.format("(%s) \t %s",
                requestSpec.getMethod(),
                requestSpec.getURI()
        );
        System.out.println(requestMethodURL);

        final var requestHeaderMessage = getHeadersInfo(requestSpec.getHeaders().asList());
        final var requestBodyMessage = new Prettifier().getPrettifiedBodyIfPossible(requestSpec);

        if (requestBodyMessage == null) {
            Logs.trace("requestInfo body is null");
            return String.format(
                    """
                            \n=============================================
                            Request
                            =============================================
                            %s
                            %s
                            """, requestMethodURL, requestHeaderMessage
            );
        } else {
            Logs.trace("requestInfo body is NOT null");
            return String.format(
                    """
                            \n=============================================
                            Request
                            =============================================
                            %s
                            Request Headers:
                            %s
                            Request Body:
                            %s
                            """, requestMethodURL, requestHeaderMessage, requestBodyMessage
            );
        }
    }

    private String getResponseInfo(Response response) {
        final var responseStatusCodeResponseTime =
                String.format("Status Code: %d \t Response Time: %dms",
                        response.getStatusCode(),
                        response.getTime()
                );
        System.out.println(responseStatusCodeResponseTime);

        final var responseHeaderMessage = getHeadersInfo(response.getHeaders().asList());
        final var responseBodyMessage = response.getBody().asPrettyString();

        if (responseBodyMessage == null) {
            Logs.trace("requestInfo body is null");
            return String.format(
                    """
                            =============================================
                            Response
                            =============================================
                            %s
                            Response Headers:
                            %s
                            """, responseStatusCodeResponseTime, responseHeaderMessage
            );
        } else {
            Logs.trace("requestInfo body is NOT null");
            return String.format(
                    """
                            =============================================
                            Response
                            =============================================
                            %s
                            Response Headers:
                            %s
                            Response Body:
                            %s
                            """, responseStatusCodeResponseTime, responseHeaderMessage, responseBodyMessage
            );
        }
    }

    private String getHeadersInfo(List<Header> listHeader) {
        final var stringBuilder = new StringBuilder();
        for (var header : listHeader) {
            stringBuilder.append(String.format("\t%s: %s%n", header.getName(), header.getValue()));
        }
        return stringBuilder.toString();
    }

    private void logMessage(String requestInfo, String responseInfo) {
        final var message = String.format("""
                %s
                %s
                """, requestInfo, responseInfo);
        Logs.debug(message);
    }
}
