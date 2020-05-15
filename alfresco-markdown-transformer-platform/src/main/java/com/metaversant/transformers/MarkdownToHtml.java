package com.metaversant.transformers;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.content.transform.AbstractContentTransformer2;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.TransformationOptions;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by jpotts, Metaversant on 5/11/20.
 */
public class MarkdownToHtml extends AbstractContentTransformer2 {
    public static final String MIMETYPE_MARKDOWN = "text/x-markdown";

    @Override
    protected void transformInternal(ContentReader reader, ContentWriter writer, TransformationOptions options) throws Exception {
        MutableDataSet transformerOptions = new MutableDataSet();

        Parser parser = Parser.builder(transformerOptions).build();
        HtmlRenderer renderer = HtmlRenderer.builder(transformerOptions).build();

        Node document = parser.parse(reader.getContentString());
        String html = renderer.render(document);

        Writer out = new BufferedWriter(new OutputStreamWriter(writer.getContentOutputStream()));
        out.write(html);

        out.flush();

        if (out != null) {
            out.close();
        }
    }

    @Override
    public boolean isTransformableMimetype(String sourceMimetype, String targetMimetype, TransformationOptions options) {
        return sourceMimetype.equals(MIMETYPE_MARKDOWN) && targetMimetype.equals(MimetypeMap.MIMETYPE_HTML);
    }
}
