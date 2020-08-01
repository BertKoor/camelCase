package nl.ordina.bertkoor.camelcase.mocks;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;

import java.util.regex.Pattern;

public class CcrsResponseTransformer extends ResponseTemplateTransformer {

    private static final Pattern IS_OK = Pattern.compile("\\d{2,10}");
    private static final Pattern IS_RATING = Pattern.compile("[1-5]{3,10}");

    public CcrsResponseTransformer() {
        super(false);
    }

    @Override
    public String getName() {
        return "ccrs-response";
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        int p = request.getUrl().lastIndexOf('/') + 1;
        String id = request.getUrl().substring(p);
        if (!IS_OK.matcher(id).matches())
            return badRequest();

        return IS_RATING.matcher(id).matches() ? ok(rating(id)) : noContent();
    }

    private String rating(String id) {
        int sum = 0;
        int count = 0;
        for (int i=0; i<id.length(); i++) {
            char c = id.charAt(i);
            int ci = (short)c - (short)'0';
            sum += ci;
            count ++;
        }
        int result = sum / count;
        return "" + result;
    }

    private ResponseDefinition badRequest() {
        return new ResponseDefinitionBuilder()
                .withStatus(HttpStatus.SC_BAD_REQUEST)
                .withHeader("Content-Type", ContentType.TEXT_PLAIN.toString())
                .withBody("Bad request")
                .build();
    }

    private ResponseDefinition ok(String rating) {
        return ResponseDefinitionBuilder
                .like(ResponseDefinition.okEmptyJson())
                .withBody("{\"rating\":" + rating + "}")
                .build();
    }

    private ResponseDefinition noContent() {
        return ResponseDefinitionBuilder
                .like(ResponseDefinition.noContent())
                .build();
    }

}
